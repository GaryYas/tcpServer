package server.tcp.requestHandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.tcp.cache.KeyDataCache;
import server.tcp.cache.keyDataCacheImp;
import server.tcp.db.dal.KeyValueDal;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Jary on 7/27/2018.
 */
@Component
public class RequestExecutor {

    @Autowired
    KeyValueDal keyDataDal;
    @Autowired
    KeyDataCache keyDataCache;

    ReadWriteLock lock = new ReentrantReadWriteLock();

    public void addFromRight(String key, String value){
        try {
            lock.writeLock().lock();
            keyDataCache.addKey(key, value, keyDataCacheImp.Direction.right);
            keyDataDal.rightAdd(key, value);
        }
        finally {
            lock.writeLock().unlock();
        }


    }

    public void addFromLeft(String key, String value){
        try {
              lock.writeLock().lock();
              keyDataCache.addKey(key,value, keyDataCacheImp.Direction.left);
              keyDataDal.leftAdd(key,value);

        }
        finally {
            lock.writeLock().unlock();
        }

    }

    public List<String> get(String key){
      try {
          lock.readLock().lock();
          List<String> values = keyDataCache.get(key);
          if (values == null) {
              values = keyDataDal.get(key);
              lock.readLock().unlock();
              lock.writeLock().lock();
              keyDataCache.set(key, values);
          }
          return values;
      }
         finally{
          try {
              lock.readLock().unlock();
              lock.writeLock().unlock();
          }catch (IllegalMonitorStateException e){}
         }
      }

    public void set(String key,List<String>values){
       try {
           lock.writeLock().lock();
           keyDataDal.set(key, values);
           keyDataCache.set(key, values);
       }
       finally {
           lock.writeLock().unlock();
       }
    }
    public List<String>getAllKeys(String key){
        try{
            lock.readLock().lock();
             return keyDataCache.getAllKeys(key);}
        finally {
                 lock.readLock().unlock();
        }
    }

}
