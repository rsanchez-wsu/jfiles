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
 * <p>This is the class that provides functionality for managing JFile objects. Examples of things
 * this class is in charge of will be copying, deleting, moving, renaming, and pasting JFiles.</p>
 * 
 * <p>It is the goal of this class to work as a medium of communication between <b>security</b> 
 * (such as authentication, authorization, and accounting), <b>maintenance</b> (such as logging),
 * <b>the GUI</b> (such as searching), and <b>JFiles</b>, by incorporating the methods of all of
 * these other APIs into the File API of JFiles.</p>
 * 
 * <p>This also exists to separate the methods involved with manipulating JFiles from an external
 * point of view from JFiles themselves. This is done so that a GUI or CLI can better make use
 * of the File API.</p>
 * 
 * @author <b>Team 5:</b>
 * @author John Wintersohle II <<a href="mailto:Dorkatron199@aol.com">Dorkatron199@aol.com</a>>
 *
 */
public class JFileManager {
	
	/**
	 * This is the clipboard used when copying JFiles. It is capable of copying and pasting
	 * multiple JFiles at a time. It is made private so that it can only be accessed via
	 * the JFileManager methods for the sake of security.
	 * 
	 */
	//This is suppressed until we build in the functionality for this field.
	//This will most likely be when we make the paste() method.
	@SuppressWarnings("unused")
	private JFile[] clipboard;
	
	/**
	 * <p>Cuts the file passed in.</p>
	 * 
	 * <p>This method calls copy then delete on the file being passed in. Adds the file to the top
	 * of the paste stack then moves the file to the trash directory set up during
	 * configuration.</p>
	 * 
	 * @param files The files being cut.
	 * 
	 */
	public void cut(JFile[] files) {
		copy(files);
		delete(files);
	}
	
	/*
	 * This needs to be made into a deep copy so that the contents of the clipboard can still
	 * be accessed even of the user later deletes the files. I will work on this later.
	 * 
	 * Though the clone method is used, I am not happy until its exact functionality is defined
	 * in JFile.
	 */
	
	/**
	 * This method copies all of the files passed in to the clipboard.
	 * 
	 */
	public void copy(JFile[] files) {
		clipboard = files.clone();
	}
	
	/**
	 * Filler.
	 * 
	 */
	public void paste() {
		
	}
	
	/**
	 * Filler.
	 * 
	 */
	public void delete(JFile[] files) {
		
	}
	
	/**
	 * Filler.
	 * 
	 */
	public void move() {
		
	}
	
	/**
	 * Filler.
	 * 
	 */
	public void rename() {
		
	}
	
	/**
	 * Filler.
	 * 
	 */
	public void open() {
		
	}
	
	/**
	 * Filler.
	 * 
	 */
	public void openWith() {
		
	}
	
	/**
	 * Filler.
	 * 
	 */
	public void getDetails() {
		
	}
	
	/**
	 * Filler.
	 * 
	 */
	public void getType() {
		
	}
}
