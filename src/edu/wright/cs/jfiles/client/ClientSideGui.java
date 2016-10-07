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

	// Global Variables used because JavaFX does not pass parameters well.

	/**
	 * This method is where most visual elements are created and manipulated.
	 */
	@Override
	public void start(Stage primaryStage) {

		// Pane Creation
		BorderPane basePane = new BorderPane();
		basePane.setStyle("-fx-background-color: LIGHTGREY;");
		// Sets basePane's color

		// Create MenuBar on window Top
		MenuBar headderMenuBar = new MenuBar();
		// headderMenuBar.prefWidthProperty().bind(primaryStage.widthProperty());
		// Line not needed because it is already set as default
		basePane.setTop(headderMenuBar);

		// Create File menu with open and close items
		Menu fileMenu = new Menu("File");// Creates Menu Name
		MenuItem openMenuItem = new MenuItem("Open");// Creates Menu Item
		MenuItem closeMenuItem = new MenuItem("Close");

		// Add Menu Items and a Separator to File Menu. Separate Close option
		// for visual distinction.
		fileMenu.getItems().addAll(openMenuItem, new SeparatorMenuItem(), closeMenuItem);

		closeMenuItem.setOnAction(actionEvent -> Platform.exit());
		// Exits the window if clicked

		// Add the menu to the menu bar
		headderMenuBar.getMenus().addAll(fileMenu);

		// Scene Creation. Put the basePane on the scene.
		Scene scene = new Scene(basePane, 1200, 600, Color.WHITE);
		// addedPain, Width, Height, backgroundColor
		// scene color is buried under the basePane and is not currently visible

		// Manipulating the primaryStage or "window"
		primaryStage.setTitle("JFiles");// Sets the title on the window itself
		primaryStage.setScene(scene);// Adds the scene to the stage. Takes up
										// the whole window.
		primaryStage.setResizable(true);// User Resizing Allowed
		primaryStage.show();// Displays the start Stage and its contents.

	} // end start method

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
		launch(args);// Launches the GUI
	} // end of method Main

} // end ClientSideGUI class
