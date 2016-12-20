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

import java.util.ArrayList;
import java.util.List;

/**
 * Basic arguments class to hold many arguments for a given command.
 *
 */
public class Arguments {

	private List<Argument> args = new ArrayList<Argument>();

	/**
	 * Add an argument to the current list of arguments.
	 *
	 * @param argument
	 *            the argument to add
	 * @return true if added
	 */
	public boolean add(Argument argument) {
		int count = args.size();
		this.args.add(argument);

		return args.size() - count == 1;
	}

	/**
	 * Add an argument to the current list of arguments.
	 *
	 * @param arg
	 *            the argument to add
	 * @return true if added
	 */
	public boolean add(String arg) {
		return add(new Argument(arg));
	}

	/**
	 * Gets the size of the current argument list.
	 *
	 * @return the number of arguments
	 */
	public int size() {
		return args.size();
	}

	/**
	 * Gets the argument at the specified index.
	 *
	 * @param index
	 *            the index to retrieve
	 * @return the argument
	 */
	public Argument get(int index) {
		return args.get(index);
	}

	/**
	 * Gets the current list of arguments.
	 *
	 * @return the list of arguments
	 */
	public List<Argument> getArgs() {
		return new ArrayList<Argument>(args);
	}

	/**
	 * Gets the last argument in the list.
	 *
	 * @return the last argument
	 */
	public Argument getLast() {
		int argCount = args.size();

		return args.get(argCount - 1);

	}
}
