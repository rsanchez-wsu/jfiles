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

package edu.wright.cs.jfiles.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  The parser class will be a class dedicated to parsing the given string from the user.
 *  An example would be given: "FIND -r:true -time:10h *.txt", the Parser class will
 *  automatically parse out the arguments and flags.
 */
public class Parser {

	private final String[] args;
	private final Map<String, String> flags;

	/**
	 * Inits a new parser.
	 * @param args The arguments to parse.
	 */
	Parser(String args) {
		List<String> tempArgs = new ArrayList<String>();
		this.flags = new HashMap<String, String>();

		for (String arg : args.split(" ")) {
			if (arg.startsWith("-")) {
				arg = arg.substring(1);
				String[] tokens = arg.split(":", 2);

				if (tokens.length > 1) {
					this.flags.put(tokens[0], tokens[1]);
				} else {
					this.flags.put(tokens[0], "");
				}
			} else {
				if (arg.length() > 0) {
					tempArgs.add(arg);
				}
			}
		}

		this.args = tempArgs.toArray(new String[tempArgs.size()]);
	}

	/**
	 * Gets the arguments.
	 * @return The arguments
	 */
	public String[] getArguments() {
		return this.args.clone();
	}

	/**
	 * Gets the flags.
	 * @return The flags.
	 */
	public Map<String, String> getFlags() {
		return this.flags;
	}

	/**
	 * FindBugs says parser variable is 'unused' in abstract class.
	 * Can't suppresswarning without something else complaining.
	 * So the solution is to call a method that does nothing.
	 */
	public void shutupFindBugs() {

	}

}
