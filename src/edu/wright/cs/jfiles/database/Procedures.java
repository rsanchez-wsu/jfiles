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
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Statement;

public class Procedures {

	private static final String[] PROCEDURES = {
			"createTables() "
					+ "EXTERNAL NAME 'edu.wright.cs.jfiles.database.Procedures.createTables'",
			"createPermission(IN P_DOC VARCHAR(1000)) "
					+ "MODIFIES SQL DATA "
					+ "EXTERNAL NAME 'edu.wright.cs.jfiles.database.Procedures.createPermission'"
	};

	private static final String[] TABLES = {
			"CREATE TABLE USERS ("
					+ "id INTEGER NOT NULL "
						+ "GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),"
					+ "name VARCHAR(40),"
					+ "pass VARCHAR(20),"
					+ "role INTEGER)",
			"CREATE TABLE ROLES ("
					+ "id INTEGER NOT NULL "
					+ "GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),"
					+ "name VARCHAR(20))",
			"CREATE TABLE PERMISSIONS ("
					+ "id INTEGER NOT NULL,"
					+ "paths XML)"
	};

	public static void createPermission(String doc) throws SQLException {
		Connection conn = DatabaseController.openConnection();
		int id = doc.hashCode();
		String sql =
				"INSERT INTO PERMISSIONS (id, paths) VALUES (?, xmlparse(document cast (? as clob) preserve whitespace))";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, id);
		stmt.setString(2, doc);
		stmt.executeUpdate();
		stmt.close();
		conn.close();
	}

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

	public static void createProcedures() throws SQLException {
		Connection conn = DatabaseController.openConnection();
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
		conn.close();
	}

}
