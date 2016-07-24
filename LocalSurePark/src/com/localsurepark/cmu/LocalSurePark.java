package com.localsurepark.cmu;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class LocalSurePark {
	private static int portNum = 550;
	public static void main(String[] args) throws IOException {
		ServerSocket localServer = null;
		try {
			localServer = new ServerSocket(portNum);
			System.out.println("\n\nWaiting for connection on port " + portNum + "." );
			
		} catch (IOException e) {
			System.err.println( "\n\nCould not instantiate socket on port: " + portNum + " " + e);
    		System.exit(1);
		}
		System.out.println("Server Socket open.....");
		while (true) {
			Socket client = null;
			try {
				client = localServer.accept();
			} catch (Exception e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}
			System.out.println("Client connected: " + client.getInetAddress());
			/*****************************************************************************
    	 	* At this point we are all connected and we need to create the streams so
    	 	* we can read and write.
		 	*****************************************************************************/
			System.out.println ("Connection successful");
    		System.out.println ("Waiting for input.....");
		}
		
	}
}
