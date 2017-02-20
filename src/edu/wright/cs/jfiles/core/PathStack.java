/*
 * Copyright (C) 2017 - WSU CEG3120 Students
 *
 *
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

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Extends Stack.
 * Purpose is to properly return a path string.
 * @author kevin
 */
public class PathStack extends Stack<String> {


	/**
	 * To have interoperability with the JDK.
	 */
	private static final long serialVersionUID = -6705075072505320794L;

	/**
	 * Constructor same as the Stack constructor.
	 */
	public PathStack() {
		super();
	}

	/**
	 * Empties out the path and returns it to the root directory.
	 */
	public void root() {
		while (size() > 0) {
			pop();
		}

		push(".");
	}

	/**
	 * Returns the file path string in a readable format.
	 * @return String
	 */
	public String toPath() {
		String path = ".";
		int len = size();

		if (len == 0) {
			throw new EmptyStackException();
		}

		for (int i = 0; i < len; i++) {
			path += "/";
			path += elementAt(i);
		}

		return (path);
	}

	/**
	 * Returns the directory of the object and the directory it is inside of.
	 * @return String[]
	 */
	public String[] getHierarchy() {
		String[] lastTwo = new String[2];
		int len = size();
		if (len == 0) {
			throw new EmptyStackException();
		}

		len -= 1;
		int backOne = len - 1;
		lastTwo[0] = elementAt(len);
		lastTwo[1] = elementAt(backOne);

		return lastTwo;
	}
}