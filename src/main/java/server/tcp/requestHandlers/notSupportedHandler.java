package server.tcp.requestHandlers;

import org.springframework.stereotype.Component;
import server.tcp.requestHandlers.types.Request;

/**
 * Created by Jary on 7/28/2018.
 */
@Component
public class notSupportedHandler implements RequestAction {
    @Override
    public String handleRequest(String[] args) {
        return "error in action or with arguments";
    }

    @Override
    public Request getType() {
        return Request.notSupported;
    }
}
