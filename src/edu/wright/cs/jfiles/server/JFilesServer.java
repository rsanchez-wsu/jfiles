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

import edu.wright.cs.jfiles.common.NetUtil;
import edu.wright.cs.jfiles.socketmanagement.SocketManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

	static final Logger logger = LogManager.getLogger(JFilesServer.class);
	private static final int PORT = 9786;
	private static ServerSocket serverSocket;
	private static final String UTF_8 = "UTF-8";
	private Socket socket;
	private boolean running = true;
	NetUtil util = new NetUtil();
	private SocketManager sockMan = null;
	
	static {
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			logger.error("Some error occurred", e);
		}
	}

	/**
	 * Handles allocating resources needed for the server.
	 * 
	 * @throws IOException If there is a problem binding to the socket
	 */
	public JFilesServer(Socket sock) {
		socket = sock;
		sockMan = new SocketManager(socket);
	}

	@Override
	public void run() {
		try {
			while (running) {
				//String cmd = in.readLine();
				String cmd = sockMan.getCommandInput();
				if (cmd != null) {
					//cmdary splits up cmd to distinguish command
					//from arguments
					//cmdary[0] is command
					//cmdary[1] is argument
					String [] cmdary = cmd.split(" ");
					//Switch statement replaces if-else structure
					//Match command to one of these cases and execute
					//accordingly
					switch (cmdary [0]) {
					//Client wants a file from the server
					//Send file to client
					case "SENDFILE":
						sockMan.sendFile(new File(cmdary[1]));
						break;
					//Client wants to send file to server
					//Prepare to receive file from client
					case "GETFILE":
						Thread thrd0 = new Thread(new Runnable() {
							@Override
							public void run() {
								sockMan.sendCommand("SENDFILE" + " " + cmdary[1]);
							}
						});
						thrd0.start();
						break;
					//Used by the socket manager to notify server of incoming file.
					case "REC_FILE":
						int identifier = Integer.parseInt(cmdary[1]);
						String filename = cmdary[2];
						File file = null;
						while (file == null) {
							file = sockMan.getFile(identifier);
						}
						file.renameTo(new File("copy-" + filename));
						break;
					//List command existed in repository's initial state
					//May be obsolete
					case "LIST":
						break;
					//Why do we have two different exit commands?
					case "EXIT":
					case "QUIT":
						running = false;
						sockMan.close();
						socket.close();
						break;

					default:
						System.out.println("Invalid Command.");
						break;
					}
				}
			}
		} catch (IOException e) {
			logger.error("An error has occurred", e);
		} 
	}
	
	/**
	 * When FILE command is received from client, server calls this method
	 * to handle file transfer.
	 * 
	 * @param servsock the socket where the server connection resides
	 */
	public void sendFile(String file, Socket servsock) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "UTF-8"))) {
			OutputStreamWriter osw = new OutputStreamWriter(servsock.getOutputStream(), UTF_8);
			BufferedWriter out = new BufferedWriter(osw);
			String line;
			
			@SuppressWarnings("unused")	//to be implemented
			String checksum = util.getChecksum(new File(file));
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				out.write(line + "\n");
			}
			out.flush();
			
			//sending Checksum
			File serverFile = new File(file);
			String sendCheck = util.getChecksum(serverFile);
			out.write(sendCheck + "\n" );
			out.flush();
			
		} catch (IOException e) {
			logger.error("Sending file error", e);
		}	
	}
	
	/**
	 * Handles the transfer of a file from client to server.
	 * @param file
	 * 			  filename of received file
	 * @param sock
	 * 			  socket with active connection
	 */
	public void getFile(String file, Socket sock) {
		BufferedWriter bw = null;
		try {
			InputStreamReader isr = new InputStreamReader(sock.getInputStream(), UTF_8);
			BufferedReader br = new BufferedReader(isr);
			// Remove .txt from end of filename. Probably better way of
			// doing this.
			//46 is ASCII value of "."
			int index = 0;
			while (file.charAt(index) != 46) {
				index++;
			}
			String newFile = file.substring(0, index) + "-copy.txt";
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile), "UTF-8"));
			String line;
			
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				bw.write(line + "\n");
			}
			
			//receive checkSum
			String sentCheck = br.readLine();
			System.out.println(line);
			
			File copiedFile = new File(newFile);
			String checkNewFile = util.getChecksum(copiedFile);
			
			if (checkNewFile.equalsIgnoreCase(sentCheck)) {
				System.out.println("An error occured in sending the file");
				logger.error("An error occured in sending the file");
			}
			System.out.println("File received.");
		} catch (IOException e) {
			logger.error("An error occurred while communicating with the client", e);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * The main entry point to the program.
	 * 
	 * @throws IOException
	 *             If there is a problem binding to the socket
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
				//Thread.sleep(2);
				System.out.println("Received connection from" 
						+ sock.getRemoteSocketAddress());
			} catch (IOException e) {
				logger.error("An error occurred while connecting to client", e);
			}
			//Iterates the numThrds variable by 1
			numThrds++;
		}
	}

}
