package server.tcp.requestHandlers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.tcp.requestHandlers.types.Request;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static server.tcp.utils.serverUtils.parseSetArgs;

/**
 * Created by Jary on 7/27/2018.
 */
@Component
public class setRequest implements RequestAction {

    @Autowired
    RequestExecutor requestExecutor;
    Logger logger = Logger.getLogger(setRequest.class);

    @Override
    public String handleRequest(String[] args) {

        if(args.length<2)
            return "incorrect number of arguments";

       String firstArg = args[0];
       List<String> paramList = Arrays.stream(args).skip(1).collect(Collectors.toList());
       requestExecutor.set(firstArg,parseSetArgs(paramList));

        return "set successfully";
    }

    @Override
    public Request getType() {
        return Request.set;
    }
}
