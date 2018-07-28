package server.tcp;

import javafx.util.Pair;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import server.tcp.requestHandlers.RequestAction;
import server.tcp.requestHandlers.RequestHandlerFactory;

import java.io.*;
import java.net.Socket;

import static server.tcp.utils.serverUtils.getParsedMessage;

/**
 * Created by Jary on 7/26/2018.
 */
@Component
@Data
@Scope("prototype")
public class RequestHandlerImp implements RequestHandler {


    private Socket socket;

    @Autowired
    RequestHandlerFactory handlerFactoryRequest;

    public RequestHandlerImp(){
    }

    @Override
    public void run() {
        handle();
    }

    public void handle(){
        /*try(DataInputStream inFromClient = new DataInputStream(socket.getInputStream());
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
        )*/
        try(InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            OutputStream outPut = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outPut, true))

        {
            while (!socket.isClosed()) {
                try {
                    Pair<String, String[]> commandParamsPair = getParsedMessage(reader);
                    RequestAction req = handlerFactoryRequest.geRequestHandler(commandParamsPair.getKey());
                    String result = req.handleRequest(commandParamsPair.getValue());
                    writer.println(result);
                    writer.flush();
                }
                   catch(Exception e){
                    if(e.getMessage().equals("closed"))
                        socket.close();
                   }
            }
        }

        catch (Exception e ) {
            System.out.println("error: " + e);
        }
    }
}
