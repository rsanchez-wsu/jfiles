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
 * @author laurence - original creater
 * @author mark - commented and edited
 * Test class for LS command.
 * ls command will return the list of files in the current directory
 * 
 * 
 */
public class LsTest {

	@Test
	public void testList() throws IOException, InterruptedException {
		//creates new connection to server
		ServerTestWidget tw = new ServerTestWidget();
		
		//creates a folder called '.' this step does not seem necessary
		File folder = new File(".");
		File[] dir = folder.listFiles();
		//checks to see if folde ris null
		assertTrue(folder != null);
		//send ls command which will return its current files in that directory
		tw.send("LS");
		//returning the message from the server as a string and then triming it by new line
		String res = tw.receive();
		String[] result = res.split("\r\n");
		//does not seem to enter the loop and goes to finally
		try {
			for (int i = 1;i < dir.length;i++) {
				String s1 = dir[i].getAbsolutePath();
				String s2 = result[i];
				assertTrue(s1.equals(s2));
			}
		} finally {
			tw.stop();
		}
	}

}
