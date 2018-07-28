package server.tcp.db.dal;

import org.springframework.data.mongodb.repository.MongoRepository;
import server.tcp.db.model.KeyData;

/**
 * Created by Jary on 7/27/2018.
 */
public interface KeyDataRepository extends MongoRepository<KeyData,Integer> {

   KeyData findByKey(String key);

}
