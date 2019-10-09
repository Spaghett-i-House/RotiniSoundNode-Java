package com.company.networking.protocol;

import java.io.IOException;

public class MessageExpectsResponse extends Message{
    public MessageExpectsResponse(Message orig){
        super(orig);
    }

    public byte[] getResponse() throws IOException{
        throw(new IOException("THis has not yet been implemented"));
    }
}
