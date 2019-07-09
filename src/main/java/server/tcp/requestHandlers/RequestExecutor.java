package server.tcp.requestHandlers;

import org.apache.log4j.Logger;
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
 * spring bean that handles addig from left adding from right
 * getting and setting the value to the key
 * uses reentratReadWritelock for atomicity of the critical operation on the chache that are performed by different threads
 */
@Component

public class RequestExecutor {

    @Autowired
    KeyValueDal keyDataDal;
    @Autowired
    KeyDataCache keyDataCache;
    Logger logger = Logger.getLogger(RequestExecutor.class);

    ReadWriteLock lock = new ReentrantReadWriteLock();

    public void addFromRight(String key, String value){
        try {
            logger.info("is going to add to cache and database key:" + key + " and value:" +value );

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
            logger.info("is going to add to cache and database key:" + key + " and value:" +value );
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

          logger.info("is going to read all the values matching to the key:" + key);
          lock.readLock().lock();
          List<String> values = keyDataCache.get(key);
          if (values == null) {
              logger.info("there are no value for the key:" + key + " in the cache ,is going to read from db and persist it in cache" );
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
           logger.info("is going to set key:" + key + " with values:" + values);

           lock.writeLock().lock();
           keyDataDal.set(key, values);
           keyDataCache.set(key, values);
       }
       finally {
           lock.writeLock().unlock();
       }
    }
    public List<String>getAllKeys(String pattern){
        try{
            logger.info("is going to read all the keys according to pattern:"  +  pattern + " from cache if possible");
            lock.readLock().lock();
             return keyDataCache.getAllKeys(pattern);}
        finally {
                 lock.readLock().unlock();
        }
    }

}
