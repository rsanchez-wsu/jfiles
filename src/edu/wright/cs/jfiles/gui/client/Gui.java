/*
 * Copyright (C) 2016 - WSU CEG3120 Students
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

import edu.wright.cs.jfiles.gui.common.Item;
import edu.wright.cs.jfiles.gui.common.Parser;
import edu.wright.cs.jfiles.server.JFilesServer;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
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
 * Gui class file that creates a frame and adds components to it.
 */
public class Gui extends Application {

	@Override
	public void start(Stage mainStage) throws Exception {

		BorderPane mainPane = new BorderPane();

		addComponents(mainPane);

		Scene scene = new Scene(mainPane, 800, 500);
		mainStage.setScene(scene);
		mainStage.setTitle("JFiles");
		mainStage.show();
	}

	/**
	 * Adds different parts of the GUI to the frame.
	 *
	 * @param pane
	 *            Passes the main pane to add components to.
	 * @throws IOException
	 *             IOException handler
	 * @throws SAXException
	 *             SAXException handler
	 * @throws ParserConfigurationException
	 *             Parser exception handler
	 * @throws XPathExpressionException
	 *             XPatchException handler
	 */
	static void addComponents(BorderPane pane) throws XPathExpressionException,
			ParserConfigurationException, SAXException, IOException {

		// Creates a box for the current path to be displayed in.
		TextField pathDisplay = new TextField();
		pathDisplay.setEditable(false);

		// Populates box with current path
		String currentPath = JFilesServer.sendPath();
		pathDisplay.appendText(currentPath);

		// Adds path display to the top of pane
		pane.setTop(pathDisplay);

		// This creates a box with changeable text that can be scrolled through.
		// Must be initialized before file panel is populated to contain event
		// information
		TextArea consoleOutput = new TextArea();
		consoleOutput.setEditable(false);
		consoleOutput.setMaxHeight(40);
		pane.setBottom(consoleOutput);

		// Creates panel to add buttons to. This keeps it separate from other
		// components.
		FlowPane filePane = new FlowPane();
		filePane.setHgap(5);
		filePane.setVgap(5);

		ScrollPane fileScroller = new ScrollPane(filePane);
		fileScroller.setFitToHeight(true);
		fileScroller.setFitToWidth(true);

		// Where files will be place (rows, columns)
		addFiles(filePane, consoleOutput);
		pane.setCenter(fileScroller);

	}

	/**
	 * Creates and populates the passed panel with files.
	 *
	 * @param filePane
	 *            Pass the pane to populate files with
	 * @param consoleOutput
	 *            Pass area to output text in console
	 * @throws ParserConfigurationException
	 *             Parser exception handler
	 * @throws SAXException
	 *             SAXException handler
	 * @throws IOException
	 *             IOException handler
	 * @throws XPathExpressionException
	 *             XPatchException handler
	 */
	static void addFiles(FlowPane filePane, TextArea consoleOutput)
			throws ParserConfigurationException, SAXException, IOException,
			XPathExpressionException {

		// Icon width and height variables
		final int iconWidth = 100;
		final int iconHeight = 100;

		/*
		 * Some initial testing for parsing XML for file names and types. Uses
		 * fake data for now, assuming this will come later. Simply logs file
		 * name + extension from XML string to console for now.
		 */

		// String containing fake XML for parsing testing (output from server
		// issue #17)
		String testXml = "<?xml version=\"1.0\"?>" + "<items>"
				+ "<item><name>Test1</name><ext>.txt</ext><type>file</type></item>"
				+ "<item><name>Test2</name><ext>.png</ext><type>file</type></item>"
				+ "<item><name>Test3</name><ext>.png</ext><type>file</type></item>"
				+ "<item><name>Test4</name><ext>.png</ext><type>file</type></item>"
				+ "<item><name>Folder 1</name><ext></ext><type>folder</type></item>"
				+ "<item><name>Folder 2</name><ext></ext><type>folder</type></item>"
				+ "<item><name>Folder 3</name><ext></ext><type>folder</type></item>"
				+ "<item><name>Folder 4</name><ext></ext><type>folder</type></item>" + "</items>";

		ArrayList<Item> items = new ArrayList<Item>();

		Parser parser = new Parser();

		Document doc = parser.parse(testXml);

		int itemCount = parser.countElements(doc, "/items/item");

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

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

		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			final String fileName = item.getName() + item.getExt();
			final String fileType = item.getType();

			BorderPane iconPane = new BorderPane();

			Label iconLabel = new Label(fileName);
			iconLabel.prefWidthProperty().bind(iconPane.widthProperty());
			iconLabel.setAlignment(Pos.CENTER);

			iconPane.setBottom(iconLabel);

			ImageView icon;
			if (fileType.equals("folder")) {
				icon = new ImageView("file:src/edu/wright/cs/jfiles/gui/img/folder_icon_2.png");
			} else {
				icon = new ImageView("file:src/edu/wright/cs/jfiles/gui/img/file_icon_2.png");
			}
			icon.setFitWidth(iconWidth);
			icon.setFitHeight(iconHeight);

			iconPane.setCenter(icon);

			iconPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					consoleOutput.appendText("You clicked " + fileName + "\n");
				}
			});

			filePane.getChildren().add(iconPane);

		}
	}

	/**
	 * Main class of GUI.
	 *
	 * @param args
	 *            The command line argument
	 * @throws IOException
	 *             IOException handler
	 * @throws SAXException
	 *             SAXException handler
	 * @throws ParserConfigurationException
	 *             Parser exception handler
	 * @throws XPathExpressionException
	 *             XPatchException handler
	 */
	public static void main(String[] args) throws XPathExpressionException,
			ParserConfigurationException, SAXException, IOException {

		launch(args);
	}
}