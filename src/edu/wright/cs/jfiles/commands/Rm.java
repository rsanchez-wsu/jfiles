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
		String filePath;
		filePath = parser.next();
		File file = new File(filePath);
		file.delete();
		
		return file.exists() 
				? filePath 
				: new Error("Missing filename. Syntax: FIND <filename> [directory]").execute();
//		The above conditional statement can also be written like so (I find this easier to read):		
//		if(file.exists())
//			return filePath;
//		else
//			return new Error("Missing filename. Syntax: FIND <filename> [directory]").execute();

	}

}
