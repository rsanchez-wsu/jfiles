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

	// CheckStyle fails if enum name is 2 letters or less.
	// So if it's 2 letters, we add an _. The client
	// Can still send "cd" for example and it will find and
	// return _CD.
	TOUCH((String[] args) -> {
		return new Touch(args);
	}),
	SEND((String[] args) -> {
		return new Send(args);
	}),
	_CD((String[] args) -> {
		return new Cd(args);
	}),
	RMDIR((String[] args) -> {
		return new Rmdir(args);
	}),
	RECV((String[] args) -> {
		return new Recv(args);
	}),
	_RM((String[] args) -> {
		return new Rm(args);
	}),
	FIND((String[] args) -> {
		return new Find(args);
	}),
	PING((String[] args) -> {
		return new Ping(args);
	}),
	QUIT((String[] args) -> {
		return new Quit(args);
	}),
	MKDIR((String[] args) -> {
		return new Mkdir(args);
	}),
	_MV((String[] args) -> {
		return new Mv(args);
	}),
	_LS((String[] args) -> {
		return new Ls(args);
	}),
	LOGIN((String[] args) -> {
		return new Login(args);
	});

	private Function<String[], Command> lam;

	/**
	 * The function that returns an instance of the command.
	 * @return A function that returns a Command instance.
	 */
	private Function<String[], Command> getLam() {
		return this.lam;
	}

	/**
	 * Inits a new enum.
	 * @param lam The function that creates a new instance of the command.
	 */
	Commands(Function<String[], Command> lam) {
		this.lam = lam;
	}

	/**
	 * Gets a new instance of the command.
	 * @param cmdName The name of the command.
	 * @param args The arguments of the command.
	 * @return Returns a new instance of the command.
	 *         If not found, returns new CommandNotFound.
	 */
	public static Command getNewInstance(String cmdName, String... args) {
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

		// CheckStyle fails if enum name is 2 letters or less.
		// So if it's 2 letters, we add an _.
		if (cmdName.length() == 2) {
			cmdName = "_" + cmdName;
		}

		for (Commands cmd : Commands.values()) {
			if (cmd.name().equals(cmdName)) {
				fcmd = cmd;
				break;
			}
		}

		return fcmd;
	}

}
