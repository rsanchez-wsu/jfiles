/*
 * Copyright (C) 2016 - WSU CEG3120 Students
 * 
 * Matthew T. Trippel <trippel.3@wright.edu>
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

package edu.wright.cs.jfiles.server;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Handles XML construction/parsing for a JFilesServer object.
 * @author Matthew T. Trippel &lttrippel.3@wright.edu&gt
 *
 */
public class XmlHandler {
	
	//static final Logger logger = LogManager.getLogger(JFilesServer.class);
	Logger logger = null;
	
	/**
	 * XmlHandler constructor.
	 * @param newLogger Logger which XmlHandler will log to
	 */
	XmlHandler(Logger newLogger) {
		logger = newLogger;
	}
	
	/**
	 * Constructs an XML document with a root node.
	 * @param rootName name of the root node
	 * @return XML document
	 * @throws TransformerFactoryConfigurationError error in configuration
	 * @throws TransformerException error in configuration
	 */
	public Document createXml(String rootName) throws TransformerFactoryConfigurationError, 
					TransformerException {
		
		Document doc = null;
		try {
			
			// Create new XML document
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			doc = builder.newDocument();
			
			// Add root element to document
			Element root = doc.createElement(rootName);
			doc.appendChild(root);
			
		} catch (ParserConfigurationException e) {
			logger.error("An error occurred while configuring the parser",e);
		} catch (TransformerFactoryConfigurationError e) {
			logger.error("An error occurred while configuring the transformer factory",e);
		}
		
		// Return the XML document
		return doc;
	}
	
	/**
	 * Parses XML and prints to console.
	 * @param newDoc XML document to print
	 */
	public void parseXml(Document newDoc) {
		Transformer transformer;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "FILESYSTEM");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "fileSystem.dtd");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			DOMSource source = new DOMSource(newDoc);
			StreamResult str = new StreamResult(System.out);
			transformer.transform(source, str);
		} catch (TransformerConfigurationException e) {
			logger.error("An error occurred while configuring the transformer",e);
		} catch (TransformerFactoryConfigurationError e) {
			logger.error("An error occurred while configuring the transformer factory",e);
		} catch (TransformerException e) {
			logger.error("An error occurred with the transformer",e);
		}
	
	}
		
}
