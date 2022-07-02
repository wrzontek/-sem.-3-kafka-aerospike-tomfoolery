package allezon.avro2json.serde;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;

public class ArrayNodeSerializer extends JsonSerializer<ArrayNode> {

    @Override
    public void serialize(ArrayNode value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        // makes jackson use ObjectNodeSerializer while iterating through ArrayNode elements
        // otherwise JsonNode.serialize is used which does not support
        // mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        provider.defaultSerializeValue(value.elements(), gen);
    }

}
