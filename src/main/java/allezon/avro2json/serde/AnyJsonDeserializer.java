package allezon.avro2json.serde;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import allezon.avro2json.AnyJson;

import java.io.IOException;

public class AnyJsonDeserializer extends JsonDeserializer<AnyJson> {
    @Override
    public AnyJson deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return AnyJson.fromJsonNode(p.readValueAsTree());
    }
}
