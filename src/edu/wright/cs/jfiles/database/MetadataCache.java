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

import edu.wright.cs.jfiles.core.FileStruct;
import edu.wright.cs.jfiles.core.XmlHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Database cache for file metadata.
 *
 * @author Matt Gilene
 *
 */
public class MetadataCache {

	private static final String JDBC_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	private static final String DATABASE_URL_OPEN = "jdbc:derby:memory:MetadataCache;create=true";
	private static final String DATABASE_URL_SHUTDOWN =
			"jdbc:derby:memory:MetadataCache;shutdown=true";
	private static final Logger logger = LogManager.getLogger(MetadataCache.class);

	private static Connection conn;

	static {
		conn = openConnection();
	}

	/**
	 * Opens a connection to the in memory database.
	 *
	 * @return JDBC Connection
	 */
	private static Connection openConnection() {
		Connection conn = null;
		try {
			// Load the JDBC driver and open a connection
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DATABASE_URL_OPEN);
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
			conn.close();
			DriverManager.getConnection(DATABASE_URL_SHUTDOWN).close();
		} catch (SQLException e) {
			if (!e.getSQLState().equals("08006")) {
				logger.error(e);
			}
		}
	}

	/**
	 * Create database tables.
	 */
	public static void createTable() {
		try (Statement stmt = conn.createStatement()) {
			stmt.executeUpdate("CREATE TABLE METADATA ("
					+ "ID INTEGER NOT NULL "
						+ "GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1),"
					+ "LOCATION VARCHAR(1000) NOT NULL,"
					+ "DATA XML NOT NULL,"
					+ "LAST_UPDATE TIMESTAMP NOT NULL,"
					+ "PRIMARY KEY (ID))");
		} catch (SQLException e) {
			logger.error(e);
		}
	}

	/**
	 * Caches the metadata for the given file in the database.
	 *
	 * @param loc
	 *            file location
	 * @param xml
	 *            metadata
	 */
	public static void cacheMetadata(String loc, String xml) {
		String sql = "INSERT INTO METADATA (LOCATION, DATA, LAST_UPDATE) "
				+ "VALUES (?, (XMLPARSE(DOCUMENT CAST (? AS CLOB) PRESERVE WHITESPACE)), ?)";
		Date now = new Date();
		Timestamp timestamp = new Timestamp(now.getTime());
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, loc);
			stmt.setString(2, xml);
			stmt.setTimestamp(3, timestamp);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads the cache and returns the metadata for the given location.
	 *
	 * @param loc
	 *            file location
	 * @return list of FileStructs
	 */
	public static List<FileStruct> readCache(String loc) {
		String sql = "SELECT XMLSERIALIZE(DATA AS CLOB) FROM METADATA WHERE LOCATION = ?";
		List<FileStruct> fileStructs = null;
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, loc);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					fileStructs = XmlHandler.readXmlString(rs.getString(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return fileStructs;
	}

	/**
	 * Public main for testing.
	 *
	 * @param args
	 *            cmdline args
	 */
	public static void main(String[] args) {
		createTable();
		XmlHandler handler = new XmlHandler("./src/edu/wright/cs/jfiles/client");
		String xml = handler.xmlToString();
		cacheMetadata("./src/edu/wright/cs/jfiles/client", xml);
		for (FileStruct fs : readCache("./src/edu/wright/cs/jfiles/client")) {
			System.out.println(fs.getValue("name"));
		}
	}
}
