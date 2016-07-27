package com.localsurepark.cmu;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.http.client.ClientProtocolException;
import org.json.simple.parser.ParseException;

import com.localsurepark.cmu.domain.PayingInfo;
import com.localsurepark.cmu.http.HttpRestFulClient;
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
    	
    	HttpRestFulClient httpRestFulClient = new HttpRestFulClient();
    	
    	try {
			httpRestFulClient.OauthToken();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    	Queue<PayingInfo> payingQueue = new ConcurrentLinkedQueue<PayingInfo>();

    	ParkingControllerServer parkingContorollerServer = new ParkingControllerServer();
    	
    	Thread parkingContorollerServerThread = new Thread(parkingContorollerServer);
    	
    	parkingContorollerServerThread.start();
    	
    	
    	
    	WebSocketServer webSocketServer = new WebSocketServer();
    	try {
			webSocketServer.setWebSocket(5050,payingQueue);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Thread webSocketServerThread = new Thread(webSocketServer);
    	
    	webSocketServerThread.start();
    	
    	
    }
}
