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
 * Test class for LS command.
 */
public class LsTest {

	@Test
	public void testList() throws IOException, InterruptedException {
		ServerTestWidget tw = new ServerTestWidget();
		File folder = new File(".");
		File[] dir = folder.listFiles();
		assertTrue(folder != null);
		tw.send("LS");
		String res = tw.receive();
		String[] result = res.split("\r\n");
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
