/*
 * Copyright (C) 2016 - WSU CEG3120 Students
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

package edu.wright.cs.jfiles.gui;

/**
 * Each file / folder is stored as an object.
 */
public class Item {
	
	/**
	 * Item Constructor.
	 */
	public Item(String name, String ext, String type) {
		this.name = name;
		this.ext = ext;
		this.type = type;
	}
	
	/**
	 * Getter for file / folder name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Getter for file / folder Extension.
	 */
	public String getExt() {
		return ext;
	}
	
	/**
	 * Getter for file / folder type.
	 */
	public String getType() {
		return type;
	}
	
	private String name;
	private String ext;
	private String type;
}
