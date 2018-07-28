package server.tcp.requestHandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.tcp.requestHandlers.types.Request;

/**
 * Created by Jary on 7/27/2018.
 */

@Component
public class addLeftRequest implements RequestAction {

    @Autowired
    RequestExecutor requestExecutor;

    @Override
    public String handleRequest(String[] args) {
        //handel exceptions
        //
        if(args.length!=2)
            return "incorrect number of arguments";
        requestExecutor.addFromLeft(args[0],args[1]);
        return "added from left";
    }

    @Override
    public Request getType() {
        return Request.leftAdd;
    }
}
