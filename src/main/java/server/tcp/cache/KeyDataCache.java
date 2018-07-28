package server.tcp.cache;

import javafx.util.Pair;

import java.util.List;
import java.util.Set;

/**
 * Created by Jary on 7/27/2018.
 */
public interface KeyDataCache {
    void addKey(String key,String value,keyDataCacheImp.Direction direction);
    Set<String>getKeys();
    List<String>get(String key);
    void set(String key,List<String>values);
    List<String> getAllKeys(String pattern);

}
