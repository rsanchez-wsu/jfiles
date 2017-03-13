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

package edu.wright.cs.jfiles.junit;

import static org.junit.Assert.assertTrue;


import org.junit.Test;


import java.io.File;
import java.io.IOException;

/**
 * @author Laure
 *
 *         This is the test class for the find command, including the secondary
 *         of the actions of the server to execute said command.
 *  @author Mark - commented and edited 
 */
public class FindTest {

	@Test
	public void testFind() throws IOException, InterruptedException {
		//creates test file with a test file variable
		File testFile = new File("armadillotestFile");
		try {
			//creates instance of server connection
			ServerTestWidget tw = new ServerTestWidget();
			//this will test if the testFile does not exist and creates it if that is the case
			if (!testFile.exists()) {
				testFile.createNewFile();
			}
			//telling the server to find our file
			tw.send("find armadillo");
			//converts the messaged recieved by the server into a string variable
			String inStr = tw.receive();
			//if the string variable contains our filename it will come back true
			assertTrue(inStr.contains("armadillo"));
			//now we are testing a file we did not create and should not be there
			tw.send("find aardvark");
			inStr = tw.receive();
			//now testing to see if this file is in the server
			assertTrue(inStr.contains("aardvark"));
		} finally {
			//this will check to see if the testFile exists
			//which it should and deletes the file
			if (testFile.exists()) {
				testFile.delete();
			}
		}
	}
}
