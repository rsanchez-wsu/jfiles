/*
 * Copyright (C) 2017 - WSU CEG3120 Students
 *
 *
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

/**
 * The WRITE command outputs to a file
 * Format:
 * 	 	WRITE <filename> <text>
 * Example:
 * 		WRITE output.txt ls
 *
 *
 */
public class Write extends Command {
	/**
	 * Calls super.
	 * @param args Command's args.
	 */
	public Write(String... args){
		super(args);
	}
	/**
	 * Gets the output file identified by the user
	 * @return The output file
	 */
	private String setFile() {
		// Prompt user to enter the file name
		String outputFile = "";
		return outputFile;
	}
	/**
	 * Get the text the user wants to publish to a file
	 * @return The text
	 */
	private String setText() {
		// Prompt user to enter the text
		String outText = "";
		return outText;
	}
	/**
	 * return null.
	 */
	@Override
	public String execute() {
		// TODO Auto-generated method stub
		return null;
	}

}
