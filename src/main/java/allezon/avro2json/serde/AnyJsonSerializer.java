package allezon.avro2json.serde;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import allezon.avro2json.AnyJson;

import java.io.IOException;

public class AnyJsonSerializer extends JsonSerializer<AnyJson> {
    @Override
    public void serialize(AnyJson value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            value.serialize(gen, serializers);
        } else {
            gen.writeNull();
        }
    }
}
