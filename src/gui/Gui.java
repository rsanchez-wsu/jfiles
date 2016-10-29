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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Gui class file that makes a panel with buttons on it.
 */
public class Gui {
	/**
	 * Main class of GUI.
	 * 
	 * @param args
	 *            The command line argument
	 */
	public static void main(String[] args)
			throws ParserConfigurationException, SAXException, IOException {

		///////////////////////Create Frame///////////////////////////////////
		JFrame frame = new JFrame();
		// Ends program when you close the window
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// Sets size of frame
		frame.setSize(450, 350);
		
		///////////////////////Create Panel for Buttons////////////////////////
		//This makes it so the output field and path fields can be separate
		JPanel filePanel = new JPanel();
		// Where buttons will be place (rows, columns)
		filePanel.setLayout(new GridLayout(0, 2));

		///////////////////////Create Path Display////////////////////////////
		//This creates a box for the current path to be displayed in.
		JTextArea pathDisplay = new JTextArea();
		pathDisplay.setEditable(false);
		
		String currentPath = JFilesServer.sendPath();
		pathDisplay.append(currentPath);
		frame.add(pathDisplay);
		frame.add(pathDisplay, BorderLayout.NORTH);
		
		///////////////////////Create Output Area/////////////////////////////
		// This creates a box with appendable text that can be scrolled through.
		// It is initialized here so that when clicking a button it can be edited.
		JTextArea consoleOutput = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(consoleOutput);
		consoleOutput.setEditable(false);		
		
		///////////////////////Create Buttons//////////////////////////////////
		// Icon width and height variables
		final int IconWidth = 100;
		final int iconHeight = 100;
		
		// Specifies a new image icon and resizes it
		ImageIcon fileIcon = new ImageIcon(new ImageIcon(Gui.class.getResource("img/file_icon.png"))
				.getImage().getScaledInstance(IconWidth, iconHeight, Image.SCALE_DEFAULT));
		
		// Reserved for folder icon when we can use it
		ImageIcon folderIcon = new ImageIcon(new ImageIcon(Gui.class.getResource("img/folder_icon.png"))
				.getImage().getScaledInstance(IconWidth, iconHeight, Image.SCALE_DEFAULT));

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

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(testXml)));

		NodeList nodes = doc.getElementsByTagName("item");

		for (int i = 0; i < nodes.getLength(); i++) {
			Node newNode = nodes.item(i);

			Element newElement = (Element) newNode;
			
			items.add(newElement.getElementsByTagName("name").item(0).getTextContent()
					+ newElement.getElementsByTagName("ext").item(0).getTextContent());
			
			/*
			 * We need to determine how we will approach differentiating files and folders 
			 * I'm thinking maybe an array within the array list that holds the 
			 * file / folder and its information
			 * 
			 * i.e. something like the below
			 * 
			 * [0] 
			 *   => array(
			 *   		name = "test",
			 *      	extension = ".txt",
			 *      	type = "file"
			 *   ),
			 * [1]
			 *   => array(
			 *   		name = "folderTest",
			 *   		extension = "",
			 *   		type = "folder"
			 *   )
			 *   
			 * etc.
			 * 
			 * I may tackle this next week. - Alex
			 * 
			 * */
		}

		for (int i = 0; i < items.size(); i++) {
			String fileName = items.get(i);
			
			JLabel fileIconLabel = new JLabel(fileName, fileIcon, JLabel.CENTER);
			fileIconLabel.setVerticalTextPosition(JLabel.BOTTOM);
			fileIconLabel.setHorizontalTextPosition(JLabel.CENTER);

			fileIconLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent event) {
					consoleOutput.append("You clicked " + fileName + "\n");
				}
			});

			// Puts the icon in the panel according to grid
			filePanel.add(fileIconLabel);
		}

		//Add items to frame
		frame.add(filePanel, BorderLayout.CENTER);
		frame.add(scrollPane, BorderLayout.SOUTH);
		
		frame.setVisible(true);
	}
}