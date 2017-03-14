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
 *  The Recv command returns "Execute in progress".
 *  Syntax:
 *      RECV filename contents... EOF
 *  Flags:
 *      - None.
 *  Example:
 *      RECV test.txt This is the contents of our test file! EOF
 */

public class Recv extends Command {

	private String sender;
	private String receiver;
	private String filename;

	/**
	 * Calls super.
	 * @param args Command's args.
	 */
	public Recv(String... args) {
		super(args);
	}

	/**
	 * Gets the user that will send the file.
	 * @return the sender of the file.
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * Gets the user that will receive the file.
	 * @return the receiver of the file.
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * Gets the filename for sending.
	 * @return the filename along with the extension that you want to send.
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 *  TODO: Program for RECV.
	 *  @return Not much yet
	 */
	@Override
	public String execute() {
		return "Execute in progress";
	}

}
