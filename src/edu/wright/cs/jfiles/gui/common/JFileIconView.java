/*
 * Copyright (C) 2016 - WSU CEG3120 Students
 *
 * Roberto C. Sánchez <roberto.sanchezwright.edu>
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

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * A view which displays an icon for an image and its file name.
 *
 * @author Matt Gilene
 *
 */
public class JFileIconView extends BorderPane {

	private String name;
	private FileStruct.Type type;
	private String ext;

	private ImageView image;
	private Label label;

	private ContextMenu contextMenu;

	private final int height = 80;
	private final int width = 80;

	private static Image fileImage;
	private static Image directoryImage;

	static {
		fileImage = new Image("file:src/edu/wright/cs/jfiles/resources/images/file_icon.png");
		directoryImage =
				new Image("file:src/edu/wright/cs/jfiles/resources/images/folder_icon.png");
	}

	/**
	 * Default constructor.
	 */
	public JFileIconView() {
		this("", "", FileStruct.Type.FILE);
	}

	/**
	 * Creates a JFileIconView with with the given filename, extension, and
	 * type.
	 *
	 * @param name
	 *            name of the file
	 * @param ext
	 *            extension of the file
	 * @param type
	 *            type of the file
	 */
	public JFileIconView(String name, String ext, FileStruct.Type type) {
		super();

		this.name = name;
		this.ext = ext;
		this.type = type;

		setMaxWidth(width);
		setMaxHeight(height);

		image = new ImageView();
		image.setFitHeight(height);
		image.setFitWidth(width);

		label = new Label(name);
		label.setWrapText(true);
		label.setMaxWidth(width);

		switch (type) {
		case FILE:
			image.setImage(fileImage);
			break;
		case DIRECTORY:
			image.setImage(directoryImage);
			break;
		default:
			break;
		}

		// Layout items
		setCenter(image);
		setBottom(label);

		// Set style
		setStyle("-fx-font-size: 15px;" + "-fx-font-family: 'Currier New' ;"
				+ "-fx-text-fill: black;" + "-fx-base: #85C1E9;");

		// Register event handlers
		setOnContextMenuRequested(event -> contextMenu.show((JFileIconView) event.getSource(),
				event.getScreenX(), event.getScreenY()));
	}

	/**
	 * Gets the name of the file.
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the type of the file.
	 *
	 * @return type
	 */
	public FileStruct.Type getType() {
		return type;
	}

	/**
	 * Gets the extension on the file.
	 *
	 * @return ext
	 */
	public String getExt() {
		return ext;
	}

	/**
	 * Sets this views context menu.
	 *
	 * @param menu
	 *            the menu to show
	 */
	public void setContextMenu(ContextMenu menu) {
		contextMenu = menu;
	}
}
