package allezon.kafka;

import allezon.UserTag;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class UserTagSerde implements Serde<UserTag> {
    @Override
    public Serializer<UserTag> serializer() {
        return new UserTagSerializer();
    }

    @Override
    public Deserializer<UserTag> deserializer() {
        return new UserTagDeserializer();
    }
}
