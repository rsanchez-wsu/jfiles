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
 * The Adduser command will register a new account.
 * Syntax:
 * 		ADDUSER <username> [password]
 * Example:
 *		REGISTER test test
 */

public class Adduser extends Command {
	/**
	 *  TODO: Implement inserting a new user to the database.
	 *  Include checking that the username is not already in the database.
	 *  If the user is already in the database then send an error.
	 *  If the username is not in the database insert the new user object to the database.
	 *  @return Nothing
	 */
	@Override
	public String execute() {
		return new Info("Username").execute();
	}
}
