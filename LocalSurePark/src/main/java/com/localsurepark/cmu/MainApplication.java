package com.localsurepark.cmu;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.localsurepark.cmu.parkingcontroller.ParkingControllerServer;
import com.localsurepark.cmu.parkingcontroller.ReceiveThread;
import com.localsurepark.cmu.parkingcontroller.SendThread;
import com.localsurepark.cmu.web.WebSocketServer;

/**
 * Hello world!
 *
 */
public class MainApplication 
{
    public static void main( String[] args )
    {
    	
    	
    	WebSocketServer webSocketServer = new WebSocketServer();
    	try {
			webSocketServer.setWebSocket(5050);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Thread webSocketServerThread = new Thread(webSocketServer);
    	
    	webSocketServerThread.start();
    	
    	
    	ParkingControllerServer parkingContorollerServer = new ParkingControllerServer();
    	
    	Thread parkingContorollerServerThread = new Thread(parkingContorollerServer);
    	
    	parkingContorollerServerThread.start();
    }
}
