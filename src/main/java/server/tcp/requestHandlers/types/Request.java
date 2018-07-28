package server.tcp.requestHandlers.types;

/**
 * Created by Jary on 7/27/2018.
 */
public enum Request {

    get("get"),
    getAllKeys("getAllKeys"),
    rightAdd("rightAdd"),
    leftAdd("leftAdd"),
    set("set"),
    notSupported("notSupported");


    String handlerType;

     Request(String handlerType){
        this.handlerType=handlerType;
    }
}
