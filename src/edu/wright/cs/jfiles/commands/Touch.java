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

	public void setName(boolean name) {
		this.name = name;
	}

}
