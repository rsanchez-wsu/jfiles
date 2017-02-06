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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Scanner;

/**
 * The main class of the JFiles client application.
 * This method can use many threads for the same object. 
 * It's purpose is to try to find connections to the server.
 * @author Roberto C. Sánchez &lt;roberto.sanchez@wright.edu&gt;
 *
 */

// Implementing runnable for reusability and inheritance
public class JFilesClient implements Runnable {
//Variables to create host and port
//*READ ME*	
//This code could use better variable names
	static final Logger logger = LogManager.getLogger(JFilesClient.class);
	private static String host = "localhost";
	private static int port = 9786;
	private static final String UTF_8 = "UTF-8";
	
	/**
	 * This is a method for the class above.
	 * Handles allocating resources needed for the client.
	 * @throws IOException if there is a problem binding to the socket
	 */
	//Needs to be coded
	public JFilesClient() {
		
	}

	/**
	 * Handles allocating resources needed for the server.
	 * 
	 * @throws IOException
	 *If there is a problem binding to the socket
	 */
	private static void init() throws IOException {
		Properties prop = new Properties();
		FileInputStream fis = null;
		File config = null;	
		
		//Array of strings containing possible paths to check for config files
		String[] configPaths = {"$HOME/.jfiles/clientConfig.xml",
				"/usr/local/etc/jfiles/clientConfig.xml",
				"/opt/etc/jfiles/clientConfig.xml",
				"/etc/jfiles/clientConfig.xml",
				"%PROGRAMFILES%/jFiles/etc/clientConfig.xml",
				"%APPDATA%/jFiles/etc/clientConfig.xml"};
		
		//Checking location(s) for the config file;
		for (int i = 0; i < configPaths.length; i++) {
			if (new File(configPaths[i]).exists()) {
				config = new File(configPaths[i]);
				break;
			}
		}
		
		//Output location where the config file was found. Otherwise warn and use defaults.
		if (config == null) {	//No configuration	
			logger.info("No config file found. Using default values.");
		} else { //configuration found
			logger.info("Config file found in " + config.getPath());
			//Read file
			try {
				//Reads xmlfile into prop object as key value pairs
				fis = new FileInputStream(config);
				prop.loadFromXML(fis);			
			} catch (IOException e) { //found exception 
				logger.error("IOException occured when trying to access the server config", e);
			} finally { //close file
				if (fis != null) {
					fis.close();
				}
			}
		}
	
		//Add setters here. First value is the key name and second is the default value.
		//Default values are required as they are used if the config file cannot be found OR if
		// the config file doesn't contain the key.
		port = Integer.parseInt(prop.getProperty("port","9786"));
		logger.info("Config set to port " + port);
		
		host = prop.getProperty("host","localhost");
		logger.info("Config set max threads to " + host);		
	}

	/**
	 * Run is a method that is suppose to 
	 * communicate between the client and the server.
	 *
	 */
	
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
					//The finalized checksum
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
			/*Eclipse complained that kb wasn't being used, not sure why.
			kb input is used on the line after it is initialized.
			Overrode resource leak warning for now */
			
			//Create scanner for UTF_8
			Scanner kb = new Scanner(System.in, UTF_8);
			String line = kb.nextLine();
			
			//Splits the input from the file into an array separated by spaces.
			//Switch statement for which command was entered. 
			switch (line.trim()) {
			//If file was entered
			case "FILE": 
				fileCommand(line, socket);
				break;
				
			default: 
				System.out.println("Not a valid command");
				break;
			
			}
		} catch (UnknownHostException e) {
			logger.error("Unknown host exception was thrown", e);
		} catch (IOException e) {
			logger.error("An error has occurred", e);
		}  //catch ( NoSuchAlgorithmException e) {
			//logger.error("No such algorithm exception was thrown", e);
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
		//Creates a new file to write to
		try {
			OutputStreamWriter osw = new OutputStreamWriter(sock.getOutputStream(), UTF_8);
			BufferedWriter out = new BufferedWriter(osw);
			out.write(file + "\n" );
			out.flush();
			InputStreamReader isr = new InputStreamReader(sock.getInputStream(), UTF_8);
			BufferedReader br = new BufferedReader(isr);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("AUTHORS-COPY"));
			String line;
			//Whenever there's an output, write to the file.
			while ((line = br.readLine()) != null) { 
				System.out.println(line);
				bw.write(line + "\n" );
			}
			bw.close();
			
		} catch (IOException e) {
			logger.error("An error has occurred", e);
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
		//Initialize checksum and the file input stream.
		byte[] checksum = null;
		FileInputStream fileSent = null;
		
		try {
			MessageDigest checkFile = MessageDigest.getInstance("MD5");
			//@SuppressWarnings("resource")
			fileSent = new FileInputStream(file);
			// Creating a byte array so we can read the bytes of the file in chunks. 
			byte[] chunkOfBytes = new byte[(int) file.length()];
			// Used as the place holder for the array
			int startPoint = 0;
			//While there is a value inside the file, update.
			while ((startPoint = fileSent.read(chunkOfBytes)) != -1) { 
				checkFile.update(chunkOfBytes, 0, startPoint);
			}
			//The finalized checksum
			checksum = checkFile.digest();
			System.out.print("Digest(in bytes):: ");
			for (int i = 0; i < checksum.length - 1; i++) {
				System.out.print(checksum[i]);
			}
		//New line	
			System.out.println();

		} catch (NoSuchAlgorithmException e) {
			logger.error("No such algorithm exception was thrown.", e);;
		} catch (FileNotFoundException e) {
			logger.error("The file was unable to be found.", e);
		} catch (IOException e) {
			logger.error("An error has occurred", e);
		} finally {
			if (fileSent != null) {
				try {
					fileSent.close();
				} catch (IOException e) {
					logger.error("An IOException was thrown while trying to close fileSent", e);
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
		logger.info("Starting the client");
		try {
			init();
		} catch (IOException e) {
			logger.error("An error has occurred", e);
		}
		JFilesClient jf = new JFilesClient();

		Thread thread = new Thread(jf);
		thread.start();
	}

}
