//package allezon.resource;
//
//import allezon.Message;
//import allezon.dao.MessageDao;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//
//import static allezon.avro2json.AvroJsonHttpMessageConverter.AVRO_JSON;
//
//@RestController
//@RequestMapping("/endpoint")
//public class NosqlResource {
//
//    @Autowired
//    private MessageDao messageDao;
//
//    @GetMapping(produces = AVRO_JSON, path = "/{id}")
//    public ResponseEntity<Message> get(@PathVariable("id") Integer id) {
//        return ResponseEntity.of(Optional.ofNullable(messageDao.get(id)));
//    }
//
//    @PutMapping(produces = AVRO_JSON, consumes = AVRO_JSON)
//    public ResponseEntity<Message> put(@RequestBody Message message) {
//        messageDao.put(message);
//        return ResponseEntity.ok(message);
//    }
//
//}
