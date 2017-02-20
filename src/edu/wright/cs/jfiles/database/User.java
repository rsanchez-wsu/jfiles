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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Database User container class.
 *
 * @author Matt Gilene
 *
 */
public class User {

	private int id;
	private String name;
	private String pass;
	private Role role;

	private List<Permission> permissions;

	/**
	 * Creates a new User.
	 *
	 * @param id
	 *            User Id
	 * @param name
	 *            User name
	 * @param pass
	 *            User password
	 * @param role
	 *            User role
	 */
	public User(int id, String name, String pass, Role role) {
		this.id = id;
		this.setName(name);
		this.setPass(pass);
		this.role = role;
		this.permissions = new ArrayList<>();
	}

	/**
	 * Adds a permission to this user.
	 *
	 * @param perm
	 *            permission to add
	 */
	public void addPermission(Permission perm) {
		if (!permissions.contains(perm)) {
			permissions.add(perm);
			DatabaseController.addPermissionToUser(id, perm.getId());
		}
	}

	/**
	 * Removes a permission from this user.
	 *
	 * @param perm
	 *            permission to remove
	 */
	public void removePermission(Permission perm) {
		if (permissions.contains(perm)) {
			permissions.remove(perm);
			// TODO:
			// DatabaseController.removePermissionFromUser(id, perm.getId());
		}
	}

	/**
	 * Checks to see if this user has access to the file specified.
	 *
	 * @param file
	 *            file to check access of.
	 * @return true or false
	 */
	public boolean hasAccess(File file) {
		boolean hasAccess = role.hasAccess(file);
		if (!hasAccess) {
			for (Permission perm : permissions) {
				hasAccess = perm.hasAccess(file);
			}
		}
		return hasAccess;
	}

	/**
	 * Gets the user's name.
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the user's name.
	 *
	 * @param name
	 *            name
	 */
	public void setName(String name) {
		this.name = name;
		// TODO: update database
	}

	/**
	 * Gets the user's password.
	 *
	 * @return password
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * Sets the user's password.
	 *
	 * @param pass
	 *            password
	 */
	public void setPass(String pass) {
		this.pass = pass;
		// TODO: update database
	}

	/**
	 * Gets the role of the user.
	 *
	 * @return role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Sets the role of this user.
	 *
	 * @param role
	 *            role to set
	 */
	public void setRole(Role role) {
		this.role = role;
		// TODO: Update the database.
	}

	@Override
	public String toString() {
		return String.format("ID: %d NAME: %s PASS: %s ROLE: (%s)", id, name, pass, role);
	}
}
