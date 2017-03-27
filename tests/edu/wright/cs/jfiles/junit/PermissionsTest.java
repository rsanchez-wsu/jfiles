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

import edu.wright.cs.jfiles.database.DatabaseUtils;

import org.junit.Test;

import java.io.IOException;
/**
 * This test the permission functions.
 * As permissions tools continue to be developed this will need to changed
 */

public class PermissionsTest {

	@Test
	public void permissionsTest() throws IOException, InterruptedException {
		ServerTestWidget tw = new ServerTestWidget();
		assertTrue(tw.client.getcp().getPermissionType() == DatabaseUtils.PermissionType.READWRITE);
	}

}
