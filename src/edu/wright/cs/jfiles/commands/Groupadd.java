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

package edu.wright.cs.jfiles.commands;

/**
 * The Groupadd command will add multiple users to the database with a generic password
 * Syntax:
 * 		GROUPADD usernames[]
 * Example:
 *		GROUPADD bhowdy, cspencer, james, shadow
 */
public class Groupadd extends Command {
	/**
	 * Build the new users array
	 * @return The array of new users
	 */
	private String[] buildUserArr() {
		// Get each user individually to build the array
		String[] newUsers = null;
		// Push users onto the array
		return newUsers;
	}
	/**
	 * Gets the user to add
	 * @return The user that needs created
	 */
	private String getUser() {
		// Prompt user to enter a username to add
		String username = null;
		return username;
	}
	/**
	 *  TODO: Implement adding multiple usernames to the database.
	 *  First check to see if each username exists in the database.
	 *  If a username does exist send back an error.
	 *  If the usernames are not in the database then add each user by calling the ADDUSER command each time passing a generic password
	 *  Once completed send back a success message by using the INFO command
	 *  @return Nothing
	 */
	@Override
	public String execute() {
		return new Info("Username").execute();
	}
}
