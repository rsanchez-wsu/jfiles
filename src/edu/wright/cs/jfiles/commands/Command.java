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

import java.lang.StringBuilder;
import java.util.List;

/**
 *  This is the master class for all commands.
 */
public abstract class Command {
	protected Parser parser;

	/**
	 * Default constructor.
	 * @param args Command Parser
	 */
	public Command(String args) {
		this.parser = new Parser(args);

		/*
		 * FindBugs says parser variable is 'unused' in abstract class.
		 * Can't suppresswarning without something else complaining.
		 * So the solution is to call a method that does nothing.
		 */
		parser.shutupFindBugs();
	}

	/**
	 * Abstract execute command.
	 * @return The output of the command.
	 */
	public abstract String execute();

	/**
	 * Turns a List into a string seperated by new lines.
	 * @return A List into a string seperated by new lines.
	 */
	protected String atos(List<String> lst) {
		StringBuilder sbr = new StringBuilder();

		for (String str : lst) {
			sbr.append(str);
			sbr.append(System.getProperty("line.separator"));
		}

		return sbr.toString();
	}
}
