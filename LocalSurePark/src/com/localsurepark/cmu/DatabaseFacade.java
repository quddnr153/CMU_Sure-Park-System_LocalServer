package com.localsurepark.cmu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class DatabaseFacade {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/localsurepark";

	static final String USERNAME = "root";
	static final String PASSWORD = "cjswo0825";

	public static void dbReservationOperation(String phoneNumber, String email, String parkingLotID
											, int carSize, Timestamp reservationTime, String identificationNumber) {
		Connection conn = null;
		Statement stmtIR = null;
		Statement stmtID = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection");

			stmtIR = conn.createStatement();
			stmtIR.execute("INSERT INTO reservation (phoneNumber,email,parkingLotID,carSize,reservationTime)"
						+ "VALUES ('" + phoneNumber + "','" + email + "','" + parkingLotID + "," + carSize
						+ ",'" + reservationTime + ")");
			System.out.println("Reservation complete.");
			stmtID = conn.createStatement();
			stmtID.execute("INSERT INTO driver (phoneNumber,identificationNumber)"
						+ "VALUES ('" + phoneNumber + "','" + identificationNumber + "')");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmtIR.close();
				stmtID.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("\n\n- MySQL Connection Close");
	}
	
	public static void dbCntDevRegOperation(String devStr) {
		Statement stmtS = null;
		ResultSet rs = null;
		Connection conn = null;

		String str = devStr;
		String[] results = str.split(",");
		/*
		 * Arduino register information
		 * (results[0],results[1],results[2],results[3],results[4])
		 * (parkingContollerDeviceID,parkingContollerDeviceType,
		 * parkingContollerDeviceAlive,parkingContollerDeviceState,
		 * parkingContollerID) Need to DB insert operation
		 */
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection");
			stmtS = conn.createStatement();

			String selectSql;
			selectSql = "SELECT parkingContollerDeviceID FROM parkinglot WHERE parkingContollerDeviceID = '"
					+ results[0] + "'";
			stmtS.execute(selectSql);
			rs = stmtS.getResultSet();
			if (rs.next()) {
				System.out.println("Parking controller device already registered. So, update device state.");
				Statement stmtU = null;
				try {
					stmtU = conn.createStatement();
					stmtU.execute("UPDATE parkinglot SET parkingContollerDeviceType=" + Integer.parseInt(results[1])
							+ ", " + "parkingContollerDeviceAlive=" + Integer.parseInt(results[2]) + ", "
							+ "parkingContollerDeviceState=" + Integer.parseInt(results[3]) + ", "
							+ "parkingContollerID='" + results[4] + "' WHERE parkingContollerDeviceID = '" + results[0]
							+ "'");
					System.out.println("Parking controller update complete.");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						stmtU.close();
						conn.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				// insert operation
				System.out.println("Parking controller device register start.");
				Statement stmtI = null;
				try {
					stmtI = conn.createStatement();
					stmtI.execute("INSERT INTO parkinglot (parkingContollerDeviceID,parkingContollerDeviceType,"
							+ "parkingContollerDeviceAlive,parkingContollerDeviceState,parkingContollerID) "
							+ "VALUES ('" + results[0] + "'," + Integer.parseInt(results[1]) + ","
							+ Integer.parseInt(results[2]) + "," + Integer.parseInt(results[3]) + ",'" + results[4]
							+ "')");
					System.out.println("Parking controller register complete.");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						stmtI.close();
						conn.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			// it is a good idea to release
			// resources in a finally{} block
			// in reverse-order of their creation
			// if they are no-longer needed
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
				} // ignore

				rs = null;
			}

			if (stmtS != null) {
				try {
					stmtS.close();
				} catch (SQLException sqlEx) {
				} // ignore

				stmtS = null;
			}
		}
		System.out.println("\n\n- MySQL Connection Close");
	}

	public static void dbAssignPSOperation(String psArg) {
		// TEST CODE
		CurrentInfo.reservationID = 1;
		Connection conn = null;
		Statement stmtI = null;
		Statement stmtU = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection");

			stmtI = conn.createStatement();
			stmtI.execute("INSERT INTO parkedposition (reservationID,parkingContollerDeviceID)" + "VALUES ("
					+ CurrentInfo.reservationID + ",'" + psArg + "')");
			System.out.println("Assign complete.");
			stmtU = conn.createStatement();
			stmtU.execute("UPDATE parkinglot SET parkingContollerDeviceState = 0 "
					+ "WHERE parkingContollerDeviceID = '" + psArg + "'");
			System.out.println("Parking controller " + psArg + " update complete.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmtU.close();
				stmtI.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("\n\n- MySQL Connection Close");
	}
}
