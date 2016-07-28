package com.localsurepark.cmu;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.http.client.ClientProtocolException;
import org.json.simple.parser.ParseException;

import com.localsurepark.cmu.domain.PayingInfo;
import com.localsurepark.cmu.domain.Reservation;
import com.localsurepark.cmu.http.HttpRestFulClient;
import com.localsurepark.cmu.parkingcontroller.ParkingControllerServer;
import com.localsurepark.cmu.parkingcontroller.SenderCallback;
import com.localsurepark.cmu.queuethread.NoShowQueueThread;
import com.localsurepark.cmu.queuethread.PayingQueueThread;
import com.localsurepark.cmu.web.WebSocketServer;

/**
 * Hello world!
 *
 */
public class MainApplication 
{
	
	
    public static void main( String[] args )
    {
    	
    	
    	/*
    	HttpRestFulClient httpRestFulClient = new HttpRestFulClient();
    	try {
			httpRestFulClient.OauthToken();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	
    	

    	HashMap<String, SenderCallback> controllerIDClientHashMap = new HashMap<String , SenderCallback>();
    	HashMap<String, String> deviceIDAndAuduioIDHashMap = new HashMap<String , String>();
    	
    	
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
    	Queue<Reservation> reservationNoShowQueue = new ConcurrentLinkedQueue<Reservation>(); 

    	
    	
    	ParkingControllerServer parkingContorollerServer = new ParkingControllerServer(controllerIDClientHashMap,deviceIDAndAuduioIDHashMap);
    	
    	Thread parkingContorollerServerThread = new Thread(parkingContorollerServer);
    	
    	parkingContorollerServerThread.start();
    	
    	
    	
    	WebSocketServer webSocketServer = new WebSocketServer();
    	try {
			webSocketServer.setWebSocket(5050,payingQueue,controllerIDClientHashMap,deviceIDAndAuduioIDHashMap,reservationNoShowQueue);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Thread webSocketServerThread = new Thread(webSocketServer);
    	
    	webSocketServerThread.start();
    	
    	PayingQueueThread payingQueueThread = new PayingQueueThread(payingQueue,httpRestFulClient);
    	
    	Thread payingThread = new Thread(payingQueueThread);
    	
    	payingThread.start();
    	
    	NoShowQueueThread noShowQueueThread = new NoShowQueueThread(reservationNoShowQueue, httpRestFulClient);
    	
    	Thread noShowThread = new Thread(noShowQueueThread);
    	
    	noShowThread.start();
    	
    	
    	
    	
    	
    }
}
