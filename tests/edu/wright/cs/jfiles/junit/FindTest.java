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
 */
public class FindTest {

	@Test
	public void testFind() throws IOException, InterruptedException {
		File testFile = new File("armadillotestFile");
		try {
			ServerTestWidget tw = new ServerTestWidget();
			if (!testFile.exists()) {
				testFile.createNewFile();
			}
			tw.send("find armadillo");
			String inStr = tw.receive();
			assertTrue(inStr.contains("armadillo"));
			tw.send("find aardvark");
			inStr = tw.receive();
			assertTrue(!inStr.contains("aardvark"));
			assertTrue(!inStr.contains("armadillo"));
		} finally {
			if (testFile.exists()) {
				testFile.delete();
			}
		}
	}
}
