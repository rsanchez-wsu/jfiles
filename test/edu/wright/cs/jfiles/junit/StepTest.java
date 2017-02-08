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

import static org.junit.Assert.*;

import edu.wright.cs.jfiles.server.JFilesServer;
import edu.wright.cs.jfiles.server.JFilesServerThread;

import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class StepTest {
	
	@Test
	public void step() throws IOException {
		JFilesServer server = new JFilesServer(9786);
		Socket socket = new Socket("localhost",(9786));
		JFilesServerThread sthread = server.clients[1];
		try {
			sthread.send("test message");
			assertTrue((new DataInputStream(new BufferedInputStream(
					socket.getInputStream()))).readUTF().equals("test message"));
		} finally {
			server.stop();
			socket.close();
		}
	}
}
