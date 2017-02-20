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
 * Database role container class.
 *
 * @author Matt Gilene
 *
 */
public class Role {
	private String name;
	private int id;
	private List<Permission> permissions;

	/**
	 * Creates a new role.
	 *
	 * @param id
	 *            Role Id
	 * @param name
	 *            Role name
	 */
	public Role(int id, String name) {
		this.id = id;
		this.name = name;
		this.permissions = new ArrayList<>();
	}


	/**
	 * Gets the Id for this role.
	 *
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the name of this role.
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Adds a permission to this role.
	 *
	 * @param perm
	 *            permission to add
	 */
	public void addPermission(Permission perm) {
		if (!permissions.contains(perm)) {
			permissions.add(perm);
		}
	}

	/**
	 * Removes a permission from this role.
	 *
	 * @param perm
	 *            the permission to remove
	 */
	public void removePermission(Permission perm) {
		if (permissions.contains(perm)) {
			permissions.remove(perm);
		}
	}

	/**
	 * Checks to see if this role has access to the specified file.
	 *
	 * @param file
	 *            file to check access off
	 * @return true or false
	 */
	public boolean hasAccess(File file) {
		boolean hasAccess = false;
		for (Permission perm : permissions) {
			hasAccess = perm.hasAccess(file);
		}
		return hasAccess;
	}

	@Override
	public String toString() {
		return String.format("ID: %d NAME: %s", id, name);
	}
}
