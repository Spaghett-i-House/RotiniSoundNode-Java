package com.company.networking.protocol;
import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;

import com.company.audio.AudioDevice;
import javafx.util.Pair;
import org.json.*;

public class MessageInfo extends MessageExpectsResponse {
    /**
     * MessageInfo Protocol:
     *  opcode: 2bytes = 1
     *  stringjson: string
     *
     *  stringjson fields:
     *      MyName: String
     *      devices: [devicename]
     *      functions: [functionname: [(argname, argtype)]
     */
    private JSONObject responseJSON;
    private Map<String, Integer> devices;
    private Map<String, Map<String, String>> functions;

    public MessageInfo(Message orig){
        // initialize parent
        //
        super(orig);
    }

    /*public MessageInfo(ArrayList<String> devices, Map<String, Map<String, String>> availableCommands) {
        super(1);
        this.responseJSON = new JSONObject();
        this.responseJSON.put("devices", devices);
        this.responseJSON.put("functions", availableCommands);
    }*/

    public JSONObject getResponseData(AudioDevice audiodevice){
        //reach out to applicable other libraries for information
        AudioDevice audio = AudioDevice.getAudioDeviceInstance();
        Object[] audioNameList = audio.getSourceNames().toArray();
        JSONObject devJSON = new JSONObject();
        devJSON.put("myid", "asdasd");
        devJSON.put("devices", audioNameList);
        devJSON.put("functions", "{}"); // TODO: POPULATE
        return devJSON;
    }

    @Override
    public byte[] getResponse() throws IOException{
        JSONObject devJSON = this.getResponseData(AudioDevice.getAudioDeviceInstance());
        byte[] devJsonBytes = devJSON.toString().getBytes();
        int byteLen = devJsonBytes.length;
        ByteBuffer buf = ByteBuffer.allocate(byteLen+4); //length of json + opcode and messagelength
        buf.putShort((short)1);
        buf.putShort((short)byteLen);
        buf.put(devJsonBytes);
        return buf.array();
    }
}
