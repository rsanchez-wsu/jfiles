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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

	static final Logger logger = LogManager.getLogger(JFilesClient.class);
	private String host = "localhost";
	private int port = 9786;
	private static final String UTF_8 = "UTF-8";
	private boolean running = true;

	/**
	 * Handles allocating resources needed for the client.
	 * 
	 * @throws IOException
	 *             If there is a problem binding to the socket
	 */

	public JFilesClient() {
	}

	@Override
	public void run() {
		try (Socket socket = new Socket(host, port)) {
			while (running) {

				
				System.out.println("Send a command to the server.");
				System.out.println("FILE to receive file");
				System.out.println("SENDFILE to send file to server");
				System.out.println("LIST to receive server directory");
				
				/*
				 * InputStreamReader isr = new
				 * InputStreamReader(socket.getInputStream(), UTF_8);
				 * 
				 * //this is temp info on CheckSum /* File datafile = new
				 * File("AUTHORS");
				 * 
				 * MessageDigest checkFile = MessageDigest.getInstance("MD5");
				 * 
				 * @SuppressWarnings("resource") FileInputStream fileSent = new
				 * FileInputStream(datafile); //Creating a byte array so we can
				 * read the bytes of the file in chunks byte[] chunkOfBytes =
				 * new byte[(int) datafile.length()]; //used as the place holder
				 * for the array int startPoint = 0;
				 * 
				 * while ((startPoint = fileSent.read(chunkOfBytes)) != -1) {
				 * checkFile.update(chunkOfBytes, 0, startPoint); } //the
				 * finalized checksum byte[] checksum = checkFile.digest();
				 * System.out.print("Digest(in bytes):: "); for (int i = 0; i <
				 * checksum.length - 1 ; i++) { System.out.print(checksum[i] );
				 * } System.out.println();
				 */
				// BufferedReader in = new BufferedReader(isr);
				// Get user input
				@SuppressWarnings("resource")
				// Eclipse complained that kb wasn't being used. Not sure why.
				// kb input is used on the line after it is initialized
				// Overrode resource leak warning for now
				Scanner kb = new Scanner(System.in, UTF_8);
				// Enter input in format:
				// command (space) argument
				String line = kb.nextLine();
				// Splits the user input into an array of words separated by
				// spaces
				// switch statement for which command was entered
				String[] cmdary = line.split(" ");
				switch (cmdary[0]) {

				case "FILE":
					Thread thrd0 = new Thread(new Runnable() {
						@Override
						public void run() {
							fileCommand(cmdary[1], socket);
						}
					});
					thrd0.start();
					break;
				case "SENDFILE":
					Thread thrd1 = new Thread(new Runnable() {
						@Override
						public void run() {
							fileSendCommand(cmdary[1], socket);
						}
					});
					thrd1.start();
					break;
				case "EXIT":
				case "QUIT":
					running = false;
					Thread thrd2 = new Thread(new Runnable() {
						@Override
						public void run() {
							fileCommand(cmdary[0], socket);
						}
					});
					thrd2.start();
					thrd2.join();
					break;
				default:
					System.out.println("Not a valid command");
					break;

				}
			}
		} catch (UnknownHostException e) {
			logger.error("Could not connect to host at that address", e);
		} catch (IOException e) {
			logger.error("An error occured with the connection", e);
		} catch (InterruptedException e) {
			logger.error("A thread has been interrupted", e);
		}
	}

	/**
	 * Method for the FILE command. Downloads a file from the server and
	 * compares checksums to verify file.
	 * 
	 * @param file
	 *            name of file that needs to be sent
	 * @param sock
	 *            an active Socket object connected to server
	 */
	public void fileCommand(String file, Socket sock) {
		BufferedWriter bw = null;
		try {
			OutputStreamWriter osw = new OutputStreamWriter(sock.getOutputStream(), UTF_8);
			BufferedWriter out = new BufferedWriter(osw);
			out.write("FILE " + file + "\n");
			out.flush();
			if (!file.equals("QUIT") || !file.equals("EXIT")) {
				InputStreamReader isr = new InputStreamReader(sock.getInputStream(), UTF_8);
				BufferedReader br = new BufferedReader(isr);
				// Remove .txt from end of filename. Probably better way of
				// doing this.
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
			}
			
		} catch (IOException e) {
			logger.error("An error occurred while communicating with the server", e);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					logger.error("An error occurred while closing a stream", e);
				}
			}
		}
	}
	
	/**
	 * Method to handle what happens when user types "filesend." Program
	 * should get bytes from the specified file and put them on the output
	 * stream to the server.
	 * @param filepath
	 * 				  the location of the file to send
	 * @param sock
	 * 			  the active socket on which the server connection resides
	 */
	public void fileSendCommand(String filepath, Socket sock) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(filepath), "UTF-8"))) {
			OutputStreamWriter osw = new OutputStreamWriter(
					sock.getOutputStream(), UTF_8);
			BufferedWriter out = new BufferedWriter(osw);
			out.write("GETFILE " + filepath + "\n");
			out.flush();
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				out.write(line + "\n");
			}
			out.flush();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Method for producing a Checksum. Takes in a file type and converts it
	 * into an MD5 standard checksum which is returned in the form of a byte
	 * array.
	 * 
	 * @param file
	 *            the file to be digested into a checksum
	 * @return a byte array containing the processed file
	 */
	public byte[] getChecksum(File file) {
		// Initialize some variables
		byte[] checksum = null;
		FileInputStream fileSent = null;

		try {
			MessageDigest checkFile = MessageDigest.getInstance("MD5");
			// @SuppressWarnings("resource")
			fileSent = new FileInputStream(file);
			// Creating a byte array so we can read the bytes of the file in
			// chunks
			byte[] chunkOfBytes = new byte[(int) file.length()];
			// used as the place holder for the array
			int startPoint = 0;

			while ((startPoint = fileSent.read(chunkOfBytes)) != -1) {
				checkFile.update(chunkOfBytes, 0, startPoint);
			}
			// the finalized checksum
			checksum = checkFile.digest();
			System.out.print("Digest(in bytes):: ");
			for (int i = 0; i < checksum.length - 1; i++) {
				System.out.print(checksum[i]);
			}
			System.out.println();

		} catch (NoSuchAlgorithmException e) {
			//e.printStackTrace();
			logger.error("An error occurred while preparing checksum", e);
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			logger.error("File was not found", e);
		} catch (IOException e) {
			//e.printStackTrace();
			logger.error("An error occurred while reading file", e);
		} finally {
			if (fileSent != null) {
				try {
					fileSent.close();
				} catch (IOException e) {
					logger.error("An error occured while closing connection to file", e);
				}
			}
		}
		return checksum;
	}

	/**
	 * Method for comparing checksums. Takes two byte arrays containing checksum
	 * data and returns true if they are the same and false if they are not.
	 * 
	 * @param first
	 *            a byte array containing the first file's checksum
	 * @param second
	 *            a byte array containing the second file's checksum
	 * @return returns a boolean of true or false based on how they compare
	 */
	public boolean isSame(byte[] first, byte[] second) {
		// Initialize the boolean value
		boolean same = true;
		// If the lengths of the arrays are not the same then they are obviously
		// different
		// and the boolean can be changes to false before the for loop.
		if (first.length != second.length) {
			same = false;
		}
		// a shorted circuited AND allows the boolean value to control the for
		// loop
		for (int i = 0; same && i < first.length; i++) {
			if (first[i] != second[i]) {
				same = false;
			}
		}

		return same;
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
