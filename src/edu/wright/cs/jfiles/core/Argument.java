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
 * Basic argument class meant to hold an argument for a given command.
 * 
 * @author brett
 *
 */
public class Argument {

	private String argument;

	/**
	 * Creates a single argument.
	 * 
	 * @param argument
	 *            the argument string
	 */
	public Argument(String argument) {
		this.argument = argument;
	}

	/**
	 * Gets the current argument.
	 * 
	 * @return the current argument
	 */
	public String getArgument() {
		return argument;
	}

	@Override
	public String toString() {
		return argument;
	}
}
