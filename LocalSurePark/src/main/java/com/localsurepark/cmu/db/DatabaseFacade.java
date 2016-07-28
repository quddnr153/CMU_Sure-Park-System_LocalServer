package com.localsurepark.cmu.db;

import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import com.localsurepark.cmu.CurrentInfo;
import com.localsurepark.cmu.domain.Driver;
import com.localsurepark.cmu.domain.ParkedPosition;
import com.localsurepark.cmu.domain.ParkingLot;
import com.localsurepark.cmu.domain.Reservation;
import com.localsurepark.cmu.parkingcontroller.SenderCallback;

public class DatabaseFacade {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/localsurepark";

	static final String USERNAME = "surepark";
	static final String PASSWORD = "surepark";

	// WEB db operation
	public static int deleteDriverForCancellation(String phoneNumber) {
		Connection conn = null;
		Statement stmtID = null;
		int result = 0;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute deleteDriverForCancellation");

			stmtID = conn.createStatement();
			result = stmtID.executeUpdate("DELETE FROM driver WHERE phoneNumber = '" + phoneNumber + "'");
			System.out.println("Reservation delete complete.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmtID.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("\n\n- MySQL Connection Close");
		return result;
	}

	public static int selectReservationIDFromReservation(String phoneNumber) {
		Statement stmtS = null;
		ResultSet rs = null;
		Connection conn = null;
		int reservationID = 0;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute selectReservationIDFromReservation");
			stmtS = conn.createStatement();

			String selectSql;
			selectSql = "SELECT reservationID FROM reservation WHERE phoneNumber = '" + phoneNumber + "'";
			stmtS.execute(selectSql);
			rs = stmtS.getResultSet();
			if (rs.next()) {
				reservationID = rs.getInt("reservationID");
			} else {
				//
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
		return reservationID;
	}

	public static int insertDriverAndReservation(String phoneNumber, String email, String parkingLotID, int carSize,
			Timestamp reservationTime, String identificationNumber) {

		int result = 0;
		Connection conn = null;
		Statement stmtIR = null;
		Statement stmtID = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute insertDriverAndReservation");

			stmtID = conn.createStatement();
			stmtID.executeUpdate("INSERT INTO driver (phoneNumber,identificationNumber)" + "VALUES ('" + phoneNumber
					+ "','" + identificationNumber + "')");

			stmtIR = conn.createStatement();
			result = stmtIR
					.executeUpdate("INSERT INTO reservation (phoneNumber,email,parkingLotID,carSize,reservationTime) "
							+ "VALUES ( '" + phoneNumber + "','" + email + "','" + parkingLotID + "'," + carSize + ",'"
							+ reservationTime + "')");
			System.out.println("Reservation complete.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmtID.close();
				stmtIR.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("\n\n- MySQL Connection Close");

		return result;
	}

	public static int updateExitTimeFromReservation(String phoneNumber, Timestamp ts) {
		Connection conn = null;
		Statement stmtU = null;
		int result = 0;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute updateExitTimeFromReservation");

			stmtU = conn.createStatement();
			result = stmtU.executeUpdate(
					"UPDATE reservation SET exitTime = '" + ts + "' WHERE phoneNumber = '" + phoneNumber + "'");
			System.out.println("Reservation update complete.");
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
		System.out.println("\n\n- MySQL Connection Close");
		return result;
	}

	public static int updateEntranceTimeFromReservation(String phoneNumber, Timestamp ts) {
		Connection conn = null;
		Statement stmtU = null;
		int result = 0;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute updateEntranceTimeFromReservation");

			stmtU = conn.createStatement();
			result = stmtU.executeUpdate(
					"UPDATE reservation SET entranceTime = '" + ts + "' WHERE phoneNumber = '" + phoneNumber + "'");
			System.out.println("Reservation update complete.");
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
		System.out.println("\n\n- MySQL Connection Close");
		return result;
	}

	public static int updatePhoneNumberFromDriver(String originalPhoneNumber, String changedPhoneNumber) {
		Connection conn = null;
		Statement stmtU = null;
		int result = 0;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute updatePhoneNumberFromDriver");

			stmtU = conn.createStatement();
			result = stmtU.executeUpdate("UPDATE driver SET phoneNumber = '" + changedPhoneNumber
					+ "' WHERE phoneNumber = '" + originalPhoneNumber + "'");
			System.out.println("Reservation update complete.");
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
		System.out.println("\n\n- MySQL Connection Close");
		return result;
	}

	public static int selectParkingControllerDeviceStateFromParkingLot(String parkingContollerDeviceID) {
		Statement stmtS = null;
		ResultSet rs = null;
		Connection conn = null;
		int parkingContollerDeviceState = 0;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute selectParkingControllerDeviceStateFromParkingLot");
			stmtS = conn.createStatement();

			String selectSql;
			selectSql = "SELECT parkingContollerDeviceState FROM ParkingLot WHERE parkingContollerDeviceID = '"
					+ parkingContollerDeviceID + "'";
			stmtS.execute(selectSql);
			rs = stmtS.getResultSet();
			if (rs.next()) {
				parkingContollerDeviceState = rs.getInt("parkingContollerDeviceState");
			} else {
				//
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
		return parkingContollerDeviceState;
	}

	public static Reservation selectAllFromReservation(String phoneNumber) {
		Reservation result = null;

		Statement stmtS = null;
		ResultSet rs = null;
		Connection conn = null;

		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute selectAllFromReservation");
			stmtS = conn.createStatement();

			String selectSql;
			selectSql = "SELECT * FROM reservation WHERE phoneNumber = '" + phoneNumber + "'";
			stmtS.execute(selectSql);
			rs = stmtS.getResultSet();
			if (rs.next()) {
				result = new Reservation();
				result.setReservationID(rs.getInt("reservationID"));
				result.setPhoneNumber(rs.getString("phoneNumber"));
				result.setEmail(rs.getString("email"));
				result.setParkingLotID(rs.getString("parkingLotID"));
				result.setCarSize(rs.getInt("carSize"));
				result.setReservationTime(rs.getTimestamp("reservationTime"));
				result.setEntranceTime(rs.getTimestamp("entranceTime"));
				result.setExitTime(rs.getTimestamp("exitTime"));
			} else {
				//
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
		return result;
	}

	public static int updateStateFromDriver(String phoneNumber, String state) {
		int result = 0;

		Connection conn = null;
		Statement stmtU = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute updateStateFromDriver");

			stmtU = conn.createStatement();
			result = stmtU.executeUpdate("UPDATE driver SET state = '" + state + "'");
			System.out.println("Driver update complete.");
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
		System.out.println("\n\n- MySQL Connection Close");

		return result;
	}

	// Arduino <-> localsureparkserver
	public static void updateParkingContollerDeviceStateFromParkinglot(String parkingContollerDeviceID, String state) {
		Connection conn = null;
		Statement stmtU = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute updateParkingContollerDeviceStateFromParkinglot");

			stmtU = conn.createStatement();
			stmtU.execute("UPDATE parkinglot SET parkingContollerDeviceState = " + Integer.parseInt(state)
					+ " WHERE parkingContollerDeviceID = '" + parkingContollerDeviceID + "'");
			System.out.println("Parking controller " + parkingContollerDeviceID + " update complete.");
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
		System.out.println("\n\n- MySQL Connection Close");
	}

	public static void updateParkingContollerDevicesFromParkinglot(String deviceString,Socket client,HashMap<String, SenderCallback> controllerIDClientHashMap, HashMap<String, String> deviceIDAndAuduioIDHashMap) {
		Statement stmtS = null;
		ResultSet rs = null;
		Connection conn = null;

		System.out.println("\n- MySQL Connection, execute updateParkingContollerDevicesFromParkinglot");
		String str = deviceString;
		String[] spaceDivide = str.split(" ");
		System.out.println(spaceDivide.length);
		for (int i = 0; i < spaceDivide.length; i++) {
			if (i == 0) { // deviceController ID, need to input some array list
				System.out.println("아두이노 등록");
				SenderCallback callback = new SenderCallback(client);
				controllerIDClientHashMap.put(spaceDivide[0], callback);
				System.out.println(controllerIDClientHashMap.size());

			} else {
				/*
				 * Arduino register information
				 * (results[0],results[1],results[2],results[3],results[4])
				 * (parkingContollerDeviceID,parkingContollerDeviceType,
				 * parkingContollerDeviceAlive,parkingContollerDeviceState,
				 * parkingContollerID) Need to DB insert operation
				 */
				String[] results = spaceDivide[i].split(",");
				try {
					Class.forName(JDBC_DRIVER);
					conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
					// System.out.println("\n- MySQL Connection, execute
					// dbCntDevRegOperation");
					stmtS = conn.createStatement();

					
					String selectSql;
					selectSql = "SELECT parkingContollerDeviceID FROM parkinglot WHERE parkingContollerDeviceID = '"
							+ results[0] + "'";
					stmtS.execute(selectSql);

					deviceIDAndAuduioIDHashMap.put(results[0],results[4]);
				
					if(Integer.parseInt(results[1]) == CurrentInfo.ENTRYTYPE)
					{
						System.out.println(results[1] + " id");
						CurrentInfo.entryID = results[0];
					}
					
					if(Integer.parseInt(results[1]) == CurrentInfo.EXITTYPE)
					{
						CurrentInfo.exitID = results[0];
					}
					
					rs = stmtS.getResultSet();
					if (rs.next()) {
						// System.out.println("Parking controller device already
						// registered. So, update device state.");
						Statement stmtU = null;
						try {
							stmtU = conn.createStatement();
							stmtU.execute("UPDATE parkinglot SET parkingContollerDeviceType="
									+ Integer.parseInt(results[1]) + ", " + "parkingContollerDeviceAlive="
									+ Integer.parseInt(results[2]) + ", " + "parkingContollerDeviceState="
									+ Integer.parseInt(results[3]) + ", " + "parkingContollerID='" + results[4]
									+ "' WHERE parkingContollerDeviceID = '" + results[0] + "'");
							// System.out.println("Parking controller update
							// complete.");
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
						// insert operation, and also need to insert array list
						// for call back
						// System.out.println("Parking controller device
						// register start.");
						Statement stmtI = null;
						try {
							
							
							stmtI = conn.createStatement();
							stmtI.execute("INSERT INTO parkinglot (parkingContollerDeviceID,parkingContollerDeviceType,"
									+ "parkingContollerDeviceAlive,parkingContollerDeviceState,parkingContollerID) "
									+ "VALUES ('" + results[0] + "'," + Integer.parseInt(results[1]) + ","
									+ Integer.parseInt(results[2]) + "," + Integer.parseInt(results[3]) + ",'"
									+ results[4] + "')");
							// System.out.println("Parking controller register
							// complete.");
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
				} // finally
			}
		}
		System.out.println("\n\n- MySQL Connection Close");
	}

	public static void insertParkingposition(String psArg) {
		// TEST CODE
		// CurrentInfo.reservationID = 1;
		Connection conn = null;
		Statement stmtI = null;
		Statement stmtU = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute insertDBParkingposition");

			System.out.println(psArg + " " + CurrentInfo.reservationID);
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

	// Added
	public static ArrayList<Reservation> selectAllFromReservation() {
		Connection conn = null;
		Statement stmtS = null;
		ResultSet rs = null;
		ArrayList<Reservation> result = new ArrayList<Reservation>();
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute selectAllFromReservation");
			stmtS = conn.createStatement();
			rs = stmtS.executeQuery("SELECT * FROM reservation");
			while (rs.next()) {
				Reservation tmp = new Reservation();
				tmp.setReservationID(rs.getInt(1));
				tmp.setPhoneNumber(rs.getString(2));
				tmp.setEmail(rs.getString(3));
				tmp.setParkingLotID(rs.getString(4));
				tmp.setCarSize(rs.getInt(5));
				tmp.setReservationTime(rs.getTimestamp(6));
				tmp.setEntranceTime(rs.getTimestamp(7));
				tmp.setExitTime(rs.getTimestamp(8));

				result.add(tmp);
			}
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
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
		return result;
	}

	// Added
	public static ArrayList<ParkingLot> selectAllFromParkingLot() {
		Connection conn = null;
		Statement stmtS = null;
		ResultSet rs = null;
		ArrayList<ParkingLot> result = new ArrayList<ParkingLot>();
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute selectAllFromParkingLot");
			stmtS = conn.createStatement();
			rs = stmtS.executeQuery("SELECT * FROM reservation");
			while (rs.next()) {
				ParkingLot tmp = new ParkingLot();
				tmp.setParkingContollerDeviceID(rs.getString(1));
				tmp.setParkingContollerDeviceType(rs.getInt(2));
				tmp.setParkingContollerDeviceAlive(rs.getInt(3));
				tmp.setParkingContollerDeviceState(rs.getInt(4));
				tmp.setParkingContollerID(rs.getString(5));
				result.add(tmp);
			}
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
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
		return result;
	}

	// Added
	public static ArrayList<Driver> selectAllFromDriver() {
		Connection conn = null;
		Statement stmtS = null;
		ResultSet rs = null;
		ArrayList<Driver> result = new ArrayList<Driver>();
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute selectAllFromDriver");
			stmtS = conn.createStatement();
			rs = stmtS.executeQuery("SELECT * FROM reservation");
			while (rs.next()) {
				Driver tmp = new Driver();
				tmp.setPhoneNumber(rs.getString(1));
				tmp.setIdentificationNumber(rs.getString(2));
				tmp.setState(rs.getString(3));
				result.add(tmp);
			}
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
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
		return result;
	}

	// Added
	public static ArrayList<ParkedPosition> selectAllFromParkedPosition() {
		Connection conn = null;
		Statement stmtS = null;
		ResultSet rs = null;
		ArrayList<ParkedPosition> result = new ArrayList<ParkedPosition>();
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute selectAllFromDriver");
			stmtS = conn.createStatement();
			rs = stmtS.executeQuery("SELECT * FROM reservation");
			while (rs.next()) {
				ParkedPosition tmp = new ParkedPosition();
				tmp.setReservationID(rs.getInt(1));
				tmp.setParkingContollerDeviceID(rs.getString(2));
				result.add(tmp);
			}
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
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
		return result;
	}

	// Added
	public static int deleteParkedPosition(int reservationID) {
		int result = 0;
		Connection conn = null;
		Statement stmtID = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute deleteParkedPosition");

			stmtID = conn.createStatement();
			result = stmtID.executeUpdate("DELETE FROM parkedposition WHERE reservationID = '" + reservationID + "'");
			System.out.println("Reservation delete complete.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmtID.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("\n\n- MySQL Connection Close");
		return result;
	}
	
	
	//Added
	   public static ArrayList<ParkingLot> selectAllFromParkingLot (int parkingContollerDeviceType) {
	      Connection conn = null;
	      Statement stmtS = null;
	      ResultSet rs = null;
	      ArrayList<ParkingLot> result = new ArrayList<ParkingLot>();
	      try {
	         Class.forName(JDBC_DRIVER);
	         conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
	         System.out.println("\n- MySQL Connection, execute selectAllFromParkingLot");
	         stmtS = conn.createStatement();
	         rs = stmtS.executeQuery("SELECT * FROM parkinglot WHERE parkingContollerDeviceType = " + parkingContollerDeviceType);
	         while (rs.next()) {
	            ParkingLot tmp = new ParkingLot();
	            tmp.setParkingContollerDeviceID(rs.getString(1));
	            tmp.setParkingContollerDeviceType(rs.getInt(2));
	            tmp.setParkingContollerDeviceAlive(rs.getInt(3));
	            tmp.setParkingContollerDeviceState(rs.getInt(4));
	            tmp.setParkingContollerID(rs.getString(5));
	            result.add(tmp);
	         }
	      } catch (SQLException ex) {
	         // handle any errors
	         System.out.println("SQLException: " + ex.getMessage());
	         System.out.println("SQLState: " + ex.getSQLState());
	         System.out.println("VendorError: " + ex.getErrorCode());
	      } catch (Exception ex) {
	         ex.printStackTrace();
	      } finally {
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
	      return result;
	   }
	   
	   // Added 16-07027 23:46
	   public static String selectParkingContollerDeviceIDFromParkedPosition(int reservationID) {
	      Connection conn = null;
	      Statement stmtS = null;
	      ResultSet rs = null;
	      String result = "";
	      try {
	         Class.forName(JDBC_DRIVER);
	         conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
	         System.out.println("\n- MySQL Connection, execute selectParkingContollerDeviceIDFromParkedPosition");
	         stmtS = conn.createStatement();
	         rs = stmtS.executeQuery("SELECT parkingContollerDeviceID FROM parkedposition WHERE reservationID = '" + reservationID + "'");
	         while (rs.next()) {
	            result = rs.getString("parkingContollerDeviceID");
	         }
	      } catch (SQLException ex) {
	         // handle any errors
	         System.out.println("SQLException: " + ex.getMessage());
	         System.out.println("SQLState: " + ex.getSQLState());
	         System.out.println("VendorError: " + ex.getErrorCode());
	      } catch (Exception ex) {
	         ex.printStackTrace();
	      } finally {
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
	      return result;
	   }

}