package com.localsurepark.cmu.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread implements Runnable {

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
			String answer = input.readLine();
			System.out.println("1. clinet send : " + answer);

			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			out.println(answer);
			System.out.println("1. server send : " + answer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
