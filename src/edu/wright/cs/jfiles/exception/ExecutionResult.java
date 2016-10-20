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

package edu.wright.cs.jfiles.exception;

/**
 * Class used to hold the execution result for a command that was ran.
 *
 */
public class ExecutionResult {

	private int statusCode;
	private boolean exitShell = false;

	/**
	 * Sets the statusCode for the current ExecutionResult of a command.
	 * 
	 * @param statusCode
	 *            the status code from a resulting command execution
	 */
	public ExecutionResult(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * Sets the statusCode and the exitShell for the ExecutionResult of a
	 * command.
	 * 
	 * @param statusCode
	 *            the status code from a resulting command execution
	 * @param exitShell
	 */
	public ExecutionResult(int statusCode, boolean exitShell) {
		this.statusCode = statusCode;
		this.exitShell = exitShell;
	}

	/**
	 * Gets the status code that was set by the previously ran command.
	 * 
	 * @return the status code result
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isExitShell() {
		return exitShell;
	}
}
