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

import edu.wright.cs.jfiles.database.DatabaseUtils.PermissionType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
	private DatabaseController() {
	}

	/**
	 * Opens a connection to the database.
	 *
	 * @return JDBC Connection
	 */
	public static Connection openConnection() {
		Connection conn = null;
		try {
			// Load the JDBC driver and open a connection
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DATABASE_URL_OPEN);
		} catch (SQLException e) {
			if (e.getSQLState().equals("XJ040")) {
				logger.error(
						"%n\tConnection already open somwhere else,"
								+ " make sure no Eclipse Data Tools Platform connections are open.",
						e);
			} else {
				logger.error(e);
			}
		} catch (ClassNotFoundException e) {
			logger.error("%n\tUnable to load JDBC Embedded Derby Driver", e);
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
		try (Connection conn = openConnection(); Statement createStmt = conn.createStatement()) {

			try {
				createStmt.executeUpdate("CREATE TABLE ROLES ("
						+ "ROLE_ID INTEGER NOT NULL "
							+ "GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),"
						+ "ROLE_NAME VARCHAR(20) NOT NULL,"
						+ "PRIMARY KEY (ROLE_ID),"
						+ "UNIQUE (ROLE_NAME))");
			} catch (SQLException e) {
				if (!e.getSQLState().equals("X0Y32")) {
					logger.error(e);
				}
			}

			try {
				createStmt.executeUpdate("CREATE TABLE PERMISSIONS ("
						+ "PERM_ID INTEGER NOT NULL "
							+ "GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),"
						+ "PERM_DOC XML NOT NULL,"
						+ "PRIMARY KEY (PERM_ID))");
			} catch (SQLException e) {
				if (!e.getSQLState().equals("X0Y32")) {
					logger.error(e);
				}
			}

			try {
				createStmt.executeUpdate("CREATE TABLE USERS ("
						+ "USER_ID INTEGER NOT NULL "
							+ "GENERATED ALWAYS AS IDENTITY (START WITH 100000, INCREMENT BY 1),"
						+ "USER_NAME VARCHAR(40) NOT NULL,"
						+ "USER_PASS VARCHAR(40) NOT NULL,"
						+ "USER_ROLE INTEGER,"
						+ "PRIMARY KEY (USER_ID),"
						+ "UNIQUE (USER_NAME),"
						+ "FOREIGN KEY (USER_ROLE) REFERENCES ROLES (ROLE_ID))");
			} catch (SQLException e) {
				if (!e.getSQLState().equals("X0Y32")) {
					logger.error(e);
				}
			}

			try {
				createStmt.executeUpdate("CREATE TABLE USER_PERMISSIONS ("
						+ "USER_ID INT NOT NULL,"
						+ "PERM_ID INT NOT NULL,"
						+ "FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID),"
						+ "FOREIGN KEY (PERM_ID) REFERENCES PERMISSIONS (PERM_ID))");
			} catch (SQLException e) {
				if (!e.getSQLState().equals("X0Y32")) {
					logger.error(e);
				}
			}

			try {
				createStmt.executeUpdate("CREATE TABLE ROLE_PERMISSIONS ("
						+ "ROLE_ID INT NOT NULL,"
						+ "PERM_ID INT NOT NULL,"
						+ "FOREIGN KEY (ROLE_ID) REFERENCES ROLES (ROLE_ID),"
						+ "FOREIGN KEY (PERM_ID) REFERENCES PERMISSIONS (PERM_ID))");
			} catch (SQLException e) {
				if (!e.getSQLState().equals("X0Y32")) {
					logger.error(e);
				}
			}
		} catch (SQLException e) {
			logger.error(e);
		}
	}

	/**
	 * Drops all the tables in the database.
	 */
	// TODO: There has to be a better way to accomplish this
	public static void dropTables() {
		try (Connection conn = openConnection(); Statement dropStmt = conn.createStatement()) {

			try {
				dropStmt.executeUpdate("DROP TABLE ROLE_PERMISSIONS");
			} catch (SQLException e) {
				if (!e.getSQLState().equals("X0Y32")) {
					logger.error(e);
				}
			}
			try {
				dropStmt.executeUpdate("DROP TABLE USER_PERMISSIONS");
			} catch (SQLException e) {
				if (!e.getSQLState().equals("X0Y32")) {
					logger.error(e);
				}
			}
			try {
				dropStmt.executeUpdate("DROP TABLE USERS");
			} catch (SQLException e) {
				if (!e.getSQLState().equals("X0Y32")) {
					logger.error(e);
				}
			}
			try {
				dropStmt.executeUpdate("DROP TABLE ROLES");
			} catch (SQLException e) {
				if (!e.getSQLState().equals("X0Y32")) {
					logger.error(e);
				}
			}
			try {
				dropStmt.executeUpdate("DROP TABLE PERMISSIONS");
			} catch (SQLException e) {
				if (!e.getSQLState().equals("X0Y32")) {
					logger.error(e);
				}
			}
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
	 * @throws FailedInsertException
	 *             thrown when invalid user data has been attempted to be
	 *             inserted into the database and the user could not be created.
	 * @throws IdNotFoundException
	 *             thrown when a database ID column can't be found for the given
	 *             role.
	 */
	public static int createUser(String name, String pass, int role)
			throws FailedInsertException, IdNotFoundException {
		String sql = "INSERT INTO USERS (USER_NAME, USER_PASS, USER_ROLE) VALUES (?, ?, ?)";
		int id = 0;
		try (Connection conn = openConnection();
				PreparedStatement insertStmt =
						conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			// Create user in database.
			insertStmt.setString(1, name);
			insertStmt.setString(2, pass);
			insertStmt.setInt(3, role);
			insertStmt.executeUpdate();

			try (ResultSet rs = insertStmt.getGeneratedKeys()) {
				if (rs.next()) {
					id = rs.getInt(1);
					logger.info(String.format("%n\tUser \"%s\" added to the database with ID=%d.",
							name, rs.getInt(1)));
				}
			}
		} catch (SQLException e) {
			if (e.getSQLState().equals("23505")) {
				throw new FailedInsertException("Users must have unique names");
			} else if (e.getSQLState().equals("23503")) {
				throw new IdNotFoundException("Role with ID= " + role + " not found");
			} else {
				logger.error(e);
			}
		}
		return id;
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
	 * @throws FailedInsertException
	 *             thrown if the the permission creation failed and no ID for
	 *             the created permission can be found.
	 */
	public static int createPermission(String doc) throws FailedInsertException {
		String sql = "INSERT INTO PERMISSIONS (PERM_DOC) "
				+ "VALUES (XMLPARSE(DOCUMENT CAST (? AS CLOB) PRESERVE WHITESPACE))";
		int id = 0;
		try (Connection conn = openConnection();
				PreparedStatement insertStmt =
						conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			// Create permission in database. (We are parsing the XML doc via
			// XMLPARSE and storing it as a binary XML data type in the
			// database.
			insertStmt.setString(1, doc);
			insertStmt.executeUpdate();

			// Get the ID of the the permission that was just created
			try (ResultSet rs = insertStmt.getGeneratedKeys()) {
				if (rs.next()) {
					id = rs.getInt(1);
					logger.info(
							String.format("%n\tPermission added to the database with ID=%d.", id));
				} else {
					throw new FailedInsertException();
				}
			}
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
	 * @throws FailedInsertException
	 *             thrown if the the role creation failed and no ID for the
	 *             created role can be found.
	 */
	public static int createRole(String name) throws FailedInsertException {
		int id = -1;
		String sql = "INSERT INTO ROLES (ROLE_NAME) VALUES (?)";

		try (Connection conn = openConnection();
				PreparedStatement insertStmt =
						conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			insertStmt.setString(1, name);
			insertStmt.executeUpdate();

			// Get the ID of the role that was just created
			try (ResultSet rs = insertStmt.getGeneratedKeys()) {
				if (rs.next()) {
					id = rs.getInt(1);
					logger.info(String.format("%n\tRole %s added to the database with ID=%d.", name,
							id));
				} else {
					throw new FailedInsertException();
				}
			}
		} catch (SQLException e) {
			if (e.getSQLState().equals("23505")) {
				logger.error(
						String.format("%n\tRole %s not added, this role already exisits.", name));
			}
		}

		return id;
	}

	/**
	 * Adds a permission to the specified role.
	 *
	 * @param roleId
	 *            ID of role to add permission to
	 * @param permId
	 *            ID of the permission to add
	 * @throws IdNotFoundException
	 *             thrown if the roleId or permId does not exist in the
	 *             database.
	 */
	public static void addPermissionToRole(int roleId, int permId) throws IdNotFoundException {
		String sql = "INSERT INTO ROLE_PERMISSIONS (ROLE_ID, PERM_ID) VALUES (?, ?)";

		try (Connection conn = openConnection();
				PreparedStatement insertStmt = conn.prepareStatement(sql)) {

			insertStmt.setInt(1, roleId);
			insertStmt.setInt(2, permId);
			insertStmt.executeUpdate();
			logger.info(String.format("%n\tPermission with ID=%d added to the role with ID=%d.",
					permId, roleId));
		} catch (SQLException e) {
			if (e.getSQLState().equals("23503")) {
				throw new IdNotFoundException("roleId or permId could not be found");
			} else {
				logger.error(e);
			}
		}
	}

	/**
	 * Adds a permission to the specified user.
	 *
	 * @param userId
	 *            ID of role to add permission to
	 * @param permId
	 *            ID of the permission to add
	 * @throws IdNotFoundException
	 *             thrown if the userId or permId does not exist in the
	 *             database.
	 */
	public static void addPermissionToUser(int userId, int permId) throws IdNotFoundException {
		String sql = "INSERT INTO USER_PERMISSIONS (USER_ID, PERM_ID) VALUES (?, ?)";

		try (Connection conn = openConnection();
				PreparedStatement insertStmt = conn.prepareStatement(sql)) {

			insertStmt.setInt(1, userId);
			insertStmt.setInt(2, permId);
			insertStmt.executeUpdate();
			logger.info(String.format("%n\tPermission with ID=%d added to the user with ID=%d.",
					permId, userId));
		} catch (SQLException e) {
			if (e.getSQLState().equals("23503")) {
				throw new IdNotFoundException("userId or permId could not be found");
			} else {
				logger.error(e);
			}
		}
	}

	/**
	 * Checks to see if the user with the given Id has permission to access the
	 * location specified and returns what type of permission access they have.
	 *
	 * @param userId
	 *            User's Id
	 * @param location
	 *            File location
	 * @return Type of access the user has (READ, WRITE, READWRITE) or NONE if
	 *         the user doesn't have access.
	 * @throws IdNotFoundException
	 *             thrown if a user with userId does not exist in the database.
	 */
	public static PermissionType userHasPermission(int userId, String location)
			throws IdNotFoundException {
		// Gets the permissions
		String sql1 = "SELECT XMLSERIALIZE(PERM_DOC AS CLOB) FROM PERMISSIONS WHERE PERM_ID = "
				+ "(SELECT PERM_ID FROM USER_PERMISSIONS WHERE USER_ID = ?)";
		String sql2 = "SELECT XMLSERIALIZE(PERM_DOC AS CLOB) FROM PERMISSIONS WHERE PERM_ID = "
				+ "(SELECT PERM_ID FROM ROLE_PERMISSIONS WHERE ROLE_ID = "
				+ "(SELECT USER_ROLE FROM USERS WHERE USER_ID = ?))";

		try (Connection conn = openConnection();
				PreparedStatement rolePermSelectStmt = conn.prepareStatement(sql1);
				PreparedStatement userPermSelectStmt = conn.prepareStatement(sql2)) {

			rolePermSelectStmt.setInt(1, userId);
			try (ResultSet rs = rolePermSelectStmt.executeQuery()) {
				while (rs.next()) {
					String xml = rs.getString(1);
					System.out.println(xml);
					PermissionType result = DatabaseUtils.hasAccess(xml, location);
					if (result != PermissionType.NONE) {
						return result;
					}
				}
			}

			userPermSelectStmt.setInt(1, userId);
			try (ResultSet rs = userPermSelectStmt.executeQuery()) {
				while (rs.next()) {
					String xml = rs.getString(1);
					PermissionType result = DatabaseUtils.hasAccess(xml, location);
					if (result != PermissionType.NONE) {
						return result;
					}
				}
			}
		} catch (SQLException e) {
			if (e.getSQLState().equals("23503")) {
				throw new IdNotFoundException("User with ID = " + userId + " could not be found");
			} else {
				logger.error(e);
			}
		}
		return PermissionType.NONE;
	}

	/**
	 * Updates all of the data for a given user.
	 *
	 * @param userId
	 *            Id of user to update
	 * @param name
	 *            New name
	 * @param pass
	 *            New pass
	 * @param role
	 *            New role
	 * @throws IdNotFoundException
	 *             thrown if a user with userId does not exist in the database.
	 */
	public static void updateUser(int userId, String name, String pass, int role)
			throws IdNotFoundException {
		String sql = "UPDATE USERS SET " + "USER_NAME = ?, USER_PASS = ?, USER_ROLE = ? "
				+ "WHERE USER_ID = ?";

		try (Connection conn = openConnection();
				PreparedStatement updateStmt = conn.prepareStatement(sql)) {

			updateStmt.setString(1, name);
			updateStmt.setString(2, pass);
			updateStmt.setInt(3, role);
			updateStmt.setInt(4, userId);
			updateStmt.executeUpdate();
		} catch (SQLException e) {
			if (e.getSQLState().equals("23503")) {
				throw new IdNotFoundException("User with ID = " + userId + " could not be found");
			} else {
				logger.error(e);
			}
		}
	}

	/**
	 * Updates all of the data for a given permission.
	 *
	 * @param permId
	 *            Id of user to update
	 * @param doc
	 *            XML doc for permission
	 * @throws IdNotFoundException
	 *             thrown if a permission with permId does not exist in the
	 *             database.
	 */
	public static void updatePermission(int permId, String doc) throws IdNotFoundException {
		String sql = "UPDATE PERMISSIONS SET "
				+ "PERM_DOC = XMLPARSE(DOCUMENT CAST (? AS CLOB) PRESERVE WHITESPACE)"
				+ " WHERE PERM_ID = ?";

		try (Connection conn = openConnection();
				PreparedStatement updateStmt = conn.prepareStatement(sql)) {

			updateStmt.setInt(1, permId);
			updateStmt.setString(2, doc);
			updateStmt.executeUpdate();
		} catch (SQLException e) {
			if (e.getSQLState().equals("23503")) {
				throw new IdNotFoundException("User with ID = " + permId + " could not be found");
			} else {
				logger.error(e);
			}
		}
	}

	/**
	 * Gets a specific user from database.
	 * @param username Get user by username.
	 * @return The User.
	 */
	public static User getUser(String username) {
		User user = null;

		String sql = "SELECT USER_ID, USER_NAME, USER_PASS, USER_ROLE FROM USERS "
						+ "WHERE USER_NAME = ?";
		List<User> users = new ArrayList<>();
		try (Connection conn = openConnection();
				PreparedStatement selectStmt = conn.prepareStatement(sql)) {

			selectStmt.setString(1, username);
			try (ResultSet rs = selectStmt.executeQuery()) {
				while (rs.next()) {
					int id = rs.getInt(1);
					String name = rs.getString(2);
					String pass = rs.getString(3);
					int role = rs.getInt(4);
					users.add(new User( id, name, pass, role ));
				}
			}
		} catch (SQLException e) {
			logger.error(e);
		}

		if (users.size() > 0) {
			user = users.get(0);
		}

		return user;
	}

	/**
	 * Returns the list of users in the database.
	 *
	 * @return List contating user id, name and role.
	 */
	public static List<User> getUsers() {
		String sql = "SELECT USER_ID, USER_NAME, USER_PASS, USER_ROLE FROM USERS";
		List<User> users = new ArrayList<>();
		try (Connection conn = openConnection();
				PreparedStatement selectStmt = conn.prepareStatement(sql)) {

			try (ResultSet rs = selectStmt.executeQuery()) {
				while (rs.next()) {
					int id = rs.getInt(1);
					String name = rs.getString(2);
					String pass = rs.getString(3);
					int role = rs.getInt(4);
					users.add(new User( id, name, pass, role ));
				}
			}
		} catch (SQLException e) {
			logger.error(e);
		}
		return users;
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
		// Drop all the tables
		dropTables();

		// Build all the tables
		createTables();

		// Create none role
		int noneid = 0;
		try {
			noneid = createRole("NONE");
		} catch (FailedInsertException e) {
			logger.error(e);
		}

		// Create ADMIN role
		int adminid = 0;
		try {
			adminid = createRole("ADMIN");
		} catch (FailedInsertException e) {
			logger.error(e);
		}

		// Create User with ADMIN role
		int user1id = 0;
		try {
			user1id = createUser("Bill Gates", "windows_vista", adminid);
		} catch (FailedInsertException | IdNotFoundException e) {
			logger.error(e);
		}

		int user2id = 0;
		try {
			user2id = createUser("Steve Jobs", "earpods_rule", noneid);
		} catch (FailedInsertException | IdNotFoundException e) {
			logger.error(e);
		}

		int user3id = 0;
		try {
			user3id = createUser("tmp", "", noneid);
		} catch (FailedInsertException | IdNotFoundException e) {
			logger.error(e);
		}


		try {
			String xml = new String(
					Files.readAllBytes(new File("tests/permissions/admin.xml").toPath()), "UTF-8");
			int permid = createPermission(xml);
			addPermissionToRole(adminid, permid);
			System.out.println(userHasPermission(user2id,
					"./src/edu/wright/cs/jfiles/client/JFilesClient.java"));
			System.out.println(userHasPermission(user1id, "./tests/permissions/admin.xml"));
			System.out.println(userHasPermission(user1id, "./serverfiles"));
			System.out.println(userHasPermission(user1id, "./serverfiles/"));
			System.out.println(userHasPermission(user1id, "serverfiles/"));
			System.out.println(userHasPermission(user1id, "src"));
			System.out.println(userHasPermission(user1id, "src/"));
			System.out.println(userHasPermission(user1id, "./src/"));
			System.out.println(userHasPermission(user1id, "src/edu"));

			System.out.println(userHasPermission(user2id, "src"));
			System.out.println(userHasPermission(user2id, "src/"));
			System.out.println(userHasPermission(user2id, "./src/"));
			System.out.println(userHasPermission(user2id, "src/edu"));
			System.out.println(userHasPermission(user3id, "src/edu"));
		} catch (IOException e) {
			logger.error(e);
		} catch (FailedInsertException e) {
			logger.error(e);
		} catch (IdNotFoundException e) {
			logger.error(e);
		}


		for (User user : getUsers()) {
			System.out.println(user);
		}

		// Make sure to shutdown the database connection before the program
		// exits.
		shutdown();
	}
}
