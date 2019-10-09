package com.company.networking.protocol;

import javafx.util.Pair;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

public class Message {
    private short opcode;
    private short messageLength;
    private ByteBuffer message;

    public Message(Message orig){
        this.opcode = orig.opcode;
        this.messageLength = orig.messageLength;
        this.message = orig.message;
    }

    public Message(ByteBuffer messageBytes){
        this.opcode = messageBytes.getShort();
        this.messageLength = messageBytes.getShort();
        this.message = messageBytes.slice();
    }

    public Message(int opcode){
        this.opcode = (short)opcode;
        this.message = null;
    }

    public int getOpcode(){
        return (int) this.opcode;
    }
    public int getMessageLength(){ return (int) this.messageLength; }
    public ByteBuffer getMessage(){
        return this.message;
    }

    /*public Pair<ByteBuffer, Integer> getResponse() {
        ByteBuffer toRet = ByteBuffer.allocate(1024);
        toRet.putShort(this.opcode);
        toRet.putShort((short)0); //length of message so far is 0
        return new Pair<ByteBuffer, Integer>(toRet, 0);
    }*/
}
