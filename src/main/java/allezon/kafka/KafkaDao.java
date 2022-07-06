package allezon.kafka;

import allezon.UserTag;
import allezon.avro.SerDe;
import allezon.domain.Action;
import allezon.domain.Aggregate;
import allezon.domain.AggregatesQueryResult;
import com.aerospike.client.admin.User;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.kstream.internals.WindowedSerializer;
import org.apache.kafka.streams.state.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Component
public class KafkaDao {

    static final String USER_TAGS_TOPIC = "user_tags";
    static final Duration windowSize = Duration.ofMinutes(1);
    static final String VIEW_TOPIC = "user_tags_views";
    static final String BUY_TOPIC = "user_tags_buys";

    Producer<String, UserTag> producer;

    Properties props;
    final Serde<UserTag> userTagSerde = new UserTagSerde();

    public KafkaDao(@Value("${kafka.bootstrapservers}") String bootstrapServers) {
        props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, UserTagSerializer.class);
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "user_tags");
        props.put(StreamsConfig.CLIENT_ID_CONFIG, "user_tags_client");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, UserTagSerde.class);
        props.put(StreamsConfig.DEFAULT_WINDOWED_KEY_SERDE_INNER_CLASS, UserTagSerde.class);
        props.put(StreamsConfig.DEFAULT_WINDOWED_VALUE_SERDE_INNER_CLASS, UserTagSerde.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        producer = new KafkaProducer<>(props);
    }

    public void put(UserTag userTag) {
//        String topic = userTag.getAction().toString().equals(Action.VIEW.toString()) ? VIEW_TOPIC : BUY_TOPIC;
//        Integer partition = hash z userTag.getProductInfo().getCategoryId() modulo 67, jak kolizje będą to trudno, ciut gorsze rozsmarowanie
        // todo rozsmarować po topicach i partitionach zamiast wszystko do jednego
        // todo usuwać stare usertagi
        // todo już na tym etapie windowować??

        KafkaUserTag kafkaUserTag = new KafkaUserTag(
                userTag.getTime().toString(),
                userTag.getOrigin().toString(), userTag.getProductInfo().getBrandId().toString(),
                userTag.getProductInfo().getCategoryId().toString(),
                userTag.getProductInfo().getPrice());

//        producer.send(new ProducerRecord<>(USER_TAGS_TOPIC, userTag)); // zakomentowane żeby nie przeszkadzać testom usecase 1/2
    }

    private boolean filterUserTag(UserTag userTag, Action action, LocalDateTime timeFrom, LocalDateTime timeTo, String origin, String brandId, String categoryId) {
        return Instant.parse(userTag.getTime()).compareTo(timeFrom.toInstant(ZoneOffset.UTC)) >= 0
                && Instant.parse(userTag.getTime()).isBefore(timeTo.toInstant(ZoneOffset.UTC))
                && userTag.getAction().toString().equals(action.toString())
                && (origin == null || userTag.getOrigin().toString().equals(origin))
                && (brandId == null || userTag.getProductInfo().getBrandId().toString().equals(brandId))
                && (categoryId == null || userTag.getProductInfo().getCategoryId().equals(categoryId));
    }

    public AggregatesQueryResult get(Action action, List<Aggregate> aggregates,
                                     LocalDateTime timeFrom, LocalDateTime timeTo,
                                     String origin, String brandId, String categoryId) throws InterruptedException {
        final StreamsBuilder builder = new StreamsBuilder();

        final KStream<String, UserTag> userTags = builder.stream(USER_TAGS_TOPIC); // todo czytać tylko z topica i partitiona odpowiadającym action/categoryid
        final KStream<UserTag, UserTag> filteredUserTags = userTags
                .filter((k, userTag) -> filterUserTag(userTag, action, timeFrom, timeTo, origin, brandId, categoryId))
                .map((k, v) -> new KeyValue<>(v, v));

        final TimeWindowedKStream<UserTag, UserTag> userTagsTable = filteredUserTags
                .groupByKey(Grouped.with(userTagSerde, userTagSerde))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(windowSize));


        userTagsTable.count(Materialized.with(userTagSerde, Serdes.Long()));
        // todo jeśli chcemy sumę cen albo count+sumę cen to custom aggregate(), coś jak poniżej
//                .aggregate(
//                        () -> 0L, /* initializer */
//                        (aggKey, newValue, aggValue) -> aggValue + newValue.getProductInfo().getPrice(), /* adder */
//                        Materialized.as("aggregated-stream-store") /* state store name */
//                                .withValueSerde(Serdes.Long())); /* serde for aggregate value */

        KafkaStreams streams = null;
        try {
            streams = new KafkaStreams(builder.build(), props);
            streams.start();

//            streams.setStateListener(); TODO stateListener zamiast czekania jak jaskiniowiec
            while (!streams.state().equals(KafkaStreams.State.RUNNING)) {
                System.out.println("CZEKAMY");
                Thread.sleep(100);
            }
            ReadOnlyWindowStore<UserTag, Long> windowStore =
                    streams.store(StoreQueryParameters.fromNameAndType("KSTREAM-AGGREGATE-STATE-STORE-0000000003", QueryableStoreTypes.windowStore())); // todo nazwa ze zmiennej nie na sztywno

            KeyValueIterator<Windowed<UserTag>, Long> range = windowStore.all();
            while (range.hasNext()) {
                KeyValue<Windowed<UserTag>, Long> next = range.next();
                System.out.println("count for " + next.key + ": " + next.value);
                // todo zrobić z tego taką tabelkę jak trzeba
            }
        } finally {
            if (streams != null)
                streams.close();
        }

        return null;
    }
}
