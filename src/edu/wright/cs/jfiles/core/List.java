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

import edu.wright.cs.jfiles.server.JFilesServerThread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class for the list command for the jFiles program.
 *
 */
public class List {

	static final Logger logger = LogManager.getLogger(List.class);
	private JFilesServerThread client;

	/**
	 * Default constructor for the list command.
	 */
	public List(JFilesServerThread client) {
		this.client = client;
	}

	/**
	 * Main function for the command execution.
	 */
	public void executeCommand() {
		listCmd(System.getProperty("user.dir"));
	}

	/**
	 * List Command function. Method for the list command.
	 *
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */
	private void listCmd(String dir) {
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(dir))) {
			for (Path path : directoryStream) {
				// out.write(path.toString() +
				// System.getProperty("line.separator"));
				client.send(path.toString() + System.getProperty("line.separator"));
			}
		} catch (IOException e) {
			// TODO AUto-generated catch block
			// e.printStackTrace();
			logger.error("Some error occured", e);
		}
	}

	/**
	 * Gets the help message for the command.
	 */
	public void getHelp() {
		client.send("list directory contents");
	}
}
