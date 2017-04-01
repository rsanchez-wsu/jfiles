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

import edu.wright.cs.jfiles.database.DatabaseController;
import edu.wright.cs.jfiles.database.DatabaseUtils;
import edu.wright.cs.jfiles.database.FailedInsertException;
import edu.wright.cs.jfiles.database.IdNotFoundException;
import edu.wright.cs.jfiles.database.User;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * This test the permission functions.
 * As permissions tools continue to be developed this will need to changed.
 * Currently:
 * -Opens a server and connects
 * -Checks that the client has READWRITE permission. (
 * -Adds a "fool" user
 * -Gives the fool write permission.
 * -Sets the clients ID to fool.
 * -Checks this in the databases.
 */

public class PermissionsTest {

	@Test
	public void permissionsTest() throws IOException, InterruptedException,
		FailedInsertException, IdNotFoundException, SQLException {
		ServerTestWidget tw = new ServerTestWidget();
		assertTrue(tw.client.getcp().getPermissionType() == DatabaseUtils.PermissionType.READWRITE);
		int uid = DatabaseController.createUser("fool", "", 0);
		String xml = new String(
				Files.readAllBytes(
						new File("tests/permissions/fool.xml").toPath()), "UTF-8");
		int permid = DatabaseController.createPermission(xml);
		DatabaseController.addPermissionToUser(uid, permid);
		User fool = new User(uid,"fool","",0);
		tw.client.getcp().setUser(fool);
		Connection conn = DatabaseController.openConnection();
		//Check if user was correctly inserted
		PreparedStatement usercheck
				= conn.prepareStatement("SELECT user_id from users where user_name = ?");
		usercheck.setString(1, "fool");
		ResultSet rs = usercheck.executeQuery();
		rs.next();
		assertTrue(rs.getInt(1) == uid);
		//There should be only one fool
		assertTrue(!rs.next());
		//There should be no doofusses
		usercheck.setString(1, "doofus");
		rs = usercheck.executeQuery();
		assertTrue(!rs.next());
		//Check perm_id table.
		usercheck = conn.prepareStatement("SELECT perm_id from user_permissions where user_id = ?");
		usercheck.setInt(1,uid);
		rs = usercheck.executeQuery();
		rs.next();
		assertTrue(rs.getInt(1) == permid);
		assertTrue(!rs.next());
		//Check Permissions table
		usercheck = conn.prepareStatement(
				"SELECT XMLSERIALIZE(PERM_DOC AS CLOB) FROM PERMISSIONS WHERE perm_id = ?");
		usercheck.setInt(1, permid);
		rs = usercheck.executeQuery();
		rs.next();
		String xmlout = rs.getString(1);
		assertTrue(DatabaseUtils.hasAccess(xmlout, "serverfiles/tmp/")
				== DatabaseUtils.PermissionType.WRITE);
		//TODO Add SQL to see if the data was actually inserted
		//assertTrue(tw.client.getcp().getPermissionType() == DatabaseUtils.PermissionType.WRITE);
		//We can't do this yet. Permissions don't actually change.
	}

}
