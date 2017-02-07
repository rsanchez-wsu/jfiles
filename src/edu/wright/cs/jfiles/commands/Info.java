/*
 * Copyright (C) 2016 - WSU CEG3120 Students
 *
 * Roberto C. Sánchez <roberto.sanchez@wright.edu>
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
 *  The Info command sends extra information to the client.
 *  Syntax:
 *      INFO message...
 *  Flags:
 *      - None.
 *  Example:
 *      INFO Last logged in 10 minutes ago
 */
public class Info extends Command {

	/**
	 * Calls super.
	 * @param args Command's args.
	 */
	public Info(String... args) {
		super(args);
	}

	/**
	 *  TODO: Provide user usage info
	 *  @return Nothing.
	 */
	public String execute() {
		return "INFO: " + "Last logged in " + "amount of time" + "ago";
		
		
	}

}
