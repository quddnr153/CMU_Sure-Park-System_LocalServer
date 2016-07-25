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

				if (answer.length() > 20) {
					// device register operation
					DatabaseFacade.dbCntDevRegOperation(answer);
				} else if (answer.length() == 10) {
					System.out.println("1. clinet send : " + answer);
					if (answer.equals(CurrentInfo.stall1)) {
						System.out.println("The driver parked on the parking space 1.");
					} else if (answer.equals(CurrentInfo.stall2)) {
						System.out.println("The driver parked on the parking space 2.");
					} else if (answer.equals(CurrentInfo.stall3)) {
						System.out.println("The driver parked on the parking space 3.");
					} else if (answer.equals(CurrentInfo.stall4)) {
						System.out.println("The driver parked on the parking space 4.");
					}
					// Assign the parking space in DB
					DatabaseFacade.dbAssignPSOperation(answer);
				} else {
					System.out.println("1. clinet send : " + answer);
					if (answer.equals("e")) {
						System.out.println("The driver is leaving on the parking space 1.");
					} else if (answer.equals("f")) {
						System.out.println("The driver is leaving on the parking space 2.");
					} else if (answer.equals("g")) {
						System.out.println("The driver is leaving on the parking space 3.");
					} else if (answer.equals("h")) {
						System.out.println("The driver is leaving on the parking space 4.");
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
