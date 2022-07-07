package allezon.kafka;

import allezon.KafkaUserTag;
import allezon.UserTag;
import allezon.domain.Action;
import allezon.domain.Aggregate;
import allezon.domain.AggregatesQueryResult;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
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

    static final String USER_TAGS_TOPIC = "user_tags"; // todo zrezygnować z tego na rzecz VIEW_TOPIC i BUY_TOPIC
    static final Duration windowSize = Duration.ofMinutes(1);
    static final String VIEW_TOPIC = "user_tags_views";
    static final String BUY_TOPIC = "user_tags_buys";

    Properties producerProps;
    Properties consumerProps;

    Producer<String, KafkaUserTag> producer;
    Consumer<Windowed<KafkaUserTag>, Long> consumer;

    final Serde<KafkaUserTag> kafkaUserTagSerde = new KafkaUserTagSerde();

    public KafkaDao(@Value("${kafka.bootstrapservers}") String bootstrapServers) {
        // todo zakomentowane żeby nie psuć kompilacji UseCase 1+2
//        producerProps = new Properties();
//        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaUserTagSerializer.class);
//        producerProps.put(ProducerConfig.ACKS_CONFIG, "all");
//        producer = new KafkaProducer<>(producerProps);
//
//        consumerProps = new Properties();
//        consumerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        consumerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        consumerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaUserTagSerializer.class);
//        consumerProps.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, KafkaUserTagSerde.class);
//        consumerProps.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, KafkaUserTagSerde.class);
//        consumerProps.put(StreamsConfig.DEFAULT_WINDOWED_KEY_SERDE_INNER_CLASS, KafkaUserTagSerde.class);
//        consumerProps.put(StreamsConfig.DEFAULT_WINDOWED_VALUE_SERDE_INNER_CLASS, KafkaUserTagSerde.class);
//        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "allezon-aggregate-consumer");
//        consumerProps.put(StreamsConfig.APPLICATION_ID_CONFIG, "allezon-aggregate-reader");
//        consumerProps.put(StreamsConfig.CLIENT_ID_CONFIG, "allezon-aggregate-reader-client");
//
//        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//
//        final Deserializer<Windowed<KafkaUserTag>> windowedDeserializer = new TimeWindowedDeserializer(kafkaUserTagSerde.deserializer(), windowSize.toMillis());
//
//        consumer = new KafkaConsumer<Windowed<KafkaUserTag>, Long>(consumerProps, windowedDeserializer, Serdes.Long().deserializer());
    }

    public void put(UserTag userTag) {
        String topic = userTag.getAction().toString().equals(Action.VIEW.toString()) ? VIEW_TOPIC : BUY_TOPIC;
        Integer partition = userTag.getProductInfo().getCategoryId().hashCode() % 67; // jak kolizje będą to trudno, ciut gorsze rozsmarowanie

        KafkaUserTag kafkaUserTag = new KafkaUserTag(
                userTag.getTime().toString(),
                userTag.getOrigin().toString(), userTag.getProductInfo().getBrandId().toString(),
                userTag.getProductInfo().getCategoryId().toString(),
                userTag.getProductInfo().getPrice());

    // todo rozsmarować po topicach i partitionach zamiast wszystko do jednego, np tak:
    //  producer.send(new ProducerRecord<>(topic, partition, null, kafkaUserTag));
        producer.send(new ProducerRecord<>(USER_TAGS_TOPIC, kafkaUserTag));
    }

    private boolean considerUserTag(KafkaUserTag userTag,  String origin, String brandId, String categoryId) {
        return (origin == null || userTag.getOrigin().toString().equals(origin))
//              && userTag.getAction().toString().equals(action.toString()) TODO zastąpić przez odczytywanie z odpowiedniego topica
                && (brandId == null || userTag.getBrandId().toString().equals(brandId))
                && (categoryId == null || userTag.getCategoryId().equals(categoryId));
    }

    public AggregatesQueryResult get(Action action, List<Aggregate> aggregates,
                                     LocalDateTime timeFrom, LocalDateTime timeTo,
                                     String origin, String brandId, String categoryId) throws InterruptedException {
        // TODO ta architektura jest mocno podejrzana, głupia i po prostu nie działa
        //  nie powinnyśmy tworzyć nowej instancji KafkaStreams tylko po to żeby móc query'ować ze store, który załatwia nam AllezonAggregator
        //  ale nie wiem jak to zrobić

        final StreamsBuilder builder = new StreamsBuilder();
        final KTable<Windowed<UserTag>, Long> viewCounts = builder.table("allezon-aggregate-KSTREAM-AGGREGATE-STATE-STORE-0000000003-repartition"); // todo -hardcode, +czytać tylko z topica i partitiona odpowiadającym action/categoryid


        StoreBuilder storeBuilder = Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore("allezon-aggregate-KSTREAM-AGGREGATE-STATE-STORE-0000000003-repartition"), // todo -hardcode
                kafkaUserTagSerde,
                kafkaUserTagSerde);

        builder.addStateStore(storeBuilder);
        KafkaStreams streams = null;
        List<String> columns = new ArrayList<>();
        List<List<String>> rows = new ArrayList<>();

        try {
            streams = new KafkaStreams(builder.build(), consumerProps);
            streams.start();

            while (!streams.state().equals(KafkaStreams.State.RUNNING)) { // todo to jest niesamowicie podejrzane
                System.out.println("CZEKAMY");
                Thread.sleep(100);
            }
            ReadOnlyWindowStore<KafkaUserTag, Long> windowStore =
                    streams.store(StoreQueryParameters.fromNameAndType("allezon-aggregate-KSTREAM-AGGREGATE-STATE-STORE-0000000003-repartition", QueryableStoreTypes.windowStore())); // todo -hardcode

            KeyValueIterator<Windowed<KafkaUserTag>, Long> range = windowStore.fetchAll(timeFrom.toInstant(ZoneOffset.UTC), timeTo.toInstant(ZoneOffset.UTC));

            columns.add("1m_bucket");
            columns.add("action");
            if (origin != null)
                columns.add("origin");
            if (brandId != null)
                columns.add("brand_id");
            if (categoryId != null)
                columns.add("category_id");
            for (Aggregate aggregate : aggregates)
                columns.add(aggregate.toString());

            while (range.hasNext()) {
                KeyValue<Windowed<KafkaUserTag>, Long> next = range.next();

                KafkaUserTag userTag = next.key.key();

                if (!considerUserTag(userTag, origin, brandId, categoryId))
                    continue;

                List<String> row = new ArrayList<>();
                row.add(next.key.window().startTime().toString());
                row.add(action.toString());
                if (origin != null)
                    row.add(userTag.getOrigin().toString());
                if (brandId != null)
                    row.add(userTag.getBrandId().toString());
                if (categoryId != null)
                    row.add(userTag.getCategoryId().toString());

                for (Aggregate aggregate : aggregates)
                    // todo next.value powinno być parą <Long, Long> czyli count i suma cen, wtedu oczywiście pakujemy odpowiednie Longi do odpowiednich kolumn
                    if (aggregate.equals(Aggregate.COUNT))
                        row.add(next.value.toString());
                    else
                        row.add(next.value.toString());

                rows.add(row);
            }
        } finally {
            if (streams != null)
                streams.close();
        }

        return new AggregatesQueryResult(columns, rows);
    }
}
