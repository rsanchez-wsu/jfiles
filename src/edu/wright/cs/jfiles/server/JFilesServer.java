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

package edu.wright.cs.jfiles.server;

import edu.wright.cs.jfiles.core.CommandExecutor;
import edu.wright.cs.jfiles.core.CommandParser;
import edu.wright.cs.jfiles.core.Environment;
import edu.wright.cs.jfiles.core.ExecutablePath;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * The main class of the JFiles server application.
 * 
 * @author Roberto C. Sánchez &lt;roberto.sanchez@wright.edu&gt;
 *
 */
public class JFilesServer implements Runnable {

	static final Logger logger = LogManager.getLogger(JFilesServer.class);
	private static final int PORT = 9786;
	private final ServerSocket serverSocket;
	private static final String UTF_8 = "UTF-8";

	/**
	 * Handles allocating resources needed for the server.
	 * 
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */
	public JFilesServer() throws IOException {
		serverSocket = new ServerSocket(PORT);
	}

	@SuppressWarnings("resource") // suppress a filewriter being closed
									// elsewhere error
	@Override
	public void run() {
		String dir = System.getProperty("user.dir");
		File history = new File("SearchHistory.txt");

		Locale.setDefault(new Locale("English"));
		try (Socket server = serverSocket.accept()) {
			logger.info("Received connection from" + server.getRemoteSocketAddress());
			InputStreamReader isr = new InputStreamReader(server.getInputStream(), UTF_8);
			BufferedReader in = new BufferedReader(isr);

			FileWriter hstWrt = new FileWriter(history); // history writer
			if (history.exists()) {
				File temp = new File("tempHistory.txt"); // create a temporary
															// file
				FileWriter tempWriter = new FileWriter(temp);
				String previous;
				FileReader readHistory = new FileReader("SearchHistory.txt");
				BufferedReader br = new BufferedReader(readHistory);
				while ((previous = br.readLine()) != null) { // read in data
																// from previous
																// searches
					tempWriter.write(previous); // put data in temp file
				}
				br.close();
				hstWrt = new FileWriter(temp);// suppressed the warning because
												// it's closed elsewhere
				tempWriter.close(); // hstWrt now writes to the temp file for
									// new searches
				history.delete(); // delete the old searchHistory so temp can be
									// renamed
				temp.renameTo(history); // rename the search file to
										// searchHistory
			}

			String cmd;
			OutputStreamWriter osw = new OutputStreamWriter(server.getOutputStream(), UTF_8);

			ExecutablePath executablePath = new ExecutablePath();
			Environment environment = new Environment();

			CommandParser parser = new CommandParser(environment);
			CommandExecutor executor = new CommandExecutor(executablePath, environment);

			BufferedWriter out = new BufferedWriter(osw);

			while (null != (cmd = in.readLine())) {
				if ("".equals(cmd)) {
					break;
				}

				String[] baseCommand = cmd.split(" ");

				switch (baseCommand[0].toUpperCase(Locale.ENGLISH)) {
				case "LIST":
					listCmd(dir, out);
					break;
				case "FIND":
					findCmd(dir, out, baseCommand[1].toLowerCase(Locale.ENGLISH), hstWrt);
					break;
				case "FINDR":
					recursiveFindCmd(dir, out, baseCommand[1].toLowerCase(Locale.ENGLISH), hstWrt);
					break;
				case "FILE":
					break;
				case "EXIT":
					try {
						out.close();
						in.close();
						serverSocket.close();
					} catch (IOException ex) {
						System.out.println("Error closing the socket and streams");

					}
				default:
					break;
				}
			}
			out.flush();
			hstWrt.close();

		} catch (IOException e) {
			// TODO AUto-generated catch block
			// e.printStackTrace();
			logger.error("Some error occured", e);
		}
	}

	/**
	 * List Command function. Method for the list command.
	 * 
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */
	private void listCmd(String dir, BufferedWriter out) {
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(dir))) {
			for (Path path : directoryStream) {
				out.write(path.toString() + System.getProperty("line.separator"));
			}
		} catch (IOException e) {
			// TODO AUto-generated catch block
			// e.printStackTrace();
			logger.error("Some error occured", e);
		}
	}

	/**
	 * Find Command function. Method for the find command. Writes results found
	 * within current directory. Search supports glob patterns
	 * 
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */
	private void findCmd(String dir, BufferedWriter out, String searchTerm,
			FileWriter historyWrite) {
		int findCount = 0;
		try (DirectoryStream<Path> directoryStream =
				Files.newDirectoryStream(Paths.get(dir), searchTerm)) {
			historyWrite.write(searchTerm + "\n");
			for (Path path : directoryStream) {
				// if
				// (path.toString().toLowerCase(Locale.ENGLISH).contains(searchTerm))
				// {
				out.write(path.toString() + "\n");
				findCount++;
				// }
			}
			System.out.println("Found " + findCount + " file(s) in " + dir + " that contains \""
					+ searchTerm + "\"\n");
		} catch (IOException e) {
			// TODO AUto-generated catch block
			// e.printStackTrace();
			logger.error("Some error occured", e);
		}
	}

	/**
	 * Recursive find Command function. Method for the recursive option of the
	 * find command. Calls itself if a child directory is found, otherwise calls
	 * findCmd to get results from current directory.
	 * 
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */
	private void recursiveFindCmd(String dir, BufferedWriter out, String searchTerm,
			FileWriter hstWrt) {
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(dir))) {
			for (Path path : directoryStream) {
				if (path.toFile().isDirectory()) {
					recursiveFindCmd(path.toString(), out, searchTerm, hstWrt);
				}
			}
		} catch (IOException e) {
			// TODO AUto-generated catch block
			// e.printStackTrace();
			logger.error("Some error occured", e);
		}
		findCmd(dir, out, searchTerm, hstWrt);
		try {
			hstWrt.write(searchTerm + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Some error occured while writing to a the history", e);
		}
	}

	/**
	 * The main entry point to the program.
	 * 
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */
	public static void main(String[] args) {
		try {
			logger.info("Starting the server");
			JFilesServer jf = new JFilesServer();
			Thread thread = new Thread(jf);
			thread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
