package com.localsurepark.cmu.domain;

public class ParkedPosition {
	private int reservationID;
	private String parkingContollerDeviceID;
	
	public int getReservationID() {
		return reservationID;
	}
	public void setReservationID(int reservationID) {
		this.reservationID = reservationID;
	}
	public String getParkingContollerDeviceID() {
		return parkingContollerDeviceID;
	}
	public void setParkingContollerDeviceID(String parkingContollerDeviceID) {
		this.parkingContollerDeviceID = parkingContollerDeviceID;
	}
}
