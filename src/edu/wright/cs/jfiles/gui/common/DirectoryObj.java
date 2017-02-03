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

import edu.wright.cs.jfiles.commands.Cd;
import edu.wright.cs.jfiles.core.FileStruct;

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
	/*
	 * Event handler will call upon the open() method and grab the file path
	 * Pass it through a JDirectoryIconView object call populate(filepath) If I
	 * don't write this down I will forget it
	 */

	@FXML
	private ImageView image;
	private String filepath;

	/**
	 * Default constructor.
	 * @param filepath.
	 */
	public DirectoryObj(String filepath) {
		this.filepath = filepath;
	}

	/** Returns image to be used in the gui.
	 * @return image
	 */
	public ImageView assignImage() {
		return image;
	}

	/**
	 * Sends an event handler the file path to be used.
	 * @param currPath.
	 * @return filepath.
	 */
	public String open(String currPath) {
		filepath = currPath + "/" + filepath;

		return filepath;
	}
}
