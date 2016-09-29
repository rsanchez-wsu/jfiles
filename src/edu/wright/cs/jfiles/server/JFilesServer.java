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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The main class of the JFiles server application.
 * 
 * @author Roberto C. Sánchez &lt;roberto.sanchez@wright.edu&gt;
 *
 */
public class JFilesServer implements Runnable {

	private static final int PORT = 9786;
	private final ServerSocket serverSocket;
	private static final String UTF_8 = "UTF-8";

	/**
	 * Handles allocating resources needed for the server.
	 * 
	 * @throws IOException If there is a problem binding to the socket
	 */
	public JFilesServer() throws IOException {
		serverSocket = new ServerSocket(PORT);
	}

	@Override
	public void run() {
		String dir = System.getProperty("user.dir");
		//These were added to implement File command
		FileInputStream fis = null;
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		File sendFile = null;
		Socket sock = null;
		//------------------------------------------
		try (Socket server = serverSocket.accept()) {
			System.out.println("Received connection from"
					+ server.getRemoteSocketAddress());
			InputStreamReader isr =
					new InputStreamReader(server.getInputStream(), UTF_8);
			BufferedReader in = new BufferedReader(isr);
			String cmd = in.readLine();
			OutputStreamWriter osw =
					new OutputStreamWriter(server.getOutputStream(), UTF_8);
			BufferedWriter out = new BufferedWriter(osw);
			sock = serverSocket.accept();
			if ("LIST".equalsIgnoreCase(cmd)) {
				try (DirectoryStream<Path> directoryStream =
						Files.newDirectoryStream(Paths.get(dir))) {
					for (Path path : directoryStream) {
						out.write(path.toString() + "\n");
					}
				}
			} else if ("FILE".equalsIgnoreCase(cmd)) {
				String sample = "This is a placeholder for testing.";
				String workingDir = System.getProperty("user.dir");
				String filepath = workingDir + "//demo.txt";
				fos = new FileOutputStream(filepath);
				fos.write(sample.getBytes("UTF-8"));
				fos.close();
				sendFile = new File(filepath);
				byte [] bytearray = new byte [(int) sendFile.length()];
				fis = new FileInputStream(sendFile);
				bis = new BufferedInputStream(fis);
				bis.read(bytearray, 0, bytearray.length);
				os = sock.getOutputStream();
				System.out.println("Sending " + sendFile.getName());
				os.write(bytearray, 0, bytearray.length);
				os.flush();
				System.out.println("Sent");
			} else {
				out.write("ERROR: Unknown command!\n");
			}
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (sock != null) {
				try {
					sock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * The main entry point to the program.
	 * 
	 * @param args The command-line arguments
	 */
	public static void main(String[] args) {
		System.out.println("Starting the server");
		try {
			JFilesServer jf = new JFilesServer();
			Thread thread = new Thread(jf);
			thread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
