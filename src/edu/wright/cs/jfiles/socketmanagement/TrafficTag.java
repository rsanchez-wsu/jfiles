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

package edu.wright.cs.jfiles.socketmanagement;

/**
 * A enum used to tag packets in SocketManager.
 * Tags have byte values. 
 * 
 * @author Daryl Arouchian
 *
 */
public enum TrafficTag {
	NO_TAG(0), FILE_ID(1), FILE(2), COMMAND(3), END(4);

	private byte value;

	/**
	 * enum constructor. Casts the integer value given into 
	 * a byte and assigns it to the value field.
	 * 
	 * @param value the integer to be assigned
	 */
	private TrafficTag(int value) {
		this.value = (byte) value;
	}

	/**
	 * Gets the byte value of the tag.
	 * 
	 * @return the tag as a byte
	 */
	public byte value() {
		return value;
	}
}