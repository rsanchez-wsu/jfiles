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

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;

public class FilePathTreeItem extends TreeItem<String> {
	private String fullPath;

	public String getFullPath() {
		return this.fullPath;
	}

	private boolean isExpanded;

	public boolean getIsExpanded() {
		return this.isExpanded;
	}

	public FilePathTreeItem(String file) {
		this.fullPath = file;
		this.addEventHandler(TreeItem.branchExpandedEvent(), new EventHandler() {
			@Override
			public void handle(Event e) {
				FilePathTreeItem source = (FilePathTreeItem) e.getSource();

			}
		});
	}
}
