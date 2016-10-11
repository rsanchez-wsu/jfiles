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

import java.io.OutputStream;
import java.io.PrintStream;

public abstract class AbstractExecutable implements Executable {

	protected static final ExecutionResult SUCCESS = new ExecutionResult(0);
	protected static final ExecutionResult FAILURE = new ExecutionResult(1);

	private String executableName;

	protected abstract ExecutionResult executeCommand(CommandLine commandLine, PrintStream out,
			ExecutionContext context);

	protected AbstractExecutable(String executableName) {
		this.executableName = executableName;
	}

	public String getName() {
		return executableName;
	}

	public ExecutionResult execute(CommandLine commandLine, OutputStream out,
			ExecutionContext context) {
		return executeCommand(commandLine, new PrintStream(out), context);
	}

	protected boolean isOption(String str) {
		return str.startsWith("-") || str.startsWith("--");
	}

	protected boolean isOption(String expected, String actual) {
		return ("-" + expected).equals(actual) || ("--" + expected).equals(actual);
	}
}
