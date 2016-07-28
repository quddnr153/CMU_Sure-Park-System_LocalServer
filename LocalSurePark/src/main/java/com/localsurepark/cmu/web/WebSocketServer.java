package com.localsurepark.cmu.web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Queue;

import com.localsurepark.cmu.domain.PayingInfo;
import com.localsurepark.cmu.domain.Reservation;
import com.localsurepark.cmu.parkingcontroller.SenderCallback;

public class WebSocketServer implements Runnable{
	
	private ServerSocket server;
	
	private Queue<PayingInfo> payingQueue;
	private HashMap<String, SenderCallback> controllerIDClientHashMap;
	private HashMap<String, String> deviceIDAndAuduioIDHashMap;
	private Queue<Reservation> reservationNoShowQueue;
	
	public void setWebSocket(int port, Queue<PayingInfo> payingQueue,HashMap<String, SenderCallback> controllerIDClientHashMap,HashMap<String, String> deviceIDAndAuduioIDHashMap,Queue<Reservation> reservationNoShowQueue) throws IOException
	{
		server = new ServerSocket(port);
		this.payingQueue = payingQueue; 
		this.controllerIDClientHashMap = controllerIDClientHashMap;
		this.deviceIDAndAuduioIDHashMap = deviceIDAndAuduioIDHashMap;
		this.reservationNoShowQueue = reservationNoShowQueue;
		
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
			
			ClientThread cli = new ClientThread(client,payingQueue,controllerIDClientHashMap,deviceIDAndAuduioIDHashMap,reservationNoShowQueue);
			
			Thread clientThread = new Thread(cli);
			
			clientThread.start();
			
			
		}
		
	}

}
