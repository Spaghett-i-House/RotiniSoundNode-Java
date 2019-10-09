package com.company.networking.protocol;

import com.company.networking.AudioTCPConnection;

import java.nio.Buffer;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class MessageStartStream extends Message{
    private short port;
    private String deviceName;

    public MessageStartStream(Message orig){
        super(orig);
        ByteBuffer msg = orig.getMessage();
        int deviceNameLength = this.getMessageLength() - 2;
        this.port = msg.getShort();
        this.deviceName = "";
        byte[] msgarr = msg.array();
        for(int i = 6; i<msgarr.length; i++){
            if(msgarr[i] == 0){
                break;
            }
            else{
                this.deviceName += (char)msgarr[i];
            }
        }
        System.out.println(String.format("Recieved stream request for %s on port %d"
                                         , this.deviceName, this.port));
        //this.deviceName = msg.asCharBuffer().t;
        //System.out.println(this.deviceName);
        // get length of device name string, message length - 2 for port
    }

    public int getPort(){
        return this.port;
    }

    public String getDeviceName(){
        return this.deviceName;
    }
}
