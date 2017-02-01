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

import java.util.Locale;
import java.util.function.Function;

/**
 * Commands enum used to hold a list of commands
 * and easily get a new instance of a command.
 */
public enum Commands {

	FIND((String args) -> {
		return new Find(args);
	}),
	PING((String args) -> {
		return new Ping(args);
	}),
	QUIT((String args) -> {
		return new Quit(args);
	});

	private Function<String, Command> lam;

	/**
	 * The function that returns an instance of the command.
	 * @return A function that returns a Command instance.
	 */
	private Function<String, Command> getLam() {
		return this.lam;
	}

	/**
	 * Inits a new enum.
	 * @param lam The function that creates a new instance of the command.
	 */
	Commands(Function<String, Command> lam) {
		this.lam = lam;
	}

	/**
	 * Gets a new instance of the command.
	 * @param cmdName The name of the command.
	 * @param args The arguments of the command.
	 * @return Returns a new instance of the command.
	 *         If not found, returns new CommandNotFound.
	 */
	public static Command getNewInstance(String cmdName, String args) {
		// Find command in enum
		Commands cmd = findCommand(cmdName.toUpperCase(Locale.ENGLISH));
		// Return a new instance if found, else null.
		return cmd != null ? cmd.getLam().apply(args) : new CommandNotFound("");
	}

	/**
	 * Finds the command in the enum based on the name.
	 * @param cmdName The name of the command to find in the enum.
	 * @return The enum of the command. If not found, null.
	 */
	private static Commands findCommand(String cmdName) {
		Commands fcmd = null;

		for (Commands cmd : Commands.values()) {
			if (cmd.name().equals(cmdName)) {
				fcmd = cmd;
				break;
			}
		}

		return fcmd;
	}

}
