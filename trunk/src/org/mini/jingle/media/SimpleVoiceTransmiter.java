package org.mini.jingle.media;

import net.sf.fmj.media.BonusAudioFormatEncodings;

import javax.media.*;
import javax.media.control.FormatControl;
import javax.media.control.TrackControl;
import javax.media.format.AudioFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import java.io.IOException;
import java.util.Vector;

public class SimpleVoiceTransmiter {

    /**
     * @param args
     */
    public static void main(String[] args) {

        AudioFormat format = new AudioFormat(AudioFormat.LINEAR, 8000, 8, 1);

        Vector devices = CaptureDeviceManager.getDeviceList(format);

        CaptureDeviceInfo di = null;

        MediaLocator mediaLocator = new MediaLocator("javasound://");

        if (devices.size() > 0) {
            di = (CaptureDeviceInfo) devices.elementAt(0);
            mediaLocator = di.getLocator();
        }


        Processor processor = null;
        try {
            processor = Manager.createProcessor(mediaLocator);
        }
        catch (IOException e) {
            System.exit(-1);
        }
        catch (NoProcessorException e) {
            System.exit(-1);
        }

        processor.configure();

        while (processor.getState() != Processor.Configured) {
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        processor.setContentDescriptor(
                new ContentDescriptor(ContentDescriptor.RAW));

        TrackControl track[] = processor.getTrackControls();

        boolean encodingOk = false;

        for (int i = 0; i < track.length; i++) {
            if (!encodingOk && track[i] instanceof FormatControl) {
                if (((FormatControl) track[i]).
                        setFormat(new AudioFormat(AudioFormat.ILBC_RTP, 8000.0, 16, 1, AudioFormat.LITTLE_ENDIAN, AudioFormat.SIGNED)) == null) {
                     //setFormat(new AudioFormat(AudioFormat.ULAW_RTP, 8000, 8, 1)) == null) {
                    track[i].setEnabled(false);
                }
                else {
                    encodingOk = true;
                }
            }
            else {
                track[i].setEnabled(false);
            }
        }

        if (encodingOk) {
            processor.realize();
            while (processor.getState() != Processor.Realized) {
                try {
                    Thread.sleep(100);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            DataSource ds = null;

            try {
                ds = processor.getDataOutput();
            }
            catch (NotRealizedError e) {
                System.exit(-1);
            }

            try {
                String url = "rtp://194.229.27.10:53122/audio/16";

                MediaLocator m = new MediaLocator(url);

                DataSink d = Manager.createDataSink(ds, m);
                d.open();
                d.start();
                processor.start();
            }
            catch (Exception e) {
                System.exit(-1);
            }
        }

    }

}
