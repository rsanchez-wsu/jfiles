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

package edu.wright.cs.jfiles.database;

/**
 * Role database class.
 *
 * @author Matt Gilene
 *
 */
public class Role {

	private int id;
	private String name;

	/**
	 * Public constructor.
	 *
	 * @param id
	 *            role id
	 * @param name
	 *            role name
	 */
	public Role(int id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * Gets the role's id.
	 *
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the role's id.
	 *
	 * @param id
	 *            role id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the role's name.
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the role's name.
	 *
	 * @param name
	 *            role name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Convert this object to a string.
	 */
	@Override
	public String toString() {
		return String.format("%d - %s", this.id, this.name);

	}

}
