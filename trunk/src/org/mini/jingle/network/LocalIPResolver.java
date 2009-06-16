package org.mini.jingle.network;

import java.util.Enumeration;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalIPResolver {

    public static String getLocalIP() {

        Enumeration ifaces = null;

        try {
            ifaces = NetworkInterface.getNetworkInterfaces();

            while (ifaces.hasMoreElements()) {

                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                Enumeration iaddresses = iface.getInetAddresses();

                while (iaddresses.hasMoreElements()) {
                    InetAddress iaddress = (InetAddress) iaddresses.nextElement();
                    if (!iaddress.isLoopbackAddress() && !iaddress.isLinkLocalAddress() && !iaddress.isSiteLocalAddress()) {
                        return iaddress.getHostAddress() != null ? iaddress.getHostAddress() : iaddress.getHostName();
                    }
                }
            }

            ifaces = NetworkInterface.getNetworkInterfaces();

            while (ifaces.hasMoreElements()) {

                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                Enumeration iaddresses = iface.getInetAddresses();

                while (iaddresses.hasMoreElements()) {
                    InetAddress iaddress = (InetAddress) iaddresses.nextElement();
                    if (!iaddress.isLoopbackAddress() && !iaddress.isLinkLocalAddress()) {
                        return iaddress.getHostAddress() != null ? iaddress.getHostAddress() : iaddress.getHostName();
                    }
                }
            }

            return InetAddress.getLocalHost().getHostAddress() != null ? InetAddress.getLocalHost().getHostAddress() : InetAddress.getLocalHost().getHostName();

        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return "127.0.0.1";
    }

}
