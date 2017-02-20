/*
 * Copyright (C) 2017 - WSU CEG3120 Students
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

package edu.wright.cs.jfiles.database;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Database permission container class.
 *
 * @author Matt Gilene
 *
 */
public class Permission {
	private int id;
	private String xml;

	/**
	 * Creates a new permission in the database.
	 *
	 * @param xml
	 *            string containing XML denoting the files this permission
	 *            grants access to
	 */
	public Permission(String xml) {
		this.id = DatabaseController.createPermission(xml);
		this.xml = xml;
	}

	/**
	 * Returns the Id of this permission.
	 *
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Checks to see if this permission grants access to the spcified file.
	 *
	 * @param file
	 *            file to check access of
	 * @return true or false
	 */
	public boolean hasAccess(File file) {
		XPath xpath = XPathFactory.newInstance().newXPath();
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse(new InputSource(new StringReader(xml)));
			NodeList nodeList =
					(NodeList) xpath.compile("//file/@path | //directory/@path")
							.evaluate(xmlDocument,
							XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				File permLocation = new File(nodeList.item(i).getTextContent());
				System.out.println(permLocation.getPath());
				if (file.equals(permLocation)) {
					return true;
				} else {
					File parent = file.getParentFile();
					while (parent != null) {
						if (parent.equals(permLocation)) {
							return true;
						}
						parent = parent.getParentFile();
					}
				}
			}
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return false;
	}
}
