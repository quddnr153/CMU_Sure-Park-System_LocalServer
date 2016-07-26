package com.localsurepark.cmu.domain;

import java.sql.Timestamp;

public class Reservation {
	public int reservationID;
	public String phoneNumber;
	public String email;
	public String parkingLotID;
	public int carSize;
	public Timestamp reservationTime;
	public Timestamp entranranceTime;
	public Timestamp exitTime;

	public int getReservationID() {
		return reservationID;
	}

	public void setReservationID(int reservationID) {
		this.reservationID = reservationID;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getParkingLotID() {
		return parkingLotID;
	}

	public void setParkingLotID(String parkingLotID) {
		this.parkingLotID = parkingLotID;
	}

	public int getCarSize() {
		return carSize;
	}

	public void setCarSize(int carSize) {
		this.carSize = carSize;
	}

	public Timestamp getReservationTime() {
		return reservationTime;
	}

	public void setReservationTime(Timestamp reservationTime) {
		this.reservationTime = reservationTime;
	}

	public Timestamp getEntranranceTime() {
		return entranranceTime;
	}

	public void setEntranranceTime(Timestamp entranranceTime) {
		this.entranranceTime = entranranceTime;
	}

	public Timestamp getExitTime() {
		return exitTime;
	}

	public void setExitTime(Timestamp exitTime) {
		this.exitTime = exitTime;
	}

}