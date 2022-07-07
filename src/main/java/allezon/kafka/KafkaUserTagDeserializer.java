package allezon.kafka;

import allezon.KafkaUserTag;
import allezon.UserTag;
import allezon.avro.SerDe;
import org.apache.kafka.common.serialization.Deserializer;

public class KafkaUserTagDeserializer implements Deserializer<KafkaUserTag> {
    private final SerDe<KafkaUserTag> userTagSerDe;

    public KafkaUserTagDeserializer() {
        userTagSerDe = new SerDe<>(KafkaUserTag.getClassSchema());
    }

    @Override
    public KafkaUserTag deserialize(String s, byte[] bytes) {
        return bytes == null ? null : userTagSerDe.deserialize(bytes, KafkaUserTag.getClassSchema());

//        try {
//            return bytes == null ? null : userTagSerDe.deserialize(Snappy.uncompress(bytes), UserTag.getClassSchema());
//        } catch (IOException e) {
//            return null;
//        }
    }
}
