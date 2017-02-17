/*
 * Copyright (C) 2017 - WSU CEG3120 Students
 *
 *
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package edu.wright.cs.jfiles.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class used to perform actions on the database.
 *
 * @author Matt Gilene
 */
public class DatabaseController {

	private static final String DATABASE_URL = "jdbc:derby:JFilesDB;create=true";
	private static final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

	private static final Logger logger = LogManager.getLogger(DatabaseController.class);

	// Array of predefined tables for the database
	private static final String[] TABLES = {
			"CREATE TABLE USERS ("
					+ "id INTEGER NOT NULL "
						+ "GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1)"
						+ "PRIMARY KEY,"
					+ "name VARCHAR(40),"
					+ "pass VARCHAR(20),"
					+ "role INTEGER)",
			"CREATE TABLE ROLES ("
					+ "id INTEGER NOT NULL "
						+ "GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1)"
						+ "PRIMARY KEY,"
					+ "name VARCHAR(20))",
			"CREATE TABLE PERMISSIONS ("
					+ "id INTEGER NOT NULL "
						+ "GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1)"
						+ "PRIMARY KEY,"
					+ "doc XML)"
	};

	/**
	 * Prevents Instantiation, this class is meant to be completely static.
	 */
	private DatabaseController() {}

	/**
	 * Opens a connection to the database.
	 * @return JDBC Connection
	 */
	public static Connection openConnection() {
		Connection conn = null;
		try {
			// Load the JDBC driver and open a connection
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DATABASE_URL);
		} catch (SQLException e) {
			if (e.getSQLState().equals("XJ040")) {
				logger.error(
						"Connection already open somwhere else,"
								+ " make sure no Eclipse Data Tools Platform connections are open.",
						e);
			} else {
				logger.error(e);
			}
		} catch (ClassNotFoundException e) {
			logger.error("Unable to load JDBC Embedded Derby Driver", e);
		}
		return conn;
	}

	/**
	 * Closes the connection to the database.
	 */
	public static void shutdown() {
		try {
			// Shutdown the connection to the database
			DriverManager.getConnection("jdbc:derby:JFilesDB;shutdown=true").close();
		} catch (SQLException e) {
			if (!e.getSQLState().equals("08006")) {
				logger.error(e);
			}
		}
	}

	/**
	 * Drops all the tables in the database.
	 */
	// NOTE: This uses regular statements + string formation for the update due
	// to JDBC/Derby not supporting the use of prepared statements when dropping
	// tables.
	public static void resetDatabase() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = openConnection();

			// This executes a batch of statements so wait until the end to
			// commit them all as a single transaction.
			conn.setAutoCommit(false);

			stmt = conn.createStatement();

			// Fetch all the tables in the APP Schema and drop each one
			rs = conn.getMetaData().getTables(null, "APP", null, null);
			while (rs.next()) {
				String tablename = rs.getString("TABLE_NAME");
				String sql = "DROP TABLE " + tablename;
				stmt.executeUpdate(sql);
			}

			// Commit the transaction.
			conn.commit();
		} catch (SQLException e) {
			logger.error(e);
		}

		try {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			logger.error(e);
		}
	}

	/**
	 * Builds all the tables for our database.
	 */
	public static void createTables() {
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = DatabaseController.openConnection();

			// This executes a batch of statements so wait until the end to
			// commit them all as a single transaction.
			conn.setAutoCommit(false);

			// For each predefined table in TABLES create the table in the
			// database
			stmt = conn.createStatement();
			for (String tbl : TABLES) {
				try {
					stmt.executeUpdate(tbl);
				} catch (SQLException e) {
					if (!e.getSQLState().equals("X0Y32")) {
						e.printStackTrace();
					}
				}
			}

			// Commit the transaction.
			conn.commit();
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error(e);
			}
		}
	}

	/**
	 * Creates a new user in the database.
	 *
	 * <p>
	 * NOTE: This is a Derby Prepared Statement This function is not to be
	 * called directly. Rather only called via an SQL call statement.
	 * </p>
	 *
	 * @param name
	 *            the user's name
	 * @param pass
	 *            the user's password
	 * @param role
	 *            the user's role
	 */
	public static void createUser(String name, String pass, int role) {
		Connection conn = null;
		PreparedStatement selectStmt = null;
		PreparedStatement insertStmt = null;
		ResultSet rs = null;

		try {
			conn = DatabaseController.openConnection();

			// Query the database to see if the role passed in actually exists.
			String sql = "SELECT * FROM ROLES WHERE ID = ?";
			selectStmt = conn.prepareStatement(sql);
			selectStmt.setInt(1, role);

			rs = selectStmt.executeQuery();
			// If the role does not exist set to default role (0)
			if (!rs.next()) {
				role = 0;
			}

			// Create user in database.
			String sql2 = "INSERT INTO USERS (name, pass, role) VALUES (?, ?, ?)";
			insertStmt = conn.prepareStatement(sql2);
			insertStmt.setString(1, name);
			insertStmt.setString(2, pass);
			insertStmt.setInt(3, role);
			insertStmt.executeUpdate();
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (rs != null) {
					rs.close();
				}
				if (insertStmt != null) {
					insertStmt.close();
				}
				if (selectStmt != null) {
					selectStmt.close();
				}

			} catch (SQLException e) {
				logger.error(e);
			}
		}
	}

	/**
	 * Creates a new permission in the database.
	 *
	 * <p>
	 * NOTE: This is a Derby Prepared Statement This function is not to be
	 * called directly. Rather only called via an SQL call statement.
	 * </p>
	 *
	 * @param doc
	 *            xml document
	 */
	public static void createPermission(String doc) {
		Connection conn = null;
		PreparedStatement insertStmt = null;

		try {
			conn = DatabaseController.openConnection();

			// Create permission in database. (We are parsing the XML doc via
			// XMLPARSE and storing it as a binary XML data type in the
			// database.
			String sql = "INSERT INTO PERMISSIONS (doc) "
					+ "VALUES (XMLPARSE(DOCUMENT CAST (? AS CLOB) PRESERVE WHITESPACE))";
			insertStmt = conn.prepareStatement(sql);
			insertStmt.setString(1, doc);
			insertStmt.executeUpdate();
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			try {
				if (insertStmt != null) {
					insertStmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error(e);
			}
		}
	}

	/**
	 * Creates a new role in the database.
	 *
	 * <p>
	 * NOTE: This is a Derby Prepared Statement This function is not to be
	 * called directly. Rather only called via an SQL call statement.
	 * </p>
	 *
	 * @param name
	 *            name of the role
	 */
	public static int createRole(String name) {
		int id = -1;
		Connection conn = null;
		PreparedStatement selectStmt = null;
		PreparedStatement insertStmt = null;
		ResultSet rs = null;

		try {
			conn = DatabaseController.openConnection();

			// Query database to see if the role already exists.
			String sql = "SELECT ID FROM ROLES WHERE NAME = ?";
			selectStmt = conn.prepareStatement(sql);
			selectStmt.setString(1, name);

			rs = selectStmt.executeQuery();
			// If the role does not exist then create the role
			if (!rs.next()) {
				rs.close();
				String sql2 = "INSERT INTO ROLES (NAME) VALUES (?)";

				insertStmt = conn.prepareStatement(sql2);
				insertStmt.setString(1, name);
				insertStmt.executeUpdate();
				insertStmt.close();

				rs = selectStmt.executeQuery();
				if (rs.next()) {
					id = rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (insertStmt != null) {
					insertStmt.close();
				}
				if (selectStmt != null) {
					selectStmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				logger.error(e);
			}
		}
		return id;
	}

	/**
	 * Main, testing purposes only. This can be used to setup the database as
	 * well.
	 *
	 * @param args
	 *            jvm args
	 * @throws ClassNotFoundException
	 *             if Driver can't be loaded
	 */
	public static void main(String[] args) {
		// Reset DB
		resetDatabase();

		// Build all the tables
		createTables();

		// Create NONE role
		int id1 = createRole("NONE");
		System.out.println("NONE: id=" + id1);

		// Create ADMIN role
		int id2 = createRole("ADMIN");
		System.out.println("ADMIN: id=" + id2);

		// Create User with ADMIN role
		createUser("Bill Gates", "windows_vista", 1);

		// Create User with invalid role -> default to NONE
		createUser("Steve Jobs", "earpods", 12);

		// Make sure to shutdown the database connection before the program exits.
		shutdown();
	}
}
