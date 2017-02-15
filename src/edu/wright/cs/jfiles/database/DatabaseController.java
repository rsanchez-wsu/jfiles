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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DATABASE_URL);
		} catch (SQLException e) {
			if (e.getSQLState().equals("XJ040")) {
				logger.info(
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
	 * Main, testing purposes only. This can be used to setup the database as
	 * well.
	 *
	 * @param args
	 *            jvm args
	 * @throws ClassNotFoundException
	 *             if Driver can't be loaded
	 */
	public static void main(String[] args) {
		try {
			Procedures.createProcedures();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		Connection conn = openConnection();
		try {
			CallableStatement stmt = conn.prepareCall("{call createTables()}");
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<permission type=\"read\">"
					+ "<directory path=\"\" />"
					+ "<directory path=\"\" />"
					+ "<file path=\"\" />"
					+ "<file path=\"\" />"
				+ "</permission>";
		try {
			CallableStatement stmt = conn.prepareCall("{call createPermission(?)}");
			stmt.setString(1, xml);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
