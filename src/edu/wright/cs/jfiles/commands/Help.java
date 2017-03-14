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

import java.util.Locale;

/**
 *  Returns information and arguments for a command.
 *  Syntax:
 *      HELP [command]
 *  Flags:
 *      - None.
 *  Example:
 *      HELP
 */
public class Help extends Command {

	/**
	 * Calls super.
	 * @param args Command's args.
	 */
	public Help(String... args) {
		super(args);
	}

	/**
	 *  @return The full directory path of the current working directory.
	 */
	@Override
	public String execute() {
		String cmdName = parser.next();

		System.out.println("CMD: " + cmdName);

		if (cmdName != null) {
			return Commands.getNewInstance(
				cmdName.toUpperCase(Locale.ENGLISH), new String[]{}).help();
		} else {
			return this.help();
		}
	}

	/**
	 * Gets the class specific help message and Syntax.
	 * It's done like this so you can extend this method and not
	 * have to worry about help working the same in all methods.
	 * @return [0] is what the command does, [1] is the syntax of command.
	 */
	protected String[] helpStrings() {
		return new String[] {
				"Gets the help message for a command.",
				"HELP [command]"
		};
	}
}
