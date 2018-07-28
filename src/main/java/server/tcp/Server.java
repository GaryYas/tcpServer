package server.tcp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Jary on 7/26/2018.
 */
@Component
public class Server {

    private static final int          serverPort   = 54321;
    private ServerSocket serverSocket = null;

    @Autowired
    TaskExecutor requestExecutor;
    @Autowired
    private ApplicationContext applicationContext;



    public void startServer(){
        openSocket();

        try{
            while(true) {
                Socket socket = serverSocket.accept();
                 RequestHandler handler = (RequestHandler)applicationContext.getBean(RequestHandlerImp.class);
                handler.setSocket(socket);
                requestExecutor.execute(handler);

            }
        }
        catch (IOException ex){
            System.out.println(ex);
        }

    }

    private void openSocket(){

      try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 54321", e);
        }

    }

}
