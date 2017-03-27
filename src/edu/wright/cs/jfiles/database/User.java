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
 * User database class.
 */
public class User {

	private int id;
	private String name;
	private String pass;
	private int role;
	private String roleName;

	/**
	 * Default constructor.
	 */
	public User() {}

	/**
	 * Constructor with all arguments.
	 * @param id The id of user.
	 * @param username The username of user.
	 * @param password The password of user.
	 * @param role The role of user.
	 */
	public User(int id, String username, String password, int role) {
		this.id = id;
		this.name = username;
		this.pass = password;
		this.role = role;
	}

	/**
	 * @return Returns id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets id.
	 * @param id Id of user.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Username.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets username.
	 * @param username Username of user.
	 */
	public void setName(String username) {
		this.name = username;
	}

	/**
	 * @return password.
	 */
	public String getPassword() {
		return pass;
	}

	/**
	 * Sets password.
	 * @param password Password of user.
	 */
	public void setPassword(String password) {
		this.pass = password;
	}

	/**
	 * @return role.
	 */
	public int getRole() {
		return role;
	}

	/**
	 * Sets the role.
	 * @param role Role of user.
	 */
	public void setRole(int role) {
		this.role = role;
	}

	/**
	 * Gets the role name for the user.
	 *
	 * @return name of the role
	 */
	public String getRoleName() {
		// Cache the role name if it doesn't exist already
		if (this.roleName == null) {
			this.roleName = DatabaseController.getRoles().stream()
					.filter(role -> role.getId() == this.role)
					.findFirst()
					.get()
					.getName();
		}
		return this.roleName;
	}

	/**
	 * Gets string format.
	 * @return The class in String format.
	 */
	@Override
	public String toString() {
		return String.format("%d\t%s\t%s\t%d", getId(), getName(), getPassword(), getRole());
	}

}
