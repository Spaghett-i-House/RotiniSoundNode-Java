package com.company;
import java.util.Map;

public class MessageInfo extends Message {
    //there are no extra components if it is being made from received message
    private Map<String, Integer> devices;
    private Map<String, Map<String, String>> functions;

    MessageInfo(Message orig){
        // initialize parent
        super(orig);
        char currentchar = orig.getMessage().getChar();
        while(currentchar != 0x00){
            if(currentchar == ':'){
                int amtBauds =
            }
        }

    }

    MessageInfo(Map<String, Integer> devices, Map<String, Map<String, String>> availableCommands) {
        super();
    }

    public MessageInfo getResponseData(){
        //reach out to applicable other libraries for information
        return this;
    }
}
