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
import java.util.Map;

/**
 * Data Struct for XML generation.
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
	private Map<String, String> attrList = new HashMap<>();
	private Type type;
	
	/**
	 * An attempt was made.
	 */
	public FileStruct() {
		
	}
	
	/**
	 * This method accepts a File object, any type including directories, and creates an array
	 * of JFile objects wrapped in a class to support serialization. 
	 * 
	 * @param input Input file or path to generate JFile(s) from
	 * @throws IOException Throws IOException when file at path cannot be accessed
	 */
	public FileStruct(Path input) throws IOException {
		populateArray(input);
	}
	
	/**
	 * Helper method to populate the attribute array.
	 * @throws IOException If object passed is inaccessible
	 */
	private void populateArray(Path path) throws IOException {
		if (path == null) {
			return;
		}
		//Populates each JFile with a complete K/V map of all basic file attributes	
		if (Files.isRegularFile(path)) {
			this.setType(Type.FILE);
		} else if (Files.isDirectory(path)) {
			this.setType(Type.DIRECTORY);
		} else if (Files.isSymbolicLink(path)) {
			this.setType(Type.SYMBOLICLINK);
		} else {
			this.setType(Type.OTHER);
		}
		
		attrList.put("name", path.toFile().getName());
		attrList.put("lastModifiedTime", Files.getAttribute(path, "lastModifiedTime").toString());
		attrList.put("lastAccessTime", Files.getAttribute(path, "lastAccessTime").toString());
		attrList.put("creationTime", Files.getAttribute(path, "creationTime").toString());
		attrList.put("size", Files.getAttribute(path, "size").toString());
		
		//Populates with DOS or POSIX attributes
		if (Files.getFileStore(path).supportsFileAttributeView(DosFileAttributeView.class)) {
			DosFileAttributes attrs = Files.getFileAttributeView(
					path, DosFileAttributeView.class).readAttributes();
			
			attrList.put("system", "DOS");
			attrList.put("readOnly", String.valueOf(attrs.isReadOnly()));
			attrList.put("hidden", String.valueOf(attrs.isHidden()));
			attrList.put("system", String.valueOf(attrs.isSystem()));
			attrList.put("archive", String.valueOf(attrs.isArchive()));				
		} else if (Files.getFileStore(path)
				.supportsFileAttributeView(PosixFileAttributeView.class)) {
			PosixFileAttributes attrs = Files.getFileAttributeView(
					path, PosixFileAttributeView.class).readAttributes();
			
			attrList.put("system", "POSIX");
			attrList.put("group", attrs.group().getName());
			attrList.put("owner", attrs.owner().getName());
			attrList.put("permissions", stringifyPermissions(path, attrs));
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
		if (Files.isDirectory(path)) {
			permissions += "d";
		} else if (Files.isSymbolicLink(path)) {
			permissions += "l";
		} else if (Files.isRegularFile(path)) {
			permissions += "-";
		} else {
			permissions += " ";
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
	 * Default.
	 * @return the attrList
	 */
	protected Map<String, String> getAttrList() {
		return attrList;
	}
	
	/**
	 * Default.
	 * @param attrList the attrList to set
	 */
	protected void setAttrList(Map<String, String> attrList) {
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
	 * getValue searches the attrList for an attribute corresponding to the given string
	 * @param name is the key to search for an attribute
	 * @return the corresponding value if the attribute exists. Otherwise returns an empty string
	 */
	public String getValue(String name) {
		String attribute = "";
		if (attrList.containsKey(name)) {
			attribute = attrList.get(name);
		}
		return attribute;
	}
	
	/**
	 * getKeys returns an array of its keys.
	 * @return an array of the keys of the attList.
	 */
	public String[] getKeys() {
		Collection<String> attNames = attrList.keySet();
		String [] namesArray = attNames.toArray(new String[attNames.size()]); 
		return namesArray;
	}
	
	/**
	 * contains determines if the attrList has a value for the given key.
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
}
