package com.company.networking;

import java.awt.desktop.SystemSleepEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class AudioTCPManager {

    private Map<String, AudioTCPConnection> connectionMap;

    public AudioTCPManager(){
        this.connectionMap = new HashMap<String, AudioTCPConnection>();
    }

    public void createConnection(InetAddress address, int port, String deviceName) throws IOException {
        System.out.println("Creating new connection");
        AudioTCPConnection acon = new AudioTCPConnection(address, port, deviceName);
        this.connectionMap.put(deviceName, acon);
        this.awaitCloseConnection(deviceName, acon);
    }

    public void closeConnection(String deviceName){
        this.connectionMap.get(deviceName).close();
        this.connectionMap.remove(deviceName);
    }

    private void awaitCloseConnection(String deviceName, AudioTCPConnection connection){
        while(true){
            try {

                if(connection.isClosed()){
                    this.connectionMap.remove(deviceName);
                    break;
                }
                Thread.sleep(1000);
            } catch (InterruptedException e){
                System.out.println("Thread ded");
                break;
            }
        }
    }
}
