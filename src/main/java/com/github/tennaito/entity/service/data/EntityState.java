/*
 * The MIT License
 *
 * Copyright 2015 Antonio Rabelo.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.tennaito.entity.service.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * EntityState class responsible to store an object state.
 * 
 * @author Antonio Rabelo
 */
public class EntityState implements Serializable {

	/**
	 * SERIAL UID
	 */
	private static final long serialVersionUID = 4325863376241569764L;

	/**
	 * Single name of the object class.
	 */
	private final String name;
	
	/**
	 * Original class. 
	 */
	private final Class<?> clazz;
	
	/**
	 * All public accessible properties. 
	 */
	private Map<String, Object> properties = new HashMap<String, Object>();
	
	/**
	 * Constructor.
	 * 
	 * @param clazz Object class type. 
	 */
	public EntityState(Class<?> clazz) {
		this.clazz = clazz;
		this.name = clazz.getSimpleName();
	}
	
	/**
	 * Put the name and value of an object property.
	 * 
	 * @param name  Name of the property.
	 * @param value Value of the property.
	 */
	public void put(String name, Object value) {
		properties.put(name, value);
	}
	
	/**
	 * Get the value of the specified property.
	 * 
	 * @param name  Name of the property.
	 * @return      The value of the property.
	 */
	public <T> T get(String name) {
		if (!properties.containsKey(name)) {
			throw new IllegalArgumentException("Undefined property from EntityResult " + getName());
		}
		return (T)properties.get(name);
	}
	
	/**
	 * Gets the original class type.
	 * 
	 * @return Original class type of the former object.
	 */
	public Class<?> getOriginalType() {
		return this.clazz;
	}
	
	/**
	 * Gets the simple name of the object class.
	 * @return Simple name of the object class.
	 */
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((properties == null) ? 0 : properties.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityState other = (EntityState) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		return true;
	}
}
