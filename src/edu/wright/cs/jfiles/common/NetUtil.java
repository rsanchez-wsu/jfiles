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

package edu.wright.cs.jfiles.common;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The Network Util class, to be used by both client and server.
 */
public class NetUtil {
	static final Logger logger = LogManager.getLogger(NetUtil.class); 

	/** 
	 * Method for producing a Checksum.
	 * Takes in a file type and converts it into an MD5 
	 * standard checksum which is returned in the form of a byte array.
	 * 
	 * @return String 
	 */

	
	public String getChecksum(File fileToCheck) {
		// Initialize some variables
		byte[] checksum = null;
		FileInputStream fileSent = null;
		String returnSum = "";
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

			returnSum = new String(checksum);
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
		return returnSum;
	}

}
