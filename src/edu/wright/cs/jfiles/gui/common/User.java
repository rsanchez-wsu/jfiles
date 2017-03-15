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

package edu.wright.cs.jfiles.gui.server;

import javafx.beans.property.SimpleStringProperty;

/**
 * Container class used to store data for use in a table.
 *
 * @author Matt Gilene
 *
 */
public class User {
	private final SimpleStringProperty id;
	private final SimpleStringProperty name;
	private final SimpleStringProperty role;

	/**
	 * Public constructor.
	 *
	 * @param id
	 *            user id
	 * @param name
	 *            user name
	 * @param role
	 *            user role
	 */
	public User(String id, String name, String role) {
		this.id = new SimpleStringProperty(id);
		this.name = new SimpleStringProperty(name);
		this.role = new SimpleStringProperty(role);
	}

	/**
	 * Get ID.
	 *
	 * @return id
	 */
	public String getId() {
		return id.get();
	}

	/**
	 * Sets ID.
	 *
	 * @param id
	 *            user id
	 */
	public void setId(String id) {
		this.id.set(id);
	}

	/**
	 * Get name.
	 *
	 * @return name
	 */
	public String getName() {
		return name.get();
	}

	/**
	 * Sets name.
	 *
	 * @param name
	 *            user name
	 */
	public void setName(String name) {
		this.name.set(name);
	}

	/**
	 * Get role.
	 *
	 * @return role
	 */
	public String getRole() {
		return role.get();
	}

	/**
	 * Sets role.
	 *
	 * @param role
	 *            user role
	 */
	public void setRole(String role) {
		this.role.set(role);
	}
}
