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
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The rm command removes a file.
 * Syntax:
 * 		RM filename
 * Flags:
 * 		- R - recursively search through [directory]s for files to delete.
 * Example:
 * 		RM *.xml
 */
public class Rm extends Command {

	/**
	 * Calls super.
	 * @param args Command's args.
	 */
	public Rm(String... args) {
		super(args);
	}

	/**
	 *	@return A new-line delimited list of files that match [filename] and
	 *  			were removed.
	 */
	@Override
	public String execute() {
		String filePath = parser.next();

		if (filePath != null) {
			if (!filePath.startsWith("/")) {
				filePath = this.cp.getCwd() + filePath;
			}

			Path path = Paths.get(filePath);

			String directory = "";

			if (path.getNameCount() > 1) {
				directory = path.subpath(0, path.getNameCount() - 1).toString();
			}

			if (!this.cp.hasPermission(directory, PermissionType.READWRITE)) {
				return new Error(
						"You do not have permission to write to directory: " + directory).execute();
			} else {
				return (new File(filePath)).delete()
						? new Info("File was deleted!").execute()
						: new Error("File not found!").execute();
			}
		} else {
			return new Error("Missing filename. Syntax: FIND <filename> [directory]").execute();
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
				"Removes a file.",
				"RM <filename>"
		};
	}

}
