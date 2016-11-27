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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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

	// Variables
	String username = "";
	String password = "";
	Button exitButton;
	Button connectButton;
	Label noConnectionLabel;
	Label invalidLabel;
	Label emptyUsernameFieldLabel;
	Label emptyPasswordFieldLabel;
	TextField usernameTextField;
	TextField passwordTextField;

	/**
	 * This method is where most visual elements are created and manipulated.
	 */
	@Override
	public void start(Stage primaryStage) {

		// Login Window Construction

		// Label Creation
		Label programNameLabel;
		programNameLabel = createLoginLabel("JFiles", "#01DF01", "Algerian", 60);
		Label usernameLabel;
		usernameLabel = createLoginLabel("Username", "#0101DF", "Currier New", 20);
		Label passwordLabel;
		passwordLabel = createLoginLabel("Password", "#0101DF", "Currier New", 20);

		// Error Labels
		noConnectionLabel = createErrorLabel("No Connection Detected.", false, "#FF0000");
		invalidLabel = createErrorLabel("Incorrect Username & Password Combination.", false,
				"#FF0000");
		emptyUsernameFieldLabel = createErrorLabel("Username Field Must Be Filled In.", false,
				"#FF0000");
		emptyPasswordFieldLabel = createErrorLabel("Password Field Must Be Filled In.", false,
				"#FF0000");

		// Text Field Creation
		usernameTextField = new TextField();
		usernameTextField.setPromptText("Enter your Username.");
		usernameTextField.setPrefColumnCount(25);
		usernameTextField.getText();

		passwordTextField = new TextField();
		passwordTextField.setPromptText("Enter your Password.");
		passwordTextField.setPrefColumnCount(25);
		passwordTextField.getText();

		// Button Styling
		exitButton = new Button("Exit");
		exitButton.setStyle("-fx-font-size: 20px;" + "-fx-font-family: 'Currier New' ;"
				+ "-fx-text-fill: black;" + "-fx-base: #85C1E9;");

		connectButton = new Button("Connect");
		connectButton.setStyle("-fx-font-size: 20px;" + "-fx-font-family: 'Currier New' ;"
				+ "-fx-text-fill: black;" + "-fx-base: #85C1E9;");

		// Adding DropShadow s
		exitButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new AddExitDropShadow());
		connectButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new AddConnectDropShadow());
		// Removing DropShadows
		exitButton.addEventHandler(MouseEvent.MOUSE_EXITED, new RemoveExitDropShadow());
		connectButton.addEventHandler(MouseEvent.MOUSE_EXITED, new RemoveConnectDropShadow());

		// connectButton Action
		connectButton.setOnAction(new ConnectButtonClicked());

		// exitButton action closes the program
		exitButton.setOnAction(actionEvent -> Platform.exit());

		// HBox For BorderPane Top Alignment
		HBox loginHbox = new HBox();
		// Padding Top, Left Bottom, Right
		loginHbox.setPadding(new Insets(5, 5, 5, 70));
		loginHbox.getChildren().add(programNameLabel);

		// Error Label StackPain
		StackPane loginStack = new StackPane();
		loginStack.getChildren().add(noConnectionLabel);
		loginStack.getChildren().add(invalidLabel);
		loginStack.getChildren().add(emptyUsernameFieldLabel);
		loginStack.getChildren().add(emptyPasswordFieldLabel);

		// VBox for BoarderPane Center Alignment
		VBox loginVbox = new VBox();
		loginVbox.setPadding(new Insets(5, 20, 5, 20));
		loginVbox.getChildren().add(usernameLabel);
		loginVbox.getChildren().add(usernameTextField);
		loginVbox.getChildren().add(passwordLabel);
		loginVbox.getChildren().add(passwordTextField);
		loginVbox.getChildren().add(loginStack);

		// HBox For BorderPane Bottom Alignment
		HBox loginHbox2 = new HBox();
		loginHbox2.setPadding(new Insets(5, 5, 20, 60));
		loginHbox2.getChildren().add(exitButton);
		loginHbox2.getChildren().add(connectButton);
		loginHbox2.setSpacing(40.0);

		BorderPane loginBorderPane = new BorderPane();
		loginBorderPane.setTop(loginHbox);
		loginBorderPane.setCenter(loginVbox);
		loginBorderPane.setBottom(loginHbox2);

		Scene loginScene = new Scene(loginBorderPane, 300, 280);

		Stage loginStage = new Stage();
		loginStage.setTitle("JFiles Login Screen");
		loginStage.setScene(loginScene);
		loginStage.setResizable(false);
		// Displays the start Stage and its contents.
		loginStage.show();

		// Main Window Creation

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
	 * Description: This method creates a Login Screen Label with the passed
	 * parameters.
	 *
	 * @param name
	 *            : The label's name
	 * @param font
	 *            : The font style of the label
	 * @param color
	 *            : The font color of the label
	 * @param fontSize
	 *            : The font size of the label
	 * @return: returns the created label
	 */
	Label createLoginLabel(String name, String color, String font, int fontSize) {
		Label label = new Label(name);
		label.setTextFill(Color.web(color));
		label.setFont(Font.font(font, FontWeight.BOLD, fontSize));
		return label;
	}

	/**
	 * Description: This method creates a Error Label with the passed
	 * parameters.
	 *
	 * @param name
	 *            : The label's name
	 * @param visible
	 *            : The label's state of visibility
	 * @param color
	 *            : The font color of the label
	 * @return: returns the created label
	 */
	Label createErrorLabel(String name, boolean visible, String color) {
		Label label = new Label(name);
		label.setTextFill(Color.web(color));
		label.setVisible(visible);
		return label;
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

	/**
	 * EventHandler for when the connectButton is clicked. Collects the users's
	 * input or displays appropriate error messages.
	 */
	public class ConnectButtonClicked implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent connectButtonClicked) {
			// Hide Old Error Labels
			noConnectionLabel.setVisible(false);
			invalidLabel.setVisible(false);
			emptyUsernameFieldLabel.setVisible(false);
			emptyPasswordFieldLabel.setVisible(false);
			// Display New Error Labels
			if ((usernameTextField.getText() == null
					|| usernameTextField.getText().trim().isEmpty())) {
				emptyUsernameFieldLabel.setVisible(true);
			} else if ((passwordTextField.getText() == null
					|| passwordTextField.getText().trim().isEmpty())) {
				emptyPasswordFieldLabel.setVisible(true);
			} else {
				username = usernameTextField.getText();
				password = passwordTextField.getText();
				// Just uses the variables to shutup FindBugs
				System.out
						.println("Username = " + username + "\n" + "Password = " + password + "\n");
			}
		}
	}

	/**
	 * Adds a DropShadow to the exitButton when it is moused over.
	 */
	public class RemoveConnectDropShadow implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent connectButtonMouseNotOver) {
			connectButton.setEffect(null);
		}
	}

	/**
	 * Adds a DropShadow to the connect button when it is moused over.
	 */
	public class RemoveExitDropShadow implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent exitButtonMouseNotOver) {
			exitButton.setEffect(null);
		}
	}

	/**
	 * Removes the DropShadow from the exitButton when the mouse has left it.
	 */
	public class AddExitDropShadow implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent exitButtonMouseOver) {
			DropShadow dropShadow = new DropShadow();
			exitButton.setEffect(dropShadow);
		}
	}

	/**
	 * Removes the DropShadow from the connectButton when the mouse has left it.
	 */
	public class AddConnectDropShadow implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent exitButtonMouseOver) {
			DropShadow dropShadow = new DropShadow();
			connectButton.setEffect(dropShadow);
		}
	}

}
