package server.tcp.requestHandlers;

import server.tcp.requestHandlers.types.Request;

/**
 * Created by Jary on 7/27/2018.
 */
public interface RequestAction {
    String handleRequest(String[] args);
    Request getType();
}
