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

/**
 * Blah.
 * 
 * @author John Wintersohle
 * @author Randy Musser
 *
 */
public class FileTest {

	/**
	 * Blah.
	 * 
	 */
	public static void testCut() {

	}

	/**
	 * This method shall copy an array of test files that comes with the program
	 * to the clipboard and display the results in the console. This method
	 * should then use the compareFiles method to verify that the files in the
	 * clipboard have the same contents as the files in the system.
	 * 
	 */
	public static void testCopy() {

	}

	/**
	 * This method shall arbitrarily assign an array of test files to the
	 * clipboard and then test the paste method by calling it and using the
	 * compareFiles method to verify that the files are the same.
	 * 
	 */
	public static void testRegPaste() {

	}

	/**
	 * This tests both the move method of JFileManager as well as the version of
	 * paste that it uses. After copying the file, this method shall call the
	 * compareFiles method to ensure that the contents of the files are the same
	 * before delete the original files. From there, this method shall delete
	 * the original files and make sure that they are deleted from the
	 * filesystem.
	 * 
	 * <p>This method MUST call the testDelete method before testing the actual
	 * move method. This is because, since the paste method in move is private,
	 * and needs to stay private, we cannot access this form of the paste method
	 * outside of the move method. This, in turn, means that we need to come up
	 * with a way of singling out the
	 * 
	 */
	public static void testMove() {

	}

	/**
	 * This method shall test the delete method of JFileManager. After deleting
	 * the files, this method shall verify that these files are deleted.
	 * 
	 */
	public static void testDelete() {

	}

	/**
	 * Blah.
	 * 
	 */
	public static void testRename() {

	}

	/**
	 * Blah.
	 * 
	 */
	public static void testOpen() {

	}

	/**
	 * Blah.
	 * 
	 */
	public static void testOpenWith() {

	}

	/**
	 * Blah.
	 * 
	 */
	public static void testGetType() {

	}

	/**
	 * This confirm that the contents of two arrays of files are the same. This
	 * may be done by a separate method that goes character by character and
	 * checks to see if they are equal.
	 * 
	 * @param files1
	 *            The first array of files being compared.
	 * @param files2
	 *            The second array of files file being compared.
	 */
	public static boolean compareFiles(JFile[] files1, JFile[] files2) {
		return false;
	}
}
