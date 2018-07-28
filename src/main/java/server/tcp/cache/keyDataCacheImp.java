package server.tcp.cache;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.tcp.db.dal.KeyDataRepository;
import server.tcp.db.model.KeyData;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Created by Jary on 7/27/2018.
 */

@Component
@Data
public class keyDataCacheImp implements KeyDataCache {
    private static final int capacity = 20;
    private LinkedHashMap<String,List<String>> orderedMap = new LinkedHashMap<>();
    Set<String>keys = new HashSet<>();
    @Autowired
    KeyDataRepository keyDataRepository;

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
       KeyData keyData = keyDataRepository.findByKey(key);
       if(keyData==null)
           return new ArrayList<>();

       return keyData.getValues();
    }

    public void addKey(String key,String value,Direction direction){
        checkCapacity();
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
    public List<String> getAllKeys(String pattern) {
        List<String> toMatch=null;
        if(keys.size()>=capacity || keys.isEmpty())
            toMatch =  keyDataRepository.findAll().stream().map(KeyData::getKey).collect(Collectors.toList());
        else toMatch = keys.stream().collect(Collectors.toList());
        return toMatch.stream().filter(keys->keys.matches(pattern)).collect(Collectors.toList());

    }

    private void checkCapacity(){

        if(orderedMap.size()>= capacity)
        {   String keyToRemove=null;
            for(Map.Entry<String,List<String>>entry :orderedMap.entrySet()){
                keyToRemove=entry.getKey();
                break;
            }
            //keyToRemove =  orderedMap.entrySet().iterator().next();
            orderedMap.remove(keyToRemove);

            if(keyToRemove!=null)
             keys.remove(keyToRemove);
        }
    }

    public enum Direction{
        right,left
    }
}
