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

package edu.wright.cs.jfiles.fileapi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
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
		try {
			logger.info("Creating File");
			this.file = file;
			logger.info("File Created");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error creating a JFile from a File.", e);
		}
	}

	/**
	 * Stores the given file and the Map of tags associated with the file. Can
	 * be given any map type.
	 *
	 * @param file
	 *            The file being stored in the JFile object.
	 * @param tags
	 *            The tags associated with the file.
	 */
	public JFile(File file, Map<String, String> tags) {
		try {
			logger.info("Storing File and Arraylist of Tags Associated with the file");
			this.file = file;
			this.tagList = tags;
			logger.info("Storing Complete");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error creating a JFile from a File and a Map of tags.", e);
		}
	}

	/**
	 * Stores the file as given by the string.
	 *
	 * @param path
	 *            Path to wanted file.
	 */
	public JFile(String path) {
		try {
			logger.info("Storing to path " + path);
			logger.info("Stored to path " + path);
			this.file = new File(path);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error creating a JFile from a file path.", e);
		}
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
		if (System.getProperty("os.name").contains("Windows")) {
			return file.renameTo(new File(file.getParent() + "\\" + name));
		}

		return file.renameTo(new File(file.getParent() + "/" + name));

	}

	/**
	 * This method is so that the JFM can access the .renameTo() method to move
	 * the file into a different directory.
	 *
	 * <p>
	 * NOTE: the .renameTo() method in File objects cannot rename things across
	 * different filesystems. I (Brand) have tried to use the method to try to
	 * move it across drives on my windows machine. Both drives are NTFS, but
	 * the file that I used to test with DID NOT get put into the right place.
	 * The file instead sat in it's original directory.
	 * </p>
	 *
	 * <p>
	 * Note: the JFM handles if the given string is a directory or not, int the
	 * paste method.
	 * </p>
	 *
	 * @param directory
	 *            directory in which the file moves to.
	 */
	protected boolean moveTo(String directory) {
		if (System.getProperty("os.name").contains("Windows")) {
			return file.renameTo(new File(directory + "\\" + file.getName()));
		} else {
			return file.renameTo(new File(directory + "/" + file.getName()));
		}

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
	 * This function is so that the name of the file is easily retrievable.
	 *
	 * @return The name of the current file.
	 */
	public String getName() {
		return file.getName();
	}

	/**
	 * Gets the absolute path of the file and returns a Path object.
	 *
	 * @return the absolute path of the file and gives it back as a Path object.
	 */
	public Path getPath() {
		return file.toPath();
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

				return file.getName().substring(file.getName().lastIndexOf('.') + 1,
						file.getName().length());

			} else if (System.getProperty("os.name").contains("Macintosh")) {
				// TODO Determine how files are defined and how to get the file
				// type on Mac.

				logger.info("OS determined to be \"Macintosh\".");

			} else if (System.getProperty("os.name").contains("Linux")) {

				// TODO Get the file type from linux.

				logger.info("OS determined to be \"Linux\".");

				if (file.getName().substring(file.getName().indexOf('.') + 1).compareTo("") > 0) {

					logger.info("file type gotten from end of file.");

					return file.getName().substring(file.getName().indexOf('.') + 1);
				} else {
					// This needs to be tested. This was done to ensure that the UTF-8 encoding
					// paradigm is used.
					InputStreamReader osw = new InputStreamReader(new FileInputStream(file),
							"UTF-8");
					try (BufferedReader in = new BufferedReader(osw)) {
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
		} catch (RuntimeException e) {
			throw e; // Needs to be done to appease FindBugs
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error getting type.", e);
		}
		// temporary until all ifs get a return.
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
					// This if/then statement exists so that the result of
					// delete() is used to appease FindBugs
					if (fileArr.get(location).delete()) {
						logger.info("File deleted.");
					} else {
						logger.warn("File not deleted!");
					}
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
			return file.delete();
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
	 * This method passes the File.list() method to the JFile so that other
	 * teams that may have used this method in scripting can more easily convert
	 * to JFile objects.
	 *
	 * <p>
	 * It functions as closely to the File.list() method as possible, to ensure
	 * easy transition. It returns null if there's an error or if this method
	 * is called on something that is not a directory. It returns an array with
	 * 0 elements if it is called on a folder that is empty.
	 * </p>
	 *
	 * @return A string list of the abstract names of the contents of the JFile.
	 */
	public String[] list() {
		logger.info("Attempting to list the abstract names of " + getPath());
		try {
			logger.info("Checking to see if the file is a directory.");
			if (file.isDirectory()) {
				logger.info("File was determined to be a directory.\n" + "   Returning list...");
				return file.list();
			} else {
				logger.warn("File was determined to be a file; system attempted to"
						+ "call list method on a file.\nReturning null value.");
				return null; // Null indicates a non-directory
			}
		} catch (Exception e) {
			logger.error("System caught an exception while attempting to list file contents.",
					e);
			return null; // Null indicates an error
		}
	}

	/**
	 * This method returns the contents of the JFile as JFiles. Since the File
	 * command is not friendly to this, and doesn't even have a nice method for
	 * listing the absolute file paths, we have to recreate this functionality.
	 *
	 * <p>
	 * To this end, we have to first turn the abstract path names to absolute.
	 * Then, we have to use those to make JFiles. In order to make the abstract
	 * path names absolute, we also have to determine what separator to use, so
	 * we have to determine what OS we're in.
	 * </p>
	 * <p>
	 * Similarly to the list method, this method returns an array of 0 elements
	 * if the method is called on a folder with no contents and null if the
	 * method is either called on something that is not a directory or if it
	 * encounters an error.
	 * </p>
	 */
	public JFile[] getContents() {
		try {
			if (file.isDirectory()) {
				String[] fileNames = file.list();
				// This if-then statement has to be kept to appease
				// FindBugs. It is concerned that file.list() may
				// return null, even though this is actually covered
				// by the outer if-then statement in theory.
				if (fileNames != null) {
					JFile[] output = new JFile[fileNames.length];
					String separator = "";
					if (System.getProperty("os.name").contains("Windows")) {
						logger.info("OS determined to be \"Windows\".");
						separator = "\\";
					} else if (System.getProperty("os.name").contains("Macintosh")) {
						logger.info("OS determined to be \"Macintosh\".");
						// TODO: Determine separator for Mac.
						separator = "";
					} else if (System.getProperty("os.name").contains("Linux")) {
						logger.info("OS determined to be \"Linux\".");
						// TODO: Determine separator for Linux.
						separator = "";
					} else {
						// TODO ??? Error maybe?
						logger.info("OS determined to be \"Unknown\".");
					}
					for (int i = 0; i < fileNames.length; i++) {
						String temp = file.getAbsolutePath() + separator + fileNames[i];
						output[i] = new JFile(temp);
					}
					// TODO: Add logging methods.
					return output;
				} else {
					// TODO: Add logging methods.
					return new JFile[0]; // Empty array indicates it is a folder, but is empty
				}

			} else {
				// TODO: Add logging methods.
				return null; // Null indicates a non-directory
			}
		} catch (RuntimeException e) {
			throw e; // Necessary for FindBugs
		} catch (Exception e) {
			// TODO: Add logging methods.
			return null; // Null indicates error
		}
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
			logger.error("Clone call on JFile caught a Clone Not Supported Exception.", e1);
			logger.info("Trying to create a clone a different way...");
			System.err.println("Clone call on JFile caught a Clone Not Supported Exception.");
			e1.printStackTrace();
			System.out.println("Trying to clone the JFile a different way.");
			try {
				output = new JFile(new File(file.getAbsolutePath()), new HashMap<>(tagList));
				logger.info("Clone successfully made.");
				return output;
			} catch (Exception e2) {
				logger.error("Clone has caught a second error. Returning null.", e2);
				System.err.println("Clone has caught a second error. Returning null.");
				e2.printStackTrace();
				return null;
			}
		}
	}
}
