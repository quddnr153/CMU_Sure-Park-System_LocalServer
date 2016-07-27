package com.localsurepark.cmu.domain;

public class PayingInfo {

	private String phoneNumber;
	private String state;
	private int reservationID;

	public PayingInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PayingInfo(String phoneNumber, String state, int reservationID) {
		super();
		this.phoneNumber = phoneNumber;
		this.state = state;
		this.reservationID = reservationID;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getReservationID() {
		return reservationID;
	}

	public void setReservationID(int reservationID) {
		this.reservationID = reservationID;
	}

}
