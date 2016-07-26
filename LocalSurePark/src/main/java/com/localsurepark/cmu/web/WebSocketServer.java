package com.localsurepark.cmu.web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebSocketServer implements Runnable{
	
	private ServerSocket server;
	
	public void setWebSocket(int port) throws IOException
	{
		server = new ServerSocket(port);
		
	}

	public void run() {
		while (true) {
			Socket client = null;
			System.out.println("Waiting Web Socket Server Accept ...");
			try {
				client = server.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("Client 접속...");
			
			ClientThread cli = new ClientThread(client);
			
			Thread clientThread = new Thread(cli);
			
			clientThread.start();
			
			
		}
		
	}

}
