package allezon.kafka;

import allezon.UserTag;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Properties;

@Component
public class KafkaDao {

    static final String USER_TAGS_TOPIC = "user_tags";
    static final Duration windowSize = Duration.ofMinutes(1);

    Producer<String, UserTag> producer;

    public KafkaDao(@Value("${kafka.bootstrapservers}") String bootstrapServers) {
        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, UserTagSerializer.class);

        producer = new KafkaProducer<>(props);
    }

    public void put(UserTag userTag) {
        producer.send(new ProducerRecord<>(USER_TAGS_TOPIC, userTag));
    }

}
