package server.tcp.requestHandlers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.tcp.requestHandlers.types.Request;

import java.util.List;

/**
 * Created by Jary on 7/27/2018.
 */
@Component
public class getRequest implements RequestAction {

    @Autowired
    RequestExecutor requestExecutor;
    Logger logger = Logger.getLogger(getRequest.class);
    @Override
    public String handleRequest(String[] args) {
        // handel exceptions
        if(args.length!=1 || args[0].equals(""))
            return "bad command or args count try again";

        List<String> values = requestExecutor.get(args[0]);
       return values.isEmpty() ? "not exist":values.toString();
    }

    @Override
    public Request getType() {
        return Request.get;
    }
}
