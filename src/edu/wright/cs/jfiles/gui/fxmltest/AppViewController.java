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

package edu.wright.cs.jfiles.gui.fxmltest;

import edu.wright.cs.jfiles.core.FileStruct;
import edu.wright.cs.jfiles.core.XmlHandler;
import edu.wright.cs.jfiles.gui.fxmltest.FileIconViewController.Size;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for main application view.
 *
 * @author Matt
 *
 */
public class AppViewController implements Initializable {

	private FileStruct selectedFile;

	@FXML
	BorderPane root;

	@FXML
	TreeView<String> treeView;

	@FXML
	FlowPane flowPane;

	/**
	 * Loads the given directory into the view.
	 *
	 * @param path
	 *            the path to load
	 */
	private void loadDirectory(String path) {
		XmlHandler handler = new XmlHandler(path);
		for (FileStruct file : handler.getFiles()) {
			FXMLLoader loader =
					new FXMLLoader(FileIconViewController.class.getResource("FileIconView.fxml"));
			try {
				final Parent view = loader.load();
				FileIconViewController controller = loader.getController();

				controller.setFileStruct(file);
				controller.setSize(Size.MEDIUM);
				controller.registerAppController(this);

				flowPane.getChildren().add(view);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets the current selected file.
	 *
	 * @param fs
	 *            fileStruct to set
	 */
	public void setSelectedFile(FileStruct fs) {
		selectedFile = fs;
		System.out.println(selectedFile.toString());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadDirectory("./src/edu/wright/cs/jfiles/core");
	}
}
