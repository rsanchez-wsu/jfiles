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
import edu.wright.cs.jfiles.server.JFilesServer;

import java.util.Arrays;

/**
 *  The Close command closes the connection.
 *  Syntax:
 *      CD directoryName
 *  Flags:
 *      - None.
 *  Example:
 *      CD src
 */
public class Cd extends Command {

	/**
	 * Calls super.
	 * @param args Command's args.
	 */
	public Cd(String... args) {
		super(args);
	}

	/**
	 *  TODO: Returning the new directory you requested.
	 *  @return Nothing.
	 */
	@Override
	public String execute() {
		String directory = this.parser.next();
		directory = directory != null ? directory : "";

		if (directory.equals("")) {
			directory = JFilesServer.getInstance().getCwd();
		} else if (directory.equals("..")) {
			String[] dir = this.cp.getCwd().split("/");
			directory = String.join("/", Arrays.copyOf(dir, dir.length - 1)) + "/";
		} else if (!directory.startsWith("/")) {
			directory = this.cp.getCwd() + directory;
		}

		if (this.cp.hasPermission(directory, PermissionType.NONE)) {
			return new Error(
					"You do not have permission to view directory: " + directory).execute();
		} else {
			this.cp.setCwd(directory);
			return new Info(this.cp.getCwd()).execute();
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
				"Sets the current working directory to <directoryName>.",
				"CD <directoryName>"
		};
	}

}
