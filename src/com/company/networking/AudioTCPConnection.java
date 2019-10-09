package com.company.networking;

import com.company.audio.AudioDevice;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.BlockingQueue;

public class AudioTCPConnection implements Runnable{
    private Socket sockfd;
    private DataOutputStream outstr;
    private BlockingQueue<byte[]> bq;
    private Thread myThread;
    private boolean stop = false;

    public AudioTCPConnection(InetAddress address, int port, String audioDevice) throws IOException{
        System.out.println("Creating TCP connection");
        this.sockfd = new Socket(address, port);
        this.outstr = new DataOutputStream(this.sockfd.getOutputStream());
        System.out.println("TCP Connection Created");
        AudioDevice ad = AudioDevice.getAudioDeviceInstance();
        try {
            this.bq = ad.getOpenDataLine(audioDevice);
            this.myThread = new Thread(this);
            this.myThread.start();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void run() {
        System.out.println("Starting audio Stream");
        while(!this.stop){
            try {
                byte[] audioBytes = this.bq.take();
                this.outstr.write(audioBytes);
            } catch (InterruptedException e){
                this.stop = true;
                break;
            } catch (IOException e){
                break;
            }
        }
        System.out.println("Audio Stream ending");
    }

    public boolean isClosed(){return this.stop;}

    public void close(){
        this.stop = true;
    }
}
