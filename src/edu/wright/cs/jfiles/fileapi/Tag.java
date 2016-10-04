/*
 * Copyright (C) 2016 - WSU CEG3120 Students
 * 
 * Roberto C. Sánchez <roberto.sanchez@wright.edu>
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

package edu.wright.cs.jfiles.fileapi;

/**
 * Class in which holds the tags given to files.
 * 
 * @author Brand Allred
 * @author Team 5
 * 
 */
public class Tag {

	private String strId;
	private int intId;

	/**
	 * Default constructor.
	 */
	protected Tag() {
	}

	/**
	 * Constructor to fill fields with.
	 * 
	 * @param strId
	 *            String to identify the tag with.
	 * @param intId
	 *            Integer to identify the tag with.
	 */
	protected Tag(String strId, int intId) {
		this.intId = intId;
		this.strId = strId;
	}

	/**
	 * Sets the string identifier.
	 * 
	 * @param strId
	 *            String to identify the tag with.
	 */
	protected void setstrId(String strId) {
		this.strId = strId;
	}

	/**
	 * Sets the integer identifier.
	 * 
	 * @param intId
	 *            Integer to identify the tag with.
	 */
	protected void setintId(int intId) {
		this.intId = intId;
	}

	/**
	 * Gets the string Identifier.
	 * 
	 * @return Returns the string identifier.
	 */
	protected String getstrId() {
		return strId;
	}

	/**
	 * Gets the integer identifier.
	 * 
	 * @return Returns the integer identifier.
	 */
	protected int getintId() {
		return intId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + intId;
		result = prime * result + ((strId == null) ? 0 : strId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Tag other = (Tag) obj;
		if (intId != other.intId) {
			return false;
		}
		if (strId == null) {
			if (other.strId != null) {
				return false;
			}
		} else if (!strId.equals(other.strId)) {
			return false;
		}
		return true;
	}

}
