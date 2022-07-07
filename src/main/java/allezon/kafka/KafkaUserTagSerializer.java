package allezon.kafka;

import allezon.KafkaUserTag;
import allezon.UserTag;
import allezon.avro.SerDe;
import org.apache.kafka.common.serialization.Serializer;

public class KafkaUserTagSerializer implements Serializer<KafkaUserTag>  {
    private final SerDe<KafkaUserTag> userTagSerDe;

    public KafkaUserTagSerializer() {
        userTagSerDe = new SerDe<>(KafkaUserTag.getClassSchema());
    }

    @Override
    public byte[] serialize(String s, KafkaUserTag userTag) {
        return userTag == null ? null : userTagSerDe.serialize(userTag);

//        try {
//            return userTag == null ? null : Snappy.compress(userTagSerDe.serialize(userTag));
//        } catch (IOException e) {
//            return null;
//        }
    }

}
