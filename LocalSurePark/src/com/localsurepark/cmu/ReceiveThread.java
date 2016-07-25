package com.localsurepark.cmu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.*;

public class ReceiveThread implements Runnable {
	private Socket client;

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/localsurepark";

	static final String USERNAME = "root";
	static final String PASSWORD = "cjswo0825";

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
					dbCntDevRegOperation(answer);
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
					dbAssignPSOperation(answer);
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

				/*
				 * PrintWriter out = new PrintWriter(client.getOutputStream(),
				 * true); out.println(answer);
				 * System.out.println("1. server send : " + answer);
				 */
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void dbCntDevRegOperation(String devStr) {
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
			selectSql = "SELECT parkingContollerDeviceID FROM parkinglot WHERE parkingContollerDeviceID = '" + results[0] + "'";
			stmtS.execute(selectSql);
			rs = stmtS.getResultSet();
			if(rs.next()) {
				System.out.println("Parking controller device already registered. So, update device state.");
				Statement stmtU = null;
				try
		        {
					stmtU = conn.createStatement();
					stmtU.execute("UPDATE parkinglot SET parkingContollerDeviceType=" + Integer.parseInt(results[1]) + ", "
							+ "parkingContollerDeviceAlive=" + Integer.parseInt(results[2]) + ", "
							+ "parkingContollerDeviceState=" + Integer.parseInt(results[3]) + ", "
							+ "parkingContollerID='" + results[4] + "' WHERE parkingContollerDeviceID = '" + results[0] + "'");
					System.out.println("Parking controller update complete.");
		        } 
		        catch (Exception e) {
		            e.printStackTrace();
		        }finally {
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
				try
		        {
					stmtI = conn.createStatement();
					stmtI.execute("INSERT INTO parkinglot (parkingContollerDeviceID,parkingContollerDeviceType,"
							+ "parkingContollerDeviceAlive,parkingContollerDeviceState,parkingContollerID) "
							+ "VALUES ('" + results[0] + "'," + Integer.parseInt(results[1]) + "," + Integer.parseInt(results[2])
							+ "," + Integer.parseInt(results[3]) +",'" + results[4] + "')");
					System.out.println("Parking controller register complete.");
		        } 
		        catch (Exception e) {
		            e.printStackTrace();
		        }finally {
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
	
	public void dbAssignPSOperation(String psArg) {
		// TEST CODE
		CurrentInfo.reservationID = 1;
		Connection conn = null;
		Statement stmtI = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection");
			
			stmtI = conn.createStatement();
			stmtI.execute("INSERT INTO parkedposition (reservationID,parkingContollerDeviceID)"
					+ "VALUES (" + CurrentInfo.reservationID + ",'" + psArg + "')");
			System.out.println("Assign complete.");
        } 
        catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
            	stmtI.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		System.out.println("\n\n- MySQL Connection Close");
	}
}
