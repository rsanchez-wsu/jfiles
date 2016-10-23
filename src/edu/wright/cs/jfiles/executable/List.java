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
import edu.wright.cs.jfiles.core.ExecutionContext;
import edu.wright.cs.jfiles.exception.ExecutionResult;

import java.io.BufferedWriter;

/**
 * Class for the list command for the jFiles program.
 *
 */
public class List extends AbstractExecutable {

	/**
	 * Default constructor for the list command.
	 */
	public List() {
		super("list");
	}

	/**
	 * Main function for the command execution.
	 */
	@Override
	protected ExecutionResult executeCommand(final CommandLine commandLine,
			final BufferedWriter out, final ExecutionContext context) {

		return SUCCESS;
	}

	/**
	 * Gets the help message for the command.
	 */
	public String getHelp() {
		return "list directory contents";
	}
}
