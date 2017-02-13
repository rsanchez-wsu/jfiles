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

import edu.wright.cs.jfiles.server.JFilesServer;

import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;



/**
 * Test class for LS command.
 */
public class LsTest {

	@Test
	public void testList() {
		JFilesServer server = new JFilesServer();
		Socket socket = null;
		try {
			socket = new Socket("localhost",9786);
		} catch (IOException e) {
			System.out.println("Error while opening socket for testing");
			System.exit(1);
		}
		File folder = new File(".");
		File[] dir = folder.listFiles();
		assertTrue(folder != null);
		server.handle((int) server.clients[1].getId(), "_LS");
		try {
			DataInputStream streamin = new DataInputStream(new BufferedInputStream(
					socket.getInputStream()));
			for (int i = 1;i <= dir.length;i++) {
				String s1 = dir[i].getPath();
				String s2 = streamin.readUTF();
				assertTrue(s1.equals(s2));
			}
		} catch (IOException e) {
			System.out.println("IOException during LS test");
		} finally {
			server.stop();
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("IOException during LS test");
			}
		}
	}

}
