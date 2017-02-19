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

	private static final String DATABASE_URL_OPEN = "jdbc:derby:JFilesDB;create=true";
	private static final String DATABASE_URL_SHUTDOWN = "jdbc:derby:JFilesDB;shutdown=true";

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
			conn = DriverManager.getConnection(DATABASE_URL_OPEN);

			// All of our connections are AutoCommit = false
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			if (e.getSQLState().equals("XJ040")) {
				logger.error("\n\tConnection already open somwhere else,"
						+ " make sure no Eclipse Data Tools Platform connections are open.", e);
			} else {
				logger.error(e);
			}
		} catch (ClassNotFoundException e) {
			logger.error("\n\tUnable to load JDBC Embedded Derby Driver", e);
		}
		return conn;
	}

	/**
	 * Closes the connection to the database.
	 */
	public static void shutdown() {
		try {
			// Shutdown the connection to the database
			DriverManager.getConnection(DATABASE_URL_SHUTDOWN).close();
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
						"CREATE TABLE ROLES ("
						+ "ROLE_ID INTEGER NOT NULL "
							+ "GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),"
						+ "ROLE_NAME VARCHAR(20) NOT NULL,"
						+ "PRIMARY KEY (ROLE_ID),"
						+ "UNIQUE (ROLE_NAME))");
			} catch (SQLException e) {
				if (!e.getSQLState().equals("X0Y32")) {
					e.printStackTrace();
				}
			}

			try {
				stmt.executeUpdate(
						"CREATE TABLE PERMISSIONS ("
						+ "PERM_ID INTEGER NOT NULL "
							+ "GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),"
						+ "PERM_DOC XML NOT NULL,"
						+ "PRIMARY KEY (PERM_ID))");
			} catch (SQLException e) {
				if (!e.getSQLState().equals("X0Y32")) {
					e.printStackTrace();
				}
			}

			try {
				stmt.executeUpdate(
						"CREATE TABLE USERS ("
						+ "USER_ID INTEGER NOT NULL "
							+ "GENERATED ALWAYS AS IDENTITY (START WITH 100000, INCREMENT BY 1),"
						+ "USER_NAME VARCHAR(40) NOT NULL,"
						+ "USER_PASS VARCHAR(20) NOT NULL,"
						+ "USER_ROLE INTEGER,"
						+ "PRIMARY KEY (USER_ID),"
						+ "UNIQUE (USER_NAME),"
						+ "FOREIGN KEY (USER_ROLE) REFERENCES ROLES (ROLE_ID))");
			} catch (SQLException e) {
				if (!e.getSQLState().equals("X0Y32")) {
					e.printStackTrace();
				}
			}

			try {
				stmt.executeUpdate(
						"CREATE TABLE USER_PERMISSIONS ("
						+ "USER_ID INT NOT NULL,"
						+ "PERM_ID INT NOT NULL,"
						+ "FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID),"
						+ "FOREIGN KEY (PERM_ID) REFERENCES PERMISSIONS (PERM_ID))");
			} catch (SQLException e) {
				if (!e.getSQLState().equals("X0Y32")) {
					e.printStackTrace();
				}
			}

			try {
				stmt.executeUpdate(
						"CREATE TABLE ROLE_PERMISSIONS ("
						+ "ROLE_ID INT NOT NULL,"
						+ "PERM_ID INT NOT NULL,"
						+ "FOREIGN KEY (ROLE_ID) REFERENCES ROLES (ROLE_ID),"
						+ "FOREIGN KEY (PERM_ID) REFERENCES PERMISSIONS (PERM_ID))");
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
		String sql = "INSERT INTO USERS (USER_NAME, USER_PASS, USER_ROLE) VALUES (?, ?, ?)";

		try (	Connection conn = openConnection();
				PreparedStatement insertStmt = conn.prepareStatement(sql)) {


			// Create user in database.
			insertStmt.setString(1, name);
			insertStmt.setString(2, pass);
			insertStmt.setInt(3, role);
			insertStmt.executeUpdate();

			// Commit the transaction.
			conn.commit();
			logger.info(String.format("User \"%s\" added to the database.", name));
		} catch (SQLException e) {
			if (e.getSQLState().equals("23505")) {
				logger.error("\n\tUser not added. Users must have unique names");
			} else if (e.getSQLState().equals("23503")) {
				logger.error("\n\tUser not added. Role must match an existing role.");
			} else {
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
	 *            XML document
	 * @return id of the permission created
	 */
	public static int createPermission(String doc) {
		String sql = "INSERT INTO PERMISSIONS (PERM_DOC) "
				+ "VALUES (XMLPARSE(DOCUMENT CAST (? AS CLOB) PRESERVE WHITESPACE))";
		String sql2 =
				"SELECT PERM_ID FROM PERMISSIONS WHERE"
						+ " PERM_DOC = (XMLPARSE(DOCUMENT CAST (? AS CLOB) PRESERVE WHITESPACE))";
		int id = -1;
		try (	Connection conn = openConnection();
				PreparedStatement insertStmt = conn.prepareStatement(sql);
				PreparedStatement selectStmt = conn.prepareStatement(sql2)) {

			// Create permission in database. (We are parsing the XML doc via
			// XMLPARSE and storing it as a binary XML data type in the
			// database.
			insertStmt.setString(1, doc);
			insertStmt.executeUpdate();

			// Get the ID of the the permission that was just created
			selectStmt.setString(1, doc);
			try (ResultSet rs = selectStmt.executeQuery()) {
				if (rs.next()) {
					id = rs.getInt(1);
					logger.info(
							String.format("\n\tPermission added to the database with ID=%d.", id));
				} else {
					logger.error("\n\tPermission not added to the database, ID not found");

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
	 * Creates a new role in the database.
	 *
	 * <p>
	 * NOTE: This is a Derby Prepared Statement This function is not to be
	 * called directly. Rather only called via an SQL call statement.
	 * </p>
	 *
	 * @param name
	 *            name of the role
	 * @return the id of the role created.
	 */
	public static int createRole(String name) {
		int id = -1;
		String sql = "INSERT INTO ROLES (ROLE_NAME) VALUES (?)";
		String sql2 = "SELECT ID FROM ROLES WHERE ROLE_NAME = ?";

		try (	Connection conn = openConnection();
				PreparedStatement insertStmt = conn.prepareStatement(sql);
				PreparedStatement selectStmt = conn.prepareStatement(sql2)) {

			insertStmt.setString(1, name);
			insertStmt.executeUpdate();

			// Query the table for the ID of the role we just created
			try (ResultSet rs = selectStmt.executeQuery()) {
				if (rs.next()) {
					id = rs.getInt(1);
					logger.info(
							String.format("\n\tRole added to the database with ID=%d.", id));
				} else {
					logger.error("\n\tRole not added to the database, ID not found");

				}
			}

			// Commit the transaction.
			conn.commit();
		} catch (SQLException e) {
			if (e.getSQLState().equals("23505")) {
				logger.error("\n\tRole not added, this role already exisits.");
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
