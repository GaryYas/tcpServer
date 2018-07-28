package server.tcp.requestHandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.tcp.requestHandlers.types.Request;

/**
 * Created by Jary on 7/27/2018.
 */
@Component
public class getAllKeysRequest implements RequestAction {

    @Autowired
    RequestExecutor requestExecutor;

    @Override
    public String handleRequest(String[] args) {

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
