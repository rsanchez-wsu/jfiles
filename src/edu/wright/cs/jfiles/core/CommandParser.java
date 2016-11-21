/*
 * Copyright (C) 2016 - WSU CEG3120 Students
 * 
 * Roberto C. Sánchez <roberto.sanchez@wright.edu>
 * 
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 */

package edu.wright.cs.jfiles.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Command parser class used to parse a command from the client.
 *
 */
public class CommandParser {

	private static final String PROPERTY_CHAR = Environment.PROPERTY_CHAR;
	private static final int PROPERTY_CHAR_LENGTH = PROPERTY_CHAR.length();

	private Environment environment;

	/**
	 * Default constructor that sets the environment to the default environment.
	 */
	public CommandParser() {
		this.environment = new Environment();
	}

	/**
	 * Creates a CommandParser with the given environment.
	 * 
	 * @param environment
	 *            the environment to use for the CommandParser
	 */
	public CommandParser(Environment environment) {
		this.environment = environment;
	}

	/**
	 * Parses the command from the client input.
	 * 
	 * <p>Reads in the entire command line string and parses out the command and any/all arguments
	 * that go with the command.
	 * 
	 * @param commandLineString
	 *            the command line to parse.
	 * @return populated command line.
	 */
	public CommandLine parse(String commandLineString) {
		CommandLine commandLine = new CommandLine();
		Arguments args = new Arguments();
		commandLine.setArguments(args);

		Stack<String> argStack = populateStackWithArgs(commandLineString.trim());

		boolean firstToken = true;
		while (!argStack.isEmpty()) {
			if (firstToken) {
				commandLine.setCommand(argStack.pop());
				firstToken = false;
			} else {
				args.add(translateEnvironmentVariable(argStack.pop()));
			}
		}

		return commandLine;
	}

	/**
	 * Parses an argument that has an effect on the environment.
	 * 
	 * @param arg
	 *            the environment argument
	 * @return the argument
	 */
	private String translateEnvironmentVariable(String arg) {
		int propCharIndex = arg.indexOf(PROPERTY_CHAR);
		if (propCharIndex >= 0) {

			int endOfEnvVar = arg.indexOf(" ", propCharIndex);
			if (endOfEnvVar <= 0) {
				endOfEnvVar = arg.length();
			}

			String envVarProperty = arg.substring(propCharIndex + PROPERTY_CHAR_LENGTH,
					endOfEnvVar);
			String envVarValue = environment.getProperty(envVarProperty, envVarProperty);

			if (!envVarProperty.equals(envVarValue)) {
				return arg.replace(PROPERTY_CHAR + envVarProperty, envVarValue);
			}

		}
		return arg;
	}

	/**
	 * Generates a stack with the list of arguments.
	 * 
	 * @param commandLine
	 *            the current parsed command line
	 * @return a stack populated with the arguments for the command
	 */
	private Stack<String> populateStackWithArgs(String commandLine) {
		List<String> args = new ArrayList<String>();

		StringBuilder argBuilder = new StringBuilder();

		int commandLineLength = commandLine.length();
		boolean startQuote = false;

		for (int i = 0; i < commandLineLength; i++) {
			Character currentChar = commandLine.charAt(i);

			if (Character.isWhitespace(currentChar)) {

				if (startQuote) {
					argBuilder.append(currentChar);
					continue;
				}

				if (argBuilder.length() > 0) {
					args.add(argBuilder.toString());
					argBuilder = new StringBuilder();
				}

			} else if ('"' == currentChar) {

				startQuote = !startQuote;

			} else {

				argBuilder.append(currentChar);

			}

		}

		if (argBuilder.length() > 0) {
			args.add(argBuilder.toString());
		}

		Stack<String> argStack = new Stack<String>();
		int argCount = args.size();
		for (int i = argCount - 1; i >= 0; i--) {
			argStack.push(args.get(i));
		}

		return argStack;
	}
}
