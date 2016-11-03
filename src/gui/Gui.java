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

package gui;

import edu.wright.cs.jfiles.server.JFilesServer;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Gui class file that creates a frame and adds components to it.
 */
public class Gui {

	/**
	 * Creates frame the components of the GUI will be places on.
	 * @throws IOException 						IOException handler
	 * @throws SAXException 					SAXException handler
	 * @throws ParserConfigurationException 	Parser exception handler
	 * @throws XPathExpressionException 		XPatchException handler
	 */
	static void createGui() throws XPathExpressionException, 
		ParserConfigurationException, SAXException, IOException {
		JFrame frame = new JFrame();
		// Ends program when you close the window
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setSize(450, 350);
		
		addComponents(frame);
		frame.setVisible(true);
	}

	/**
	 * Adds different parts of the GUI to the frame.
	 * @param pane Passes the frame to add components to.
	 * @throws IOException 						IOException handler
	 * @throws SAXException 					SAXException handler
	 * @throws ParserConfigurationException 	Parser exception handler
	 * @throws XPathExpressionException 		XPatchException handler
	 */
	static void addComponents(Container pane) throws XPathExpressionException, 
		ParserConfigurationException, SAXException, IOException {

		// Creates a box for the current path to be displayed in.
		JTextArea pathDisplay = new JTextArea();
		pathDisplay.setEditable(false);

		// Populates box with current path
		String currentPath = JFilesServer.sendPath();
		pathDisplay.append(currentPath);
		pane.add(pathDisplay);
		pane.add(pathDisplay, BorderLayout.NORTH);
		
		// This creates a box with changeable text that can be scrolled through.
		// Must be initialized before file panel is populated to contain event information
		JTextArea consoleOutput = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(consoleOutput);
		consoleOutput.setEditable(false);
		pane.add(scrollPane, BorderLayout.SOUTH);
		
		// Creates panel to add buttons to. This keeps it separate from other components.
		JPanel filePanel = new JPanel();
		// Where files will be place (rows, columns)
		filePanel.setLayout(new GridLayout(0, 2));
		addFiles(filePanel, consoleOutput);
		pane.add(filePanel, BorderLayout.CENTER);

	}

	/**
	 * Creates and populates the passed panel with files.
	 * @param filePanel							Pass the panel to populate files with
	 * @param consoleOutput						Pass area to output text in console
	 * @throws ParserConfigurationException		Parser exception handler
	 * @throws SAXException						SAXException handler
	 * @throws IOException						IOException handler
	 * @throws XPathExpressionException			XPatchException handler
	 */
	static void addFiles(Container filePanel, Container consoleOutput) throws 
		ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		// Icon width and height variables
		final int IconWidth = 100;
		final int iconHeight = 100;

		// Specifies a new image icon and resizes it
		ImageIcon fileIcon = new ImageIcon(new ImageIcon(Gui.class.getResource("img/file_icon.png"))
				.getImage().getScaledInstance(IconWidth, iconHeight, Image.SCALE_DEFAULT));

		// Reserved for folder icon when we can use it
		ImageIcon folderIcon = new ImageIcon(
				new ImageIcon(Gui.class.getResource("img/folder_icon.png")).getImage()
						.getScaledInstance(IconWidth, iconHeight, Image.SCALE_DEFAULT));

		/*
		 * Some initial testing for parsing XML for file names and types. Uses
		 * fake data for now, assuming this will come later. Simply logs file
		 * name + extension from XML string to console for now.
		 */

		// String containing fake XML for parsing testing (output from server
		// issue #17)
		String testXml = "<?xml version=\"1.0\"?>" + "<items>"
				+ "<item><name>Test</name><ext>.txt</ext><type>file</type></item>"
				+ "<item><name>Test2</name><ext>.png</ext><type>file</type></item>" + "</items>";

		ArrayList<String> items = new ArrayList<String>();

		Parser parser = new Parser();

		Document doc = parser.parse(testXml);

		int itemCount = parser.countElements(doc, "/items/item");

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		for (int i = 1; i <= itemCount; i++) {
			XPathExpression getFileName = xpath.compile("/items/item[" + i + "]/name");
			XPathExpression getFileExt = xpath.compile("/items/item[" + i + "]/ext");

			String itemName = getFileName.evaluate(doc, XPathConstants.STRING).toString()
					+ getFileExt.evaluate(doc, XPathConstants.STRING).toString();

			items.add(itemName);
		}

		for (int i = 0; i < items.size(); i++) {
			String fileName = items.get(i);

			JLabel fileIconLabel = new JLabel(fileName, fileIcon, JLabel.CENTER);
			fileIconLabel.setVerticalTextPosition(JLabel.BOTTOM);
			fileIconLabel.setHorizontalTextPosition(JLabel.CENTER);

			fileIconLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent event) {
					((JTextArea) consoleOutput).append("You clicked " + fileName + "\n");
				}
			});

			// Puts the icon in the panel according to grid
			filePanel.add(fileIconLabel);
		}
	}

	/**
	 * Main class of GUI.
	 * 
	 * @param args								The command line argument
	 * @throws IOException 						IOException handler
	 * @throws SAXException 					SAXException handler
	 * @throws ParserConfigurationException 	Parser exception handler
	 * @throws XPathExpressionException			XPatchException handler
	 */
	public static void main(String[] args) throws XPathExpressionException,
		ParserConfigurationException, SAXException, IOException {
		
		createGui();
	}
}