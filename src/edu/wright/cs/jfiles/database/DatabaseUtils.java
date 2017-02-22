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
 * Database utility methods.
 *
 * @author Matt Gilene
 */
public class DatabaseUtils {

	/**
	 * Possible permission access results.
	 */
	public enum PermissionResult {
		READ, WRITE, READWRITE, NONE
	}

	/**
	 * Checks if the given XML string gives access to given location.
	 *
	 * @param xml
	 *            XML document string
	 * @param location
	 *            path to file
	 * @return PermissonResult type
	 */
	public static PermissionResult hasAccess(String xml, String location) {
		try {
			File file = new File(location);
			XPath xpath = XPathFactory.newInstance().newXPath();
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse(new InputSource(new StringReader(xml)));
			NodeList nodeList = (NodeList) xpath.compile("//location/@path")
					.evaluate(xmlDocument, XPathConstants.NODESET);
			String type = (String) xpath.compile("/permission/@type").evaluate(xmlDocument,
					XPathConstants.STRING);
			PermissionResult permissionType = PermissionResult.valueOf(type);
			for (int i = 0; i < nodeList.getLength(); i++) {
				File permLocation = new File(nodeList.item(i).getTextContent());
				if (file.equals(permLocation)) {
					return permissionType;
				} else {
					File parent = file.getParentFile();
					while (parent != null) {
						if (parent.equals(permLocation)) {
							return permissionType;
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
		return PermissionResult.NONE;
	}
}
