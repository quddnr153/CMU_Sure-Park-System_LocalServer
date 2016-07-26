package com.localsurepark.cmu.parkingcontroller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SenderCallback {

	private Socket client;
	private PrintWriter clietOut;

	public SenderCallback(Socket client) {
		// TODO Auto-generated constructor stub
		this.client = client;
		setOut();
	}

	public void setOut() {
		try {
			clietOut = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void SendParkingController(String Message) {
		try {
			clietOut.println(Message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*public void run() {
		// TODO Auto-generated method stub

		while (true) {
			if (client.isConnected()) {
				Scanner scan = new Scanner(System.in);
				System.out.print("Send command to client: ");
				String answer = scan.nextLine();

				PrintWriter clietOut;
				try {
					clietOut = new PrintWriter(client.getOutputStream(), true);
					clietOut.println(answer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("2. server send : " + answer);

				
				 * BufferedReader input = null; try { input = new
				 * BufferedReader(new
				 * InputStreamReader(client.getInputStream())); } catch
				 * (IOException e1) { // TODO Auto-generated catch block
				 * e1.printStackTrace(); } try { answer = input.readLine();
				 * System.out.println("1. clinet send : " + answer); }catch
				 * (IOException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 * 
				 * try { Thread.sleep(3000); } catch (InterruptedException e) {
				 * // TODO Auto-generated catch block e.printStackTrace(); }
				 

				scan.close();
			}
		}
	}*/
}