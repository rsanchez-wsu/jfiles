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

package edu.wright.cs.jfiles.gui.client;

import javafx.scene.control.TreeItem;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;



/**
 * Used to create TreeItems that expand and allow us to browse the file hierarchy.
 * @author Ian and Kevin
 */

public class FilePathTreeItem extends TreeItem<Object> {
	private String fullPath;

	/**
	 * Grabs the path of the item.
	 * @return fullPath
	 */
	public String getFullPath() {
		return this.fullPath;
	}

	private boolean isExpanded;

	/**
	 * Grabs whether or not it is expanded.
	 * @return isExpanded
	 */
	public boolean getIsExpanded() {
		return this.isExpanded;
	}

	/**
	 * Creates the object and sets up the path which will be expanded.
	 */
	public FilePathTreeItem(Path file2) {
		super(file2.toString());
		this.fullPath = file2.toString();
		this.addEventHandler(TreeItem.branchExpandedEvent(), e -> {
			FilePathTreeItem source = (FilePathTreeItem)e.getSource();
			try {
				if (source.getChildren().isEmpty()) {
					Path path = Paths.get(source.getFullPath());
					BasicFileAttributes attr =
							Files.readAttributes(path, BasicFileAttributes.class);
					if (attr.isDirectory()) {
						DirectoryStream<Path> dir = Files.newDirectoryStream(path);
						for (Path file:dir) {
							FilePathTreeItem treeNode = new FilePathTreeItem(file);
							source.getChildren().add(treeNode);
						}
					}
				//If you want to rescan a directory go ahead and include an else statement
				}
				//I don't know what would cause this to throw exceptions
				//I'm just gonna have it print out what causes it
				//It should not have exceptions as far as I understand but just in case
			} catch (IOException x) {
				x.printStackTrace();
			}
		});
	}
}
