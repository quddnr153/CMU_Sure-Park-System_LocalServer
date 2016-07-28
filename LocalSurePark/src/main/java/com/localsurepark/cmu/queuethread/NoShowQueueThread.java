package com.localsurepark.cmu.queuethread;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Queue;

import org.apache.http.client.ClientProtocolException;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.localsurepark.cmu.CurrentInfo;
import com.localsurepark.cmu.db.DatabaseFacade;
import com.localsurepark.cmu.domain.Reservation;
import com.localsurepark.cmu.http.HttpRestFulClient;

public class NoShowQueueThread implements Runnable{
	
	
	private Queue<Reservation> reservationNoShowQueue;
	
	private HttpRestFulClient httpRestFulClient;
	

	public NoShowQueueThread(Queue<Reservation> reservationNoShowQueue,HttpRestFulClient httpRestFulClient) {
		super();
		this.reservationNoShowQueue = reservationNoShowQueue;
		this.httpRestFulClient = httpRestFulClient;
	}




	public void run() {
		
		
		while(true)
		{
			if(!reservationNoShowQueue.isEmpty() && reservationNoShowQueue !=null)
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
					Reservation reservation = reservationNoShowQueue.poll();
					
					Calendar calendar = Calendar.getInstance();
			        java.util.Date date = calendar.getTime();
			        String today = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
					
			        long diffTime = diffTimes(reservation.getReservationTime().toString(), today);
					
			        if(diffTime > CurrentInfo.GRACEPERIOD)
			        {
			        	DatabaseFacade.deleteDriverForCancellation(reservation.getPhoneNumber());
			        	
			        	JSONObject responseResult = null;
			        	
			        	try {
			        		responseResult = httpRestFulClient.notifyNoShow(reservation.getPhoneNumber());
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
			        		
			        		System.out.println("no show 제거 성공....");
			        		
						}else
						{
							System.out.println("no show 서버 제거 실패....");
						}
			        	
			        	
			        }else
			        {
			        	reservationNoShowQueue.offer(reservation);
			        	// 허용
			        }
					
				}else
				{
					
				}
				
				
				
				
			}
			
			
			try {
				Thread.sleep(60*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private long diffTimes(String reservationTime, String currentTime)
	{
		String start= reservationTime;
		Calendar tempcal=Calendar.getInstance();
		SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date startday=sf.parse(start, new ParsePosition(0));

		long startTime=startday.getTime();

		String end= currentTime;
		tempcal=Calendar.getInstance();
		sf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date endday=sf.parse(end, new ParsePosition(0));
		
		long endTime=endday.getTime();
			
		long mills=endTime-startTime;
			
		long min=mills/60000;
		
		System.out.println(min +" min.");
		
		return min;
	}

}
