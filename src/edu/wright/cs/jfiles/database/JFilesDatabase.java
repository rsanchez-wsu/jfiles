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

import org.apache.derby.tools.sysinfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Container for database constants.
 *
 * @author Matt Gilene
 *
 */
public class JFilesDatabase {

	private static JFilesDatabase instance = new JFilesDatabase();

	// Map for quick lookup of users by Id
	private Map<Integer, User> users;
	// Map for quick lookup of roles by id
	private Map<String, Role> roles;
	// Map for quick lookup of Permissions by XML
	private Map<String, Permission> permissions;

	/**
	 * Private constructor. This class is a singleton.
	 */
	private JFilesDatabase() {
		loadDatabase();
	}

	/**
	 * Creates a new user in the database.
	 *
	 * @param name
	 *            User name
	 * @param pass
	 *            User pass
	 * @param role
	 *            User role
	 */
	public void createUser(String name, String pass, Role role) {
		int id = DatabaseController.createUser(name, pass, role.getId());
		if (id != -1) {
			User user = new User(id, name, pass, role);
			users.put(id, user);
		}
	}

	/**
	 * Creates a new role in the database.
	 *
	 * @param name
	 *            Role name
	 */
	public void createRole(String name) {
		int id = DatabaseController.createRole(name);
		if (id != -1) {
			Role role = new Role(id, name);
			roles.put(name, role);
		}
	}

	/**
	 * Gets the role with the given name.
	 *
	 * @param name
	 *            Role name
	 * @return Role or null if no role with that name is found
	 */
	public Role getRole(String name) {
		return roles.get(name);
	}

	/**
	 * Loads the database.
	 */
	private void loadDatabase() {
		users = new HashMap<>();
		roles = new HashMap<>();
		permissions = new HashMap<>();

		loadRoles();
		loadPermissions();
		loadUsers();

		if (!roles.containsKey("NONE")) {
			createRole("NONE");
		}
	}

	/**
	 * Loads the list of roles from the database.
	 */
	private void loadRoles() {
		List<Object[]> dbroles = DatabaseController.getAllRoles();
		for (Object[] cols : dbroles) {
			int id = (int) cols[0];
			String name = (String) cols[1];

			Role role = new Role(id, name);
			roles.put(name, role);
		}
	}

	/**
	 * Loads the list of permissions from the database.
	 */
	private void loadPermissions() {
		List<Object[]> dbpermissions = DatabaseController.getAllRoles();
		for (Object[] cols : dbpermissions) {
			int id = (int) cols[0];
			String xml = (String) cols[1];

			Permission perm = new Permission(id, xml);
			permissions.put(xml, perm);
		}
	}

	/**
	 * Loads the list of users from the database.
	 */
	private void loadUsers() {
		List<Object[]> dbusers = DatabaseController.getAllUsers();
		for (Object[] cols : dbusers) {
			int id = (int) cols[0];
			String name = (String) cols[1];
			String pass = (String) cols[2];
			int roleid = (int) cols[3];
			User user;
			for (Role role : roles.values()) {
				if (roleid == role.getId()) {
					user = new User(id, name, pass, role);
					users.put(id, user);
					break;
				}
			}
		}
	}

	/**
	 * Deletes all data from the database.
	 */
	public void reset() {
		DatabaseController.dropTables();
		DatabaseController.createTables();
		loadDatabase();
	}

	/**
	 * Gets the instance of the JFilesDatabase.
	 *
	 * @return JFilesDatabase instance
	 */
	public static JFilesDatabase getInstance() {
		return instance;
	}

	/**
	 * Closes the database.
	 */
	public void close() {
		DatabaseController.shutdown();
	}

	/**
	 * Prints out the list of users in the database.
	 */
	public void print() {
		users.forEach((key, value) -> System.out.println(value));
	}

	/**
	 * Test main.
	 *
	 * @param args
	 *            cmdline args
	 */
	public static void main(String[] args) {
		JFilesDatabase jfilesdb = JFilesDatabase.getInstance();
		// jfilesdb.reset();
		jfilesdb.createRole("ADMIN");
		// NOTE: Failed inserts are still causing the database to auto increment
		// the Id counter thus leaving holes for the next successful insert.
		jfilesdb.createUser("Steve Jobs", "earpods", jfilesdb.getRole("NONE"));
		jfilesdb.createUser("Bill Gates", "windows_vista", jfilesdb.getRole("ADMIN"));
		jfilesdb.createUser("Bob Gates", "windows_vista", jfilesdb.getRole("ADMIN"));
		jfilesdb.createUser("Bob1 Gates", "windows_vista", jfilesdb.getRole("ADMIN"));
		jfilesdb.createUser("Bob2 Gates", "windows_vista", jfilesdb.getRole("ADMIN"));
		jfilesdb.createUser("Bob3 Gates", "windows_vista", jfilesdb.getRole("ADMIN"));
		jfilesdb.createUser("Bob4 Gates", "windows_vista", jfilesdb.getRole("ADMIN"));
		jfilesdb.createUser("Bob5 Gates", "windows_vista", jfilesdb.getRole("ADMIN"));

		jfilesdb.print();
		jfilesdb.close();
	}
}
