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

	/**
	 * This method is where most visual elements are created and manipulated.
	 */
	@Override
	public void start(Stage primaryStage) {

		// Variables
		String username = "";
		String password = "";

		// Login Window Construction
		// Label Creation
		Label programNameLabel = new Label("JFiles");
		programNameLabel.setTextFill(Color.web("#01DF01"));
		programNameLabel.setFont(Font.font("Algerian", FontWeight.BOLD, 60));

		Label usernameLabel = new Label("Username:");
		usernameLabel.setFont(Font.font("Currier New", FontWeight.BOLD, 20));
		usernameLabel.setTextFill(Color.web("#0101DF"));

		Label passwordLabel = new Label("Password:");
		passwordLabel.setFont(Font.font("Currier New", FontWeight.BOLD, 20));
		passwordLabel.setTextFill(Color.web("#0101DF"));

		// Error Labels
		Label noConnectionLabel = new Label("No Connection Detected.");
		Label invalidLabel = new Label("Incorrect Username & Password Combination.");
		Label emptyUsernameFieldLabel = new Label("Username Field Must Be Filled In.");
		Label emptyPasswordFieldLabel = new Label("Password Field Must Be Filled In.");
		noConnectionLabel.setVisible(false);
		noConnectionLabel.setTextFill(Color.web("#FF0000"));
		invalidLabel.setVisible(false);
		invalidLabel.setTextFill(Color.web("#FF0000"));
		emptyUsernameFieldLabel.setVisible(false);
		emptyUsernameFieldLabel.setTextFill(Color.web("#FF0000"));
		emptyPasswordFieldLabel.setVisible(false);
		emptyPasswordFieldLabel.setTextFill(Color.web("#FF0000"));

		// Text Field Creation
		TextField usernameTextField = new TextField();
		usernameTextField.setPromptText("Enter your Username.");
		usernameTextField.setPrefColumnCount(25);
		usernameTextField.getText();

		TextField passwordTextField = new TextField();
		passwordTextField.setPromptText("Enter your Password.");
		passwordTextField.setPrefColumnCount(25);
		passwordTextField.getText();

		// Button Creation
		// Image image name = new
		// Image(getClass().getResourceAsStream("imagename.png"));
		// Button button3 = new Button("Accept", new ImageView(imageOk));
		Button exitButton = new Button("Exit");
		exitButton.setStyle("-fx-font-size: 20px;" + "-fx-font-family: 'Currier New' ;"
				+ "-fx-text-fill: black;" + "-fx-base: #85C1E9;");

		// Image image name = new
		// Image(getClass().getResourceAsStream("imagename.png"));
		Button connectButton = new Button("Connect");
		connectButton.setStyle("-fx-font-size: 20px;" + "-fx-font-family: 'Currier New' ;"
				+ "-fx-text-fill: black;" + "-fx-base: #85C1E9;");

		// exitButton DropShadow
		DropShadow exitButtonShadow = new DropShadow();
		// Adding the shadow when the mouse cursor is on
		exitButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				exitButton.setEffect(exitButtonShadow);
			}
		});
		// Removing the shadow when the mouse cursor is off
		exitButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				exitButton.setEffect(null);
			}
		});

		// connectButton DropShadow
		DropShadow ConnectButtonShadow = new DropShadow();
		// Adding the shadow when the mouse cursor is on
		connectButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				connectButton.setEffect(ConnectButtonShadow);
			}
		});
		// Removing the shadow when the mouse cursor is off
		connectButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				connectButton.setEffect(null);
			}
		});

		// Setting an action for the connectButton
		connectButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				// Hide Old Error Labels
				noConnectionLabel.setVisible(false);
				invalidLabel.setVisible(false);
				emptyUsernameFieldLabel.setVisible(false);
				emptyPasswordFieldLabel.setVisible(false);

				if ((usernameTextField.getText() == null
						|| usernameTextField.getText().trim().isEmpty())) {
					// username = usernameTextField.getText();
					// password = passwordTextField.getText();
					emptyUsernameFieldLabel.setVisible(true);
				} else if ((passwordTextField.getText() == null
						|| passwordTextField.getText().trim().isEmpty())) {
					// username = usernameTextField.getText();
					// password = passwordTextField.getText();
					emptyPasswordFieldLabel.setVisible(true);
				}
				/*
				 * if correct
				 * username = usernameTextField.getText(); // password =
				 * passwordTextField.getText(); 
				 */
			}
		});

		// Setting an action for the exitButton
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
		// Use for Error Labels
		// loginVbox.getChildren().add();
		// loginVbox.getChildren().add();

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
		// loginMainPane.getChildren().add(featuresNames);
		Scene loginScene = new Scene(loginBorderPane, 300, 280);

		Stage loginStage = new Stage();
		loginStage.setTitle("JFiles Login Screen");
		loginStage.setScene(loginScene);
		loginStage.setResizable(false);
		// Displays the start Stage and its contents.
		loginStage.show();

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
