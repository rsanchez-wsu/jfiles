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
	private static ServerSocket serverSocket;
	private static final String UTF_8 = "UTF-8";
	private Socket socket;
	
	static {
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Handles allocating resources needed for the server.
	 * 
	 * @throws IOException If there is a problem binding to the socket
	 */
	public JFilesServer(Socket sock) {
		socket = sock;
	}

	@Override
	public void run() {
		//String dir = System.getProperty("user.dir");
		//These were added to implement File command
		//------------------------------------------
		try {
			System.out.println("Received connection from"
					+ socket.getRemoteSocketAddress());
			InputStreamReader isr =
					new InputStreamReader(socket.getInputStream(), UTF_8);
			BufferedReader in = new BufferedReader(isr);
			String cmd = in.readLine();
			OutputStreamWriter osw =
					new OutputStreamWriter(socket.getOutputStream(), UTF_8);
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
					socket.close();
					
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
	public void sendFile(String filelocation, Socket servsock) {
		//Currently this closes the socket used by the server requiring
		//the client to reconnect or open a new socket
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			File sendFile = new File(filelocation);
			byte [] byteArray = new byte [(int) sendFile.length()];
			bis = new BufferedInputStream(
					new FileInputStream(sendFile));
			//findBugs gets mad that bis.read() isn't assigned
			//to a variable, so plchlder is used until
			//a better solution is found.
			int plchlder = bis.read(byteArray, 0, byteArray.length);
			System.out.println(plchlder);
			os = servsock.getOutputStream();
			os.write(byteArray, 0, byteArray.length);
			os.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (servsock != null) {
				try {
					servsock.close();
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
		//Counts how many Threads there are
		int numThrds = 1;
		//A while loop that currently runs forever that will constantly obtain
		//new connections and passing them to new threads.
		//Recycles variable names
		while (true) {
			System.out.println("Preparing thread " + numThrds);
			System.out.println("Waiting for connection...");
			try {
				//Obtain a Socket object
				Socket sock = serverSocket.accept();
				//Passes socket object to new server object
				JFilesServer jf = new JFilesServer(sock);
				//Create and start a new Thread object with server object
				Thread thread = new Thread(jf);
				thread.start();
				//Sleep the main thread so that status messages remain organized
				Thread.sleep(100);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Iterates the numThrds variable by 1
			numThrds++;
		}
	}

}
