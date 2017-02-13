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

package edu.wright.cs.jfiles.gui.client;

import edu.wright.cs.jfiles.gui.common.Item;
import edu.wright.cs.jfiles.gui.common.Parser;
import edu.wright.cs.jfiles.server.JFilesServer;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * This class will form the body of the JFiles client side GUI application. This
 * is a JavaFX application.
 *
 * @author Jason Phares &lt;phares.705@gmail.com&gt;
 * @author Roberto C. Sánchez &lt;roberto.sanchez@wright.edu&gt; (I used some
 *         of his code.)
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

	// String containing fake XML for parsing testing (output from server
	// issue #17)
	String testXml = "<?xml version=\"1.0\"?>" + "<items>"
			+ "<item><name>Test</name><ext>.txt</ext><type>file</type></item>"
			+ "<item><name>Test2</name><ext>.png</ext><type>file</type></item>"
			+ "<item><name>Test2</name><ext>.png</ext><type>file</type></item>"
			+ "<item><name>Test2</name><ext>.png</ext><type>file</type></item>"
			+ "<item><name>Test2</name><ext>.png</ext><type>file</type></item>"
			+ "<item><name>Test2</name><ext>.png</ext><type>file</type></item>"
			+ "<item><name>Test2</name><ext>.png</ext><type>file</type></item>"
			+ "<item><name>Folder</name><ext></ext><type>folder</type></item>" + "</items>";

	/**
	 * This method is where most visual elements are created and manipulated.
	 *
	 * @throws IOException
	 *             Thrown if parsing fails.
	 * @throws SAXException
	 *             Thrown if parsing fails.
	 * @throws ParserConfigurationException
	 *             Thrown if parsing fails.
	 * @throws XPathExpressionException
	 *             Thrown when XPath counting elements when parsing, compiling,
	 *             or evaluating fails.
	 */
	@Override
	public void start(Stage primaryStage) throws ParserConfigurationException, SAXException,
			IOException, XPathExpressionException {

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
		invalidLabel =
				createErrorLabel("Incorrect Username & Password Combination.", false, "#FF0000");
		emptyUsernameFieldLabel =
				createErrorLabel("Username Field Must Be Filled In.", false, "#FF0000");
		emptyPasswordFieldLabel =
				createErrorLabel("Password Field Must Be Filled In.", false, "#FF0000");

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

		// Create MenuBar For Menu Items
		MenuBar headderMenuBar = new MenuBar();

		// Creates a label for path and text field to display path
		Label pathLabel = new Label("Current Directory: ");

		// Create GridPane to organize the objects
		GridPane gridPane = new GridPane();
		gridPane.add(headderMenuBar, 0, 0, 2, 1);
		gridPane.add(pathLabel, 0, 1);

		TextField pathDisplay = new TextField();
		pathDisplay.setEditable(false);
		pathDisplay.setPromptText("File Path");

		// Populates text field with path
		String currentPath = JFilesServer.sendPath();
		pathDisplay.appendText(currentPath);

		// Creates a box for the search area
		TextField searchArea = new TextField();
		gridPane.add(searchArea, 1, 2);

		// Creates a button to collect the search
		Button searchButton = new Button();
		searchButton.setText("Search");
		searchButton.setMinWidth(100);

		gridPane.add(pathDisplay, 1, 1);
		gridPane.add(searchButton, 0, 2);

		// Add Column Constraints to get areas even
		ColumnConstraints column1 = new ColumnConstraints();
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setHgrow(Priority.ALWAYS);
		gridPane.getColumnConstraints().addAll(column1, column2);

		// Pane Creation
		BorderPane basePane = new BorderPane();

		// Add GridPane to top of BasePane
		basePane.setTop(gridPane);

		// Sets basePane's color
		// basePane.setStyle("-fx-background-color: LIGHTGREY;");

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

		// Tools Area
		final int imageHeight = 30;
		final int imageWidth = 30;

		// Specifies a new image icon and resizes it
		Image fileImage = new Image("file:src/edu/wright/cs/jfiles/resources/images/file_icon.png");

		// Reserved for folder icon when we can use it
		Image folderImage =
				new Image("file:src/edu/wright/cs/jfiles/resources/images/folder_icon.png");

		// ArrayList of item objects to hold files or folders
		ArrayList<Item> items = new ArrayList<Item>();

		// XML Parsing object
		Parser parser = new Parser();

		// Parsed document
		Document doc = parser.parse(testXml);

		// Count of items in the /items/item XML struct
		int itemCount = parser.countElements(doc, "/items/item");

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		// Gather each name, extension, and type of each item in /items
		for (int i = 1; i <= itemCount; i++) {
			XPathExpression getFileName = xpath.compile("/items/item[" + i + "]/name");
			XPathExpression getFileExt = xpath.compile("/items/item[" + i + "]/ext");
			XPathExpression getFileType = xpath.compile("/items/item[" + i + "]/type");

			String fileName = getFileName.evaluate(doc, XPathConstants.STRING).toString();
			String fileExt = getFileExt.evaluate(doc, XPathConstants.STRING).toString();
			String fileType = getFileType.evaluate(doc, XPathConstants.STRING).toString();

			Item item = new Item(fileName, fileExt, fileType);

			items.add(item);
		}

		// //////////////////////////////////////////////////////////////////////
		// // This code block will implement the old functionality of the output
		// Region and the path display. Placement of the text field and
		// scrollpane will change when a better layout is made.
		// // Creates a box for the current path to be displayed in.
		// TextField pathDisplay = new TextField();
		// pathDisplay.setEditable(false);
		// // Populates box with current path
		// String currentPath = JFilesServer.sendPath();
		// pathDisplay.appendText(currentPath);
		// //currently sets at bottom of pane to test it functions properly,
		// this will change
		// basePane.setBottom(pathDisplay);
		//
		// // This creates a box with changeable text that can be scrolled
		// through.
		// // Must be initialized before file panel is populated to contain
		// event information
		// TextField consoleOutput = new TextField();
		// ScrollPane scrollPane = new ScrollPane(consoleOutput);
		// consoleOutput.setEditable(false);
		// //basePane.setBottom(scrollPane);
		// //////////////////////////////////////////////////////////////////////

		FlowPane filePane = new FlowPane();
		filePane.setStyle("-fx-background-color: LIGHTBLUE;");
		basePane.setCenter(filePane);

		// This for loop loops through the items parsed from the XML string
		// and puts them into the GUI with an image and name
		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);

			String fileType = item.getType();
			ImageView openImageView = new ImageView();
			openImageView.setFitHeight(imageHeight);
			openImageView.setFitWidth(imageWidth);

			// If block to determine if item is a folder or a file
			if (fileType.equals("folder")) {
				openImageView.setImage(folderImage);
			} else {
				openImageView.setImage(fileImage);
			}

			String fileName = item.getName() + item.getExt();
			BorderPane file = new BorderPane();
			file.setBottom(new Label(fileName));
			file.setCenter(openImageView);
			file.setStyle("-fx-font-size: 15px;" + "-fx-font-family: 'Currier New' ;"
					+ "-fx-text-fill: black;" + "-fx-base: #85C1E9;");

			// Puts the button in frame
			filePane.getChildren().add(file);
			FlowPane.setMargin(file, new Insets(5, 5, 5, 5));
		}

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
