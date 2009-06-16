package org.mini.jingle;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.ProviderManager;
import org.mini.jingle.xmpp.Jingle;
import org.mini.jingle.xmpp.Candidate;
import org.mini.jingle.network.LocalIPResolver;
import org.mini.jingle.media.jmf.JMFMediaHandler;

import java.sql.ClientInfoStatus;

public class JingleRawUDPClient {

    private final XMPPConnection connection;
    private final String username, password, server;
    private CallManager callManager;
    private final String proxy;

    public JingleRawUDPClient(String username, String password, String server, String proxy) {
        this.username = username;
        this.password = password;
        this.server = server;
        this.proxy = proxy;
        ProviderManager.getInstance().addIQProvider(Jingle.NAME, Jingle.XMLNS, new JingleProvider());
        ConnectionConfiguration conf = new ConnectionConfiguration(server, 5222);
        conf.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        conf.setSASLAuthenticationEnabled(false);
        connection = new XMPPConnection(conf);

        login();
    }

    private void login() {

        try {
            connection.connect();
            connection.login(username, password);
            final Candidate localCandidate = new Candidate(LocalIPResolver.getLocalIP(),"10002","0");
            callManager = new CallManager(connection,localCandidate, new JMFMediaHandler(), proxy);
        }
        catch (XMPPException e) {
            e.printStackTrace();
        }

    }


    public static void main(String args[]){

        XMPPConnection.DEBUG_ENABLED=true;

        JingleRawUDPClient client = new JingleRawUDPClient(args[1],args[2],args[3],args[4]);

        try {
              Thread.sleep(2000);
          }
          catch (InterruptedException e) {
              e.printStackTrace();
          }

          try {
                Thread.sleep(160000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

    }

}
