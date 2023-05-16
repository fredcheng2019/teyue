package com.sifang.test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class HttpClientUtil {

	public static void main(String[] args) {
		String SERVER_IP = null;
	    try {
	      Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
	      InetAddress ip = null;
	      while (netInterfaces.hasMoreElements()) {
	        NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
	        ip = (InetAddress) ni.getInetAddresses().nextElement();
	        SERVER_IP = ip.getHostAddress();
	        if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
	            && ip.getHostAddress().indexOf(":") == -1) {
	          SERVER_IP = ip.getHostAddress();
	          System.out.println(SERVER_IP);
	          break;
	        } else {
	          ip = null;
	        }
	      }
	    } catch (SocketException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
	}

}
