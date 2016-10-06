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

import java.io.File;
import java.util.ArrayList;

/**
 * This is the Class of holding and managing the actual content of files. It is
 * use by the JFileManager class to manipulate files.
 * 
 * @author Brand Allred
 * @author Team 5
 *
 */
public class JFile  implements Cloneable  {

	private File file;
	private ArrayList<Tag> tagList = new ArrayList<>();

	/**
	 * Default constructor.
	 */
	protected JFile() {
	}

	/**
	 * This is the file for storing files in the JFile object.
	 * 
	 * @param file
	 *            The file being stored in the JFile object.
	 */
	protected JFile(File file) {
		this.file = file;
	}
	
	/**
	 * Stores the file as given by the string.
	 * 
	 * @param path Path to wanted file.
	 */
	protected JFile(String path) {
		this.file = new File(path);
	}
	
	/**
	 * Deletes JFile's File contents.
	 * 
	 */
	protected void deleteContents() /* throws IOException */{

		/*
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
	protected boolean rename(String name) {

		if (name.contains("/") || name.contains("\\")) {
			return false;
		}

		file.renameTo(new File(file.getParent() + name));
		return true;

	}

	/**
	 * Checks to see if the current file has the wanted tag.
	 * 
	 * @param tagName
	 *            String that the tag can go by.
	 * @return True if the File has the tag, false if it doesn't.
	 */
	protected boolean hasTag(String tagName) {
		if (searchTags(tagName).getstrId().compareTo("") != 0) {
			return true;
		}
		return false;
	}

	/**
	 * Checks to see if the current file has the wanted tag.
	 * 
	 * @param intId
	 *            Integer that the tag can go by.
	 * @return True if the file has the tag, false if it doesn't.
	 */
	protected boolean hasTag(int intId) {
		if (searchTags(intId).getintId() != -1) {
			return true;
		}
		return false;
	}

	/**
	 * Checks to see if the current file has the wanted tag.
	 * 
	 * @param tag
	 *            The actual tag that the wanted tag may be.
	 * @return True if the file has the tag, false if it doesn't.
	 */
	protected boolean hasTag(Tag tag) {
		if (!searchTags(tag).equals(new Tag("", -1))) {
			return true;
		}
		return false;
	}

	/**
	 * Gives the file the specified tag.
	 * 
	 * @param strId
	 *            String to identify the tag with.
	 * @param intId
	 *            Integer to identify the tag with.
	 */
	protected void giveTag(String strId, int intId) {
		tagList.add(new Tag(strId, intId));
	}

	/**
	 * Gives the file the specified tag.
	 * 
	 * @param tag
	 *            Tag to give to the file.
	 */
	protected void giveTag(Tag tag) {
		tagList.add(tag);
	}

	/**
	 * Takes away the tag specified by a string.
	 * 
	 * @param strId
	 *            String to find the tag with.
	 * @return True if there is a similar tag, false otherwise.
	 */
	protected boolean revokeTag(String strId) {
		if (tagList.remove(searchTags(strId))) {
			return true;
		}
		return false;
	}

	/**
	 * Takes away the tag specified by an integer.
	 * 
	 * @param intId
	 *            Integer to find the tag with.
	 * @return True if there was a tag with the specified integer, false
	 *         otherwise
	 */
	protected boolean revokeTag(int intId) {
		if (tagList.remove(searchTags(intId))) {
			return true;
		}
		return false;
	}

	/**
	 * Takes away the tag specified by another tag.
	 * 
	 * @param tag
	 *            Tag to revoke
	 * @return True if there was a tag to be removed, false otherwise.
	 */
	protected boolean revokeTag(Tag tag) {
		if (tagList.remove(searchTags(tag))) {
			return true;
		}
		return false;
	}

	/**
	 * Searches for Tags using the given object. If the Object is not a string,
	 * integer, or tag, it will fail and return a tag with "" and -1 as the
	 * string and integer. Otherwise, as long as the file has the tag, the tag
	 * will be returned.
	 * 
	 * @param thing
	 *            The thing to search by (will change in future).
	 * @return The tag specified by the search object. If there is no tag found,
	 *         at tag with "" as the string id and -1 as the integer id is
	 *         returned instead.
	 */
	private <E> Tag searchTags(E thing) {

		if (thing instanceof String) {
			for (int i = 0; i < tagList.size(); i++) {
				if (tagList.get(i).getstrId().compareTo((String) thing) == 0) {
					return tagList.get(i);
				}
			}
		} else if (thing instanceof Tag) {
			for (int i = 0; i < tagList.size(); i++) {
				if (tagList.get(i).equals(thing)) {
					return tagList.get(i);
				}
			}
		} else {
			for (int i = 0; i < tagList.size(); i++) {
				if (tagList.get(i).getintId() == (Integer) thing) {
					return tagList.get(i);
				}
			}
		}

		return new Tag("", -1);
	}

	/**
	 * gets the file's type. TODO: getType()
	 */
	protected void getType() {
		/*
		 * if (System.getProperty("os.name").contains("Windows")) {
		 * System.out.println(Files.getFileExtension(file.getName())); }
		 */
	}
	
	/**
	 * Gets the raw size of the current file.
	 * @return A long that represents the raw size of the file.
	 */
	protected long getFileSize() {
		return file.length();
	}
}
