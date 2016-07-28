package com.localsurepark.cmu.queuethread;

import java.io.IOException;
import java.util.Queue;

import org.apache.http.client.ClientProtocolException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.localsurepark.cmu.db.DatabaseFacade;
import com.localsurepark.cmu.domain.PayingInfo;
import com.localsurepark.cmu.http.HttpRestFulClient;

public class PayingQueueThread implements Runnable {

	private Queue<PayingInfo> payingQueue;

	private HttpRestFulClient httpRestFulClient;
	
	

	public PayingQueueThread() {

	}

	public PayingQueueThread(Queue<PayingInfo> payingQueue,HttpRestFulClient httpRestFulClient) {
		
		this.payingQueue = payingQueue;
		this.httpRestFulClient = httpRestFulClient;
	}
	
	


	public void run() {

		
		while(true)
		{
			if(!payingQueue.isEmpty() && payingQueue !=null)
			{

				
				boolean result = false;
				try {
					result = httpRestFulClient.OauthToken();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(result)
				{
					PayingInfo payingInfo= payingQueue.poll();
					JSONObject responseResult = null;
					
					try {
						responseResult = httpRestFulClient.paymentRestfulPost(payingInfo.getPhoneNumber(), payingInfo.getReservationID());
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(responseResult.containsKey("result")&&responseResult.get("result").equals("success"))
					{
						int deleteResult = DatabaseFacade.deleteDriverForCancellation(payingInfo.getPhoneNumber());
						
						if(deleteResult > 0)
						{
							System.out.println("결제 성공....");
							
						}else
						{
							
						}
						
					}else
					{
						
						payingQueue.offer(payingInfo);
						
					}
				}
				else
				{
					
				}
				
			}

			
		}
	}

}
