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

import java.util.Collection;

/**
 * Interface that defined the transformation methods.
 * 
 * @author Antonio Rabelo
 *
 * @param <T> Entity Type
 */
public interface EntityStateConverter<T> {
	
	/**
	 * Create an EntityState from an Entity. 
	 * 
	 * @param entity Entity type.
	 * @return An EntityState.
	 */
	public EntityState createState(T entity);
	
	/**
	 * Create an EntityState List from an Entity List.
	 * 
	 * @param entityList Entity List
	 * @return EntityState List.
	 */
	public Collection<EntityState> createStateList(Collection<T> entityList);

	/**
	 * Create an Entity from an EntityState. 
	 * 
	 * @param state EntityState.
	 * @return An Entity.
	 */	
	public T createEntity(EntityState state);
	
	/**
	 * Create an Entity List from an EntityState List.
	 * 
	 * @param stateList EntityState List
	 * @return Entity List.
	 */
	public Collection<T> createEntityList(Collection<EntityState> stateList);
}
