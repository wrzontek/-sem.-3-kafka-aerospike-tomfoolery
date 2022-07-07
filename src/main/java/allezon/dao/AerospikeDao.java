package allezon.dao;

import allezon.UserProfile;
import allezon.UserTag;
import allezon.avro.SerDe;
import allezon.domain.Action;
import com.aerospike.client.*;
import com.aerospike.client.Record;
import com.aerospike.client.policy.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xerial.snappy.Snappy;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class AerospikeDao {

    private static final String NAMESPACE = "mimuw";
    private static final String SET = "messages";
    private static final String MESSAGE_BIN = "message";
    private static final Logger logger = LoggerFactory.getLogger(AerospikeDao.class);
    private final AerospikeClient client;
    private final SerDe<UserProfile> userProfileSerDe;


    private static ClientPolicy defaultClientPolicy() {
        ClientPolicy defaulClientPolicy = new ClientPolicy();
        defaulClientPolicy.readPolicyDefault.replica = Replica.MASTER_PROLES;
        defaulClientPolicy.readPolicyDefault.socketTimeout = 100;
        defaulClientPolicy.readPolicyDefault.totalTimeout = 100;
        defaulClientPolicy.writePolicyDefault.socketTimeout = 15000;
        defaulClientPolicy.writePolicyDefault.totalTimeout = 15000;
        defaulClientPolicy.writePolicyDefault.maxRetries = 3;
        defaulClientPolicy.writePolicyDefault.commitLevel = CommitLevel.COMMIT_MASTER;
        defaulClientPolicy.writePolicyDefault.recordExistsAction = RecordExistsAction.REPLACE;
        return defaulClientPolicy;
    }

    public AerospikeDao(@Value("${aerospike.seeds}") String[] aerospikeSeeds, @Value("${aerospike.port}") int port) {
        this.client = new AerospikeClient(defaultClientPolicy(), Arrays.stream(aerospikeSeeds).map(seed -> new Host(seed, port)).toArray(Host[]::new));
        this.userProfileSerDe = new SerDe<>(UserProfile.getClassSchema());
    }

    private static void insertUserTagSorted(List<UserTag> userTags, UserTag newUserTag) {
        if (userTags.size() == 0) {
            userTags.add(newUserTag);
            return;
        }
        Instant newUserTagTime = Instant.parse(newUserTag.getTime());
        for (int i = userTags.size() - 1; i >= 0; i--) { // idziemy od tyłu (najnowszych), bo raczej nowy usertag jest dość świeży czasowo
            if (newUserTagTime.compareTo(Instant.parse(userTags.get(i).getTime())) <= 0) {
                userTags.add(i + 1, newUserTag);
                return;
            }
        }
        userTags.add(0, newUserTag);
    }

    public void put(UserTag userTag) throws IOException {
        Policy readPolicy = new Policy(client.readPolicyDefault);
        WritePolicy writePolicy = new WritePolicy(client.writePolicyDefault);
        // mówimy aerospike, że rekord przy zapisie ma mieć wskazaną generację
        writePolicy.generationPolicy = GenerationPolicy.EXPECT_GEN_EQUAL;

        // zakładamy trzy próby zapisu rekordu
        for (int attempt = 0; attempt < 3; attempt++) {
            // odczytujemy rekord, dekompresujemy json, parsujemy, dodajemy tag do odpowiedniej listy, przycinamy do 200
            Key key = new Key(NAMESPACE, SET, String.valueOf(userTag.getCookie()));
            Record record = client.get(readPolicy, key, MESSAGE_BIN);

            UserProfile userProfile = record == null ?
                    new UserProfile(userTag.getCookie(), new ArrayList<>(), new ArrayList<>())
                    : userProfileSerDe.deserialize(Snappy.uncompress((byte[]) record.getValue(MESSAGE_BIN)), UserProfile.getClassSchema());

            if (userTag.getAction().toString().equals(Action.BUY.toString())) {
                insertUserTagSorted(userProfile.getBuys(), userTag);
                if (userProfile.getBuys().size() > 200)
                    userProfile.getBuys().remove(userProfile.getBuys().size() - 1);
            } else {
                insertUserTagSorted(userProfile.getViews(), userTag);
                if (userProfile.getViews().size() > 200)
                    userProfile.getViews().remove(userProfile.getViews().size() - 1);
            }

            // przy zapisie zmienionego profilu we writePolicy ustawiamy generację, czyli wersję odczytanego rekordu
            writePolicy.generation = record == null ? 0 : record.generation;

            // serializujemy jsona, kompresujemy, zapisujemy do aerospike
            Bin messageBin = new Bin(MESSAGE_BIN, Snappy.compress(userProfileSerDe.serialize(userProfile)));
            try {
                client.put(writePolicy, key, messageBin);
                break;
            } catch (AerospikeException e) {
                // jeżeli generacja przy zapisie została podbita przez równoległe zapytanie, to powtarzamy
                if (e.getResultCode() == ResultCode.GENERATION_ERROR) {
                    logger.warn("Generation error while trying to update the profile for cookie: {}, attempt: {} - retrying",
                            userTag.getCookie(), attempt + 1);
                    continue;
                }
                throw e;
            }
        }
    }

    public UserProfile get(String cookie) throws IOException {
        Policy readPolicy = new Policy(client.readPolicyDefault);

        Key key = new Key(NAMESPACE, SET, cookie);
        Record record = client.get(readPolicy, key, MESSAGE_BIN);

        if (record == null) {
            return null;
        }

        return userProfileSerDe.deserialize(Snappy.uncompress((byte[]) record.getValue(MESSAGE_BIN)), UserProfile.getClassSchema());
    }

    @PreDestroy
    public void close() {
        client.close();
    }
}
