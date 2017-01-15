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

package edu.wright.cs.jfiles.core;

import edu.wright.cs.jfiles.exception.EnvironmentException;

import java.util.Properties;

/**
 * Environment class for the command executor.
 *
 */
public class Environment {

	public static final String PROPERTY_CHAR = "$";

	/**
	 * The exit status of the last executed command.
	 */
	public static final String EXIT_STATUS = "?";

	/**
	 * The current working directory of the shell.
	 */
	public static final String CWD = "user.dir";

	/**
	 * The currently logged in users home directory.
	 */
	public static final String USER_HOME = "user.home";

	private Properties properties;

	/**
	 * Creates the environment class with the systems properties.
	 */
	public Environment() {
		this.properties = System.getProperties();
		loadDefaults();
	}

	/**
	 * Gets the current properties.
	 * 
	 * @return current list of properties
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Searches for the property with the specified key in this property list.
	 * If the key is not found in this property list, the default property list,
	 * and its defaults, recursively, are then checked. The method returns null
	 * if the property is not found.
	 * 
	 * @param propertyName
	 *            the name of the property to search for
	 * @return the property
	 */
	public String getProperty(String propertyName) {
		return properties.getProperty(propertyName);
	}

	/**
	 * Searches for the property with the specified key in this property list.
	 * If the key is not found in this property list, the default property list,
	 * and its defaults, recursively, are then checked. The method returns the
	 * default value argument if the property is not found.
	 * 
	 * @param propertyName
	 *            the name of the property to find
	 * @param defaultValue
	 *            the default value of the property
	 * @return the string value of the system property, or the default value if
	 *         there is no property with that key
	 */
	public String getProperty(String propertyName, String defaultValue) {
		return properties.getProperty(propertyName, defaultValue);
	}

	/**
	 * Sets the system property indicated by the specified key.
	 * 
	 * @param key
	 *            the property to set
	 * @param value
	 *            the value to set the property to
	 */
	public void setProperty(String key, String value) {
		if (key == null || value == null) {
			throw new EnvironmentException("Property key and property value cannot be null");
		}
		properties.setProperty(key, value);
	}

	/**
	 * Loads the default properties and values from the system.
	 */
	private void loadDefaults() {
	}
}

