package com.localsurepark.cmu.parkingcontroller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import com.localsurepark.cmu.domain.ParkingLot;

public class ParkingControllerServer implements Runnable{

	private static int portNum = 550;
	private static SenderCallback callback;
	private HashMap<String, SenderCallback> controllerIDClientHashMap;
	
	private HashMap<String, String> deviceIDAndAuduioIDHashMap;
	




	public ParkingControllerServer(HashMap<String, SenderCallback> controllerIDClientHashMap,HashMap<String, String> deviceIDAndAuduioIDHashMap) {
		super();
		this.controllerIDClientHashMap = controllerIDClientHashMap;
		this.deviceIDAndAuduioIDHashMap = deviceIDAndAuduioIDHashMap;
	}



	public void run() {
		// TODO Auto-generated method stub
		ServerSocket localServer = null;
		try {
			localServer = new ServerSocket(portNum);
			System.out.println("\n\nWaiting for connection on port " + portNum + ".");

		} catch (IOException e) {
			System.err.println("\n\nCould not instantiate socket on port: " + portNum + " " + e);
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
			 * At this point we are all connected and we need to create the
			 * streams so we can read and write.
			 *****************************************************************************/
			System.out.println("Connection successful");
			System.out.println("Waiting for input.....");

			ReceiveThread rcv = new ReceiveThread(client, controllerIDClientHashMap,deviceIDAndAuduioIDHashMap);
			//SendThread snd = new SendThread(client);
			Thread rcvThread = new Thread(rcv);
			//Thread sndThread = new Thread(snd);
			rcvThread.start();
			//sndThread.start();

			callback = new SenderCallback(client);
			
		}
	}



	public static SenderCallback getCallback() {
		return callback;
	}



	
	
	
	

}
