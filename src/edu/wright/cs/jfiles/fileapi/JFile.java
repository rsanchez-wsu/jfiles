/*
 * Copyright (C) 2016 - WSU CEG3120 Students
 * 
 * Roberto C. Sánchez <roberto.sanchez@wright.edu>
 * John T. Wintersohle II <Dorkatron199@aols.com>
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

package edu.wright.cs.jfiles.fileapi;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the Class of holding and managing the actual content of files. It is
 * use by the JFileManager class to manipulate files.
 * 
 * @author Brand Allred
 * @author Team 5
 *
 */
public class JFile implements Cloneable, Serializable {

	/**
	 * Default serialization ID.
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger(JFile.class.getName());
	private File file;
	private Map<String, String> tagList = new HashMap<>();

	/**
	 * Default constructor.
	 */
	public JFile() {
	}

	/**
	 * This is the file for storing files in the JFile object.
	 * 
	 * @param file
	 *            The file being stored in the JFile object.
	 */
	public JFile(File file) {
		logger.info("Creating File");
		this.file = file;
		logger.info("File Created");
		logger.error("Error Creating File");
	}

	/**
	 * Stores the given file and the Map of tags associated with the file.
	 * Can be given any map type.
	 * 
	 * @param file
	 *            The file being stored in the JFile object.
	 * @param tags
	 *            The tags associated with the file.
	 */
	public JFile(File file, Map<String, String> tags) {
		logger.info("Storing File and Arraylist of Tags Associated with the file");
		logger.info("Storing Complete");
		this.file = file;
		this.tagList = tags;
		logger.error("Error Storing File and Arraylist");
	}

	/**
	 * Stores the file as given by the string.
	 * 
	 * @param path
	 *            Path to wanted file.
	 */
	public JFile(String path) {
		logger.info("Storing to path " + path);
		logger.info("Stored to path " + path);
		this.file = new File(path);
		logger.error("Error Storing to path " + path);
	}

	/**
	 * Renames the file.
	 * 
	 * @param name
	 *            The new name of the file.
	 * @return true is the name given doesn't have any illegal characters, false
	 *         if it does.
	 */
	public boolean rename(String name) {

		logger.info("Renaming " + name);
		if (name.contains("/") || name.contains("\\")) {
			logger.error("Error Renaming " + name);
			return false;
		}

		logger.info("Successful rename of " + name);
		return file.renameTo(new File(file.getParent() + name));

	}

	/**
	 * Tests whether or not the given key value is within the map
	 * 
	 * @param key
	 *            The given key to look for.
	 * @return True if the key is in the map, false if it not.
	 */
	public boolean hasTagkey(String key) {
		if (tagList.containsKey(key)) {
			return true;
		}
		return false;
	}

	/**
	 * Tests whether or not the given value is within the map
	 * 
	 * @param value
	 *            The given value to look for.
	 * @return True if the given value is in the map, false if it is not.
	 */
	public boolean hasTagValue(String value) {
		if (tagList.containsValue(value)) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the value stored at the key location from the map.
	 * 
	 * @param key
	 *            The given key for the key value pair.
	 * @return The value from the key value pair.
	 */
	public String getTag(String key) {
		return tagList.get(key);
	}

	/**
	 * Gives the file a key value pair to put into the map.
	 * 
	 * @param key
	 *            String to identify the value with
	 * @param value
	 *            the value to store
	 */
	public void setTag(String key, String value) {
		tagList.put(key, value);
	}

	/**
	 * Gets the absolute path of the file.
	 * 
	 * @return the absolute path of the file.
	 */
	public String getPath() {
		return file.getAbsolutePath();
	}
	
	/**
	 * Takes away the tag specified by a string.
	 * 
	 * @param key
	 *            String to find the tag with.
	 * @return True if there is a similar tag, false otherwise.
	 */
	public boolean revokeTag(String key) {
		tagList.remove(key);
		if (!tagList.containsKey(key)) {
			return true;
		}
		return false;
	}

	/**
	 * gets the file's type. TODO: getType()
	 * 
	 * @return String of what the file is. Example: JFile.java returns java
	 */
	public String getType() {
		logger.info("determining file type");
		try {
			if (System.getProperty("os.name").contains("Windows")) {
				
				logger.info("OS determined to be \"Windows\".");
				
				return file.getName().substring(
						file.getName().lastIndexOf('.') + 1,
						file.getName().length());
				
			} else if (System.getProperty("os.name").contains("Macintosh")) {
				// TODO Determine how files are defined and how to get the file type on Mac.
				
				logger.info("OS determined to be \"Macintosh\".");
				
			} else if (System.getProperty("os.name").contains("Linux")) {
				
				// TODO Get the file type from linux.
				
				logger.info("OS determined to be \"Linux\".");
				
				if ( file.getName().substring(
						file.getName().indexOf('.') + 1)
						.compareTo("") > 0 
					) {
					
					logger.info("file type gotten from end of file.");
					
					return file.getName().substring(
							file.getName().indexOf('.') + 1
							);
				} else {
					try (BufferedReader in = new BufferedReader(new FileReader(file))) {
						String tmp = in.readLine();
						
						if (tmp != null && tmp.contains("#!")) {
							
							logger.info("File type gotten from within the file.");
							
							return tmp.substring(tmp.lastIndexOf('/'));
							
						}
						
					}
					
					logger.info("Couldn't find file type.");
					
					return null;
				}
				
			} else {
				
				// TODO ??? Error maybe?
				
				logger.info("OS determined to be \"Unknown\".");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error getting type.");
			logger.error(e.getStackTrace());
		}
		//temporary until all ifs get a return.
		return null;
	}

	/**
	 * Gets the raw size of the current file.
	 * 
	 * @return A long that represents the raw size of the file.
	 */
	public long getFileSize() {
		return file.length();
	}

	/**
	 * Determines whether the file is a directory.
	 * 
	 * @return Try if he file in this JFile object is a directory; false is not.
	 */
	public boolean isDirectory() {
		return file.isDirectory();
	}

	/**
	 * Determines whether the file is a file.
	 * 
	 * @return Try if he file in this JFile object is a file; false is not.
	 */
	public boolean isFile() {
		return file.isFile();
	}

	/**
	 * Gets the current working directory is the current file is a directory.
	 * <p>
	 * Variable out gets cleared and then stores the JFiles made from the
	 * current working directory, if the current file is a directory.
	 * </p>
	 * 
	 * @param out
	 *            List of JFile objects returned if method returns true. Is also
	 *            cleared first before the new JFiles are stored.
	 * @return True, if the current file is a directory, and stores the files in
	 *         the directory into the given list. False otherwise.
	 */
	public boolean getWorkingDirectory(List<JFile> out) {

		if (file.isFile()) {
			return false;
		}
		
		out.clear();

		File[] fileList = file.listFiles();
		if (fileList != null) {
			for (int i = 0; i < fileList.length; i++) {
				out.add(new JFile(fileList[i]));
			} 
		}
		return true;

	}
	
	/**
	 * Deletes the actual file contents. Will loop through and delete files if
	 * the given file is actually a folder that has content in it.
	 * 
	 * <p>
	 * TODO: Revise method so that there is less complexity.
	 * </p>
	 * 
	 * <b>Meant to be used with a JFM</b>
	 * 
	 * @return true if the file was able to be deleted; false otherwise.
	 */
	protected boolean deleteContents() {
		
		// Since dirs cannot be deleted if they
		// are holding something else,
		// files and dirs have to be handled differently.
		
		if (file.isDirectory()) {
		
			// Arraylist to hold the dirs/files that need to be deleted.
			ArrayList<File> fileArr = new ArrayList<>();
			
			// int to note which fir/object we are looking at.
			int location = 0;
			
			// load the file we overall want to be deleted.
			fileArr.add(file);
			
			// Move through array until the array is empty.
			while (fileArr.size() != 0) {

				if (fileArr.get(location) == null) {
					fileArr.remove(location);
					continue;
				}

				// detect if the file object is a dir
				if (fileArr.get(location).isDirectory()) {
					
					// attempt to delete the dir
					if (fileArr.get(location).delete()) {

						// if deleted, move where you're looking at back.
						fileArr.remove(location);
						location--;

						// if it cannot be deleted, get its values and move on.
					} else {

						File[] fileList = fileArr.get(location).listFiles();
						if (fileList != null) {
							fileArr.addAll(Arrays.asList(fileList));
						}

					}
					// if it is a file, delete it, and move where you are
					// looking at.
				} else {

					fileArr.get(location).delete();
					fileArr.remove(location);
					location--;

				}

				// increment where you're looking at
				// if there are less things then where you're looking at
				// move back to the start
				location++;
				if (location >= fileArr.size()) {
					location = 0;
				}
				
			}

		} else {
			file.delete();
		}

		return true;
	}

	/**
	 * Tells whether the file is hidden or not. The method in File is OS-aware,
	 * so this method is inherently OS-aware.
	 * 
	 * @return true if the file is hidden; false otherwise
	 */
	public boolean isHidden() {
		return file.isHidden();
	}

	/**
	 * Returns a deep copy of the JFile being clones.
	 * 
	 */
	@Override
	public JFile clone() {
		JFile output;
		logger.info("Creating a clone of a JFile.");
		try {
			output = (JFile) super.clone();
			output.file = new File(file.getAbsolutePath());
			output.tagList = new HashMap<>(tagList);
			return output;
		} catch (CloneNotSupportedException e1) {
			logger.error("Clone call on JFile caught a Clone Not Supported Exception.");
			logger.error(e1.getStackTrace());
			logger.info("Trying to create a clone a different way...");
			System.err.println("Clone call on JFile caught a Clone Not Supported Exception.");
			e1.printStackTrace();
			System.out.println("Trying to clone the JFile a different way.");
			try {
				output = new JFile(new File(file.getAbsolutePath()),
						new HashMap<>(tagList));
				logger.info("Clone successfully made.");
				return output;
			} catch (Exception e2) {
				logger.error("Clone has caught a second error. Returning null.");
				logger.error(e2.getStackTrace());
				System.err.println("Clone has caught a second error. Returning null.");
				e2.printStackTrace();
				return null;
			}
		}
	}
}
