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

package edu.wright.cs.jfiles.gui.common;

import edu.wright.cs.jfiles.core.FileStruct;
import edu.wright.cs.jfiles.gui.client.ClientAppViewController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Controller for FileIconViews.
 *
 * @author Matt
 *
 */
public class FileIconViewController implements Initializable {

	private static Image fileImage;
	private static Image directoryImage;

	static {
		fileImage = new Image("file:src/edu/wright/cs/jfiles/resources/images/file_icon.png");
		directoryImage =
				new Image("file:src/edu/wright/cs/jfiles/resources/images/folder_icon.png");
	}

	/**
	 * The model for this view.
	 */
	private FileStruct fileStruct;

	/**
	 * The main app view controller.
	 */
	private ClientAppViewController appController;

	// These are injected automatically by the FXML loading system when the view
	// associated with this controller is loaded.
	@FXML
	private BorderPane root;

	@FXML
	private Label label;

	@FXML
	private ImageView image;

	/**
	 * Sets the FileStruct this icon represents.
	 *
	 * @param file
	 *            the FileStruct to load
	 */
	public void setFileStruct(FileStruct file) {
		fileStruct = file;
		populate();
	}

	/**
	 * Sets the size of the icon.
	 *
	 * @param size
	 *            Size enum value to set
	 */
	public void setSize(Size size) {
		root.setMaxWidth(size.getIntValue());
		root.setMaxHeight(size.getIntValue());
		image.setFitWidth(size.getIntValue());
		image.setFitHeight(size.getIntValue());
	}

	/**
	 * Registers the AppViewController with this view.
	 *
	 * @param controller
	 *            the main app controller
	 */
	public void registerAppController(ClientAppViewController controller) {
		appController = controller;
	}

	/**
	 * Populates image and label with correct values.
	 */
	private void populate() {
		label.setText((String) fileStruct.getValue("name"));
		switch (fileStruct.getType()) {
		case FILE:
			image.setImage(fileImage);
			break;
		case DIRECTORY:
			image.setImage(directoryImage);
			break;
		default:
			image.setImage(fileImage);
		}
	}

	/**
	 * Handles the mouse click event.
	 */
	@FXML
	public void handleMouseClick() {
		appController.setSelectedFile(fileStruct);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setSize(Size.MEDIUM);

		label.setWrapText(true);
		label.maxWidthProperty().bind(image.fitWidthProperty());
		label.setAlignment(Pos.CENTER);
	}

	/**
	 * Enum to declare various icon sizes.
	 *
	 * @author Matt
	 *
	 */
	public enum Size {
		SMALL(60), MEDIUM(80), LARGE(100);
		int size;

		/**
		 * Enum constructor.
		 *
		 * @param intVal
		 *            integer size
		 */
		private Size(int intVal) {
			size = intVal;
		}

		/**
		 * Gets the integer value for this size.
		 *
		 * @return integer value of size
		 */
		private int getIntValue() {
			return size;
		}
	}
}
