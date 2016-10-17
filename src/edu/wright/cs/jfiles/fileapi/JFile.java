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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the Class of holding and managing the actual content of files. It is
 * use by the JFileManager class to manipulate files.
 * 
 * @author Brand Allred
 * @author Team 5
 *
 */
public class JFile implements Cloneable {

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
	 * Stores the given file and the arraylist of tags associated with the file.
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
	 * Deletes JFile's File contents. Eventually. Right now, nothing.
	 * 
	 */
	public void deleteContents() /* throws IOException */ {

		/* TODO: find out what is wanted from this method and implement.
		 * 
		 * 
		 * if (file.canWrite() == false) { return false; } fOut = new
		 * BufferedOutputStream(new FileOutputStream( file.getAbsoluteFile()));
		 * fOut.write(' '); fOut.flush(); fOut.close(); return true;
		 */
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
	 */
	public void getType() {
		/*
		 * if (System.getProperty("os.name").contains("Windows")) {
		 * System.out.println(Files.getFileExtension(file.getName())); }
		 */
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
	 * Clone method used to clone the JFile object.
	 */
	public Object clone() {
		return new JFile(this.file, this.tagList);
	}
}
