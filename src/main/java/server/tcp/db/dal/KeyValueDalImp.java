package server.tcp.db.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import server.tcp.db.model.KeyData;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jary on 7/27/2018.
 * data access level for storing values according it's key on the mongodb database
 */
@Component
public class KeyValueDalImp implements KeyValueDal {

    @Autowired
    KeyDataRepository dataRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void rightAdd(String key, String value) {
        Query query = new Query();
        query.addCriteria(Criteria.where("key").is(key));
        Update update = new Update();
        update.push("values",value);
        mongoTemplate.upsert(query, update, KeyData.class);
    }

    @Override
    public void leftAdd(String key, String value) {
        Query query = new Query();
        query.addCriteria(Criteria.where("key").is(key));
        Update update = new Update();
        update.push("values").atPosition(0).value(value);
        mongoTemplate.upsert(query, update, KeyData.class);
    }

    @Override
    public void set(String key, List<String> values) {
        KeyData keyData = KeyData.builder()
                .key(key)
                .values(values).build();
        mongoTemplate.save(keyData);
    }

    @Override
    public List<String> get(String key) {
       try {
           Query query = new Query();

           query.addCriteria(Criteria.where("key").is(key));

           dataRepository.findByKey(key).getValues();
           return mongoTemplate.find(query, KeyData.class).get(0).getValues();
       }catch(Exception e){
           return new ArrayList<>();
       }
    }
}
