package allezon.avro2json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.collect.Iterators;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.*;

/**
 * Serializes only fields from avro schema.
 */
class JacksonAvroSerializer extends JsonSerializer<GenericRecord> {

    @Override
    public void serialize(GenericRecord value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        Map<String, Object> valueAsMap = new AbstractMap<>() {
            @Override
            public Set<Entry<String, Object>> entrySet() {
                return new AbstractSet<>() {
                    @Override
                    public Iterator<Entry<String, Object>> iterator() {
                        return Iterators.transform(value.getSchema().getFields().iterator(),
                                field -> Pair.of(field.name(), value.get(field.pos())));
                    }

                    @Override
                    public int size() {
                        return value.getSchema().getFields().size();
                    }
                };
            }
        };

        provider.defaultSerializeValue(valueAsMap, gen);
    }

}
