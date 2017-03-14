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
import java.util.Locale;
import java.util.Map;

/**
 *  The parser class will be a class dedicated to parsing the given string from the user.
 *  An example would be given: "FIND -r:true -time:10h *.txt", the Parser class will
 *  automatically parse out the arguments and flags.
 */
public class Parser {

	private final List<String> args;
	private final Map<String, String> flags;
	private int currentArg = 0;

	/**
	 * Inits a new parser.
	 * @param args The arguments to parse.
	 */
	Parser(String[] args) {
		this();

		for (String arg : args) {
			add(arg);
		}
	}

	/**
	 * Inits flag and args.
	 */
	Parser() {
		flags = new HashMap<>();
		args = new ArrayList<>();
	}

	/**
	 * Adds arg to end of parser.
	 * @param arg The arg to add to parser.
	 */
	public void add(String arg) {
		if (arg.startsWith("-")) {
			arg = arg.substring(1);
			String[] tokens = arg.split(":", 2);
			String flagName = tokens[0].toUpperCase(Locale.ENGLISH);

			if (tokens.length > 1) {
				this.flags.put(flagName, tokens[1]);
			} else {
				this.flags.put(flagName, "");
			}
		} else {
			if (arg.length() > 0) {
				this.args.add(arg);
			}
		}
	}

	/**
	 * stuff.
	 */
	public String next() {
		return currentArg < args.size() ? args.get(currentArg++) : null;
	}

	/**
	 * Resets the current argument counter to 0.
	 */
	public void reset() {
		currentArg = 0;
	}

	/**
	 * Returns the remaining arguments, separated by a space.
	 * @return the remaining arguments, separated by a space.
	 */
	public String rest() {
		StringBuilder rest = new StringBuilder();

		String next = next();

		while (next != null) {
			rest.append(next + " ");
			next = next();
		}

		// Remove the ending space.
		if (rest.length() > 0) {
			rest.setLength(rest.length() - 1);
		}

		return rest.toString();
	}

	/**
	 * Gets the arguments.
	 * @return The arguments
	 */
	public String[] getArguments() {
		return args.toArray(new String[args.size()]);
	}

	/**
	 * Gets the flags.
	 * @return The flags.
	 */
	public Map<String, String> getFlags() {
		return this.flags;
	}

	/**
	 * @return If flag exists.
	 */
	public boolean doesFlagExist(String flag) {
		return this.flags.containsKey(flag);
	}

	@Override
	public String toString() {
		StringBuilder end = new StringBuilder();

		for (Map.Entry<String, String> entry : flags.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			end.append("-" + key);

			if (value.length() > 0) {
				end.append(":" + value);
			}

			end.append(" ");
		}

		for (String arg : args) {
			end.append(arg + " ");
		}

		if (end.length() > 0) {
			end.deleteCharAt(end.length() - 1);
		}

		return end.toString();
	}

}
