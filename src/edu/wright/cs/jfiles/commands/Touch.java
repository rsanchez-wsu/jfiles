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


import java.io.File;
import java.io.IOException;
import edu.wright.cs.jfiles.database.DatabaseUtils.PermissionType;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The touch command creates an empty file.
 * Syntax:
 * 		TOUCH filename
 * Example:
 * 		TOUCH test.txt
 */
public class Touch extends Command {
	private boolean name;

	/**
	 * Calls super.
	 * @param args Command's args.
	 */
	public Touch(String... args) {
		super(args);
	}

	/**
	 * Method for creating a new file
	 * @return true.
	 */
	public boolean createNewFile() {
		return true;
	}

	/**
	 * TODO: Program for TOUCH.
	 * @return f
	 */
	@Override
	public String execute() {

		File file = new File("name.txt");

		setName(false);
		if (!file.exists()) {
			try {
				setName(file.createNewFile());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("file " + file.exists() + " already exists");
		}
		return null;
	}

	public boolean isName() {
		return name;
	}

	public String setName(boolean name) {
		this.name = name;

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
				try {
					return (new File(filePath)).createNewFile()
							? (String) new Info("File was created!").execute()
							: new Error("File failed to create!").execute();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return new Error("File really failed to create!").execute();
				}
			}
		} else {
			return new Error("Missing filename. Syntax: FIND <filename>").execute();
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
				"Creates an empty file.",
				"TOUCH <filename>"
		};
	}

}
