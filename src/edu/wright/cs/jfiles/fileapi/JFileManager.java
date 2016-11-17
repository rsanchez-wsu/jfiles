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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.lang.Object;
import java.lang.ProcessBuilder.Redirect;

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
	private static ArrayList<JFile> clipboard = new ArrayList<>();

	/**
	 * This file functions as the working directory for the File API. This is
	 * useful for tracking what directory the user is considered to be in. To
	 * make this function correctly, the method setting this ensures that the
	 * file being assigned is a directory.
	 */
	private static JFile workingDirectory;

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
	public static void cut(JFile[] files) {
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
	 * 
	 ******************************
	 * NOTE ABOUT STATIC FUNCTIONS
	 ******************************
	 * Since we are using multithreading, there is concern for errors at the
	 * lower levels. These issues are around the dynamic variables being around
	 * static methods. The thought is that if two threads happen into the same
	 * static method at the same time, one of the threads could change the
	 * variable that the other is using, causing an error. The solution is to be
	 * found at a later date. The current theory to the solution is to have
	 * multiple JFMs. This is currently untested, and probably never really will
	 * be because of the nature of this error.
	 */

	/**
	 * This method copies all of the files passed in to the clipboard.
	 * 
	 * @param files
	 *            The files being copied to the clipboard.
	 * 
	 */
	public static void copy(JFile[] files) {
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
	public static void paste(String dirName) {
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
	private static void paste(JFile[] files, String dirName) {
		// Updated logging strings to better document method.
		logger.info("Copying Files.");
		try {

			File tempFile = new File(dirName);

			if (tempFile.isDirectory()) {
				for (int i = 0; i < files.length; i++) {
					files[i].moveTo(dirName);
				}
				logger.info(Arrays.toString(files) + " Moved to " + dirName + " Successfully");
			} else {
				logger.error("Given string isn't a directory.");
			}

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
	 * Tests what OS the JFM is on, then loops through to check where the
	 * file(s) are located.
	 * </p>
	 * 
	 * @param files
	 *            The files being deleted.
	 * 
	 */
	public static void delete(JFile[] files) {
		logger.info("Deleting File(s)");
		try {
			if (System.getProperty("os.name").contains("Windows")) {
				for (int i = 0; i < files.length; i++) {
					// The problem with how windows handles the recycling bin
					// (from win 10) is that it is not an absolute file.
					if (files[i].getPath().contains("$Recycle.Bin") == true) {
						files[i].deleteContents();
					} else {
						// TODO: find out how to move files to recycle bin
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
	public static void move(JFile[] files, String dirName) {
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
	public static void rename(String oldName, String newName) {
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
	public static void open(String name) {

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

	public static void openWith(String name/* , Application app */) {
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
	 * This method takes in a JFile argument, checks to see if it is a
	 * directory, and, if it is, sets the current working directory to be this
	 * file.
	 */
	public static void setWorkingDirectory(JFile file) {
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
			logger.error(
					"The system ran into an excpetion while " + "setting the working directory.",
					e);
		}
	}

	/**
	 * This method gets the current working directory as a JFile.
	 * 
	 * @return The current working directory as a JFile.
	 */
	public static JFile getWorkingDirectory() {
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
	 * This method gets the contents of the working directory as an array of
	 * JFiles. This is suitable for use listing the contents of the working
	 * directory in a UI. Though the listWorkingDirectoryContents method is
	 * suitable for simply listing out the names of the files/directories in the
	 * working directory, this method s necessary for doing anything meaningful,
	 * such as getting absolute paths, file sizes and types, and for easily
	 * getting the JFile to pass it into a method, such as cut, copy, or paste.
	 * 
	 * @return The contents of the working directory as an array of JFiles.
	 */
	public static JFile[] getWorkingDirectoryContents() {
		try {
			// TODO: Add logging methods.
			return workingDirectory.getContents();
		} catch (Exception e) {
			// TODO: Add logging methods.
			return null;
		}
	}

	/**
	 * This method returns the names of the contents of the working directory as
	 * an array of abstract path names. For anything more advanced, the
	 * getWorkingDirectoryContents method must be called.
	 * 
	 * @return An abstract listing of the contents of the working directory.
	 */
	public static String[] listWorkingDirectoryContents() {
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
	public static void getType(String name) {
		logger.info("Requesting file type");
		try {
			logger.info("Acknowledged and Sent " + name + "'s Type");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error Requesting Type", e);
		}
	}
}

final class ProcessBuilder extends Object {
	Process p1 = new ProcessBuilder().start();
	ProcessBuilder pb = new ProcessBuilder();
	Map<String, String> env = pb
			.environment();env.put("VAR1","myValue");env.remove("OTHERVAR");env.put("VAR2",env.get("VAR1")+"suffix");pb.directory(new File("myDir"));
	File log = new File(
			"log");pb.@redirectErrorStream(true);pb.redirectOutput(Redirect.appendTo(log));
	Process p = pb.start();assert pb.redirectInput()==Redirect.PIPE;assert pb.redirectOutput().file()==log;assert p.getInputStream().read()==-1;

	public ProcessBuilder(List<String> command) {

	}

	public ProcessBuilder(String command) {

	}

	/**
	 * Sets this process builder's operating system program and arguments. This
	 * method does not make a copy of the command list. Subsequent updates to
	 * the list will be reflected in the state of the process builder. It is not
	 * checked whether command corresponds to a valid operating system command.
	 * Parameters: command - the list containing the program and its arguments
	 * Returns: this process builder Throws: NullPointerException - if the
	 * argument is null
	 */
	public ProcessBuilder command(List<String> command) {
		return null;
	}

	/**
	 * Sets this process builder's operating system program and arguments. This
	 * is a convenience method that sets the command to a string list containing
	 * the same strings as the command array, in the same order. It is not
	 * checked whether command corresponds to a valid operating system command.
	 * Parameters: command - a string array containing the program and its
	 * arguments Returns: this process builder
	 */
	public ProcessBuilder command(String command) {
		return null;
	}

	/**
	 * Returns this process builder's operating system program and arguments.
	 * The returned list is not a copy. Subsequent updates to the list will be
	 * reflected in the state of this process builder. Returns: this process
	 * builder's program and its arguments
	 */
	public List<String> command() {
		return null;
	}

	/**
	 * Returns a string map view of this process builder's environment. Whenever
	 * a process builder is created, the environment is initialized to a copy of
	 * the current process environment (see System.getenv()). Subprocesses
	 * subsequently started by this object's start() method will use this map as
	 * their environment. The returned object may be modified using ordinary Map
	 * operations. These modifications will be visible to subprocesses started
	 * via the start() method. Two ProcessBuilder instances always contain
	 * independent process environments, so changes to the returned map will
	 * never be reflected in any other ProcessBuilder instance or the values
	 * returned by System.getenv.
	 * 
	 * If the system does not support environment variables, an empty map is
	 * returned.
	 * 
	 * The returned map does not permit null keys or values. Attempting to
	 * insert or query the presence of a null key or value will throw a
	 * NullPointerException. Attempting to query the presence of a key or value
	 * which is not of type String will throw a ClassCastException.
	 * 
	 * The behavior of the returned map is system-dependent. A system may not
	 * allow modifications to environment variables or may forbid certain
	 * variable names or values. For this reason, attempts to modify the map may
	 * fail with UnsupportedOperationException or IllegalArgumentException if
	 * the modification is not permitted by the operating system.
	 * 
	 * Since the external format of environment variable names and values is
	 * system-dependent, there may not be a one-to-one mapping between them and
	 * Java's Unicode strings. Nevertheless, the map is implemented in such a
	 * way that environment variables which are not modified by Java code will
	 * have an unmodified native representation in the subprocess.
	 * 
	 * The returned map and its collection views may not obey the general
	 * contract of the Object.equals(java.lang.Object) and Object.hashCode()
	 * methods.
	 * 
	 * The returned map is typically case-sensitive on all platforms.
	 * 
	 * If a security manager exists, its checkPermission method is called with a
	 * RuntimePermission("getenv.*") permission. This may result in a
	 * SecurityException being thrown.
	 * 
	 * When passing information to a Java subprocess, system properties are
	 * generally preferred over environment variables.
	 * 
	 * Returns: this process builder's environment Throws: SecurityException -
	 * if a security manager exists and its checkPermission method doesn't allow
	 * access to the process environment See Also:
	 * Runtime.exec(String[],String[],java.io.File), System.getenv()
	 */
	public Map<String, String> environment() {
		return null;
	}

	/**
	 * Returns this process builder's working directory. Subprocesses
	 * subsequently started by this object's start() method will use this as
	 * their working directory. The returned value may be null -- this means to
	 * use the working directory of the current Java process, usually the
	 * directory named by the system property user.dir, as the working directory
	 * of the child process. Returns: this process builder's working directory
	 */
	public ProcessBuilder directory(File directory) {
		return null;
	}

	/**
	 * Sets this process builder's working directory. Subprocesses subsequently
	 * started by this object's start() method will use this as their working
	 * directory. The argument may be null -- this means to use the working
	 * directory of the current Java process, usually the directory named by the
	 * system property user.dir, as the working directory of the child process.
	 * Parameters: directory - the new working directory Returns: this process
	 * builder
	 */
	public ProcessBuilder redirectInput(ProcessBuilder.Redirect source) {
		return null;
	}

	/**
	 * Sets this process builder's standard input source. Subprocesses
	 * subsequently started by this object's start() method obtain their
	 * standard input from this source. If the source is Redirect.PIPE (the
	 * initial value), then the standard input of a subprocess can be written to
	 * using the output stream returned by Process.getOutputStream(). If the
	 * source is set to any other value, then Process.getOutputStream() will
	 * return a null output stream.
	 * 
	 * Parameters: source - the new standard input source Returns: this process
	 * builder Throws: IllegalArgumentException - if the redirect does not
	 * correspond to a valid source of data, that is, has type WRITE or APPEND
	 */
	public ProcessBuilder redirectOutput(ProcessBuilder.Redirect destination) {
		return null;
	}

	/***
	 * Sets this process builder's standard output destination. Subprocesses
	 * subsequently started by this object's start() method send their standard
	 * output to this destination. If the destination is Redirect.PIPE (the
	 * initial value), then the standard output of a subprocess can be read
	 * using the input stream returned by Process.getInputStream(). If the
	 * destination is set to any other value, then Process.getInputStream() will
	 * return a null input stream.
	 * 
	 * Parameters: destination - the new standard output destination Returns:
	 * this process builder Throws: IllegalArgumentException - if the redirect
	 * does not correspond to a valid destination of data, that is, has type
	 * READ
	 */
	public ProcessBuilder redirectError(ProcessBuilder.Redirect destination) {
		return null;
	}

	/**
	 * Sets this process builder's standard error destination. Subprocesses
	 * subsequently started by this object's start() method send their standard
	 * error to this destination. If the destination is Redirect.PIPE (the
	 * initial value), then the error output of a subprocess can be read using
	 * the input stream returned by Process.getErrorStream(). If the destination
	 * is set to any other value, then Process.getErrorStream() will return a
	 * null input stream.
	 * 
	 * If the redirectErrorStream attribute has been set true, then the
	 * redirection set by this method has no effect.
	 * 
	 * Parameters: destination - the new standard error destination Returns:
	 * this process builder Throws: IllegalArgumentException - if the redirect
	 * does not correspond to a valid destination of data, that is, has type
	 * READ
	 */
	public ProcessBuilder redirectInput(File file) {
		return null;
	}

	/***
	 * Sets this process builder's standard input source to a file. This is a
	 * convenience method. An invocation of the form redirectInput(file) behaves
	 * in exactly the same way as the invocation redirectInput
	 * (Redirect.from(file)).
	 * 
	 * Parameters: file - the new standard input source Returns: this process
	 * builder
	 */
	public ProcessBuilder redirectOutput(File file) {
		return null;
	}

	/***
	 * Sets this process builder's standard output destination to a file. This
	 * is a convenience method. An invocation of the form redirectOutput(file)
	 * behaves in exactly the same way as the invocation redirectOutput
	 * (Redirect.to(file)).
	 * 
	 * Parameters: file - the new standard output destination Returns: this
	 * process builder
	 */
	public ProcessBuilder redirectError(File file) {
		return null;
	}

	/**
	 * Returns this process builder's standard input source. Subprocesses
	 * subsequently started by this object's start() method obtain their
	 * standard input from this source. The initial value is Redirect.PIPE.
	 * Returns: this process builder's standard input source
	 */
	public ProcessBuilder.Redirect redirectInput() {
		return null;
	}

	/***
	 * Returns this process builder's standard output destination. Subprocesses
	 * subsequently started by this object's start() method redirect their
	 * standard output to this destination. The initial value is Redirect.PIPE.
	 * Returns: this process builder's standard output destination
	 */
	public ProcessBuilder.Redirect redirectOutput() {
		return null;
	}

	/***
	 * Returns this process builder's standard error destination. Subprocesses
	 * subsequently started by this object's start() method redirect their
	 * standard error to this destination. The initial value is Redirect.PIPE.
	 * Returns: this process builder's standard error destination
	 */
	public ProcessBuilder.Redirect redirectError() {
		return null;
	}

	/***
	 * Sets the source and destination for subprocess standard I/O to be the
	 * same as those of the current Java process. This is a convenience method.
	 * An invocation of the form
	 * 
	 * pb.inheritIO()
	 * 
	 * behaves in exactly the same way as the invocation
	 * pb.redirectInput(Redirect.INHERIT) .redirectOutput(Redirect.INHERIT)
	 * .redirectError(Redirect.INHERIT)
	 * 
	 * This gives behavior equivalent to most operating system command
	 * interpreters, or the standard C library function system(). Returns: this
	 * process builder
	 */
	public ProcessBuilder inheritIO() {
		return null;
	}

	/***
	 * Tells whether this process builder merges standard error and standard
	 * output. If this property is true, then any error output generated by
	 * subprocesses subsequently started by this object's start() method will be
	 * merged with the standard output, so that both can be read using the
	 * Process.getInputStream() method. This makes it easier to correlate error
	 * messages with the corresponding output. The initial value is false.
	 * 
	 * Returns: this process builder's redirectErrorStream property
	 */
	public boolean redirectErrorStream() {
		return false;
	}

	/***
	 * Sets this process builder's redirectErrorStream property. If this
	 * property is true, then any error output generated by subprocesses
	 * subsequently started by this object's start() method will be merged with
	 * the standard output, so that both can be read using the
	 * Process.getInputStream() method. This makes it easier to correlate error
	 * messages with the corresponding output. The initial value is false.
	 * 
	 * Parameters: redirectErrorStream - the new property value Returns: this
	 * process builder
	 */
	public ProcessBuilder redirectErrorStream(boolean redirectErrorStream) {
		return null;
	}

	/***
	 * Starts a new process using the attributes of this process builder. The
	 * new process will invoke the command and arguments given by command(), in
	 * a working directory as given by directory(), with a process environment
	 * as given by environment().
	 * 
	 * This method checks that the command is a valid operating system command.
	 * Which commands are valid is system-dependent, but at the very least the
	 * command must be a non-empty list of non-null strings.
	 * 
	 * A minimal set of system dependent environment variables may be required
	 * to start a process on some operating systems. As a result, the subprocess
	 * may inherit additional environment variable settings beyond those in the
	 * process builder's environment().
	 * 
	 * If there is a security manager, its checkExec method is called with the
	 * first component of this object's command array as its argument. This may
	 * result in a SecurityException being thrown.
	 * 
	 * Starting an operating system process is highly system-dependent. Among
	 * the many things that can go wrong are:
	 * 
	 * The operating system program file was not found. Access to the program
	 * file was denied. The working directory does not exist. In such cases an
	 * exception will be thrown. The exact nature of the exception is
	 * system-dependent, but it will always be a subclass of IOException.
	 * 
	 * Subsequent modifications to this process builder will not affect the
	 * returned Process.
	 * 
	 * Returns: a new Process object for managing the subprocess Throws:
	 * NullPointerException - if an element of the command list is null
	 * IndexOutOfBoundsException - if the command is an empty list (has size 0)
	 * SecurityException - if a security manager exists and its checkExec method
	 * doesn't allow creation of the subprocess, or the standard input to the
	 * subprocess was redirected from a file and the security manager's
	 * checkRead method denies read access to the file, or the standard output
	 * or standard error of the subprocess was redirected to a file and the
	 * security manager's checkWrite method denies write access to the file
	 * IOException - if an I/O error occurs See Also: Runtime.exec(String[],
	 * String[], java.io.File)
	 */
	public Process start() throws IOException {
		return null;
	}

}
