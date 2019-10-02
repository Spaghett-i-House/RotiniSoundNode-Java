package com.company;

import java.nio.ByteBuffer;

public class Message {
    private short opcode;
    private ByteBuffer message;

    Message(Message orig){
        this.opcode = orig.opcode;
        this.message = orig.message;
    }

    Message(ByteBuffer messageBytes){
        this.opcode = messageBytes.getShort();
        this.message = messageBytes.slice();
    }

    Message(int opcode){
        this.opcode = (short)opcode;
        this.message = null;
    }

    public int getOpcode(){
        return (int) this.opcode;
    }

    public ByteBuffer getMessage(){
        return this.message;
    }
}
