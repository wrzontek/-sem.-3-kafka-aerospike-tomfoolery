package allezon.kafka;

import allezon.UserTag;
import allezon.avro.SerDe;
import allezon.domain.Action;
import allezon.domain.Device;
import allezon.domain.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.kafka.common.serialization.Serializer;

import java.time.Instant;

public record KafkaUserTag(String time, String origin, String brandId, String categoryId, int price) {

}
