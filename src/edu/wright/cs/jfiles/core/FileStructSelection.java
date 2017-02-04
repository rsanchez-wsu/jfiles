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

package edu.wright.cs.jfiles.core;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Class to contain a FileStruct in order to put it on the system clipbaord.
 *
 * @author Matt Gilene
 *
 */
public class FileStructSelection implements Transferable, ClipboardOwner {

	private static DataFlavor dataFlavor = new DataFlavor(FileStruct.class, "FileStruct");

	private FileStruct selection;

	/**
	 * Default public constructor.
	 *
	 * @param file
	 *            a FileStruct
	 */
	public FileStructSelection(FileStruct file) {
		selection = file;
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		System.out.println("FileStructSelection : lost clipboard ownership");
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (isDataFlavorSupported(flavor)) {
			return this.selection;
		}
		return null;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { dataFlavor };
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return dataFlavor.equals(flavor);
	}

}
