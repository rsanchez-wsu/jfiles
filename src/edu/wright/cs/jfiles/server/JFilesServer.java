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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
	
	/**
	 * Method that creates an XML file.
	 */
	private Document createXml() {
		Document doc = null;
		try {
			// Create document
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			doc = builder.newDocument();
			//Create doc type
			DOMImplementation domImpl = doc.getImplementation();
			DocumentType doctype = domImpl.createDocumentType("fileSystem", null, "fileSystem.dtd");
			doc.appendChild(doctype);
			// Add root element
			Element rootElement = doc.createElement("fileSystem");
			doc.appendChild(rootElement);
			return doc;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return doc;
		}
		
	}

	@Override
	public void run() {
		String dir = System.getProperty("user.dir");
		try (Socket server = serverSocket.accept()) {
			logger.info("Received connection from" + server.getRemoteSocketAddress());
			InputStreamReader isr = new InputStreamReader(server.getInputStream(), UTF_8);
			BufferedReader in = new BufferedReader(isr);
			String cmd;
			while (null != (cmd = in.readLine())) {
				if ("".equals(cmd)) {
					break;
				}
				OutputStreamWriter osw = new OutputStreamWriter(server.getOutputStream(), UTF_8);

				BufferedWriter out = new BufferedWriter(osw);
				String[] baseCommand = cmd.split(" ");
				if ("LIST".equalsIgnoreCase(baseCommand[0])) {
					try (DirectoryStream<Path> directoryStream = Files
							.newDirectoryStream(Paths.get(dir))) {
						for (Path path : directoryStream) {
							out.write(path.toString() + "\n");
						}
					}

				}
				// start Search block
				if ("FIND".equalsIgnoreCase(baseCommand[0])) {

					try (DirectoryStream<Path> directoryStream = Files
							.newDirectoryStream(Paths.get(dir))) {
						for (Path path : directoryStream) {
							// out.write(path.toString() + "\n");
							if (path.toString().contains(baseCommand[1])) {
								out.write(path.toString() + "\n");
							}

						}
					}

				} else { // End search block
					logger.error("Unknown commad");
				}
				out.flush();
			}
		} catch (IOException e) {
			//TODO AUto-generated catch block
			//e.printStackTrace();
			logger.error("Some error occured", e);
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
			System.out.println(jf.createXml());
			//Thread thread = new Thread(jf);
			//thread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
