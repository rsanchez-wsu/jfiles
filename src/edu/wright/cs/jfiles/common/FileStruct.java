/*
 * Copyright (C) 2016 - WSU CEG3120 Students
 * 
 * Brian Denlinger <brian.denlinger1@gmail.com>
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

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Data Struct for XML generation. No logger as that adds too much overhead.
 * @author brian
 *
 */
public class FileStruct implements Serializable {
	
	/**
	 * Enum inner class to define what type of object.
	 * @author brian
	 *
	 */	
	enum Type {
		FILE("file"), DIRECTORY("directory"), SYMBOLICLINK("symbolicLink"), OTHER("other");
		private String type;
		
		/**
		 * Constructor.
		 * @param input Type of object
		 */
		Type(String input) {
			setType(input);
		}
		
		/**
		 * Public getter for type.
		 * @return type
		 */
		public String getType() {
			return type;
		}
		
		/**
		 * Public setter for type.
		 * @param type type of object
		 */
		public void setType(String type) {
			this.type = type;
		}
	}
	
	private static final long serialVersionUID = -5456733342924856091L;
	private Map<String, Object> attrList = new HashMap<>();
	private Type type;
	
	/**
	 * Zero argument constructor.
	 */
	public FileStruct() {
		
	}
	
	/**
	 * This method accepts a Path object, any type including directories, and populates a Map with
	 * file attributes. 
	 * @param input Input file or path to generate JFile(s) from
	 * @throws IOException Throws IOException when file at path cannot be read
	 */
	public FileStruct(Path input) throws IOException {
		populateArray(input);
	}
	
	/**
	 * Helper method to populate the attribute array.
	 * @throws IOException Throws IOException when file at path cannot be read
	 */
	private void populateArray(Path path) throws IOException {
		if (path == null) {
			return;
		}
		//Populates Type variable
		if (Files.isRegularFile(path)) {
			this.setType(Type.FILE);
		} else if (Files.isDirectory(path)) {
			this.setType(Type.DIRECTORY);
		} else if (Files.isSymbolicLink(path)) {
			this.setType(Type.SYMBOLICLINK);
		} else {
			//Should never get here hopefully
			this.setType(Type.OTHER);
		}
		
		//Basic file attributes
		attrList.put("name", path.toFile().getName());
		attrList.put("lastModifiedTime", Files.getAttribute(path, "lastModifiedTime"));
		attrList.put("lastAccessTime", Files.getAttribute(path, "lastAccessTime"));
		attrList.put("creationTime", Files.getAttribute(path, "creationTime"));
		attrList.put("size", Files.getAttribute(path, "size"));
		
		//Populates with DOS or POSIX attributes
		if (Files.getFileStore(path).supportsFileAttributeView(DosFileAttributeView.class)) {
			//Creates a DOS file attribute view from the file
			DosFileAttributes attrs = Files.getFileAttributeView(
					path, DosFileAttributeView.class).readAttributes();
			
			//Populates DOS specific file attributes
			attrList.put("system", "DOS");
			attrList.put("readOnly", String.valueOf(attrs.isReadOnly()));
			attrList.put("hidden", String.valueOf(attrs.isHidden()));
			attrList.put("system", String.valueOf(attrs.isSystem()));
			attrList.put("archive", String.valueOf(attrs.isArchive()));				
		} else if (Files.getFileStore(path).supportsFileAttributeView(
				PosixFileAttributeView.class)) {
			//Creates a POSIX file attribute from the file
			PosixFileAttributes attrs = Files.getFileAttributeView(
					path, PosixFileAttributeView.class).readAttributes();
			
			//Populates POSIX specific file attributes
			attrList.put("system", "POSIX");
			attrList.put("group", attrs.group().getName());
			attrList.put("owner", attrs.owner().getName());
			attrList.put("permissions", stringifyPermissions(path, attrs));
			attrList.put("numericPermissions", getNumericPermissions());
			
		//Catching weird behavior	
		} else {
			attrList.put("system", "Unknown");
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
		
		//Type descriptor
		if (Files.isDirectory(path)) {
			permissions += "d";
		} else if (Files.isSymbolicLink(path)) {
			permissions += "l";
		} else if (Files.isRegularFile(path)) {
			permissions += "-";
		} else {
			permissions += " ";
		}
		
		//Permissions
		permissions += attrs.permissions().contains(PosixFilePermission.OWNER_READ) ? "r" : "-";
		permissions += attrs.permissions().contains(PosixFilePermission.OWNER_WRITE) ? "w" : "-";
		permissions += attrs.permissions().contains(PosixFilePermission.OWNER_EXECUTE) ? "x" : "-";
		permissions += attrs.permissions().contains(PosixFilePermission.GROUP_READ) ? "r" : "-";
		permissions += attrs.permissions().contains(PosixFilePermission.GROUP_WRITE) ? "w" : "-";
		permissions += attrs.permissions().contains(PosixFilePermission.GROUP_EXECUTE) ? "x" : "-";
		permissions += attrs.permissions().contains(PosixFilePermission.OTHERS_READ) ? "r" : "-";
		permissions += attrs.permissions().contains(PosixFilePermission.OTHERS_WRITE) ? "w" : "-";
		permissions += attrs.permissions().contains(PosixFilePermission.OTHERS_EXECUTE) ? "x" : "-";
	
		return permissions;
	}

	/**
	 * Default.
	 * @return the attrList
	 */
	protected Map<String, Object> getAttrList() {
		return attrList;
	}
	
	/**
	 * Default.
	 * @param attrList the attrList to set
	 */
	protected void setAttrList(Map<String, Object> attrList) {
		this.attrList = attrList;
	}
	
	/**
	 * Default.
	 * @return type of object
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Default.
	 * @param type type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * Searches the attrList for an attribute corresponding to the given string
	 * @param name is the key to search for an attribute
	 * @return the corresponding value if the attribute exists. Otherwise returns an empty string
	 */
	public Object getValue(String name) {
		Object attribute = "";
		if (attrList.containsKey(name.toLowerCase(Locale.ENGLISH))) {
			attribute = attrList.get(name);
		}
		return attribute;
	}
	
	/**
	 * Returns an array of the keys.
	 * @return an array of the keys of the attrList.
	 */
	public String[] getKeys() {
		Collection<String> attNames = attrList.keySet();
		String [] namesArray = attNames.toArray(new String[attNames.size()]); 
		return namesArray;
	}
	
	/**
	 * Determines if the attrList has a value for the given key.
	 * @param name is the key to search for a value
	 * @return true or false depending on if a value is found
	 */
	public boolean contains(String name) {
		boolean truth = false;
		if (attrList.containsKey(name)) {
			truth = true;
		}
		return truth;
	}
	
	/**
	 * A method to calculate the numeric representation of POSIX permissions.
	 * @return A string of 3 numbers
	 */
	public String getNumericPermissions() {
		StringBuffer buffer = new StringBuffer();
		String permissions = "";
		String temp = (String) this.getValue("permissions");
		
		int single = 0;
		
		//i =  1 because the first character is the type descriptor
		for (int i = 1; i < 10; i++) {
			single += temp.charAt(i) == 'r' ? 4 : 0;
			single += temp.charAt(i) == 'w' ? 2 : 0;
			single += temp.charAt(i) == 'x' ? 1 : 0;
			
			//Every third loop add the single value to the string and reset
			if (i % 3 == 0) {
				buffer.append(Integer.toString(single));
				single = 0;
			}
		}
		permissions = buffer.toString();
		return permissions;	
	}
}
