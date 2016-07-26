package com.localsurepark.cmu.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.localsurepark.cmu.CurrentInfo;
import com.localsurepark.cmu.db.DatabaseFacade;
import com.localsurepark.cmu.domain.Reservation;
import com.localsurepark.cmu.parkingcontroller.ParkingControllerServer;

public class ClientThread implements Runnable {

	private final int MAKERESERVATION = 1;
	private final int DELETERESERVATION = 2;
	private final int HANDOVERRESERVATION = 3;
	private final int OPENTHEENTRYGATE = 4;
	private final int OPENTHEEXITGATE = 5;
	
	
	private Socket client;

	public ClientThread(Socket client) {
		this.client = client;

		System.out.println("클라이언트 접속 시작!");
	}

	public void run() {

		System.out.println("읽기 대기중");
		BufferedReader input = null;
		try {
			input = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			String readLine = input.readLine();
			System.out.println("1. clinet send : " + readLine);
			JSONParser parser = new JSONParser();
			String phoneNumber = null;
			JSONObject sendJsonO = null;
			try {
				//read message
				Object obj = parser.parse(readLine);
				JSONObject recvJsonObject = (JSONObject) obj;

				
				int type = Integer.parseInt(recvJsonObject.get("type").toString());

				//process read message 
				sendJsonO = processType(type , recvJsonObject);
			
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			//write result message 
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			out.println(sendJsonO.toJSONString());
			System.out.println("1. server send : " + sendJsonO.toJSONString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public JSONObject processType(int type,JSONObject recvJsonObject) {
		JSONObject result = null;

		switch (type) {
		case MAKERESERVATION:
			
			result = processMakeReservation(recvJsonObject);
			
			break;
		case DELETERESERVATION:

			result = processDeleteReservation(recvJsonObject);
			
			break;
		case HANDOVERRESERVATION:

			result = processHandoverReservation(recvJsonObject);
			
			break;
		case OPENTHEENTRYGATE:

			result = processOpenTheEntryGate(recvJsonObject);
			
			break;
		case OPENTHEEXITGATE:

			result = processOpenTheExitGate(recvJsonObject);
			
			break;

		default:
			break;
		}

		return result;
	}
	
	public JSONObject processMakeReservation(JSONObject recvJsonObject)
	{
		JSONObject result = new JSONObject();
		int insertResrult = DatabaseFacade.dbReservationInsert(recvJsonObject.get("phoneNumber").toString(), recvJsonObject.get("email").toString(), recvJsonObject.get("parkingLotID").toString(), Integer.parseInt(recvJsonObject.get("carSize").toString()), Timestamp.valueOf(recvJsonObject.get("reservationTime").toString()), recvJsonObject.get("identificationNumber").toString());
		
		System.out.println(insertResrult + "");
		if(insertResrult > 0)
		{
			System.out.println("reservation 성공");
			int reservationID = DatabaseFacade.dbReservationIDSelect(recvJsonObject.get("phoneNumber").toString());
			result.put("result", "success");
			result.put("reservationID", reservationID+"");
			
		}else
		{
			System.out.println("reservation 실패");
			result.put("result", "fail");
			result.put("reservationID", "null");
		}
		
		return result;
	}
	
	
	public JSONObject processDeleteReservation(JSONObject recvJsonObject)
	{
		JSONObject result = new JSONObject();
		
		String phoneNumber = recvJsonObject.get("phoneNumber").toString();
		
		int deleteResult = DatabaseFacade.dbReservationDelete(phoneNumber);


		if(deleteResult > 0)
		{
			result.put("result", "success");
		}else
		{
			result.put("result", "fail");
		}
		
		
		return result;
	}
	
	
	public JSONObject processHandoverReservation(JSONObject recvJsonObject)
	{
		JSONObject result = new JSONObject();
		
		String phoneNumber = recvJsonObject.get("phoneNumber").toString();
		String secondaryPhoneNumber = recvJsonObject.get("secondaryPhoneNumber").toString();
		
		int updateResult = DatabaseFacade.dbReservationPhoneNumberUpdate(phoneNumber, secondaryPhoneNumber);
		
		if(updateResult > 0)
		{
			result.put("result", "success");
		}else
		{
			result.put("result", "fail");
		}
		
		return result;
	}
	
	public JSONObject processOpenTheEntryGate(JSONObject recvJsonObject)
	{
		JSONObject result = new JSONObject();
		
		String phoneNumber = recvJsonObject.get("phoneNumber").toString();
		String state = recvJsonObject.get("state").toString();

		Reservation reservation = DatabaseFacade.dbReservationSelect(phoneNumber);
		int entryState = DatabaseFacade.dbDeviceStateSelect(CurrentInfo.entryID);
		int satll1 = DatabaseFacade.dbDeviceStateSelect(CurrentInfo.stall1);
		int satll2 = DatabaseFacade.dbDeviceStateSelect(CurrentInfo.stall2);
		int satll3 = DatabaseFacade.dbDeviceStateSelect(CurrentInfo.stall3);
		int satll4 = DatabaseFacade.dbDeviceStateSelect(CurrentInfo.stall4);
		int[] stallArray = {satll1,satll2,satll3,satll4};
		ArrayList<Integer> availableStall = new ArrayList<Integer>();
		for(int i=0;i<stallArray.length;i++)
		{
			if(stallArray[i] !=0)
			{
				availableStall.add(i);
			}
		}

		if(reservation ==null)
		{
			result.put("result", "fail");
		}
		else if(entryState !=0)
		{
			result.put("result", "fail");
		}
		else if(reservation.getPhoneNumber().equals(phoneNumber) && entryState ==0)
		{
			
			Calendar calendar = Calendar.getInstance();
	        java.util.Date date = calendar.getTime();
	    	String today = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
	    	int assignNum = availableStall.iterator().next();
	    	ParkingControllerServer.getCallback().SendParkingController(assignNum+"");
	    	result.put("result", "success");
	    	result.put("entranceTime", today);
	    	result.put("state", "parked");
	    	DatabaseFacade.dbReservationEntranceUpdate(phoneNumber, Timestamp.valueOf(today));
	    	DatabaseFacade.dbDriverStateUpdate(phoneNumber, "parked");
		}
		
		return result;
		
	}
	
	public JSONObject processOpenTheExitGate(JSONObject recvJsonObject)
	{
		JSONObject result = new JSONObject();
		
		
		
		return result;
		
	}

}
