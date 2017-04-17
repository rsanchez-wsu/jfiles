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

import edu.wright.cs.jfiles.database.DatabaseController;
import edu.wright.cs.jfiles.database.FailedInsertException;
import edu.wright.cs.jfiles.database.IdNotFoundException;
import edu.wright.cs.jfiles.database.NameNotFoundException;
/**
 * @author Laurence Forshaw
 *     Command to add a user.
 *     Syntax:
 *     AddUser username password role
 *     If the password is "NOPASSWORD" (case insensitive) then there is no password.
 *     password is default none and role is default none
 */

public class AddUser extends Command {

	/**
	 * Creation method.
	 */
	public AddUser(String... args) {
		super(args);
	}

	/**
	 * Executes the command.
	 */
	public String execute() {
		String username;
		String password;
		String role;
		int roleId;
		try {
			if (!DatabaseController.findRoleName(this.cp.getUser().getRole()).equals("ADMIN")) {
				return ("You must be an Admin to do that.");
			}
		} catch (IdNotFoundException e) {
			//The value of role in the user object should a legitimate role.
			return ("Unexpected database error.");
		}
		//Three arguments max
		if (this.parser.getArguments().length > 3) {
			return ("Unexpected symbol " + this.parser.getArguments()[4] + " encountered.");
		}
		//First Argument, User is mandatory.
		if (this.parser.getArguments().length < 1) {
			return ("AddUser must specify a username");
		} else {
			username = this.parser.getArguments()[0];
		}
		//Second argument password has default value ""
		if (this.parser.getArguments().length < 2
				|| this.parser.getArguments()[1].toLowerCase().equals("nopassword")) {
			password = "";
		} else {
			password = this.parser.getArguments()[1];
		}
		//Third argument role has default value "NONE"
		if (this.parser.getArguments().length < 3) {
			role = "NONE";
		} else {
			role = this.parser.getArguments()[2];
		}
		try {
			roleId = DatabaseController.findRole(role);
		} catch (NameNotFoundException e) {
			return ("Role " + role + " does not exist.");
		}
		if (roleId < 0) {
			return ("Unexpected database error.");
		}
		try {
			// Actually create the user. The id is not relevant except to check for errors.
			if (DatabaseController.createUser(username, password, roleId) >= 0) {
				return ("User successfully added.");
			} else {
				return ("Unexpected database error.");
			}
		} catch (FailedInsertException e) {
			return ("User " + username + " already exists.");
		} catch (IdNotFoundException e) {
			// Since we just looked up the role we should not get this error.
			return ("Unexpected database error.");
		}
	}
}
