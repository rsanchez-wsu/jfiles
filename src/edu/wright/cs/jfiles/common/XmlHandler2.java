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

package edu.wright.cs.jfiles.common;

import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * This class encapsulates the XMLStruct class in an ArrayList and provides methods to read and 
 * write to data streams.
 * @author brian
 *
 */
public class XmlHandler2 {
	
	private Path currentPath;
	private ArrayList<XmlStruct> arrlist;
	private XStream xstream;
	
	/**
	 * Constructor for sending XML.
	 * @param path path that you want to XMLify
	 * @throws IOException If Path object is inaccessible 
	 */
	public XmlHandler2(Path path) throws IOException {
		this.currentPath = path;
		arrlist = new ArrayList<XmlStruct>();
		populateArray();
		confXStream();				
	}
	
	/**
	 * Helper method to populate the ArrayList.
	 * @throws IOException If Path object is inaccessible
	 */
	private void populateArray() throws IOException {
		//Tried this with an iterator and it blew up
		File[] temp = currentPath.toFile().listFiles();
		
		if (temp == null) {
			return;
		}
		for (int i = 0; i < temp.length; i++) {
			arrlist.add(new XmlStruct(temp[i].toPath()));	
		}
	}
	
	/**
	 * Method to serialize an object and write XML to an output stream.
	 * @param osw OutputStreamWriter to write to
	 */
	public void sendXml(OutputStreamWriter osw) {
		
	}
	
	/**
	 * Method to read XML and deserialze to an object.
	 * @param isr InputStreamReader to read from
	 * @return reconstructed object
	 */
	public ArrayList<XmlStruct> readXml(InputStreamReader isr) {
		return arrlist;	
	}
	
	/**
	 * Helper method to configure XStream before writing to OSW.
	 */
	private void confXStream() {
		
	}

}
