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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 * Gui class file that makes a panel with buttons on it. 
 */
public class Gui {

	/**
	 * Main class of GUI.
	 * @param args The command line argument
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		// Creates the frame
		JFrame frame = new JFrame();
		// Sets size of frame
		frame.setSize(450, 350);
		// Where buttons will be place (rows, columns)
		frame.setLayout(new GridLayout(2, 5));
		
		/*
		 * Some initial testing for parsing XML for file names and types. Uses fake data for now, assuming this will come later.
		 * Simply logs file name + extension from XML string to console for now.
		 */
		//String containing fake XML for parsing testing (output from server issue #17 from milestone 1)
	    String testXML = "<?xml version=\"1.0\"?><item><name>Test</name><ext>.txt</ext><type>file</type></item>";
	    
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document doc = builder.parse(new InputSource(new StringReader(testXML)));
	    
	    NodeList nodes = doc.getElementsByTagName("item");
	    
	    for(int i = 0; i < nodes.getLength(); i++){
	    	Node nNode = nodes.item(i);
	    	
	    	Element eElement = (Element) nNode;
	    	
	    	System.out.println(eElement.getElementsByTagName("name").item(0).getTextContent() + eElement.getElementsByTagName("ext").item(0).getTextContent());
	    	
	    }

		for (int i = 0; i < 10; i++) {
			JButton thebutton = new JButton();
			thebutton.setText("Button: " + i);
			thebutton.addActionListener(new ActionListener() {
				//This creates the event for when the button is clicked
				public void actionPerformed(ActionEvent error) {
					System.out.println("You clicked a button");
				}
			});
			//Sets size of the button
			thebutton.setSize(10, 10);
			//Puts the button on the frame
			frame.add(thebutton);
		}
		frame.setVisible(true);
	}
}