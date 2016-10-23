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

package edu.wright.cs.jfiles.executable;

import edu.wright.cs.jfiles.core.CommandLine;
import edu.wright.cs.jfiles.core.Executable;
import edu.wright.cs.jfiles.core.ExecutionContext;
import edu.wright.cs.jfiles.exception.ExecutionResult;

import java.io.BufferedWriter;
import java.io.PrintStream;

/**
 * Abstract class for the use of executable class files for the commands.
 *
 */
public abstract class AbstractExecutable implements Executable {

	protected static final ExecutionResult SUCCESS = new ExecutionResult(0);
	protected static final ExecutionResult FAILURE = new ExecutionResult(1);

	private String executableName;

	/**
	 * Abstract function for the main execution of the given command.
	 * 
	 * @param commandLine
	 *            the parsed command line to use
	 * @param out
	 *            the output of the command
	 * @param context
	 *            the context of the current command execution
	 * @return the execution result of the command that is ran
	 */
	protected abstract ExecutionResult executeCommand(CommandLine commandLine, BufferedWriter out,
			ExecutionContext context);

	/**
	 * Function to load in an executable file.
	 * 
	 * @param executableName
	 *            the name of the class file that is the executable command
	 */
	protected AbstractExecutable(String executableName) {
		this.executableName = executableName;
	}

	/**
	 * Returns the name of the executable class file.
	 */
	public String getName() {
		return executableName;
	}

	/**
	 * 
	 */
	public ExecutionResult execute(CommandLine commandLine, BufferedWriter out,
			ExecutionContext context) {
		return executeCommand(commandLine, new BufferedWriter(out), context);
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	protected boolean isOption(String str) {
		return str.startsWith("-") || str.startsWith("--");
	}

	/**
	 * 
	 * @param expected
	 * @param actual
	 * @return
	 */
	protected boolean isOption(String expected, String actual) {
		return ("-" + expected).equals(actual) || ("--" + expected).equals(actual);
	}
}
