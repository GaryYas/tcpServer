package server.tcp.db.dal;

import java.util.List;

/**
 * Created by Jary on 7/27/2018.
 */
public interface KeyValueDal {

    public void rightAdd(String key,String value);
    public void leftAdd(String key,String value);
    public void set(String key, List<String> values);
    public List<String> get(String key);
}
