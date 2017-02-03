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

/**
 *  The Send command returns ".send".
 *  Syntax:
 *      SEND filename
 *  Flags:
 *      - None.
 *  Example:
 *      SEND build.xml
 */
public class Send extends Command {

	private String filename;

	/**
	 * Calls super.
	 * @param args Command's args.
	 */
	public Send(String... args) {
		super(args);
	}

	/**
	 * TODO: Program file send.
	 * @return RECV to sender of SEND.
	 */
	@Override
	public String execute() {
		return ".send" + filename;
	}
	
	/**
	 * Gets the filename for sending.
	 * @return the filename along with the extension that you want to send.
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Sets the filename that you want to send.
	 * @param filename - the filename string you want to send.
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
}

