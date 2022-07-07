package allezon;

import allezon.dao.AerospikeDao;
import allezon.domain.*;
import allezon.kafka.KafkaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EchoClient {
//  token:     ddccadaf631946c29bf95123b4b183c4

    @Autowired
    private AerospikeDao aerospikeDao;

    @Autowired
    private KafkaDao kafkaDao;

    @PostMapping("/user_tags")
    public ResponseEntity<Void> addUserTag(@RequestBody(required = false) UserTag userTag) throws IOException {
        aerospikeDao.put(userTag);
//        kafkaDao.put(userTag); // TODO odkomentować jeśli chcemy usecase 3

        return ResponseEntity.noContent().build();
    }

    private ArrayList<UserTag> filterUserTagEvents(List<UserTag> userTags, LocalDateTime timeFrom, LocalDateTime timeTo, int limit) {
        ArrayList<UserTag> result = new ArrayList<>();
        for (UserTag userTag : userTags) {
            if (Instant.parse(userTag.getTime()).compareTo(timeFrom.toInstant(ZoneOffset.UTC)) >= 0
                    && Instant.parse(userTag.getTime()).isBefore(timeTo.toInstant(ZoneOffset.UTC))) {
                result.add(userTag);

                if (result.size() == limit)
                    return result;
            }
        }

        return result;
    }

    private UserTagEvent userTagToUserTagEvent(UserTag userTag) {
        return new UserTagEvent(
                Instant.parse(userTag.getTime()),
                userTag.getCookie().toString(),
                userTag.getCountry().toString(),
                Device.valueOf(userTag.getDevice().toString()),
                Action.valueOf(userTag.getAction().toString()),
                userTag.getOrigin().toString(),
                new Product(
                        userTag.getProductInfo().getProductId().toString(),
                        userTag.getProductInfo().getBrandId().toString(),
                        userTag.getProductInfo().getCategoryId().toString(),
                        userTag.getProductInfo().getPrice()
                )
        );
    }

    @PostMapping("/user_profiles/{cookie}")
    public ResponseEntity<UserProfileResult> getUserProfile(@PathVariable("cookie") String cookie,
                                                            @RequestParam("time_range") String timeRangeStr,
                                                            @RequestParam(defaultValue = "200") int limit,
                                                            @RequestBody(required = false) UserProfileResult expectedResult) throws IOException, InterruptedException {
        String[] splitTimeRange = timeRangeStr.split("_");
        LocalDateTime timeFrom = LocalDateTime.parse(splitTimeRange[0]);
        LocalDateTime timeTo = LocalDateTime.parse(splitTimeRange[1]);

        UserProfile userProfile = aerospikeDao.get(cookie);

        if (userProfile != null) {
            ArrayList<UserTag> views = filterUserTagEvents(userProfile.getViews(), timeFrom, timeTo, limit);
            ArrayList<UserTag> buys = filterUserTagEvents(userProfile.getBuys(), timeFrom, timeTo, limit);
            UserProfileResult result = new UserProfileResult(
                    cookie,
                    views.stream().map(this::userTagToUserTagEvent).collect(Collectors.toList()),
                    buys.stream().map(this::userTagToUserTagEvent).collect(Collectors.toList())
            );
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.ok(new UserProfileResult(cookie, Collections.emptyList(), Collections.emptyList()));
        }
    }

    @PostMapping("/aggregates")
    public ResponseEntity<AggregatesQueryResult> getAggregates(@RequestParam("time_range") String timeRangeStr,
            @RequestParam("action") Action action,
            @RequestParam("aggregates") List<Aggregate> aggregates,
            @RequestParam(value = "origin", required = false) String origin,
            @RequestParam(value = "brand_id", required = false) String brandId,
            @RequestParam(value = "category_id", required = false) String categoryId,
            @RequestBody(required = false) AggregatesQueryResult expectedResult) throws InterruptedException {
        return ResponseEntity.ok(expectedResult);

        // TODO odkomentować jeśli chcemy usecase 3
//        String[] splitTimeRange = timeRangeStr.split("_");
//        LocalDateTime timeFrom = LocalDateTime.parse(splitTimeRange[0]);
//        LocalDateTime timeTo = LocalDateTime.parse(splitTimeRange[1]);
//
//        AggregatesQueryResult result = kafkaDao.get(action, aggregates, timeFrom, timeTo, origin, brandId, categoryId);
//
//        return ResponseEntity.ok(result);
    }
}
