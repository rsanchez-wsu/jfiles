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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Contains our Derby Prepared Statements.
 *
 * @author Matt Gilene
 *
 */
public class Procedures {

	private static final String[] PROCEDURES = {
			"createTables() "
					+ "EXTERNAL NAME 'edu.wright.cs.jfiles.database.Procedures.createTables'",
			"createPermission(P_DOC VARCHAR(1000)) "
					+ "EXTERNAL NAME 'edu.wright.cs.jfiles.database.Procedures.createPermission'",
			"getPermission(ID INTEGER, OUT DOC VARCHAR(1000)) "
					+ "EXTERNAL NAME 'edu.wright.cs.jfiles.database.Procedures.getPermission'"
	};

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
	 * Queries the database for a permission with the given id.
	 *
	 * <p>
	 * NOTE: This is a Derby Prepared Statement This function is not to be
	 * called directly. Rather only called via an SQL call statement.
	 * </p>
	 *
	 * @param id
	 *            permission id
	 * @param doc
	 *            this is an OUT parameter. Used to return the queried XML
	 * @throws SQLException
	 *             doesn't handle any exceptions
	 */
	public static void getPermission(int id, String[] doc) throws SQLException {
		Connection conn = DatabaseController.openConnection();
		String sql =
				"SELECT XMLSERIALIZE(doc as CLOB) FROM PERMISSIONS WHERE id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, id);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			doc[0] = rs.getString(1);
		} else {
			doc[0] = null;
		}

		rs.close();
		stmt.close();
		conn.close();
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
	 * @throws SQLException
	 *             doesn't handle any exceptions
	 */
	public static void createPermission(String doc) throws SQLException {
		Connection conn = DatabaseController.openConnection();
		String sql =
				"INSERT INTO PERMISSIONS (doc) "
						+ "VALUES (XMLPARSE(DOCUMENT CAST (? AS CLOB) PRESERVE WHITESPACE))";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, doc);
		stmt.executeUpdate();

		stmt.close();
		conn.close();
	}

	/**
	 * Builds all the tables for our database.
	 *
	 * <p>
	 * NOTE: This is a Derby Prepared Statement This function is not to be
	 * called directly. Rather only called via an SQL call statement.
	 * </p>
	 *
	 * @throws SQLException
	 *             doesn't handle any exceptions
	 */
	public static void createTables() throws SQLException {
		Connection conn = DatabaseController.openConnection();
		conn.setAutoCommit(false);

		Statement stmt = conn.createStatement();
		for (String tbl : TABLES) {
			try {
				stmt.executeUpdate(tbl);
			} catch (SQLException e) {
				if (!e.getSQLState().equals("X0Y32")) {
					e.printStackTrace();
				}
			}
		}
		conn.commit();

		stmt.close();
		conn.close();
	}

	/**
	 * Defines all of our stored procedures in the database.
	 *
	 * @param conn
	 *            database connection
	 * @throws SQLException
	 *             doesn't handle SQL exceptions other than those for stored
	 *             procedures already exisiting.
	 */
	public static void createProcedures(Connection conn) throws SQLException {
		conn.setAutoCommit(false);
		Statement stmt = conn.createStatement();
		for (String proc : PROCEDURES) {
			String sql = "CREATE PROCEDURE ";
			sql += proc;
			sql += "PARAMETER STYLE JAVA LANGUAGE JAVA";
			try {
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				if (!e.getSQLState().equals("X0Y68")) {
					e.printStackTrace();
				}
			}
		}
		conn.commit();

		stmt.close();
	}

}
