package com.localsurepark.cmu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiveThread implements Runnable {
	private Socket client;
	
	public ReceiveThread(Socket client) {
		this.client = client;
		
		System.out.println("Connection Complete: Hello Client!");
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

		while (true) {

			System.out.println("");
			BufferedReader input = null;
			try {
				input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				String answer = input.readLine();
				System.out.println("1. clinet send : " + answer);
				if (answer.length() > 20) {
					System.out.println("Arduino register start");
					/*
					 * Arduino register information
					 * (parkingContollerDeviceID,parkingContollerDeviceType,parkingContollerDeviceAlive,parkingContollerDeviceState,parkingContollerID)
					 * Need to DB insert operation
					 */
				}
				if (answer.equals("1")) {
					System.out.println("The driver is in front of entry gate.");
				} else if (answer.equals("2")) {
					System.out.println("The driver is in front of exit gate.");
				} else if (answer.equals("a")) {
					System.out.println("The driver parked on the parking space 1.");
				} else if (answer.equals("b")) {
					System.out.println("The driver parked on the parking space 2.");
				} else if (answer.equals("c")) {
					System.out.println("The driver parked on the parking space 3.");
				} else if (answer.equals("d")) {
					System.out.println("The driver parked on the parking space 4.");
				} else if (answer.equals("e")) {
					System.out.println("The driver is leaving on the parking space 1.");
				} else if (answer.equals("f")) {
					System.out.println("The driver is leaving on the parking space 2.");
				} else if (answer.equals("g")) {
					System.out.println("The driver is leaving on the parking space 3.");
				} else if (answer.equals("h")) {
					System.out.println("The driver is leaving on the parking space 4.");
				}
					
/*
				PrintWriter out = new PrintWriter(client.getOutputStream(), true);
				out.println(answer);
				System.out.println("1. server send : " + answer);
				*/
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
