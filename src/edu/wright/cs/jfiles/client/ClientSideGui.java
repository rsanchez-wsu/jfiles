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

package edu.wright.cs.jfiles.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * This class will form the body of the JFiles client side GUI application. This
 * is a JavaFX application.
 * 
 * @author Jason Phares &lt;phares.705@gmail.com&gt;
 * @author Roberto C. Sánchez &lt;roberto.sanchez@wright.edu&gt; (I used some of
 *         his code.)
 *
 */
public class ClientSideGui extends Application {

	/**
	 * This method is where most visual elements are created and manipulated.
	 */
	@Override
	public void start(Stage primaryStage) {

		// Pane Creation
		BorderPane basePane = new BorderPane();
		// Sets basePane's color
		basePane.setStyle("-fx-background-color: LIGHTGREY;");

		// Create MenuBar on window Top
		MenuBar headderMenuBar = new MenuBar();
		basePane.setTop(headderMenuBar);

		// Create Menus
		Menu fileMenu = new Menu("File");
		Menu editMenu = new Menu("Edit");

		// Create File Menu Items
		MenuItem openFileMenuItem = new MenuItem("Open");
		MenuItem closeMenuItem = new MenuItem("Close");
		// Create Edit Menu Items
		MenuItem createMenuItem = new MenuItem("Create");
		MenuItem openMenuItem = new MenuItem("Open");
		MenuItem deleteMenuItem = new MenuItem("Delete");
		MenuItem copyMenuItem = new MenuItem("Copy");
		MenuItem pasteMenuItem = new MenuItem("Paste");
		MenuItem cutMenuItem = new MenuItem("Cut");

		// Add Menu Items and a Separator to Menu.
		fileMenu.getItems().addAll(openFileMenuItem, new SeparatorMenuItem(), closeMenuItem);
		editMenu.getItems().addAll(createMenuItem, openMenuItem, deleteMenuItem, copyMenuItem,
				pasteMenuItem, cutMenuItem);

		// Exits the window if Close is clicked
		closeMenuItem.setOnAction(actionEvent -> Platform.exit());

		// Add the menus to the menu bar
		headderMenuBar.getMenus().addAll(fileMenu, editMenu);

		// Scene Creation. Put the basePane on the scene.
		Scene scene = new Scene(basePane, 1200, 600, Color.WHITE);

		// Manipulating the primaryStage or "window"
		primaryStage.setTitle("JFiles");
		// Adds the scene to the stage. Takes up the whole window.
		primaryStage.setScene(scene);
		// User Resizing Allowed
		primaryStage.setResizable(true);
		// Displays the start Stage and its contents.
		primaryStage.show();

	}

	/**
	 * Main method. This method is where the program starts in this class. It
	 * launches the GUI.
	 * 
	 * @param args
	 *            The command-line arguments
	 * 
	 */
	public static void main(String[] args) {

		System.out.println("Launching Client Main GUI Window.");
		// Launches the GUI
		launch(args);
	}

}
