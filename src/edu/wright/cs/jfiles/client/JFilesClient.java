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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

	private String host = "localhost";
	private int port = 9786;
	private static final String UTF_8 = "UTF-8";
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

			/*
			OutputStreamWriter osw =
					new OutputStreamWriter(socket.getOutputStream(), UTF_8);
					*/
			//BufferedWriter out = new BufferedWriter(osw);
			System.out.println("Send a command to the server.");
			System.out.println("FILE to receive file");
			System.out.println("LIST to receive server directory");
			//out.write("FILE\n");
			//out.flush();
			/*
			InputStreamReader isr =
					new InputStreamReader(socket.getInputStream(), UTF_8);

				//this is temp info on CheckSum
				/*
					File datafile = new File("AUTHORS");

					MessageDigest checkFile = MessageDigest.getInstance("MD5");
					@SuppressWarnings("resource")
					FileInputStream fileSent = new FileInputStream(datafile);
					//Creating a byte array so we can read the bytes of the file in chunks
					byte[] chunkOfBytes = new byte[(int) datafile.length()];
					//used as the place holder for the array
					int startPoint = 0;

					while ((startPoint = fileSent.read(chunkOfBytes)) != -1) {
						checkFile.update(chunkOfBytes, 0, startPoint);
					}
					//the finalized checksum
					byte[] checksum = checkFile.digest();
					System.out.print("Digest(in bytes):: ");
					for (int i = 0; i < checksum.length - 1 ; i++) {
						System.out.print(checksum[i] );
					}
					System.out.println();
				*/
			//BufferedReader in = new BufferedReader(isr);
			//Get user input
			@SuppressWarnings("resource")
			//Eclipse complained that kb wasn't being used. Not sure why.
			//kb input is used on the line after it is initialized
			//Overrode resource leak warning for now
			Scanner kb = new Scanner(System.in, UTF_8);
			//Enter input in format:
			//command (space) argument
			String line = kb.nextLine();
			//Splits the user input into an array of words separated by spaces
			//switch statement for which command was entered
			String [] cmdary = line.split(" ");
			switch (cmdary [0]) {
			
			case "FILE": 
				fileCommand(cmdary [1], socket);
				break;
				
			default: 
				System.out.println("Not a valid command");
				break;
			
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // catch ( NoSuchAlgorithmException e) {
			//e.printStackTrace();
		//} 
	}
		

		/** 
		 * Method for the FILE command.
		 * Downloads a file from the server and compares checksums to verify file.
		 * 
		 * @param file name of file that needs to be sent
		 * @param sock an active Socket object connected to server
		 */
	public void fileCommand(String file, Socket sock) {
		BufferedWriter bw = null;
		try {
			OutputStreamWriter osw = new OutputStreamWriter(sock.getOutputStream(), UTF_8);
			BufferedWriter out = new BufferedWriter(osw);
			out.write("FILE " + file + "\n" );
			out.flush();
			InputStreamReader isr = new InputStreamReader(sock.getInputStream(), UTF_8);
			BufferedReader br = new BufferedReader(isr);
			//Remove .txt from end of filename. Probably better way of doing this.
			int index = 0;
			while (file.charAt(index) != 46) {
				index++;
			}
			String newFile = file.substring(0, index) + "-copy.txt";
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(newFile), "UTF-8"));
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				bw.write(line + "\n" );
			}
			bw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/** 
	 * Method for producing a Checksum.
	 * Takes in a file type and converts it into an MD5 
	 * standard checksum which is returned in the form of a byte array.
	 * 
	 * @param file the file to be digested into a checksum
	 * @return a byte array containing the processed file
	 */
	public byte[] getChecksum(File file) {
		//Initialize some variables
		byte[] checksum = null;
		FileInputStream fileSent = null;
		
		try {
			MessageDigest checkFile = MessageDigest.getInstance("MD5");
			//@SuppressWarnings("resource")
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
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileSent != null) {
				try {
					fileSent.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return checksum;
	}
	
	/** 
	 * Method for comparing checksums.
	 * Takes two byte arrays containing checksum data and returns true if 
	 * they are the same and false if they are not.
	 * 
	 * @param first a byte array containing the first file's checksum
	 * @param second a byte array containing the second file's checksum
	 * @return returns a boolean of true or false based on how they compare
	 */
	public boolean isSame(byte[] first, byte[] second) {
		//Initialize the boolean value
		boolean same = true;
		//If the lengths of the arrays are not the same then they are obviously different
		//and the boolean can be changes to false before the for loop.
		if (first.length != second.length) {
			same = false;
		}
		//a shorted circuited AND allows the boolean value to control the for loop
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
