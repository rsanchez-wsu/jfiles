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
import edu.wright.cs.jfiles.core.XmlHandler;

import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.ContextMenu;
import javafx.scene.layout.FlowPane;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * A view which displays the given file structure in a flow layout using file
 * icons.
 *
 * @author Matt Gilene
 */
public class JDirectoryIconView extends FlowPane {

	private Map<FileStruct, JFileIconView> contents;

	private Map<FileStruct.Type, ContextMenu> contextMenus;

	/**
	 * Default Constructor. This constructor creates an empty JDirectoryIconView
	 */
	public JDirectoryIconView() {
		super();
		contents = new HashMap<>();
		contextMenus = new HashMap<>();

		this.setRowValignment(VPos.TOP);
	}

	/**
	 * Populates view with local files given by path.
	 *
	 * @param path
	 *            The path of this directory
	 */
	public void populateLocal(String path) {
		// Build xml for given directory
		XmlHandler xmlHandler = new XmlHandler(path);

		populate(xmlHandler);
	}

	/**
	 * Populates view with remote files given by xml.
	 *
	 * @param xml
	 *            The xml structure of the directory
	 */
	public void populateRemote(String xml) {
		// Build xml for given directory
		XmlHandler xmlHandler = new XmlHandler();
		InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
		xmlHandler.readXml(new InputStreamReader(stream, StandardCharsets.UTF_8));

		populate(xmlHandler);
	}

	/**
	 * Populates the view with data from xmlHandler.
	 *
	 * @param xmlHandler
	 *            the xmlHandler
	 */
	private void populate(XmlHandler xmlHandler) {
		for (FileStruct file : xmlHandler.getFiles()) {
			String name = (String) file.getValue("name");
			String ext = name.split("[.]")[1];
			FileStruct.Type type = file.getType();

			JFileIconView iconView = new JFileIconView(name, ext, type);
			if (contextMenus.containsKey(type)) {
				iconView.setContextMenu(contextMenus.get(type));
			}

			JDirectoryIconView.setMargin(iconView, new Insets(5, 5, 5, 5));
			getChildren().add(iconView);

			contents.put(file, iconView);
		}
	}

	/**
	 * Adds a context menu to the view.
	 *
	 * @param menu
	 *            the menu to add
	 * @param type
	 *            the type of file to display for
	 */
	public void addContextMenu(ContextMenu menu, FileStruct.Type type) {
		// Register menu for the future
		contextMenus.put(type, menu);

		// Update all current children
		for (JFileIconView view : contents.values()) {
			if (view.getType() == type) {
				view.setContextMenu(menu);
			}
		}
	}
}
