package com.localsurepark.cmu;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.localsurepark.cmu.domain.PayingInfo;
import com.localsurepark.cmu.parkingcontroller.ParkingControllerServer;
import com.localsurepark.cmu.web.WebSocketServer;

/**
 * Hello world!
 *
 */
public class MainApplication 
{
	
	
    public static void main( String[] args )
    {
    	
    	Queue<PayingInfo> payingQueue = new ConcurrentLinkedQueue<PayingInfo>();

    	ParkingControllerServer parkingContorollerServer = new ParkingControllerServer();
    	
    	Thread parkingContorollerServerThread = new Thread(parkingContorollerServer);
    	
    	parkingContorollerServerThread.start();
    	
    	
    	
    	WebSocketServer webSocketServer = new WebSocketServer();
    	try {
			webSocketServer.setWebSocket(5050);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Thread webSocketServerThread = new Thread(webSocketServer);
    	
    	webSocketServerThread.start();
    	
    	
    }
}
