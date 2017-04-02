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
 * Edits user information
 * Format:
 * 		USER <username>
 * Flags:
 * 		addgroup:<group>
 * 		rmgroup:<group>
 * 		addperm:<perm>
 * 		rmperm:<perm>
 * 		delete
 * 		copy:<newusername>
 * 		setname:<newusername>
 * 		setpassword:<newpassword>
 * Example:
 * 		USER test -addgroup:testgroup
 */

public class User extends Command {

	@Override
	public String execute() {
		
		return new Info("User").execute();
	}

}
