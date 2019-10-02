package com.company;
import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AudioDevice {

    private static final AudioDevice SINGLE_INSTANCE = new AudioDevice();

    private Map<String, Mixer> AudioPortNameMap;
    private AudioDevice(){
        //this.AudioPortNameMap = new HashMap<String, Mixer>();
        //this.getAudioSources();
    }

    public Map<String, TargetDataLine> getTargetAudioSources(){
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        Map<String, TargetDataLine> audioinputs = new HashMap<String, TargetDataLine>();
        AudioFormat format = this.getAudioFormat(44100, 16);
        for(Mixer.Info mixerinf: mixers){
            Mixer mixer = AudioSystem.getMixer(mixerinf);
            try{
                Line.Info[] infolist = mixer.getTargetLineInfo();
                if(infolist.length == 0){
                    continue;
                }
                Line.Info info = infolist[0];
                Line mline = mixer.getLine(info);
                if(mixer.getLine(info) instanceof TargetDataLine){

                    audioinputs.put(mixerinf.getName(), (TargetDataLine)mline);
                }
            } catch(LineUnavailableException e){
                e.printStackTrace();
                return null;
            }
        }
        return audioinputs;
    }

    public Map<String, SourceDataLine> getSourceAudioSources(){
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        Map<String, SourceDataLine> audioinputs = new HashMap<String, SourceDataLine>();
        AudioFormat format = this.getAudioFormat(44100, 16);
        for(Mixer.Info mixerinf: mixers){
            Mixer mixer = AudioSystem.getMixer(mixerinf);
            try{
                Line.Info[] infolist = mixer.getSourceLineInfo();
                if(infolist.length == 0){
                    continue;
                }
                Line.Info info = infolist[0];
                Line mline = mixer.getLine(info);
                if(mixer.getLine(info) instanceof SourceDataLine){

                    audioinputs.put(mixerinf.getName(), (SourceDataLine) mline);
                }
            } catch(LineUnavailableException e){
                e.printStackTrace();
                return null;
            }
        }
        return audioinputs;
    }

    private AudioFormat getAudioFormat(float samplerate, int ssizeinbits){
        float sampleRate = samplerate;
        int sampleSizeInBits = ssizeinbits;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;

        return new AudioFormat(sampleRate,
                sampleSizeInBits,
                channels,
                signed,
                bigEndian);
    }//end getAudioFormat

    public ArrayList<String> getAudioSources(){
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        ArrayList<String> mixernames = new ArrayList<String>();
        for(Mixer.Info info: mixers){
            //System.out.println(info.getDescription());
            String currentName = info.getName();
            mixernames.add(currentName);
            //get the actual audio line control
            Mixer currentMixer = AudioSystem.getMixer(info);
            this.AudioPortNameMap.put(currentName, currentMixer);
        }
        return mixernames;
    }

    public Mixer getAudioMixer(String name){
        return this.AudioPortNameMap.get(name);
    }

    public TargetDataLine getAudioDataLine(String name, float samplerate, int samplesize){
        TargetDataLine line = null;
        Mixer gotmixer = this.getAudioMixer(name);
        Line.Info[] mixerInfo = gotmixer.getTargetLineInfo();
        AudioFormat format = this.getAudioFormat(samplerate, samplesize);
        DataLine.Info info = new DataLine.Info(
                TargetDataLine.class,
                format
        );

        try{
            line = (TargetDataLine) gotmixer.getLine(info);
            line.open(format);
        }
        catch (LineUnavailableException e){
            e.printStackTrace();
            return null;
        }

        if(line == null){
            throw new UnsupportedOperationException("No recording devices found");
        }
        return line;
    }

    public byte[] makeBuffer(float samplerate, int framesize){
        int bufferSize = (int) samplerate * framesize;
        byte buffer[] = new byte[bufferSize];
        return buffer;
    }

    public static AudioDevice getAudioDeviceInstance(){
        return SINGLE_INSTANCE;
    }
}
