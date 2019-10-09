package com.company.audio;

import javax.sound.sampled.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public class AudioDevice {
    /**
     * AudioDevice: Provides an easy way to access system audio input device streams
     *  Follows singleton design pattern
     */
    private static final AudioDevice SINGLE_INSTANCE = new AudioDevice();

    private Map<String, TargetDataLine> audioPortDataLines;
    private Map<String, AudioHandlerThread> openAudioPortDataLines;

    private AudioDevice(){
        /**
         * Constructor: creates empty member variables, populates audio line map
         */
        this.audioPortDataLines = new HashMap<String, TargetDataLine>();
        this.openAudioPortDataLines = new HashMap<String, AudioHandlerThread>();
        this.populateSystemAudioSources();
    }

    public static AudioDevice getAudioDeviceInstance(){
        /**
         * getAudioDeviceInstance: there may be errors if multiple lines are opened,
         *  so this makes it to where there is only ever 1 instance of the AudioDevice class
         *
         *  --Singleton design pattern--
         */
        return SINGLE_INSTANCE;
    }

    private void populateSystemAudioSources(){
        /**
         * populateSystemAudioSources: updates the available input lines
         *      Only grabs lines that are for input, look at java mixers
         */
        Mixer.Info[] mixers = AudioSystem.getMixerInfo(); //gets info on all mixers

        for(Mixer.Info mixerInfo: mixers){ //iterate through all mixers by info
            Mixer mixer = AudioSystem.getMixer(mixerInfo); // get mixer from info
            Line.Info[] infolist = mixer.getTargetLineInfo();
            if(infolist.length == 0){ //check that this line does have an audio input
                continue;
            }
            System.out.println(
                    String.format("Received %d input lines from %s",
                            infolist.length,
                            mixerInfo.getName()));
            try {
                //get the actual line instead of the mixer
                Line.Info firstLineInfo = infolist[0];
                Line firstLine = mixer.getLine(firstLineInfo);
                if (firstLine instanceof TargetDataLine) { //check that it is an input line
                    //add input line to map
                    this.audioPortDataLines.put(mixerInfo.getName(), (TargetDataLine) firstLine);
                } else {
                    System.out.println("Not a targetdataline");
                    continue;
                }
            } catch(LineUnavailableException e){
                //line does not exist?
                e.printStackTrace();
                continue;
            }
        }
    }

    public Set<String> getSourceNames(){
        /**
         * getSourceNames: grabs a set of all input line device names
         * @returns: Set<String> a set containing all audio device names
         */
        return this.audioPortDataLines.keySet();
    }

    public BlockingQueue<byte[]> getOpenDataLine(String dataLineName) throws InterruptedException{
        /**
         * getOpenDataLine: give access to a queue that gets filled with audio data from line
         * @returns: Queue<byte[]> a queue which gets continuously populated with audio data or null for error
         */
        if(!this.audioPortDataLines.containsKey(dataLineName)){
            //this is not a valid name of a audio source
            return null;
        }
        // audio source name is a valid source
        TargetDataLine gotLine = this.audioPortDataLines.get(dataLineName);
        try {
            AudioHandlerThread newAudioThread = new AudioHandlerThread(gotLine);
            this.openAudioPortDataLines.put(dataLineName, newAudioThread);
            return newAudioThread.getByteQueue();
        } catch (LineUnavailableException e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean closeDataLine(String dataLineName){
        /**
         * closeDataLine: closes a data line and stops the addition of data to its associated queues
         */
        if(!this.openAudioPortDataLines.containsKey(dataLineName)){
            return false;
        }
        // the line thread exists
        this.openAudioPortDataLines.get(dataLineName).close();
        return true;
    }


}
