package allezon.kafka;

import allezon.KafkaUserTag;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class KafkaUserTagSerde implements Serde<KafkaUserTag> {
    @Override
    public Serializer<KafkaUserTag> serializer() {
        return new KafkaUserTagSerializer();
    }

    @Override
    public Deserializer<KafkaUserTag> deserializer() {
        return new KafkaUserTagDeserializer();
    }
}
