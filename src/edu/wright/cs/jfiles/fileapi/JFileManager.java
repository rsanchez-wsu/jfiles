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

import java.util.Arrays;


/**
 * <p>
 * This is the class that provides functionality for managing JFile objects.
 * Examples of things this class is in charge of will be copying, deleting,
 * moving, renaming, and pasting JFiles.
 * </p>
 * 
 * <p>
 * It is the goal of this class to work as a medium of communication between
 * <b>security</b> (such as authentication, authorization, and accounting),
 * <b>maintenance</b> (such as logging), <b>user interfaces</b> (such as GUIs,
 * CLIs, and searching algorithms), and <b>JFiles</b>, by incorporating the
 * methods of all of these other APIs into the File API of JFiles.
 * </p>
 * 
 * <p>
 * In other words, this exist to both keep the duties of front-end and back-end
 * APIs separate by being a middle-ground for them to communicate. This also
 * means that the system can be more modular. For example, if a team wants to
 * create a new GUI, this system should be able to accommodate that. Likewise,
 * the same could be said for a different security API.
 * </p>
 * 
 * <p>
 * This also exists to separate the methods involved with manipulating JFiles
 * from an external point of view from JFiles themselves. This is done for both
 * logical and functional reasons. It may not make sense for a JFile to copy,
 * delete, or move itself. Creating the commands in this way also means that
 * this system can be modular.
 * </p>
 * 
 * <p>
 * It is also important to note that this method does not hold JFiles. If there
 * is a need for such an object, a JFolder object will be made. This method is
 * not intended for instantiation, and, as a result, has no constructors. This
 * method just provides functions to unify different APIs.
 * </p>
 * 
 * @author <b>Team 5:</b>
 * @author John Wintersohle II
 *         <<a href="mailto:Dorkatron199@aol.com">Dorkatron199@aol.com</a>>
 *
 */
public class JFileManager {
	
	static final Logger logger = LogManager.getLogger(JFileManager.class.getName());
	
	// This is suppressed until we build in the functionality for this field.
	// This will most likely be when we make the paste() method functional.
	/**
	 * This is the clipboard used when copying JFiles. It is capable of copying
	 * and pasting multiple JFiles at a time. It is made private so that it can
	 * only be accessed via the JFileManager methods for the sake of security.
	 * 
	 */
	@SuppressWarnings("unused")
	private JFile[] clipboard;

	/*
	 * This method entirely depends on copy and delete. It is pretty much done.
	 */
	/**
	 * <p>
	 * Cuts the file passed in.
	 * </p>
	 * 
	 * <p>
	 * This method calls copy then delete on the file being passed in. Copies
	 * the files selected to the clipboard then moves the files to the trash
	 * directory set up during configuration.
	 * </p>
	 * 
	 * @param files
	 *            The files being cut.
	 */
	public void cut(JFile[] files) {
		logger.info("Copying Files");
		copy(files);
		logger.info("Copy of " + Arrays.toString(files) + "Successful");
		logger.error("Error Copying Files"); // If error occurs
		logger.info("Deleting Contents");
		delete(files);
		logger.info("Delete of " + Arrays.toString(files) + " Successful");
		logger.error("Error Deleting Files"); // If error occurs
	}

	/*
	 * This needs to be made into a deep copy so that the contents of the
	 * clipboard can still be accessed even of the user later deletes the files.
	 * I will work on this later.
	 * 
	 * Though the clone method is used, I am not happy until its exact
	 * functionality is defined in JFile...
	 */

	/**
	 * This method copies all of the files passed in to the clipboard.
	 * 
	 * @param files
	 *            The files being copied to the clipboard.
	 * 
	 */
	public void copy(JFile[] files) {
		logger.info("Copying");
		clipboard = files.clone();
		logger.info("Copy of " + Arrays.toString(files) + " Successful");
		logger.error("Error Copying Files");
	}

	/*
	 * Collaborate with team 1 on this method. They are developing a way to
	 * transfer objects from one machine to another. This is not necessary for a
	 * single machine, but should be built for network pasting from the start.
	 */
	/**
	 * This method will copy the contents of the clipboard into the directory at
	 * a specified location name.
	 * 
	 * @param dirName
	 *            The name of the directory being pasted to.
	 */
	public void paste(String dirName) {
		
		logger.info("Pasting File");
		logger.info("Successful Past of " + dirName);
		logger.error("Error Pasting");

	}

	/**
	 * This is a version of paste for use in the move method. This version of
	 * paste is independent of the clipboard, which is important because we want
	 * the move method to be independent of the clipboard because the use may
	 * not expect the move command to mess with the clipboard.
	 * 
	 * <p>
	 * This method should be used when files need to be moved from one location
	 * to another without affecting the clipboard. It is important to note that
	 * this method should only duplicate the files to the specified location, it
	 * should not delete those files. The deletion is to be handled in the
	 * actual move method.
	 * </p>
	 * 
	 * @param files
	 *            The files being duplicated into a directory.
	 * @param dirName
	 *            The directory the files are being duplicated to.
	 */
	private void paste(JFile[] files, String dirName) {
		
		logger.info("Moving File");
		logger.info("File Moved to " + dirName);
		logger.error("Error Moving File");

	}

	/*
	 * Should be do-able without collaboration.
	 */
	/**
	 * Deletes a particular JFile object. If the file(s) is/are not in the trash
	 * bin, it moves it/them to that location. If they are, then it deletes them
	 * off of the hard drive the same way the OS would.
	 * 
	 * @param files
	 *            The files being deleted.
	 * 
	 */
	public void delete(JFile[] files) {
		
		logger.info("Deleting File");
		logger.info("Successful Delete of " + Arrays.toString(files));
		logger.error("Error Deleting");

	}

	/*
	 * We need to determine how this will be done.
	 *
	 * Can it be done in one action, or does it need to copy the contents, then
	 * paste them, then delete? As one can see, this will put it all together,
	 * meaning that moving a file will test all of this functionality the other
	 * route.
	 *
	 * My primary concern with the later option is that it would mess with the
	 * clipboard, even though the user didn't do a copy-like action that would
	 * make sense for copying to the clip board. The danger in this is that the
	 * user may lose what they currently have on the clipboard as a result,
	 * since they would not be expecting it to override what is currently in it.
	 * 
	 * One way to get around this is to use a temporary variable in the method
	 * and do all of the swapping around internally. It will make the method a
	 * bit longer, but may make the functionality closer to what we want.
	 * 
	 * I suggest we discuss this at some point.
	 * 
	 * *************************************************************************
	 * 
	 * Collaborate with team 1 on this method. They are developing a way to
	 * transfer objects from one machine to another.
	 */

	/**
	 * This method moves a file or list of files from one location to another.
	 * 
	 * @param files
	 *            The files being moved.
	 * @param dirName
	 *            The name of the directory where files will be moved to.
	 */
	public void move(JFile[] files, String dirName) {
		logger.info("Moving Files");
		paste(files, dirName);
		logger.info("Files Moved");
		logger.info("Deleting Files");
		delete(files);
		logger.info("Files Deleted");
		logger.error("Error Moving Files");
	}

	/*
	 * Should be do-able without collaboration.
	 */
	/**
	 * Renames a file.
	 * 
	 * @param oldName
	 *            The location of the file being renamed.
	 * @param newName
	 *            The name that the file is changing to.
	 * 
	 */
	public void rename(String oldName, String newName) {
		logger.info("Renaming File");
		logger.info("Renamed " + oldName + " to " + newName);
		logger.error("Error Renaming File");

	}

	/*
	 * Should be do-able without collaboration.
	 */
	/**
	 * Opens the file with the name passed in using the default application
	 * associated with this kind of file. How this is determined will be
	 * different for each operating system.
	 * 
	 * @param file
	 *            The file being opened.
	 * 
	 */
	public void open(JFile file) {
		if (System.getProperty("os.name").contains("Windows")) {
			// Open the file in a way compatible to Windows.
		} else if (System.getProperty("os.name").contains("Macintosh")) {
			// Open the file in a way compatible to Macintosh.
		} else if (System.getProperty("os.name").contains("Linux")) {
			// Open the file in a way compatible to Linux.
		} else {
			// ??? Error maybe?
		}
		
		logger.info("Opening File");
		logger.info(file + "opened");
		logger.error("Error Opening " + file);
	}

	/*
	 * Should be do-able without collaboration.
	 */
	/**
	 * Opens the file passed in with a particular application which is also
	 * passed in.
	 * 
	 * @param name
	 *            The name of the file being opened.
	 * 
	 */

	public void openWith(String name/* , Application app */) {
		if (System.getProperty("os.name").contains("Windows")) {
			// Open the file in a way compatible to Windows.
		} else if (System.getProperty("os.name").contains("Macintosh")) {
			// Open the file in a way compatible to Macintosh.
		} else if (System.getProperty("os.name").contains("Linux")) {
			// Open the file in a way compatible to Linux.
		} else {
			// ??? Error maybe?
		}
		
		logger.info("Opening File");
		logger.info("Opened " + name + "With (insert app)");
		logger.error("Error Opening with " + "(insert app)");
	}

	/*
	 * We need to determine what properties we need to add to details. This data
	 * may be used by other teams, and when we determine what properties we are
	 * returning, we will need to update it in our documentation.
	 * 
	 * Should be do-able without collaboration.
	 */

	/**
	 * Returns various details of a file in key-value pairs. By this, the user
	 * can ask for a specific key, such as name, size, or type, and get an
	 * appropriate response.
	 * 
	 * @param name
	 *            The name of the file being displayed.
	 * 
	 */
	public void getDetails(String name) {
		logger.info("Requesting Details");
		logger.info(name + " Acknowledged and Sent Details");
		logger.error("Error Senging Details");

	}

	// We may not use this or put this method's functionality in the getDetails
	// method.
	// Should be do-able without collaboration.
	/**
	 * Returns the type of the file passed in. This method's functionality will
	 * change depending on which OS the user is using.
	 * 
	 */
	public void getType() {
		logger.info("Requesting file type");
		logger.info("Acknowledged and Sent Type");
		logger.error("Error Requesting Type");

	}
}
