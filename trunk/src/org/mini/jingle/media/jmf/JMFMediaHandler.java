package org.mini.jingle.media.jmf;

import org.mini.jingle.media.MediaHandler;
import org.mini.jingle.xmpp.Candidate;
import org.mini.jingle.xmpp.Description;
import org.mini.jingle.xmpp.PayloadType;

import javax.media.MediaLocator;
import javax.media.format.AudioFormat;

public class JMFMediaHandler extends MediaHandler{

    private AudioChannel channel;

    public JMFMediaHandler(){
        this.getSupportedPayloadTypes().add(new PayloadType("8000","102","ILBC"));
        //this.getSupportedPayloadTypes().add(new PayloadType("8000","0","ULAW"));
    }

    public void startMedia(Candidate localCandidate, Candidate remoteCandidate, Description description) {
      //  channel = new AudioChannel(new MediaLocator("javasound://44100"), localCandidate.getIp(), remoteCandidate.getIp() , Integer.valueOf(localCandidate.getPort()) ,Integer.valueOf(remoteCandidate.getPort()) , new AudioFormat(AudioFormat.ULAW_RTP));
      //  channel.start();
    }

    public void stopMedia() {

        if(channel!=null){
            channel.stop();
        }

    }
}
