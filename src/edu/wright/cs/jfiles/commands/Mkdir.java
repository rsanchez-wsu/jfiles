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

import edu.wright.cs.jfiles.database.DatabaseUtils.PermissionType;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *  The MKDIR command returns ".mkdir".
 *  Syntax:
 *      MKDIR directory
 *  Flags:
 *      - None.
 *  Example:
 *      MKDIR testfolder
 */

public class Mkdir extends Command {

	/**
	 * Calls super.
	 * @param args Command's args.
	 */
	public Mkdir(String... args) {
		super(args);
	}

	/**
	 * A method to make a directory based on the directory path given.
	 * @param directoryPath - the path where the directory will be created
	 * @return - whether the directory was created successfully or not
	 */
	private String makeDirectory(String directoryPath) {
		if (new File(directoryPath).mkdir()) {
			return "Directory Created at " + directoryPath;
		} else {
			return "Directory Not Created";
		}
	}
	/**
	 *  TODO: Program routine for creating directories.
	 *  @return Directory created or an error
	 */
	@Override
	public String execute() {
		String directory = this.parser.next();

		if (directory == null) {
			return new Error("Missing directory. Syntax: MKDIR [directoryPath]").execute();
		}

		if (!directory.startsWith("/")) {
			directory = this.cp.getCwd() + directory;
		}

		Path path = Paths.get(directory);

		String dir = "";

		if (path.getNameCount() > 1) {
			dir = path.subpath(0, path.getNameCount() - 1).toString();
		}

		if (!this.cp.hasPermission(dir, PermissionType.READWRITE)) {
			return new Error(
					"You do not have permission to write to directory: " + dir).execute();
		} else {
			return makeDirectory(directory);
		}
	}

	/**
	 * Gets the class specific help message and Syntax.
	 * It's done like this so you can extend this method and not
	 * have to worry about help working the same in all methods.
	 * @return [0] is what the command does, [1] is the syntax of command.
	 */
	protected String[] helpStrings() {
		return new String[] {
				"Creates a directory.",
				"MKDIR <directory>"
		};
	}
}
