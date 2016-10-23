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

import edu.wright.cs.jfiles.exception.ExecutionResult;

import java.io.BufferedWriter;

/**
 * Interface for an executable command.
 *
 */
public interface Executable {

	/**
	 * Main command execution method.
	 * 
	 * @param commandLine
	 *            the current command line to be executed
	 * @param out
	 *            the output stream
	 * @param context
	 *            the execution context for the command
	 * @return the execution result from the command
	 */
	ExecutionResult execute(CommandLine commandLine, BufferedWriter out, ExecutionContext context);

	/**
	 * Returns the name of the file or directory denoted by this abstract
	 * pathname. This is just the last name in the pathname's name sequence. If
	 * the pathname's name sequence is empty, then the empty string is returned.
	 *
	 * @return The name of the file or directory denoted by this abstract
	 *         pathname, or the empty string if this pathname's name sequence is
	 *         empty
	 */
	String getName();

	/**
	 * Gets the help message for a given command.
	 * 
	 * @return the help message
	 */
	String getHelp();
}
