/*
 * Copyright (C) 2017 - WSU CEG3120 Students
 *
 *
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

package edu.wright.cs.jfiles.gui.common;

import edu.wright.cs.jfiles.core.FileStruct;
import edu.wright.cs.jfiles.core.PathStack;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * An object to hold a file path and image for directories in the GUI.
 * @author Ehteshami and Barney
 *
 */
public class DirectoryObj {

	private static Image directoryImage;

	static {
		directoryImage =
				new Image("file:src/edu/wright/cs/jfiles/resources/images/folder_icon.png");
	}
	/**
	 * Event handler will call upon the open() method and grab the file path.
	 * Pass it through a JDirectoryIconView object call populate(filepath) If I
	 * don't write this down I will forget it
	**/

	@FXML
	private ImageView image;
	private String filepath;
	private String lastPath;
	private PathStack newDirr;

	/**
	 * Default Constructor, everything is set to root.
	 */
	public DirectoryObj() {
		newDirr = new PathStack();
		newDirr.root();

		filepath = ".";
		lastPath = ".";
	}

	/**
	 * Constructor for a PathStack being passed through.
	 * Writes the filepath then updates
	 */
	public DirectoryObj(PathStack currPath) {
		newDirr = new PathStack();
		newDirr.root();

		filepath = currPath.toPath();
		update();
	}

	/**
	 * Constructor for a String with the path.
	 * breaks up the string and populates the PathStack with it
	 */
	public DirectoryObj(String filepath) {
		newDirr = new PathStack();
		newDirr.root();

		this.filepath = filepath;
		update();
	}

	/**
	 * As long as the filepath is up to date this method will be used to populate the fields.
	 */
	public void update() {
		String[] pathHolder;

		pathHolder = this.filepath.split("/");
		for (int i = 0; i < pathHolder.length; i++) {
			newDirr.push("/" + pathHolder[i]);
		}
		lastPath = newDirr.peek();
	}

	/** Returns image to be used in the gui.
	 * @return image
	 */
	public ImageView getImage() {
		return image;
	}

	/**
	 * Set image to be used in the gui.
	 */
	public void setImage(ImageView image) {
		this.image = image;
	}

	/**
	 * return whole filepath for gui name.
	 * @return String
	 */
	public String getFilePath() {
		return filepath;
	}

	/**
	 * Set the file path and update the directory.
	 * This is meant for multiple directories in one string.
	 */
	public void setFilePath(String path) {
		newDirr.root();

		String[] pathHolder = path.split("/");
		for (int i = 0; i < pathHolder.length; i++) {
			newDirr.push("/" + pathHolder[i]);
		}
	}

	/**
	 * Return the directory the object is in.
	 * @return String
	 */
	public String getLastPath() {
		return lastPath;
	}

	/**
	 * Set the lastPath as needed.
	 */
	public void setLastPath(String lastPath) {
		this.lastPath = lastPath;
	}
}
