package com.localsurepark.cmu.domain;

public class PayingInfo {

	private String phoneNumber;
	private String identificationNumber;
	private String state;
	private String reservationID;

	public PayingInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PayingInfo(String phoneNumber, String identificationNumber, String state, String reservationID) {
		super();
		this.phoneNumber = phoneNumber;
		this.identificationNumber = identificationNumber;
		this.state = state;
		this.reservationID = reservationID;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getReservationID() {
		return reservationID;
	}

	public void setReservationID(String reservationID) {
		this.reservationID = reservationID;
	}

}
