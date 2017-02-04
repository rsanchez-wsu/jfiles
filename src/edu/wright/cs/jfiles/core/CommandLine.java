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

package edu.wright.cs.jfiles.core;

/**
 * Class to hold the command line.
 *
 */
public class CommandLine {

	private String command;

	private Arguments arguments;

	/**
	 * Sets the current command from the command line.
	 *
	 * @param command
	 *            the parsed command
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * Gets the current command.
	 *
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Sets the arguments for the command.
	 *
	 * @param arguments
	 *            the arguments that were parsed
	 */
	public void setArguments(Arguments arguments) {
		this.arguments = arguments;
	}

	/**
	 * Gets the list of arguments for the current command.
	 *
	 * @return the arguments
	 */
	public Arguments getArguments() {
		return arguments;
	}
}
