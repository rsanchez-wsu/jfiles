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

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * XML Parser for GUI.
 */
public class Parser {

	XPathFactory factory = XPathFactory.newInstance();
	XPath xpath = factory.newXPath();

	/**
	 * Parse a string of XML and return a parsed XML doc.
	 */
	public Document parse(String xml)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(xml)));
		return doc;
	}

	/**
	 * Takes an XPath string and returns the count at that location.
	 */
	public int countElements(Document doc, String expression) throws XPathExpressionException {
		XPathExpression getCount = xpath.compile("count(" + expression + ")");

		Number itemCount = (Number) getCount.evaluate(doc, XPathConstants.NUMBER);

		return itemCount.intValue();
	}
}
