package allezon.dao;

import allezon.UserProfile;
import allezon.UserTag;
import allezon.avro.SerDe;
import allezon.domain.Action;
import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Host;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.cdt.ListOperation;
import com.aerospike.client.policy.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MessageDao {

    private static final String NAMESPACE = "mimuw";
    private static final String SET = "userprofiles";
    private static final String VIEWS_BIN = "views";
    private static final String BUYS_BIN = "buys";

    private final AerospikeClient client;

    private final SerDe<UserTag> userTagSerDe;

    private static ClientPolicy defaultClientPolicy() {
        ClientPolicy defaulClientPolicy = new ClientPolicy();
        defaulClientPolicy.readPolicyDefault.replica = Replica.SEQUENCE;
        defaulClientPolicy.readPolicyDefault.socketTimeout = 100;
        defaulClientPolicy.readPolicyDefault.totalTimeout = 100;
        defaulClientPolicy.writePolicyDefault.socketTimeout = 15000;
        defaulClientPolicy.writePolicyDefault.totalTimeout = 15000;
        defaulClientPolicy.writePolicyDefault.maxRetries = 3;
        defaulClientPolicy.writePolicyDefault.commitLevel = CommitLevel.COMMIT_MASTER;
        defaulClientPolicy.writePolicyDefault.recordExistsAction = RecordExistsAction.REPLACE;
        return defaulClientPolicy;
    }

    public MessageDao(@Value("${aerospike.seeds}") String[] aerospikeSeeds, @Value("${aerospike.port}") int port) {
        this.client = new AerospikeClient(defaultClientPolicy(), Arrays.stream(aerospikeSeeds).map(seed -> new Host(seed, port)).toArray(Host[]::new));
        this.userTagSerDe = new SerDe<>(UserTag.getClassSchema());
    }

    public void put(UserTag userTag) {
        WritePolicy writePolicy = new WritePolicy(client.writePolicyDefault);

        String binName;
        if (userTag.getAction().toString().equals(Action.VIEW.toString()))
            binName = VIEWS_BIN;
        else
            binName = BUYS_BIN;

        Key key = new Key(NAMESPACE, SET, String.valueOf(userTag.getCookie()));

        client.operate(writePolicy, key,
                ListOperation.append(binName, com.aerospike.client.Value.get(userTagSerDe.serialize(userTag)))
        ); // todo tu pewnie dałoby się zrobić żeby trzymało tylko 200 najnowszych
    }

    public UserProfile get(String cookie, int limit) {
        Policy readPolicy = new Policy(client.readPolicyDefault);
        WritePolicy writePolicy = new WritePolicy(client.writePolicyDefault);

        Key key = new Key(NAMESPACE, SET, cookie);

        if (!client.exists(readPolicy, key)) {
            return null;
        }

        Record record = client.operate(writePolicy, key, // todo to pewnie inaczej powinno wyglądać ale ciężko sprawdzić jak wstawianie nie działa
                ListOperation.getRange(VIEWS_BIN, 0, limit),
                ListOperation.getRange(BUYS_BIN, 0, limit)
        );

        ArrayList<UserTag> views = new ArrayList<>();
        ArrayList<UserTag> buys = new ArrayList<>();

        List<?> viewList = record.getList(VIEWS_BIN);
        for (Object o : viewList) {
            views.add(userTagSerDe.deserialize((byte[]) o, UserTag.getClassSchema()));
        }

        List<?> buyList = record.getList(BUYS_BIN);
        for (Object o : buyList) {
            buys.add(userTagSerDe.deserialize((byte[]) o, UserTag.getClassSchema()));
        }

        return new UserProfile(cookie, views, buys);
    }

    @PreDestroy
    public void close() {
        client.close();
    }
}
