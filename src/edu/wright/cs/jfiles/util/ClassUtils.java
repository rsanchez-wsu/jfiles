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

package edu.wright.cs.jfiles.util;

/**
 * Utility class for loading in class files.
 * 
 * @author brett
 *
 */
public class ClassUtils {

	/**
	 * Loads a class from the file.
	 * 
	 * @param fileName
	 *            the name for the class file
	 * @return class object for the file
	 * @throws ClassNotFoundException
	 *             if the class cannot be found
	 */
	public static Class<?> loadClassFromFile(String fileName) throws ClassNotFoundException {

		int endIndex = fileName.length();

		if (fileName.endsWith(".class")) {
			endIndex -= ".class".length();
		}

		String className = fileName.substring(0, endIndex);
		className = className.replace("/", ".");

		return Thread.currentThread().getContextClassLoader().loadClass(className);
	}
}
