package com.company;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

enum Opcodes {
    GETINFO,
    PING,
    STARTAUDIOSTREAM,
    ENDAUDIOSTREAM;

    private static Opcodes[] values = null;
    public static Opcodes fromInt(int i){
        if(Opcodes.values == null){
            Opcodes.values = Opcodes.values();
        }
        return Opcodes.values[i];
    }
}

public class UDPServer {
    private DatagramSocket serverSocket;
    private byte[] receivedBytes;
    private byte[] sendBytes;
    private int DEFAULTLENGTH = 1024;
    private boolean active = false;

    UDPServer(int port, int messageLength) throws UnknownHostException, IOException {
        this.serverSocket = new DatagramSocket(port);
        this.receivedBytes = new byte[messageLength];
        this.sendBytes = new byte[messageLength];
        this.active = true;
    }

    UDPServer(int port) throws IOException{
        this.serverSocket = new DatagramSocket(port);
        this.receivedBytes = new byte[this.DEFAULTLENGTH];
        this.sendBytes = new byte[this.DEFAULTLENGTH];
        this.active = true;
    }

    private void receiveData(){
        while(this.active) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(this.receivedBytes, receivedBytes.length);
                this.serverSocket.receive(receivePacket);
                ByteBuffer newByteBuffer = ByteBuffer.allocate(receivePacket.getLength());
                Message receivedMessage = new Message(newByteBuffer);
                this.handleMessage(receivedMessage);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    private void handleMessage(Message newMessage){
        switch(Opcodes.fromInt(newMessage.getOpcode())){
            case GETINFO: //GetInfo

                break;
            case PING: //ping
                break;
            case STARTAUDIOSTREAM:
                break;
            case ENDAUDIOSTREAM:
                break;
        }
    }

    private boolean attemptReconnect(){

    }
}
