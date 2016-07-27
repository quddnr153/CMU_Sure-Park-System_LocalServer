package com.localsurepark.cmu.queuethread;

import java.util.Queue;

import com.localsurepark.cmu.domain.PayingInfo;

public class PayingQueueThread implements Runnable {

	private Queue<PayingInfo> payingQueue;

	public PayingQueueThread() {

	}

	public PayingQueueThread(Queue<PayingInfo> payingQueue) {
		
		this.payingQueue = payingQueue;

	}

	public void run() {

		while(!payingQueue.isEmpty())
		{
			
			
			
			
		}
	}

}
