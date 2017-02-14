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

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
/**
 * Test class for:
 * Opening a server
 * Opening a socket
 * Closing that socket
 * Closing the server.
 */

public class QuitTest {

	@Test
	public void quitTest() throws IOException, InterruptedException {
		ServerTestWidget tw = new ServerTestWidget();
		assertTrue(tw.server.firstClient() != null);
		tw.send("QUIT");
		int delayCounter = 0;
		while (tw.server.firstClient() != null) {
			TimeUnit.MILLISECONDS.sleep(1);
			assertTrue(delayCounter++ < 1000);
		}
		tw.stop();
		/*The following attempt to open a socket is
		 * expected to fail as the server should have quit at this point*/
		try {
			Socket socket = new Socket("localhost",9786);
			assertTrue(false);
			/*to prevent findbugs warning:*/
			socket.close();
		} catch (IOException e) {
			/*to prevent warnings*/
			int bob = 1;
			bob = bob + 1;
		}
	}

}
