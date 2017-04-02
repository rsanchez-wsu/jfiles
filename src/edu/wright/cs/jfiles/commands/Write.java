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
 * The WRITE command outputs to a command to a file instead of the console
 * Format:
 * 	 	WRITE <filename> <command...>
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
	 * return null.
	 */
	@Override
	public String execute() {
		// TODO Auto-generated method stub
		return null;
	}

}
