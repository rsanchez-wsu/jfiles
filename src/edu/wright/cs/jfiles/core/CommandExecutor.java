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

import java.io.OutputStream;

public class CommandExecutor {

	private ExecutablePath path;
	private Environment environment;

	/**
	 * Create a command executor with the given executable path and environment.
	 * 
	 * @param executablePath
	 * @param environment
	 */
	public CommandExecutor(ExecutablePath executablePath, Environment environment) {
		this.path = executablePath;
		this.environment = environment;
	}

	public ExecutionResult executeCommand(CommandLine commandLine, OutputStream out) {
		final Executable executable = path.locateExecutable(commandLine.getCommand());

		ExecutionContext context = new ExecutionContext(environment, path);
		ExecutionResult result = executable.execute(commandLine, out, context);

		environment.setProperty(Environment.EXIT_STATUS, String.valueOf(result.getStatusCode()));

		return result;
	}
}
