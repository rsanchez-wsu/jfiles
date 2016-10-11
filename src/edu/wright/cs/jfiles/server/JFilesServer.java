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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
		//String dir = System.getProperty("user.dir");
		//These were added to implement File command
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
			if (cmd != null) {
				String [] words = cmd.split(" ");
				String cmdName = words[0]; 
				switch (cmdName) {
				case "FILE":
					String fileName = words[1];
					String fileLocation = words[2];
					System.out.println("Client wants " + fileName + " at " 
							+ fileLocation);
					
					break;
				
				case "LIST":
					break;
				
				default:
					System.out.println("Invalid Command.");
					break;
				}
			}
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	/**
	 * When FILE command is received from client, server calls this method
	 * to handle file transfer.
	 * @param filelocation the location of the desired file
	 * @param servsock the socket where the server connection resides
	 */
	public void sendFile(String filelocation, ServerSocket servsock) {
		Socket sock = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			File sendFile = new File(filelocation);
			sock = serverSocket.accept();
			byte [] byteArray = new byte [(int) sendFile.length()];
			bis = new BufferedInputStream(
					new FileInputStream(sendFile));
			//findBugs gets mad that bis.read() isn't assigned
			//to a variable, so plchlder is used until
			//a better solution is found.
			int plchlder = bis.read(byteArray, 0, byteArray.length);
			System.out.println(plchlder);
			os = sock.getOutputStream();
			os.write(byteArray, 0, byteArray.length);
			os.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
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
			if (os != null) {
				try {
					os.close();
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
