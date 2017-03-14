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

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

/**
 * The mv command moves or renames a file.
 * Syntax:
 * 		MV from to
 * Example:
 * 		MV build.xml builds.xml
 */
public class Mv extends Command {

	/**
	 * Calls super.
	 * @param args Command's args.
	 */
	public Mv(String... args) {
		super(args);
	}

	/**
	 *  TODO: Program for MV.
	 *  @return Nothing.
	 */
	@Override
	public String execute() {
		String fromName = this.parser.next();
		String toName = this.parser.next();

		if (fromName == null || toName == null) {
			return new Error("Invalid from or to name.").execute();
		}

		if (!fromName.startsWith("/")) {
			fromName = this.cp.getCwd() + fromName;
		}

		if (!toName.startsWith("/")) {
			toName = this.cp.getCwd() + toName;
		}

		if (!this.cp.hasPermission(fromName, PermissionType.READWRITE)
				&& !this.cp.hasPermission(toName, PermissionType.READWRITE)) {
			return new Error(
					"You do not have permission to edit that directory.").execute();
		}

		try {
			Files.move(new File(fromName), new File(toName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Error("Move failed!").execute();
		}

		return new Info("Move successful!").execute();
	}

	/**
	 * Gets the class specific help message and Syntax.
	 * It's done like this so you can extend this method and not
	 * have to worry about help working the same in all methods.
	 * @return [0] is what the command does, [1] is the syntax of command.
	 */
	protected String[] helpStrings() {
		return new String[] {
				"Moves or renames a file.",
				"MV <from> <to>"
		};
	}

}
