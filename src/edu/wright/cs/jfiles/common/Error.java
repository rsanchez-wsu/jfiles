/*
 * Copyright (C) 2016 - WSU CEG3120 Students
 * 
 * Roberto C. Sánchez <roberto.sanchez@wright.edu>
 * Matthew T. Trippel <trippel.3@wright.edu>
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

package edu.wright.cs.jfiles.common;

/**
 * Class to abstract error codes from source code.
 * @author Brian Denlinger
 *
 */
public enum Error {
	IOEXCEPTION(0, "The file cannot be read."),
	IOEXCEPTION1(1, "IOException occured when trying to access the server config.");
	
	private final int code;
	private final String description;
	
	/**
	 * Constructor for error codes.
	 * @param code Integar value error number
	 * @param description Text to display
	 */
	private Error(int code, String description) {
		this.code = code;
		this.description = description;
	}
	
	/**
	 * Returns error code description.
	 * @return Text of the error message
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns error code number.
	 * @return Int value of the error code
	 */
	public int getCode() {
		return code;
	}
	
	@Override
	public String toString() {
		return code + ": " + description;
	}
}