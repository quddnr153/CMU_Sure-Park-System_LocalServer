package com.localsurepark.cmu.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ClientThread implements Runnable {

	private final int RESERVATION = 1;
	private final int OPENTHEGATE = 2;
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
			boolean result = false;
			try {
				Object obj = parser.parse(readLine);
				JSONObject recvJsonObject = (JSONObject) obj;

				int type = Integer.parseInt(recvJsonObject.get("type").toString());

				result = processType(type , recvJsonObject);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			JSONObject sendJsonO = new JSONObject();

			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			out.println(sendJsonO.toJSONString());
			System.out.println("1. server send : " + sendJsonO.toJSONString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean processType(int type,JSONObject recvJsonObject) {
		boolean result = false;

		switch (type) {
		case RESERVATION:

			
			break;
		case OPENTHEGATE:

			
			break;

		default:
			break;
		}

		return result;
	}
	
	public boolean processReservation(JSONObject recvJsonObject)
	{
		boolean result = false;
		
		
		
		
		
		return result;
	}

}
