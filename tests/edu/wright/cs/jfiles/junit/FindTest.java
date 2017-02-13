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
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * @author Laure
 *
 *         This is the test class for the find command, including the secondary
 *         of the actions of the server to execute said command.
 */
public class FindTest {

	@Test
	public void testFind() {
		JFilesServer server = new JFilesServer();
		Socket socket = null;
		File testFile = new File("armadillotestFile");
		try {
			socket = new Socket("localhost", 9786);
			DataInputStream in =
					new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			DataOutputStream out =
					new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			testFile.createNewFile();
			out.writeUTF("find armadillo");
			String inStr = in.readUTF();
			assertTrue(inStr.contains("armadillo"));
			out.writeUTF("find aardvark");
			inStr = in.readUTF();
			assertTrue(!inStr.contains("aardvark"));
			assertTrue(!inStr.contains("armadillo"));
		} catch (IOException e) {
			System.out.println("IO Exception during find test.");
		} finally {
			server.stop();
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println("Error closing socket during findTest");
				}
			}
			if (testFile.exists()) {
				testFile.delete();
			}
		}
	}
}
