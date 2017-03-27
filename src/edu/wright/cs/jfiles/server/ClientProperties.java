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

package edu.wright.cs.jfiles.server;

import edu.wright.cs.jfiles.database.DatabaseController;
import edu.wright.cs.jfiles.database.DatabaseUtils.PermissionType;
import edu.wright.cs.jfiles.database.IdNotFoundException;
import edu.wright.cs.jfiles.database.User;

/**
 * Contains client information and properties.
 */
public class ClientProperties {

	/**
	 * < 0 means not logged in.
	 */
	private User user = new User();
	private PermissionType cachePermissionType = PermissionType.NONE;
	private String cwd;

	/**
	 * Default constructor for ClientProperties.
	 */
	public ClientProperties() {}

	/**
	 * Sets the Current Working Directory.
	 * @param cwd The directory to set CWD to.
	 */
	public void setCwd(String cwd) {
		if (!cwd.endsWith("/")) {
			cwd += "/";
		}

		this.cwd = cwd;
	}

	/**
	 * Sets the current user.
	 * @param user The new user.
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Gets the Current Working Directory.
	 * @return The Current Working Directory.
	 */
	public String getCwd() {
		return this.cwd;
	}

	/**
	 * Gets the user for client properties.
	 * @return The user.
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Logs a user in. Sets userId.
	 * @param username The username of the user.
	 * @param password The password of the user.
	 * @return If login was successful.
	 */
	public boolean login(String username, String password) {
		for (User user : DatabaseController.getUsers()) {
			if (user.getName().equals(username)) {
				if (user.getPassword().equals(password)) {
					this.user = user;
					return true;
				} else {
					return false;
				}
			}
		}

		return false;
	}

	/**
	 * Caches the permissiontype of the current working directory.
	 */
	public void cachePermissionType() {
		this.cachePermissionType = getPermissionType(this.getCwd());
	}

	/**
	 * Returns the current working directory permission type.
	 * @param type The type to see if they have.
	 * @return If the user has that permission for current working directory.
	 */
	public boolean hasPermission(PermissionType type) {
		return cachePermissionType == type;
	}

	/**
	 * Returns if user has permission.
	 * @param dir The directory to check for.
	 * @return If the user has permission.
	 */
	public boolean hasPermission(String dir, PermissionType type) {
		return getPermissionType(dir) == type;
	}

	/**
	 * Returns the permission type for the current working directory.
	 * @return The type of permission.
	 */
	public PermissionType getPermissionType() {
		return cachePermissionType;
	}

	/**
	 * Returns the permission type.
	 * @param dir The directory to be checked.
	 * @return The type of permission.
	 */
	public PermissionType getPermissionType(String dir) {
		try {
			return this.user.getId() > 0
					? DatabaseController.userHasPermission(this.user.getId(), dir)
					: PermissionType.NONE;
		} catch (IdNotFoundException e) {
			e.printStackTrace();
			return PermissionType.NONE;
		}
	}

}
