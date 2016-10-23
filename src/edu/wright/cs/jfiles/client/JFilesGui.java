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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * The main class of the JFiles client GUI application. This is a JavaFX
 * application.
 * 
 * @author Roberto C. Sánchez &lt;roberto.sanchez@wright.edu&gt;
 *
 */
public class JFilesGui extends Application {

	private Button btn;

	/**
	 * Handles allocating resources needed for the GUI.
	 */
	public JFilesGui() {
		// Create the button
		btn = new Button();
	}

	@Override
	public void start(Stage primaryStage) {
		btn.setText("Click me please!");
		btn.setOnAction(e -> buttonClick());

		// Add the button to a layout pane
		BorderPane pane = new BorderPane();
		pane.setCenter(btn);
		// Add the layout pane to a scene
		Scene scene = new Scene(pane, 300, 250);

		// Finalize and show the stage
		primaryStage.setScene(scene);
		primaryStage.setTitle("JFiles");
		primaryStage.show();
	}

	/**
	 * Handle the button click events.
	 */
	public void buttonClick() {
		if ("Click me please!".equalsIgnoreCase(btn.getText())) {
			btn.setText("You clicked me!");
		} else {
			btn.setText("Click me please!");
		}
	}

	/**
	 * The main entry point to the program.
	 * 
	 * @param args The command-line arguments
	 */
	public static void main(String[] args) {
		System.out.println("Starting the GUI");
		launch(args);
	}

}
