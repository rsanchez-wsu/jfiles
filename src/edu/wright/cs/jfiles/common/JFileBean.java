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

package edu.wright.cs.jfiles.common;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.wright.cs.jfiles.fileapi.JFile;

/**
 * This is a class to support serializing the JFile class.
 * @author brian
 *
 */
public class JFileBean implements Serializable {

	private static final long serialVersionUID = -6944212605660133613L;
	private ArrayList<JFile> jfileArr = new ArrayList<JFile>();
	private File currentPath;
	
	/**
	 * Zero argument constructor.
	 */
	public JFileBean() {
		
	}
	
	/**
	 * This method accepts a File object, any type including directories, and creates an array
	 * of JFile objects wrapped in a class to support serialization. 
	 * 
	 * @param input Input file or path to generate JFile(s) from
	 * @throws IOException Throws IOException when file at path cannot be accessed
	 */
	public JFileBean(File input) throws IOException {
		this.currentPath = input;
		populateArray();
	}
	
	/**
	 * A helper method to populate the arraylist.
	 */
	private void populateArray() throws IOException {
		if (currentPath == null) {
			return;
		}
		
		//Reads all paths at give path
		File[] dir = currentPath.listFiles();
		
		//Returns if supplied input returns no results
		if (dir == null) {
			return;
		}
			
		for (int i = 0; i < dir.length; i++) {
			Path path = Paths.get(dir[i].getAbsolutePath());
			Map<String, String> temp = new HashMap<>();
			
			//Populates each JFile with a complete K/V map of all basic file attributes	
			temp.put("name", dir[i].getName());
			temp.put("lastModifiedTime", Files.getAttribute(path, "lastModifiedTime").toString());
			temp.put("lastAccessTime", Files.getAttribute(path, "lastAccessTime").toString());
			temp.put("creationTime", Files.getAttribute(path, "creationTime").toString());
			temp.put("size", Files.getAttribute(path, "size").toString());
			temp.put("isRegularFile", Files.getAttribute(path, "isRegularFile").toString());
			temp.put("isDirectory", Files.getAttribute(path, "isDirectory").toString());
			temp.put("isSymbolicLink", Files.getAttribute(path, "isSymbolicLink").toString());
			temp.put("isOther", Files.getAttribute(path, "isOther").toString());
			
			//Populates with DOS or POSIX attributes
			if (Files.getFileStore(path).supportsFileAttributeView(DosFileAttributeView.class)) {
				DosFileAttributes attrs = Files.getFileAttributeView(
						path, DosFileAttributeView.class).readAttributes();
				
				temp.put("system", "DOS");
				temp.put("readOnly", String.valueOf(attrs.isReadOnly()));
				temp.put("hidden", String.valueOf(attrs.isHidden()));
				temp.put("system", String.valueOf(attrs.isSystem()));
				temp.put("archive", String.valueOf(attrs.isArchive()));				
			} else if (Files.getFileStore(path)
					.supportsFileAttributeView(PosixFileAttributeView.class)) {
				PosixFileAttributes attrs = Files.getFileAttributeView(
						path, PosixFileAttributeView.class).readAttributes();
				
				temp.put("system", "POSIX");
				temp.put("group", attrs.group().getName());
				temp.put("owner", attrs.owner().getName());
				temp.put("permissions", stringifyPermissions(path, attrs));
			//Catching weird behavior	
			} else {
				temp.put("system", "Unknown");
			}
			
			//Add JFile object to the arraylist
			jfileArr.add(new JFile(dir[i],temp));
		}
	}
	
	/**
	 * Helper method to rwx stringify POSIX permissions.
	 * @param path path of object
	 * @param attrs POSIX attributes object
	 * @return rwx permissions as string
	 */
	private String stringifyPermissions(Path path, PosixFileAttributes attrs) {
		String permissions = "";
		if (Files.isDirectory(path)) {
			permissions += "d";
		} else if (Files.isSymbolicLink(path)) {
			permissions += "l";
		} else if (Files.isRegularFile(path)) {
			permissions += "-";
		}
		
		if (attrs.permissions().contains(PosixFilePermission.OWNER_READ)) {
			permissions += "r";
		} else {
			permissions += "-";
		}
		if (attrs.permissions().contains(PosixFilePermission.OWNER_WRITE)) {
			permissions += "w";
		} else {
			permissions += "-";
		}
		if (attrs.permissions().contains(PosixFilePermission.OWNER_EXECUTE)) {
			permissions += "x";
		} else {
			permissions += "-";
		}
		if (attrs.permissions().contains(PosixFilePermission.GROUP_READ)) {
			permissions += "r";
		} else {
			permissions += "-";
		}
		if (attrs.permissions().contains(PosixFilePermission.GROUP_WRITE)) {
			permissions += "w";
		} else {
			permissions += "-";
		}
		if (attrs.permissions().contains(PosixFilePermission.GROUP_EXECUTE)) {
			permissions += "x";
		} else {
			permissions += "-";
		}
		if (attrs.permissions().contains(PosixFilePermission.OTHERS_READ)) {
			permissions += "r";
		} else {
			permissions += "-";
		}
		if (attrs.permissions().contains(PosixFilePermission.OTHERS_WRITE)) {
			permissions += "w";
		} else {
			permissions += "-";
		}
		if (attrs.permissions().contains(PosixFilePermission.OTHERS_EXECUTE)) {
			permissions += "x";
		} else {
			permissions += "-";
		}
		
		return permissions;
	}

	/**
	 * Default getter.
	 * @return the jfileArr
	 */
	protected ArrayList<JFile> getJfileArr() {
		return jfileArr;
	}

	/**
	 * Getter for arraylist elements.
	 * @return the jfileArr
	 */
	protected JFile getJfileArr(int index) {
		if (0 <= index && index < jfileArr.size()) {
			return jfileArr.get(index);
		} else {
			return null;
		}
	}

	/**
	 * Default getter.
	 * @return the currentPath
	 */
	protected File getCurrentPath() {
		return currentPath;
	}

	/**
	 * Default setter.
	 * @param jfileArr the jfileArr to set
	 */
	protected void setJfileArr(ArrayList<JFile> jfileArr) {
		this.jfileArr = jfileArr;
	}

	/**
	 * Setter for each arraylist element.
	 * @param index index to get
	 * @param jfile the jfile to set
	 */
	protected void setJfileArr(int index, JFile jfile) {
		if (0 <= index && index < jfileArr.size()) {
			jfileArr.set(index, jfile);
		}
	}

	/**
	 * Default setter.
	 * @param currentPath the currentPath to set
	 */
	protected void setCurrentPath(File currentPath) {
		this.currentPath = currentPath;
	}
}
