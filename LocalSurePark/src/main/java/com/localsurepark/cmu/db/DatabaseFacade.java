package com.localsurepark.cmu.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.localsurepark.cmu.CurrentInfo;
import com.localsurepark.cmu.domain.Reservation;

public class DatabaseFacade {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/localsurepark";

	static final String USERNAME = "surepark";
	static final String PASSWORD = "surepark";

	public static int dbReservationDelete(String phoneNumber) {
		Connection conn = null;
		Statement stmtID = null;
		int result = 0;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute dbReservationDelete");

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

	public static int dbReservationIDSelect(String phoneNumber) {
		Statement stmtS = null;
		ResultSet rs = null;
		Connection conn = null;
		int reservationID = 0;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute dbReservationIDSelect");
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

	public static int dbReservationInsert(String phoneNumber, String email, String parkingLotID, int carSize,
			Timestamp reservationTime, String identificationNumber) {

		int result = 0;
		Connection conn = null;
		Statement stmtIR = null;
		Statement stmtID = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute dbReservationInsert");

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

	public static void dbParkingContollerDeviceOperations(String devStr) {
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
			System.out.println("\n- MySQL Connection, execute dbCntDevRegOperation");
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

	public static void dbParkingSpaceAssignOperation(String psArg) {
		// TEST CODE
		// CurrentInfo.reservationID = 1;
		Connection conn = null;
		Statement stmtI = null;
		Statement stmtU = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute dbParkingSpaceAssignOperation");

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

	public static void dbReservationExitUpdate(String phoneNumber, Timestamp ts) {
		Connection conn = null;
		Statement stmtU = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute dbReservationExitUpdate");

			stmtU = conn.createStatement();
			stmtU.execute("UPDATE reservation SET exitTime = '" + ts + "' WHERE phoneNumber = '" + phoneNumber + "'");
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
	}

	public static void dbReservationEntranceUpdate(String phoneNumber, Timestamp ts) {
		Connection conn = null;
		Statement stmtU = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute dbReservationEntranceUpdate");

			stmtU = conn.createStatement();
			stmtU.execute(
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
	}

	public static int dbReservationPhoneNumberUpdate(String originalPhoneNumber, String changedPhoneNumber) {
		Connection conn = null;
		Statement stmtU = null;
		int result = 0;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute dbReservationExitUpdate");

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

	public static void dbParkingLotStallUpdate(String psArg) {
		Connection conn = null;
		Statement stmtU = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute dbParkingLotStallUpdate");

			stmtU = conn.createStatement();
			stmtU.execute("UPDATE parkinglot SET parkingContollerDeviceState = 1 "
					+ "WHERE parkingContollerDeviceID = '" + psArg + "'");
			System.out.println("Parking controller " + psArg + " update complete.");
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

	public static void dbParkingLotGateUpdate(String psArg, int state) {
		Connection conn = null;
		Statement stmtU = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute dbParkingLotGateUpdate");

			stmtU = conn.createStatement();
			stmtU.execute("UPDATE parkinglot SET parkingContollerDeviceState = " + state
					+ " WHERE parkingContollerDeviceID = '" + psArg + "'");
			System.out.println("Parking controller " + psArg + " update complete.");
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

	public static int dbDeviceStateSelect(String parkingContollerDeviceID) {
		Statement stmtS = null;
		ResultSet rs = null;
		Connection conn = null;
		int parkingContollerDeviceState = 0;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute dbReservationIDSelect");
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

	public static Reservation dbReservationSelect(String phoneNumber) {
		Reservation result = null;

		Statement stmtS = null;
		ResultSet rs = null;
		Connection conn = null;

		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute dbReservationIDSelect");
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
				result.setEntranranceTime(rs.getTimestamp("entranranceTime"));
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

	public static int dbDriverStateUpdate(String phoneNumber, String state) {
		int result = 0;

		Connection conn = null;
		Statement stmtU = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			System.out.println("\n- MySQL Connection, execute dbDriverStateUpdate");

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

}