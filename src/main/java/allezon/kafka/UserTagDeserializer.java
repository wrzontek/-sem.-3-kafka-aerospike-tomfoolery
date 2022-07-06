package allezon.kafka;

import allezon.UserProfile;
import allezon.UserTag;
import allezon.avro.SerDe;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;
import org.xerial.snappy.Snappy;

import java.io.IOException;

public class UserTagDeserializer implements Deserializer<UserTag> {
    private final SerDe<UserTag> userTagSerDe;

    public UserTagDeserializer() {
        userTagSerDe = new SerDe<>(UserTag.getClassSchema());
    }

    @Override
    public UserTag deserialize(String s, byte[] bytes) {
        return bytes == null ? null : userTagSerDe.deserialize(bytes, UserTag.getClassSchema());

//        try {
//            return bytes == null ? null : userTagSerDe.deserialize(Snappy.uncompress(bytes), UserTag.getClassSchema());
//        } catch (IOException e) {
//            return null;
//        }
    }
}
