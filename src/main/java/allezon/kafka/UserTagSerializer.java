package allezon.kafka;

import allezon.UserTag;
import allezon.avro.SerDe;
import org.apache.kafka.common.serialization.Serializer;
import org.xerial.snappy.Snappy;

import java.io.IOException;
import java.util.Map;

public class UserTagSerializer implements Serializer<UserTag>  {
    private final SerDe<UserTag> userTagSerDe;

    public UserTagSerializer() {
        userTagSerDe = new SerDe<>(UserTag.getClassSchema());
    }

    @Override
    public byte[] serialize(String s, UserTag userTag) {
        return userTag == null ? null : userTagSerDe.serialize(userTag);

//        try {
//            return userTag == null ? null : Snappy.compress(userTagSerDe.serialize(userTag));
//        } catch (IOException e) {
//            return null;
//        }
    }

}
