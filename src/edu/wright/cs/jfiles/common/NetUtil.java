package edu.wright.cs.jfiles.common;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.wright.cs.jfiles.server.JFilesServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NetUtil {
	static final Logger logger = LogManager.getLogger(NetUtil.class); 

	/** 
	 * Method for producing a Checksum.
	 * Takes in a file type and converts it into an MD5 
	 * standard checksum which is returned in the form of a byte array.
	 * 
	 * @param file the file to be digested into a checksum
	 * @return a byte array containing the processed file
	 */

	
	public String getChecksum(File fileToCheck) {
		// Initialize some variables
		byte[] checksum = null;
		FileInputStream fileSent = null;
		
		try {
			MessageDigest checkFile = MessageDigest.getInstance("MD5");
			// @SuppressWarnings("resource")
			fileSent = new FileInputStream(fileToCheck);
			// Creating a byte array so we can read the bytes of the file in
			// chunks
			byte[] chunkOfBytes = new byte[(int) fileToCheck.length()];
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
		String returnSum = new String(checksum);
		return returnSum;
	}

}
