package com.localsurepark.cmu.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.localsurepark.cmu.db.DatabaseFacade;

public class ClientThread implements Runnable {

	private final int MAKERESERVATION = 1;
	private final int DELETERESERVATION = 2;
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
				Object obj = parser.parse(readLine);
				JSONObject recvJsonObject = (JSONObject) obj;

				
				int type = Integer.parseInt(recvJsonObject.get("type").toString());

				sendJsonO = processType(type , recvJsonObject);
			
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


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

			result = processMakeReservation(recvJsonObject);
			
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

}
