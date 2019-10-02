package com.company;
import java.util.ArrayList;
import java.util.Map;
import org.json.*;

public class MessageInfo extends Message {
    /**
     * MessageInfo Protocol:
     *  opcode: 2bytes = 1
     *  stringjson: string
     *
     *  stringjson fields:
     *      devices: [devicename]
     *      functions: [functionname: [(argname, argtype)]
     */
    private JSONObject responseJSON;
    private Map<String, Integer> devices;
    private Map<String, Map<String, String>> functions;

    MessageInfo(Message orig){
        // initialize parent
        //
        super(orig);
    }

    MessageInfo(ArrayList<String> devices, Map<String, Map<String, String>> availableCommands) {
        super(1);
        this.responseJSON = new JSONObject();
        this.responseJSON.put("devices", devices);
        this.responseJSON.put("functions", availableCommands);
    }

    public MessageInfo getResponseData(AudioDevice audiodevice){
        //reach out to applicable other libraries for information

        return this;
    }
}
