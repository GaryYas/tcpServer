package server.tcp.utils;

import javafx.util.Pair;
import org.apache.log4j.Logger;
import server.tcp.Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jary on 7/26/2018.
 */
public class serverUtils {


   static Logger logger = Logger.getLogger(serverUtils.class);
    //parses the request from the socket buffered input stream and pair of command and arguments
    public static Pair<String,String[]> getParsedMessage(BufferedReader reader) throws Exception {

        {
                    String line = reader.readLine();
                    logger.info("read line with content:" + line + " going to parse it");
                    if(line==null)
                        throw new Exception("closed");

                    int paramIndex = line.trim().indexOf('(');
                    String command = line.trim();
                    String lineToEvaluate = command;
                    int endOfParams = line.length()-1;



                    if (paramIndex<3 || line.trim().lastIndexOf(')')!=endOfParams)
                        return new Pair<>(null,null);

                    command = line.substring(0, paramIndex);
                    lineToEvaluate = line.substring(paramIndex+1,endOfParams).trim();

                    String[] args = lineToEvaluate.split(",");

                    logger.info("finished parsing the line:" + line + " command is:" + command + " , arguments are:" + args);

                    return new Pair<>(command,args);
        }
    }
    public static List<String>parseSetArgs(List<String>arguments){

        logger.info("is going to parse arguments for set action:" + arguments);

        int last = arguments.size()-1;
        arguments.set(0,arguments.get(0).substring(1));
        int sizeOfLastArg = arguments.get(last).length();
        arguments.set(last,arguments.get(last).substring(0,sizeOfLastArg-1));
        return arguments;
    }
}
