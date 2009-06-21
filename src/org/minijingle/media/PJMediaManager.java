package org.minijingle.media;

import org.minijingle.jingle.description.Payload;
import org.minijingle.jingle.transport.Candidate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PJMediaManager implements MediaManager {

    final private ExecutorService es = Executors.newCachedThreadPool();
    private Process p;

    private List<Payload> payloads = new ArrayList<Payload>();

    public PJMediaManager() {
        payloads.add(new Payload("102", "iLBC", 8000));
    }

    public void startMedia(final Candidate local, final Candidate remote, final Payload payload) {

        es.submit(new Runnable() {
            public void run() {

                try {
                    Runtime rt = Runtime.getRuntime();
                    p = rt.exec(" /home/thiago/projects/pjproject-1.0.2/pjsip-apps/bin/samples/streamutil-i686-pc-linux-gnu --send-recv --remote=" + remote.getIp() + ":" + remote.getPort() + " --local-port=" + local.getPort() + " --codec=" + payload.getName());
                    InputStream stderr = p.getInputStream();
                    InputStreamReader isr = new InputStreamReader(stderr);
                    BufferedReader br = new BufferedReader(isr);
                    String line;
                    System.out.println("<Output>");
                    while ((line = br.readLine()) != null)
                        System.out.println(line);
                    System.out.println("</Output>");

                    int exitVal;
                    exitVal = p.waitFor();
                    System.out.println("Process exitValue: " + exitVal);


                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        });
    }

    public void stopMedia() {
        try {
            p.getOutputStream().write("q".getBytes());
            p.getOutputStream().flush();
            p.getOutputStream().write("\n".getBytes());
            p.getOutputStream().flush();
            p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Payload> getPayloads() {
        return payloads;
    }
}
