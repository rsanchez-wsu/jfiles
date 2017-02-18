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

			// All of our connections are AutoCommit = false
			conn.setAutoCommit(false);
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
	 * Builds all the tables for our database.
	 */
	// TODO: There has to be a better way to accomplish this
	public static void createTables() {
		try (	Connection conn = openConnection();
				Statement stmt = conn.createStatement()) {

			try {
				stmt.executeUpdate(
						"CREATE TABLE USERS ("
						+ "id INTEGER NOT NULL "
							+ "GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1)"
							+ "PRIMARY KEY,"
						+ "name VARCHAR(40),"
						+ "pass VARCHAR(20),"
						+ "role INTEGER)");
			} catch (SQLException e) {
				if (!e.getSQLState().equals("X0Y32")) {
					e.printStackTrace();
				}
			}

			try {
				stmt.executeUpdate(
						"CREATE TABLE ROLES ("
						+ "id INTEGER NOT NULL "
							+ "GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1)"
							+ "PRIMARY KEY,"
						+ "name VARCHAR(20))");
			} catch (SQLException e) {
				if (!e.getSQLState().equals("X0Y32")) {
					e.printStackTrace();
				}
			}

			try {
				stmt.executeUpdate(
						"CREATE TABLE PERMISSIONS ("
						+ "id INTEGER NOT NULL "
							+ "GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1)"
							+ "PRIMARY KEY,"
						+ "doc XML)");
			} catch (SQLException e) {
				if (!e.getSQLState().equals("X0Y32")) {
					e.printStackTrace();
				}
			}

			// Commit the transaction.
			conn.commit();
		} catch (SQLException e) {
			logger.error(e);
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
		String sql = "SELECT * FROM ROLES WHERE ID = ?";
		String sql2 = "INSERT INTO USERS (name, pass, role) VALUES (?, ?, ?)";

		try (	Connection conn = openConnection();
				PreparedStatement selectStmt = conn.prepareStatement(sql);
				PreparedStatement insertStmt = conn.prepareStatement(sql2)) {

			// Prepare our query
			selectStmt.setInt(1, role);

			// Query the database to see if the role passed in actually exists.
			try (ResultSet rs = selectStmt.executeQuery()) {
				// If the role does not exist set to default role (0)
				if (!rs.next()) {
					role = 0;
				}

				// Create user in database.
				insertStmt.setString(1, name);
				insertStmt.setString(2, pass);
				insertStmt.setInt(3, role);
				insertStmt.executeUpdate();
			}

			// Commit the transaction.
			conn.commit();
		} catch (SQLException e) {
			logger.error(e);
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
		String sql = "INSERT INTO PERMISSIONS (doc) "
				+ "VALUES (XMLPARSE(DOCUMENT CAST (? AS CLOB) PRESERVE WHITESPACE))";
		try (	Connection conn = openConnection();
				PreparedStatement insertStmt = conn.prepareStatement(sql)) {

			// Create permission in database. (We are parsing the XML doc via
			// XMLPARSE and storing it as a binary XML data type in the
			// database.
			insertStmt.setString(1, doc);
			insertStmt.executeUpdate();

			// Commit the transaction.
			conn.commit();
		} catch (SQLException e) {
			logger.error(e);
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
		String sql = "SELECT ID FROM ROLES WHERE NAME = ?";
		String sql2 = "INSERT INTO ROLES (NAME) VALUES (?)";

		try (	Connection conn = openConnection();
				PreparedStatement selectStmt = conn.prepareStatement(sql);
				PreparedStatement insertStmt = conn.prepareStatement(sql2)) {

			// Prepare our query
			selectStmt.setString(1, name);

			// Query database to see if the role already exists.
			try (ResultSet rs = selectStmt.executeQuery()) {
				// If the role does not exist then create the role
				if (!rs.next()) {
					insertStmt.setString(1, name);
					insertStmt.executeUpdate();
				}
			}

			// Query the table for the ID of the role we just created
			try (ResultSet rs = selectStmt.executeQuery()) {
				if (rs.next()) {
					id = rs.getInt(1);
				}
			}

			// Commit the transaction.
			conn.commit();
		} catch (SQLException e) {
			logger.error(e);
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
