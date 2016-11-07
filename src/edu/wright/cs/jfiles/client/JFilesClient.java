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

package edu.wright.cs.jfiles.client;

import edu.wright.cs.jfiles.common.NetUtil;
import edu.wright.cs.jfiles.socketmanagement.SocketManager;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * The main class of the JFiles client application.
 * 
 * @author Roberto C. Sánchez &lt;roberto.sanchez@wright.edu&gt;
 *
 */
public class JFilesClient implements Runnable {

	//static final Logger logger = LogManager.getLogger(JFilesClient.class);
	private String host = "localhost";
	private int port = 9786;
	private static final String UTF_8 = "UTF-8";
	private boolean running = true;
	static volatile String serverCommand = null;
	private SocketManager sockMan;
	private Scanner kb;
	NetUtil util = new NetUtil();

	/**
	 * No argument constructor.
	 */
	public JFilesClient() {
	}

	@Override
	public void run() {
		try (Socket socket = new Socket(host, port)) {
			sockMan = new SocketManager(socket);
			kb = new Scanner(System.in);
			Thread keyboard = new Thread(new Runnable() {

				@Override
				public void run() {
					while (running) {
						serverCommand = kb.nextLine();
					}
				}

			});
			keyboard.setDaemon(true);
			keyboard.start();
			Thread internalCommand = new Thread(new Runnable() {

				@Override
				public void run() {
					while (running) {
						serverCommand = sockMan.getCommandInput();
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}

			});
			internalCommand.setDaemon(true);
			internalCommand.start();

			System.out.println("Send a command to the server.");
			System.out.println("FILE to receive file");
			System.out.println("SENDFILE to send file to server");
			System.out.println("LIST to receive server directory \n");
			
			while (running) {
				
				// Read in command as: "<COMMAND> <FILE>"
				//kb = new Scanner(System.in, UTF_8);
				//String line = kb.nextLine();
				String line = serverCommand;
				if (line == null) {
					continue;
				}
				
				// Parse command and convert to upper case
				String[] cmdary = line.split(" ");
				String commandInput = cmdary[0].toUpperCase();
				
				// Find which command was entered
				switch (commandInput) {
				case "FILE":
					//cmdary[1] = getFileName(0);
					Thread thrd0 = new Thread(new Runnable() {
						@Override
						public void run() {
							//fileCommand(cmdary[1], socket);
							sockMan.sendCommand(cmdary[0] + " " + cmdary[1]);
						}
					});
					thrd0.start();
					break;
				case "SENDFILE":
					cmdary[1] = getFileName(1);
					Thread thrd1 = new Thread(new Runnable() {
						@Override
						public void run() {
							fileSendCommand(cmdary[1], socket);
						}
					});
					thrd1.start();
					break;
				case "REC_FILE":
					int identifier = Integer.parseInt(cmdary[1]);
					String filename = cmdary[2];
					File file = null;
					while (file == null) {
						file = sockMan.getFile(identifier);
					}
					file.renameTo(new File("copy-" + filename));
					break;
				case "LIST":
					System.out.println("**List of files**");
					break;
				case "EXIT":
				case "QUIT":
					running = false;
					Thread thrd2 = new Thread(new Runnable() {
						@Override
						public void run() {
							//fileCommand(cmdary[0], socket);
							sockMan.sendCommand(cmdary[0]);
						}
					});
					thrd2.start();
					thrd2.join();
					sockMan.close();
					break;
				default:
					System.out.println("Not a valid command");
					break;
				}
				commandInput = null;
			}
		} catch (UnknownHostException e) {
		//	logger.error("Could not connect to host at that address", e);
		} catch (IOException e) {
		//	logger.error("An error occured with the connection", e);
		} catch (InterruptedException e) {
		//	logger.error("A thread has been interrupted", e);
		}
	}

	/**
	 * Method for the FILE command. Downloads a file from the server and
	 * compares checksums to verify file.
	 * 
	 * @param file name of file that needs to be sent
	 * @param sock an active Socket object connected to server
	 */
	public void fileCommand(String file, Socket sock) {
		BufferedWriter bw = null;
		try {
			// Sends FILE command to output stream
			OutputStreamWriter osw = new OutputStreamWriter(sock.getOutputStream(), UTF_8);
			BufferedWriter out = new BufferedWriter(osw);
			out.write("FILE " + file + "\n");
			out.flush();
			
			// Reads in file from server
			if (!file.equals("QUIT") || !file.equals("EXIT")) {
				InputStreamReader isr = new InputStreamReader(sock.getInputStream(), UTF_8);
				BufferedReader br = new BufferedReader(isr);
				// Remove .txt from end of filename. Probably better way of doing this.
				int index = 0;
				while (file.charAt(index) != 46) {
					index++;
				}
				String newFile = file.substring(0, index) + "-copy.txt";
				bw = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(newFile), "UTF-8"));
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
					//Logger.error("An error occured in sending the file");
				}
			}
			
		} catch (IOException e) {
			//logger.error("An error occurred while communicating with the server", e);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					//logger.error("An error occurred while closing a stream", e);
				}
			}
		}
	}
	
	/**
	 * Method to handle FILESEND command. Read bytes from file and sends to output stream.
	 * @param filepath the location of the file to send
	 * @param sock the active socket on which the server connection resides
	 */
	public void fileSendCommand(String filepath, Socket sock) {
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(filepath), "UTF-8"))) {
			
			// Send GETFILE command to output stream
			OutputStreamWriter osw = new OutputStreamWriter(sock.getOutputStream(), UTF_8);
			BufferedWriter out = new BufferedWriter(osw); 
			out.write("GETFILE " + filepath + "\n");
			out.flush();
			
			// Read response from server and log it
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				out.write(line + "\n");
			}
			out.flush();
			
			File clientFile = new File(filepath);
			String sendCheck = util.getChecksum(clientFile);
			out.write(sendCheck + "\n" );
			out.flush();
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method gets the name of a file that the user wants to
	 *  receive from the server or send to the server. This method
	 * also makes sure that the file name is vaild.
	 * TODO: make sure input is vaild 
	 * @param mode if mode = 0 gets the name of the file that is to be received from the server 
	 * 			   if mode = 1 gets the name of the file that is to be sent to the server
	 * 
	 * @return the name of file 
	 */
	public String getFileName(int mode) {
		String filename = "";
		Boolean getInput = true;
		kb = new Scanner(System.in, UTF_8);
	
		while (getInput) {
			
			switch (mode) {
			
			case 0:
				System.out.println("What file would you like to receive from the server?");
				filename = kb.nextLine();
				getInput = false;
				break;
			case 1:
				System.out.println("What file would you like to send to the server?");
				filename = kb.nextLine();
				getInput = false;
				break;
			default:
				getInput = false;
				break;
			}
		}
		return filename;
	}
	/**
	 * The main entry point to the program.
	 * 
	 * @param args
	 *            The command-line arguments
	 */

	public static void main(String[] args) {
		System.out.println("Starting the client");
		JFilesClient jf = new JFilesClient();

		Thread thread = new Thread(jf);
		thread.start();
	}

}
