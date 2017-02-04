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
import java.util.ArrayList;
import java.util.List;

/**
 *  The Close command closes the connection.
 *  Syntax:
 *      FIND filename [directory]
 *  Flags:
 *      - R - recursively search through [directory]s.
 *  Example:
 *      FIND *.xml
 */
public class Find extends Command {

	/**
	 * Calls super.
	 * @param args Command's args.
	 */
	public Find(String... args) {
		super(args);
	}

	/**
	 * @return The list of files that match filename.
	 */
	private List<String> findFiles(String filename, String directory) {
		List<String> res = new ArrayList<String>();

		File folder = new File(directory);
		File[] listOfFiles = folder.listFiles();

		if (folder.isDirectory() && listOfFiles != null) {
			for (File f : listOfFiles) {
				if (f.isFile() && f.getName().contains(filename)) {
					res.add(f.getAbsolutePath());
				} else if (f.isDirectory() && this.parser.doesFlagExist("R")) {
					res.addAll(findFiles(filename, f.getAbsolutePath()));
				}
			}
		}

		return res;
	}

	/**
	 * @return The list of files that match filename as a string.
	 */
	private String getFiles(String filename, String directory) {
		String dir = directory != null ? directory : ".";

		return atos(findFiles(filename, dir));
	}

	/**
	 *  TODO: Returning findings
	 *  @return A new-line delimited list of files that match filename in
	 *          [directory]. If no [directory] is given, the current working
	 *          directory is used.
	 */
	public String execute() {
		String filename = this.parser.next();
		String directory = this.parser.next();

		return filename != null
				? getFiles(filename, directory)
				: new Error("Missing filename. Syntax: FIND <filename> [directory]").execute();
	}

}
