package com.company.networking;

import com.company.networking.protocol.Message;
import com.company.networking.protocol.MessageExpectsResponse;
import com.company.networking.protocol.MessageInfo;
import com.company.networking.protocol.MessageStartStream;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

enum Opcodes {
    UNKNOWN,
    GETINFO,
    PING,
    STARTAUDIOSTREAM,
    ENDAUDIOSTREAM;

    private static Opcodes[] values = null;
    public static Opcodes fromInt(int i){
        if(Opcodes.values == null){
            Opcodes.values = Opcodes.values();
        }
        if(i > values.length){
            return Opcodes.values[0];
        }
        return Opcodes.values[i];
    }
}

public class UDPServer implements Runnable{
    private DatagramSocket serverSocket;
    private byte[] receivedBytes;
    private byte[] sendBytes;
    private int DEFAULTLENGTH = 1024;
    private boolean active = false;
    private AudioTCPManager audioStreamManager;

    public UDPServer(int port, int messageLength) throws UnknownHostException, IOException {
        this.serverSocket = new DatagramSocket(port);
        this.receivedBytes = new byte[messageLength];
        this.sendBytes = new byte[messageLength];
        this.active = true;
        this.audioStreamManager = new AudioTCPManager();
        Thread myThread = new Thread(this);
        myThread.start();
        System.out.println(String.format("The server is listening on %d", port));
    }

    public UDPServer(int port) throws IOException{
        this.serverSocket = new DatagramSocket(port);
        this.receivedBytes = new byte[this.DEFAULTLENGTH];
        this.sendBytes = new byte[this.DEFAULTLENGTH];
        this.active = true;
        this.audioStreamManager = new AudioTCPManager();
        Thread myThread = new Thread(this);
        myThread.start();
        System.out.println(String.format("The server is listening on %d", port));
    }

    public void run(){
        while(this.active) {
            try {
                DatagramPacket receivePacket = new DatagramPacket(this.receivedBytes, receivedBytes.length);
                this.serverSocket.receive(receivePacket);
                //System.out.println("Rec");
                ByteBuffer newByteBuffer = ByteBuffer.wrap(receivePacket.getData());
                //System.out.println("Buf");
                Message receivedMessage = new Message(newByteBuffer);
                //System.out.println("MSG");
                this.handleMessage(receivedMessage, receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    private void handleMessage(Message newMessage, DatagramPacket packet){
        switch(Opcodes.fromInt(newMessage.getOpcode())){
            case UNKNOWN:
                System.out.println("UNKNOWN");
                break;
            case GETINFO: //GetInfo
                System.out.println("INFO");
                newMessage = new MessageInfo(newMessage);
                break;
            case PING: //ping
                System.out.println("PING");
                break;
            case STARTAUDIOSTREAM:
                System.out.println("ASTEAMSTART");
                newMessage = new MessageStartStream(newMessage);
                try {
                    int port = ((MessageStartStream) newMessage).getPort();
                    String deviceName = ((MessageStartStream) newMessage).getDeviceName();
                    InetAddress ipaddr = packet.getAddress();
                    //System.out.println(String.format("%s %s %d", ipaddr, deviceName, port));
                    System.out.println(this.audioStreamManager);
                    this.audioStreamManager.createConnection(ipaddr, port, deviceName);
                } catch (IOException e){
                    e.printStackTrace();
                    return;
                }
                break;
            case ENDAUDIOSTREAM:
                System.out.println("ASTREAMSTOP");
                break;
        }
        if(newMessage instanceof MessageExpectsResponse){
            MessageExpectsResponse msg = (MessageExpectsResponse) newMessage;
            try {
                byte[] responseMSG = msg.getResponse();
                DatagramPacket responsePacket = new DatagramPacket(
                        responseMSG,
                        responseMSG.length,
                        packet.getAddress(),
                        packet.getPort()
                );
                try{
                    this.serverSocket.send(responsePacket);
                } catch(IOException e){
                    e.printStackTrace();
                    System.out.println("Could not send packet");
                    return;
                }
            } catch (IOException e){
                e.printStackTrace();
                return;
            }
        }
    }

    private boolean attemptReconnect(){
        return true;
    }
}
