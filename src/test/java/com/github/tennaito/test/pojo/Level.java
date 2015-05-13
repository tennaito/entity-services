/*
 * The MIT License
 *
 * Copyright 2015 Antonio Rabelo
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
package com.github.tennaito.test.pojo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Just an objeto to test dependency cycle, and properties transformation.
 * 
 * @author AntonioRabelo
 */
public class Level implements Serializable {
	
	/**
	 * SERIAL UID
	 */
	private static final long serialVersionUID = -1013500478576146684L;
	
	private Level level;
	private String name;
	private List<Level> rooms;
	private Level[] base;
	private Map<Level, Level> baseRooms;
	
	public Level() {
		this(null);
	}
	
	public Level(String name) {
		this.name = name;
		this.level = null;
	}	
	
	public Level(String name, Level sub) {
		this.name = name;
		this.level = sub;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Level getLevel() {
		return this.level;
	}
	
	public void setLevel(Level level) {
		this.level = level;
	}

	/**
	 * @return the rooms
	 */
	public List<Level> getRooms() {
		return rooms;
	}

	/**
	 * @param rooms the rooms to set
	 */
	public void setRooms(List<Level> rooms) {
		this.rooms = rooms;
	}

	/**
	 * @return the base
	 */
	public Level[] getBase() {
		return base;
	}

	/**
	 * @param base the base to set
	 */
	public void setBase(Level[] base) {
		this.base = base;
	}

	/**
	 * @return the baseRooms
	 */
	public Map<Level, Level> getBaseRooms() {
		return baseRooms;
	}

	/**
	 * @param baseRooms the baseRooms to set
	 */
	public void setBaseRooms(Map<Level, Level> baseRooms) {
		this.baseRooms = baseRooms;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(base);
		result = prime * result
				+ ((baseRooms == null) ? 0 : baseRooms.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((rooms == null) ? 0 : rooms.hashCode());
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
		Level other = (Level) obj;
		if (!Arrays.equals(base, other.base))
			return false;
		if (baseRooms == null) {
			if (other.baseRooms != null)
				return false;
		} else if (!baseRooms.equals(other.baseRooms))
			return false;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (rooms == null) {
			if (other.rooms != null)
				return false;
		} else if (!rooms.equals(other.rooms))
			return false;
		return true;
	}
}
