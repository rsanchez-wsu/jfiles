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

/**
 * Container for database constants.
 *
 * @author Matt Gilene
 *
 */
public class JFilesDatabase {

	public static final Role NONE = new Role("NONE");
	public static final Role ADMIN = new Role("ADMIN");

	private static JFilesDatabase instance = new JFilesDatabase();

	/**
	 * Private constructor. This class is a singleton.
	 */
	private JFilesDatabase() {
		DatabaseController.createTables();
		// TODO Load existing data from database
	}

	/**
	 * Gets the instance of the JFilesDatabase.
	 *
	 * @return JFilesDatabase instance
	 */
	public JFilesDatabase getInstance() {
		return instance;
	}

	/**
	 * Closes the database.
	 */
	public void close() {
		DatabaseController.shutdown();
	}
}
