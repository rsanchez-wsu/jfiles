/*
 * Copyright (C) 2016 - WSU CEG3120 Students
 *
 * Roberto C. Sánchez <roberto.sanchez@wright.edu>
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

package edu.wright.cs.jfiles.junit;

import static org.junit.Assert.assertTrue;

import edu.wright.cs.jfiles.core.XmlHandler;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


/**
 * Test class for the XmlHandler.
 */
public class XmlHandlerTest {

	String testPath = "C:\fubar\foo.txt";

	@Test
	public void testXmlHandler() {

		OutputStream os = new ByteArrayOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os);

		XmlHandler handler = new XmlHandler(testPath, osw);
		handler.sendXml(osw);

		String result = os.toString();

		assertTrue(result.contains("foo.txt"));

//		ByteArrayOutputStream baos = (ByteArrayOutputStream) os;
//		byte[] bytes = baos.toByteArray();
//		InputStream is = new ByteArrayInputStream(bytes);
//
//		InputStreamReader isr = new InputStreamReader(is);
//
//		ArrayList<FileStruct> files = handler.readXml(isr);
//
//		assertEquals("hello", "hello");

	}

}
