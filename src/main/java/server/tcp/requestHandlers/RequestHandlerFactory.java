package server.tcp.requestHandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jary on 7/27/2018.
 *
 * factory class for request handler
 * reqeust map is built during spring boot up each request is registers itself by its type.
 */
@Component
public class RequestHandlerFactory {

    private Map<String,RequestAction> requestHandlerMap = new HashMap<>();

    @Autowired
    public void setMapRequest(List<RequestAction> requestHandlers){
        for(RequestAction req:requestHandlers){
            requestHandlerMap.putIfAbsent(req.getType().name(),req);
        }
    }

    //return the correct handler due to compatible command,which is the key in the map
    public RequestAction geRequestHandler(String request){
        RequestAction requestHandler =requestHandlerMap.get(request);
        if(requestHandler ==null)
           return requestHandlerMap.get("notSupported");
        return requestHandler;
    }
}

