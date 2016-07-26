package com.localsurepark.cmu;

import java.io.IOException;

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
    	Thread thread = new Thread(webSocketServer);
    	
    	thread.start();
    }
}
