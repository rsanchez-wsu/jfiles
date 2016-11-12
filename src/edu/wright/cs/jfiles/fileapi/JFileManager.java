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

import java.util.ArrayList;
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
 * 
 * 
 * @author <b>Team 5:</b>
 * @author John Wintersohle II
 *         <<a href="mailto:Dorkatron199@aol.com">Dorkatron199@aol.com</a>>
 *
 */
public class JFileManager {

	/**
	 * <p>
	 * The logger for the JFileManager class. This object records all actions
	 * done by this class. This can be done either through info or error
	 * logging.
	 * </p>
	 * 
	 * <p>
	 * Logging generally follows this process:
	 * </p>
	 * 
	 * <ol>
	 * <b>
	 * <li>Startup log</b> (before try block) <b>
	 * <li>Try Block Starts</b> <b>
	 * <li>Complete log</b> (in try block) <b>
	 * <li>Catch Block Starts</b> <b>
	 * <li>Error log</b> (in catch block)
	 * </ol>
	 * 
	 * @see Logger
	 * @see LogManager
	 */
	static final Logger logger = LogManager.getLogger(JFileManager.class.getName());

	// This is suppressed until we build in the functionality for this field.
	// This will most likely be when we make the paste() method functional.
	/**
	 * This is the clipboard used when copying JFiles. It is capable of copying
	 * and pasting multiple JFiles at a time. It is made private so that it can
	 * only be accessed via the JFileManager methods for the sake of security.
	 * 
	 */
	private ArrayList<JFile> clipboard;
	
	/**
	 * This file functions as the working directory for the File API. This is
	 * useful for tracking what directory the user is considered to be in. To
	 * make this function correctly, the method setting this ensures that the
	 * file being assigned is a directory.
	 */
	private JFile workingDirectory;
	
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
		// Since the copy and delete methods already document themselves,
		// I changed this documentation to only reflect the cut method.
		logger.info("Cut Started");
		try {
			copy(files);
			delete(files);
			logger.info("Successfully Cut " + Arrays.toString(files));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error Cutting Files", e);
		}
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
		try {
			for (int i = 0; i < files.length; i++) {
				clipboard.add(files[i].clone());
			}
			logger.info("Copy of " + Arrays.toString(files) + " Successful");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error Copying Files.", e);
		}
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
		try {
			logger.info("Successful Paste of " + dirName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error pasting files.", e);
		}
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
		// Updated logging strings to better document method.
		logger.info("Copying Files.");
		try {
			logger.info(Arrays.toString(files) + " Moved to " + dirName + " Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error Moving Files", e);
		}
	}

	/*
	 * Should be do-able without collaboration.
	 */
	/**
	 * Deletes a particular JFile object. If the file(s) is/are not in the trash
	 * bin, it moves it/them to that location. If they are, then it deletes them
	 * off of the hard drive the same way the OS would.
	 * <p>
	 * Tests what OS the JFM is on, then loops through to check where the file(s)
	 * are located.
	 * </p>
	 * @param files
	 *            The files being deleted.
	 * 
	 */
	public void delete(JFile[] files) {
		logger.info("Deleting File(s)");
		try {
			if (System.getProperty("os.name").contains("Windows")) {
				for (int i = 0; i < files.length; i++) {
					//The problem with how windows handles the recycling bin
					//(from win 10) is that it is not an absolute file.
					if (files[i].getPath().contains("$Recycle.Bin") == true) {
						files[i].deleteContents();
					} else {
						//TODO: find out how to move files to recycle bin
					}
				}
				logger.info("OS determined to be \"Windows\".");
			} else if (System.getProperty("os.name").contains("Macintosh")) {
				
				logger.info("OS determined to be \"Macintosh\".");
			} else if (System.getProperty("os.name").contains("Linux")) {
				
				logger.info("OS determined to be \"Linux\".");
			} else {
				
				logger.info("OS determined to be \"Unknown\".");
			}
			logger.info("Successfully Deleted " + Arrays.toString(files));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error Deleting", e);
		}
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
		try {
			paste(files, dirName);
			delete(files);
			logger.info(Arrays.toString(files) + " Successfully Moved to " + dirName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error Moving Files", e);
		}
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
		try {
			logger.info("Renamed " + oldName + " to " + newName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error Renaming File", e);
		}
	}

	/*
	 * Should be do-able without collaboration.
	 */
	/**
	 * Opens the file with the name passed in using the default application
	 * associated with this kind of file. How this is determined will be
	 * different for each operating system.
	 * 
	 * @param name
	 *            The file being opened.
	 * 
	 */
	public void open(String name) {

		/*
		 * Notes on how to do this:
		 **********************************************************************
		 * The ProcessBuilder should be able to get this method functioning. The
		 * command(string[] args) takes in an OS command and its arguments and
		 * acts accordingly. It is, however, system-dependent, so it will
		 * function differently for different OSs, so we will have to create a
		 * selection statement for different OSs. This has already been done.
		 * 
		 * How exactly this works will have to be investigated. If it
		 * effectively put a command into the command line, we need to figure
		 * out how different operating systems open files via the command line.
		 * This may mean getting help from Brand will be wise. If it isn't like
		 * that, then we need to find out how this works.
		 * 
		 * If nothing else, this gives us a starting point to do research. And
		 * after looking into it a bit, it seems like we will have a bit of
		 * research and tinkering to do.
		 * 
		 * - John Wintersohle
		 * 
		 * (This comment is temporary and will be removed when this issue is
		 * solved.)
		 */

		logger.info("Opening File");
		try {
			if (System.getProperty("os.name").contains("Windows")) {
				// TODO Open the file in a way compatible to Windows.
				logger.info("OS determined to be \"Windows\".");
			} else if (System.getProperty("os.name").contains("Macintosh")) {
				// TODO Open the file in a way compatible to Macintosh.
				logger.info("OS determined to be \"Macintosh\".");
			} else if (System.getProperty("os.name").contains("Linux")) {
				// TODO Open the file in a way compatible to Linux.
				logger.info("OS determined to be \"Linux\".");
			} else {
				// TODO ??? Error maybe?
				logger.info("OS determined to be \"Unknown\".");
			}
			logger.info("Opened " + name + " With " /* application name */);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error Opening the File", e);
		}
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
		// See open(JFile file) for details.

		logger.info("Opening File");
		try {
			if (System.getProperty("os.name").contains("Windows")) {
				// TODO Open the file in a way compatible to Windows.
				logger.info("OS determined to be \"Windows\".");
			} else if (System.getProperty("os.name").contains("Macintosh")) {
				// TODO Open the file in a way compatible to Macintosh.
				logger.info("OS determined to be \"Macintosh\".");
			} else if (System.getProperty("os.name").contains("Linux")) {
				// TODO Open the file in a way compatible to Linux.
				logger.info("OS determined to be \"Linux\".");
			} else {
				// TODO ??? Error maybe?
				logger.info("OS determined to be \"Unknown\".");
			}
			logger.info("Opened " + name + " With " /* application name */);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error Opening File", e);
		}
	}
	
	// This will be made to handle security (permissions) later, for now,
	// I am just making something that is functioning. Leave this in until
	// such functionality is added.
	/**
	 * This method takes in a JFile argument, checks to see if it is a directory,
	 * and, if it is, sets the current working directory to be this file.
	 */
	public void setWorkingDirectory(JFile file) {
		logger.info("Attempting to set the working directory.");
		try {
			logger.info("Checking whether the file argumet is a file or directory.");
			if (file.isDirectory()) {
				logger.info("File argument was determined to be a directory.");
				workingDirectory = file;
				logger.info("System successfully set the working directory to its new location.");
			} else {
				logger.info("File argument was determined to be a file.");
				logger.error("System attempted to set a file as the working directory.");
			}
		} catch (Exception e) {
			System.err.println("System ran into an error setting the working directory.");
			e.printStackTrace();
			logger.error("The system ran into an excpetion while "
					+ "setting the working directory.", e);
		}
	}
	
	/**
	 * This method gets the current working directory as a JFile.
	 * 
	 * @return The current working directory as a JFile.
	 */
	public JFile getWorkingDirectory() {
		logger.info("Attempting to get the working directory.");
		try {
			return workingDirectory;
		} catch (Exception e) {
			System.err.println("System ran into an error setting the working directory.");
			e.printStackTrace();
			logger.error("The system ran into an exception getting the working directory.\n"
					+ "Returning null value.", e);
			return null;
		}
	}
	
	/**
	 * This method gets the contents of the working directory as an array of JFiles. This
	 * is suitable for use listing the contents of the working directory in a UI. Though the
	 * listWorkingDirectoryContents method is suitable for simply listing out the names of
	 * the files/directories in the working directory, this method s necessary for doing
	 * anything meaningful, such as getting absolute paths, file sizes and types, and for
	 * easily getting the JFile to pass it into a method, such as cut, copy, or paste.
	 * 
	 * @return The contents of the working directory as an array of JFiles.
	 */
	public JFile[] getWorkingDirectoryContents() {
		try {
			// TODO: Add logging methods.
			return workingDirectory.getContents();
		} catch (Exception e) {
			// TODO: Add logging methods.
			return null;
		}
	}
	
	/**
	 * This method returns the names of the contents of the working directory
	 * as an array of abstract path names. For anything more advanced, the
	 * getWorkingDirectoryContents method must be called.
	 * 
	 * @return An abstract listing of the contents of the working directory.
	 */
	public String[] listWorkingDirectoryContents() {
		try {
			// TODO: Add logging methods.
			return workingDirectory.list();
		} catch (Exception e) {
			// TODO: Add logging methods.
			return null;
		}
	}

	// We may not use this or put this method's functionality in the getDetails
	// method.
	// Should be do-able without collaboration.
	/**
	 * Returns the type of the file passed in. This method's functionality will
	 * change depending on which OS the user is using.
	 * 
	 */
	public void getType(String name) {
		logger.info("Requesting file type");
		try {
			logger.info("Acknowledged and Sent " + name + "'s Type");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error Requesting Type", e);
		}
	}
}
