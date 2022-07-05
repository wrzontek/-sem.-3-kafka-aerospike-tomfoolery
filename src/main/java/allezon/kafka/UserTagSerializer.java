package allezon.kafka;

import allezon.UserTag;
import allezon.avro.SerDe;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class UserTagSerializer implements Serializer<UserTag>  {
    private final SerDe<UserTag> userTagSerDe;

    public UserTagSerializer() {
        userTagSerDe = new SerDe<>(UserTag.getClassSchema());
    }

    @Override
    public byte[] serialize(String s, UserTag userTag) {
        return userTag == null ? null : userTagSerDe.serialize(userTag);
    }

}
