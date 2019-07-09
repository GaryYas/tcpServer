package server.tcp.cache;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.tcp.db.dal.KeyDataRepository;
import server.tcp.db.model.KeyData;
import server.tcp.requestHandlers.addLeftRequest;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Created by Jary on 7/27/2018.
 * lru cache with fixed size of 20, which may be modified and increased,the size 20 indicates that there are 20 different keys
 * uses linkedHashMap for the cache in order to preserve order
 */

@Component
@Data
public class keyDataCacheImp implements KeyDataCache {
    private static final int capacity = 20;
    private LinkedHashMap<String,List<String>> orderedMap = new LinkedHashMap<>();
    Set<String>keys = new HashSet<>();
    @Autowired
    KeyDataRepository keyDataRepository;
    Logger logger = Logger.getLogger(keyDataCacheImp.class);

   private Map<Direction,BiFunction>directionFunc=new HashMap<>();

    @PostConstruct
    private void setDirectionMap(){
        directionFunc.put(Direction.right,(key, value)->{
            keys.add((String)key);
           return orderedMap.computeIfAbsent((String)key,k->getValuesFromDbIfAbsent((String)key)).add((String)value);
         });
        directionFunc.put(Direction.left,(key, value)->{
             keys.add((String)key);
             orderedMap.computeIfAbsent((String)key,k->getValuesFromDbIfAbsent((String)key)).add(0,(String)value);
            return true;
        });

    }

    private List<String> getValuesFromDbIfAbsent(String key){
        logger.info("no values found in the cache ,is going to read from db and store it in the cahce");
       KeyData keyData = keyDataRepository.findByKey(key);
       if(keyData==null)
           return new ArrayList<>();

       return keyData.getValues();
    }

    public void addKey(String key,String value,Direction direction){
        checkCapacity();
        logger.info("adding to the " + direction + " key :" + key + " and value:" + value);
        directionFunc.get(direction).apply(key,value);

    }

    @Override
    public List<String> get(String key) {
        return orderedMap.get(key);
    }


    @Override
    public void set(String key, List<String> values) {
        if(key!=null&&!values.isEmpty()){
            checkCapacity();
            orderedMap.put(key,values);
            keys.add(key);
        }
    }

    @Override
    //reading all the keys matching to the pattern ,if the capacity is no exceeded and keys set si not empty reads it from cache
    //otherwise read it from db
    public List<String> getAllKeys(String pattern) {
        List<String> toMatch=null;
        if(keys.size()>=capacity || keys.isEmpty()){
            logger.info("the key cache is empty or the capacity is exceeded ,going to read all keys from db");
            toMatch =  keyDataRepository.findAll().stream().map(KeyData::getKey).collect(Collectors.toList());
        }
        else toMatch = keys.stream().collect(Collectors.toList());
        return toMatch.stream().filter(keys->keys.matches(pattern)).collect(Collectors.toList());

    }

    //checking the capacity of the cache ,if the capacity exceeds in the adding operation , las element is removed
    private void checkCapacity(){

        if(orderedMap.size()>= capacity)
        {
            String keyToRemove=null;
            for(Map.Entry<String,List<String>>entry :orderedMap.entrySet()){
                keyToRemove=entry.getKey();
                break;
            }
            orderedMap.remove(keyToRemove);
            logger.info("capacity of the cache is exceeded going to remove value and key of the last added to the cache ,with key:" + keyToRemove);
            if(keyToRemove!=null)
             keys.remove(keyToRemove);
        }

    }

    public enum Direction{
        right,left
    }
}
