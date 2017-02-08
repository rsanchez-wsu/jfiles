/*
 * Copyright (C) 2016 - WSU CEG3120 Students
 *
 * Brian Denlinger <brian.denlinger1@gmail.com>
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

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * A custom converter for k/v maps.
 *
 * @author brian
 *
 */
public class MapEntryConverter implements Converter {

	/**
	 * A constructor.
	 */
	@Override
	// Suppress the "rawtypes" warning because the super-interface constrains
	// the method signature to taking a raw Class object
	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return AbstractMap.class.isAssignableFrom(type);
	}

	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {

		AbstractMap<?, ?> map = (AbstractMap<?, ?>) value;
		for (Object obj : map.entrySet()) {
			Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
			writer.startNode(entry.getKey().toString());
			Object val = entry.getValue();
			if (null != val) {
				writer.setValue(val.toString());
			}
			writer.endNode();
		}

	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {

		Map<String, String> map = new HashMap<>();

		while (reader.hasMoreChildren()) {
			reader.moveDown();

			String key = reader.getNodeName(); // nodeName aka element's name
			String value = reader.getValue();
			map.put(key, value);

			reader.moveUp();
		}

		return map;
	}

}
