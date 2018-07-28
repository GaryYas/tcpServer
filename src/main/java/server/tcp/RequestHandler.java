package server.tcp;

import java.net.Socket;

/**
 * Created by Jary on 7/26/2018.
 */
public interface RequestHandler extends Runnable {

    void handle();
    void setSocket(Socket socket);
}
