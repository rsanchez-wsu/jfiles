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

import java.io.File;

/**
 *  The Close command closes the connection.
 *  Syntax:
 *      PWD
 *  Flags:
 *      - None.
 *  Example:
 *      PWD
 */
public class Pwd extends Command {

	/**
	 * Calls super.
	 * @param args Command's args.
	 */
	public Pwd(String... args) {
		super(args);
	}

	/**
	 * Gets the abs path of the current directory path and returns it.
	 * @param directory The path of the directory.
	 * @return The abs path of directory.
	 */
	private String curDir(String directory) {
		File folder = new File(directory);

		return (folder.getAbsolutePath());
	}

	/**
	 *  @return The full directory path of the current working directory.
	 */
	public String execute() {
		String directory = this.parser.next();

		return curDir(directory);
	}

}
