package com.company;

import com.company.audio.AudioDevice;
import com.company.networking.UDPServer;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public class Main {

    public static void main(String[] args){
        try {
            UDPServer myServer = new UDPServer(9877);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void usingAudioDataTest() throws InterruptedException{
        AudioDevice audiod = AudioDevice.getAudioDeviceInstance();
        System.out.println(audiod.getSourceNames());
        String firstname = audiod.getSourceNames().iterator().next();
        System.out.println(firstname);
        BlockingQueue<byte[]> audioQ = audiod.getOpenDataLine(firstname);
        while(true){
            try{
                byte[] b = audioQ.take();
                for(byte i: b){
                    System.out.print(String.format("%d ",i));
                }
                System.out.println();
            } catch (NoSuchElementException e){
                System.out.println("Nothing to remove");
            }
        }
    }

    private static String findName(ArrayList<String> names, String namecontains){
        for (String name: names){
            if(name.contains(namecontains)){
                return name;
            }
        }
        return null;
    }
}

// testing stuff
        /*System.out.println(AD.getTargetAudioSources());
        System.out.println(AD.getSourceAudioSources());
        TargetDataLine line = AD.getTargetAudioSources().get("Loopback [plughw:2,1]");
        SourceDataLine outline = AD.getSourceAudioSources().get("default [default]");
	    System.out.println(line.getFormat());

	    AudioFormat format = new AudioFormat(
                44100f,
                16,
                2,
                true,
                true);
	    try {
            line.open(format);
            line.start();
            outline.open(format);
            outline.start();
        } catch (LineUnavailableException e){
	        e.printStackTrace();
	        return;
        }
	    int bufferlength = 16;
	    byte[] buffer = new byte[bufferlength];
	    int counter = 0;
	    while(true){
	        int red = line.read(buffer, 0, buffer.length);
	        //if pulseaudio is configured correctly should contain all system sound
	        //System.out.println(red);
	        int total = 0;*/
	        /*for(byte b: buffer) {
                total += b;
            }*/
	        /*System.out.println(total/bufferlength);
            for (int i = 0; i < 16; i++) {
                total+=buffer[i];
                System.out.print(buffer[i]);
                System.out.print(":");
            }
            System.out.println(total/16);*/
    //}
// write your code here
        /*AudioDevice AD = AudioDevice.getAudioDeviceInstance();
        ArrayList<String> names = (AD.getAudioSources());
        System.out.println(names);
        String name = findName(names, "default");
        System.out.println(name);
        TargetDataLine line = AD.getAudioDataLine(name, 44100, 16);
        while(true){
            byte [] buf = AD.makeBuffer(44100, 16);
            int count = line.read(buf, 0, buf.length);
            if(count > 0){
                System.out.println(count);
            }
            else{
                //System.out.println("no count");
            }
        }*//*
        AudioFormat format = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                44100,
                16, 2, 4,
                44100, false);

        TargetDataLine line = null;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class,
                format); // format is an AudioFormat object


        SourceDataLine line2 = null;
        DataLine.Info info2 = new DataLine.Info(SourceDataLine.class,
                format); // format is an AudioFormat object

        if (!AudioSystem.isLineSupported(info))
        {
            System.out.println("line not supported:" + line );
        }

        try
        {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line2 = (SourceDataLine) AudioSystem.getLine(info2);
            line.open(format);
            line2.open(format);
            System.out.println("line opened:" + line);

            line.start();

            byte[] buffer = new byte[1024];
            int ii = 0;
            int numBytesRead = 0;
            while (true) {
                // Read the next chunk of data from the TargetDataLine.
                numBytesRead =  line.read(buffer, 0, buffer.length);
                line2.write(buffer, 0, numBytesRead);
                System.out.println(line2.getLevel());
                //System.out.println("\nnumBytesRead:" + numBytesRead);
                if (numBytesRead == 0) continue;
                // following is a quickie test to see if content is only 0 vals
                // present in the data that was read.

                for (int i = 0; i < 16; i++)
                {
                    if (buffer[i] != 0)
                        System.out.print(".");
                    else
                        System.out.print("0");
                }
                System.out.println();
            }

        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
            //...
        }*/
