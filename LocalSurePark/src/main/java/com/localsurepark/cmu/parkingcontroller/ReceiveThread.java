package com.localsurepark.cmu.parkingcontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import com.localsurepark.cmu.db.DatabaseFacade;
import com.localsurepark.cmu.domain.ParkingLot;

public class ReceiveThread implements Runnable {
	private Socket client;
	private HashMap<String, SenderCallback> controllerIDClientHashMap;
	private HashMap<String, String> deviceIDAndAuduioIDHashMap;

	public ReceiveThread(Socket client, HashMap<String, SenderCallback> controllerIDClientHashMap,
			HashMap<String, String> deviceIDAndAuduioIDHashMap) {
		this.client = client;
		this.controllerIDClientHashMap = controllerIDClientHashMap;
		this.deviceIDAndAuduioIDHashMap = deviceIDAndAuduioIDHashMap;
		System.out.println("Connection Complete: Hello Client!");
	}

	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			if (client.isConnected()) {
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

					System.out.println(answer.length());
					if (answer.length() > 30) {
						// device register operation

						System.out.println(answer);

						DatabaseFacade.updateParkingContollerDevicesFromParkinglot(answer, client,
								controllerIDClientHashMap, deviceIDAndAuduioIDHashMap);

					} else {
						System.out.println("1. clinet 10 send : " + answer);
						String[] results = answer.split(",");
						if (results[0].equals("0")) {
							System.out.println("Device " + results[1] + " state change to not broken or not parked");
							DatabaseFacade.updateParkingContollerDeviceStateFromParkinglot(results[1], results[0]);
						} else if (results[0].equals("1")) {
							System.out.println("Device " + results[1] + " state change to broken or parked");
							DatabaseFacade.updateParkingContollerDeviceStateFromParkinglot(results[1], results[0]);
						} else if (results[0].equals("2")) {
							System.out.println("Device " + results[1] + " state change to parked");

							DatabaseFacade.insertParkingposition(results[1]);
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
					if (client != null) {
						client = null;
					}
					break;
				}

			} else {
				System.out.println("parking controller closed.....");
				if (client != null) {
					client = null;
				}
				break;
			}

		}
	}
}