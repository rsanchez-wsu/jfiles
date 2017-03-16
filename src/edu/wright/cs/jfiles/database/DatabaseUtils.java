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

import org.apache.commons.io.FileUtils;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
	public enum PermissionType {
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
	public static PermissionType hasAccess(String xml, String location) {
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
			PermissionType permissionType = PermissionType.valueOf(type);
			for (int i = 0; i < nodeList.getLength(); i++) {
				File permLocation = new File(nodeList.item(i).getTextContent());
				if (permLocation.equals(file)) {
					return permissionType;
				}
				if (FileUtils.directoryContains(permLocation, file)) {
					return permissionType;
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
		return PermissionType.NONE;
	}

	/**
	 * Generates the xml permission documentation for a users folder.
	 *
	 * @param loc
	 *            location of the user's folder
	 * @return xml document string
	 */
	public static String generateUserPermission(String loc) {
		String doc = "";
		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document document = builder.newDocument();
			Element root = document.createElement("permission");
			root.setAttribute("type", PermissionType.READWRITE.toString());
			Element location = document.createElement("location");
			location.setAttribute("path", loc);
			root.appendChild(location);
			document.appendChild(root);

			DOMImplementation domImpl = document.getImplementation();
			DocumentType doctype = domImpl.createDocumentType("permission", null,
					"tests/permissions/permission.dtd");

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
			StringWriter sw = new StringWriter();
			transformer.transform(new DOMSource(document), new StreamResult(sw));
			return sw.toString();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		generateUserPermission("serverfiles/default");
	}
}
