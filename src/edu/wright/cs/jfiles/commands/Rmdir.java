/*
 * Copyright (C) 2016 - WSU CEG3120 Students
 *
 * Roberto C. Sanchez <roberto.sanchez@wright.edu>
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

/**
 * The rmdir command removes a directory.
 * Syntax:
 *      RMDIR directory
 *  Flags:
 *      - None.
 *  Example:
 *      RMDIR testfolder
 */

public class Rmdir extends Command {
	/**
	 * Calls super.
	 * @param args Command's args.
	 */
	public Rmdir(String... args) {
		super(args);
	}

	/**
	 * TODO: Program for RMDIR.
	 * @return a statement saying either the directory was found and remove or that it was not found
	 */
	@Override
	public String execute() {
		String dir = parser.next();

		if (dir != null) {
			if (!dir.startsWith("/")) {
				dir = this.cp.getCwd() + dir;
			}

			if (!this.cp.hasPermission(dir, PermissionType.READWRITE)) {
				return new Error(
						"You do not have permission to write to directory: " + dir).execute();
			} else {
				return (new File(dir)).delete()
						? new Info("Directory was deleted!").execute()
						: new Error("Directory not found or not empty!").execute();
			}
		} else {
			return new Error("Missing directory name.").execute();
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
				"Removes a directory.",
				"RMDIR <directory>"
		};
	}
}
