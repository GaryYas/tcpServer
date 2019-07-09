package server.tcp.requestHandlers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.tcp.requestHandlers.types.Request;

/**
 * Created by Jary on 7/27/2018.
 *
 * returning all the keys in the database or cache according to the pattern
 */
@Component
public class getAllKeysRequest implements RequestAction {

    @Autowired
    RequestExecutor requestExecutor;
    Logger logger = Logger.getLogger(getAllKeysRequest.class);

    @Override
    public String handleRequest(String[] args) {

        logger.info("is going to execute add get all keys request with pattern:" + args[0]);
        if(args.length!=1)
            return "invalid params";

        String output = requestExecutor.getAllKeys(args[0]).toString();
        return output;
    }

    @Override
    public Request getType() {
        return Request.getAllKeys;
    }
}
