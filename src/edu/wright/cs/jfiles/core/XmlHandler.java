/*
 * Copyright (C) 2016 - WSU CEG3120 Students
 *
 * Brian Denlinger <brian.denlinger1@gmail.com>
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

package edu.wright.cs.jfiles.core;

import com.thoughtworks.xstream.XStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * This class encapsulates the FileStruct class in an ArrayList and provides methods to read and
 * write to data streams.
 * @author brian
 *
 */
public class XmlHandler {

	private String currentPath;
	private ArrayList<FileStruct> arrlist;
	private static transient XStream xstream = new XStream();
	static final transient Logger logger = LogManager.getLogger(XmlHandler.class);

	//Static init block to configure XStream
	static {
		xstream.alias("fileObject", FileStruct.class);
		xstream.alias("root", XmlHandler.class);
		xstream.aliasAttribute(XmlHandler.class, "arrlist", "filesystem");
		xstream.aliasAttribute(FileStruct.class, "attrList", "attributeList");
		xstream.useAttributeFor(FileStruct.class, "type");
		xstream.omitField(XStream.class, "xstream");
		xstream.omitField(XStream.class, "logger");
		xstream.registerConverter(new MapEntryConverter());
	}

	/**
	 * Zero argument constructor.
	 */
	public XmlHandler() {
	}

	/**
	 * Constructs an ArrayList of FileStruct objects. If passed a path with
	 * a trailing slash in the path, list only the directory. Otherwise, list the contents of the
	 * directory.
	 * @param path path that you want to XMLify
	 */
	public XmlHandler(String path) {
		this.currentPath = path;
		arrlist = new ArrayList<FileStruct>();
		populateArray();
	}

	/**
	 * Constructs an ArrayList of FileStruct objects and sends it as XML. If passed a path with
	 * a trailing slash in the path, list only the directory. Otherwise, list the contents of the
	 * directory.
	 * @param path that you want to XMLify
	 * @param str stream to send XML to
	 */
	public XmlHandler(String path, OutputStreamWriter str) {
		this.currentPath = path;
		arrlist = new ArrayList<FileStruct>();
		populateArray();
		sendXml(str);
	}

	/**
	 * Helper method to populate the ArrayList.
	 * @throws IOException If Path object is inaccessible
	 */
	private void populateArray() {
		String lastCharacter = currentPath.substring(currentPath.length() - 1);

		try {
			if (Files.isDirectory(Paths.get(currentPath))
					&& !lastCharacter.equals(System.getProperty("file.separator"))) {
				//If passed a directory without a trailing slash list contents of the path

				File[] temp = new File(currentPath).listFiles();
				//Terminates if the folder is empty. Otherwise XML gets weird.
				if (temp == null) {
					return;
				}
				//Add the contents of the directory to the ArrayList
				for (int i = 0; i < temp.length; i++) {
					arrlist.add(new FileStruct(temp[i].toPath()));
				}
			} else {
				arrlist.add(new FileStruct(Paths.get(currentPath)));
			}
		} catch (IOException e) {
			logger.error(Error.IOEXCEPTION3.toString() + currentPath, e);
		}
	}

	/**
	 * Method to serialize an object and write XML to an output stream.
	 * @param osw OutputStreamWriter to write to
	 */
	public void sendXml(OutputStreamWriter osw) {
		xstream.toXML(this, osw);
	}

	/**
	 * Method to read XML and deserialize to an object.
	 * @param isr InputStreamReader to read from
	 * @return reconstructed object
	 */
	public ArrayList<FileStruct> readXml(InputStreamReader isr) {
		XmlHandler temp = (XmlHandler) xstream.fromXML(isr);
		return temp.arrlist;
	}

	/**
	 * Gets a copy of the contents.
	 *
	 * @return copy of arrlist
	 */
	public List<FileStruct> getFiles() {
		return Collections.unmodifiableList(arrlist);
	}
}
