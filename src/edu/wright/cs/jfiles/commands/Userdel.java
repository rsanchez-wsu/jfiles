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
 * The Userdel command will delete a user account.
 * Syntax:
 * 		USERDEL <username>
 * Example:
 *		USERDEL test
 */
public class Userdel extends Command {
	/**
	 * Get the username that needs to be deleted
	 * @return The username
	 */
	private String getUser() {
		// Prompt user to enter the username
		String username = "";
		return username;
	}
	/**
	 * Get the username and check to see if the user is valid
	 * @return The output file
	 */
	private Boolean validate() {
		// Prompt user to enter the file name
		Boolean isValid = true;
		return outputFile;
	}
	/**
	 *  TODO: Implement deleting a user from the database.
	 *  First check to see if the username exists in the database.
	 *  If the username does not exist send back an error.
	 *  If the username is in the database then delete the user from the db and send back confirmation via the Info command
	 *  @return Nothing
	 */
	@Override
	public String execute() {
		return new Info("Username").execute();
	}
}
