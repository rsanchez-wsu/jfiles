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

import edu.wright.cs.jfiles.exception.CommandNotFoundException;
import edu.wright.cs.jfiles.exception.ShellException;
import edu.wright.cs.jfiles.util.ClassUtils;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class is used to hold and register commands that will be executed.
 * 
 * @author brett
 *
 */
public class ExecutablePath {

	private static final String DEFAULT_EXECUTABLE_PACKAGE =
			"/edu/wright/cs/jfiles/core/Executable.java";

	private Map<String, Executable> executables = new HashMap<String, Executable>();

	/**
	 * Creates the executable path with from the default settings.
	 */
	public ExecutablePath() {
		registerExecutablesFromDefaultPath();
	}

	/**
	 * Registers an executable command.
	 * 
	 * @param executable
	 *            the command to register
	 */
	public void registerExecutable(Executable executable) {
		executables.put(executable.getName(), executable);
	}

	/**
	 * Locates the command executable.
	 * 
	 * @param name
	 *            the name of the command
	 * @return the executable for the command
	 */
	public Executable locateExecutable(String name) {
		Executable executable = executables.get(name);

		if (executable == null) {
			throw new CommandNotFoundException(name);
		}

		return executable;
	}

	/**
	 * Registers the executable commands from the default path/settings.
	 */
	private void registerExecutablesFromDefaultPath() {
		try {
			List<String> packageFiles = getFileNamesInPackage(DEFAULT_EXECUTABLE_PACKAGE);
			for (String fileName : packageFiles) {
				if (isClassFile(fileName)) {
					Class<?> foo = ClassUtils.loadClassFromFile(
							sanitizeFileName(fileName, DEFAULT_EXECUTABLE_PACKAGE));

					if (isExecutable(foo)) {
						registerExecutable((Executable) foo.newInstance());
					}
				}
			}
		} catch (IOException e) {
			throw new ShellException("Problem trying to create the default bin path", e);
		} catch (ClassNotFoundException e) {
			throw new ShellException("Problem trying to load class from executable package "
					+ DEFAULT_EXECUTABLE_PACKAGE, e);
		} catch (InstantiationException e) {
			throw new ShellException("Problem trying to instantiate instance of executable", e);
		} catch (IllegalAccessException e) {
			throw new ShellException("Problem trying to instantiate instance of executable", e);
		}
	}

	/**
	 * Cleanses the file names.
	 * 
	 * @param fileName
	 *            the name of the command file
	 * @param packageName
	 *            the name of the package
	 * @return a string of the sanitized name
	 */
	private String sanitizeFileName(String fileName, String packageName) {
		String sanitizedName = fileName;

		if (fileName.contains(packageName)) {
			sanitizedName = fileName.substring(fileName.indexOf(packageName));
		}
		return sanitizedName;
	}

	/**
	 * Checks is the class is executable.
	 * 
	 * @param foo
	 *            the class file
	 * @return true if the class is instantiable
	 */
	private boolean isExecutable(Class<?> foo) {
		int modifiers = foo.getModifiers();
		if (isNonInstantiable(modifiers)) {
			return false;
		}

		// We have a non abstract class, now let's see whether we implement the
		// Executable interface
		return Executable.class.isAssignableFrom(foo);
	}

	/**
	 * Check to see if the executable can be instantiated.
	 * 
	 * @param modifiers
	 *            the modifiers for the executable command
	 * @return true if instantiable
	 */
	private boolean isNonInstantiable(int modifiers) {
		return Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)
				|| Modifier.isStatic(modifiers);
	}

	/**
	 * Checks if the file is a class file.
	 * 
	 * @param fileName
	 *            the name of the file
	 * @return true if it is a class file
	 */
	private boolean isClassFile(String fileName) {
		return fileName.endsWith(".class");
	}

	/**
	 * Gets all the names of the files in the package.
	 * 
	 * @param packageName
	 *            the package name to go through
	 * @return a List of the files
	 * @throws IOException
	 *             if there is a problem reading the file names
	 */
	private List<String> getFileNamesInPackage(String packageName) throws IOException {
		List<String> fileNames = new ArrayList<String>();

		Enumeration<URL> resources =
				Thread.currentThread().getContextClassLoader().getResources(packageName);
		while (resources.hasMoreElements()) {
			// Currently commented out to wait to check what jfiles will have/be
			// fileNames.addAll(FileUtils.getFileNamesFromUrl(resources.nextElement()));
		}

		return fileNames;
	}

	/**
	 * Gets the list of all the registered executables.
	 * 
	 * @return a List of the executable commands
	 */
	public List<Executable> getAllRegisteredExecutables() {
		return new ArrayList<Executable>(executables.values());
	}
}
