package com.company.audio;

import javax.sound.sampled.*;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AudioHandlerThread implements Runnable {
    private TargetDataLine audioLine;
    private AudioFormat audioLineFormat;
    private BlockingQueue<byte[]> byteQueue;
    private boolean closeFlag;

    public AudioHandlerThread(TargetDataLine openaudioline) throws LineUnavailableException{
        this.audioLine = openaudioline;
        this.byteQueue = new ArrayBlockingQueue<byte[]>(10);
        TargetDataLine.Info lineInfo = (TargetDataLine.Info)this.audioLine.getLineInfo();
        this.audioLineFormat = this.audioLine.getFormat();
        System.out.println(this.audioLineFormat);
        this.audioLine.open(this.audioLineFormat);
        //for testing
        /*this.audioLine.open(new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
                44100,
                16,
                2,
                4,
                4,
                false
        ));*/
        this.audioLine.start();
        this.closeFlag = false;
        Thread audioHandlerThread = new Thread(this);
        audioHandlerThread.start();
    }

    public BlockingQueue<byte[]> getByteQueue(){
        return this.byteQueue;
    }

    public void close(){
        this.closeFlag = true;
    }

    public void run(){
        byte[] buffer = new byte[this.audioLineFormat.getSampleSizeInBits()];
        while(!this.closeFlag){
            int readamt = this.audioLine.read(buffer, 0, buffer.length);
            if(readamt < buffer.length){
                System.out.println("Audio device not filling buffer");
            }
            try {
                this.byteQueue.add(buffer.clone());
            } catch (IllegalStateException e){
                //e.printStackTrace();
                //System.out.println("Queue is probably full");
                continue;
            }
            //System.out.print(this.byteQueue.poll());
        }
        this.audioLine.stop();
        this.audioLine.close();
        System.out.println("An audio input thread has stopped");
    }
}
