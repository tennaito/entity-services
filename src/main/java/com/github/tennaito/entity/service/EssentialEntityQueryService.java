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
package com.github.tennaito.entity.service;

import java.util.List;

import javax.persistence.NonUniqueResultException;

/**
 * Interface with essential querying purposes.
 * 
 * @author Antonio Rabelo
 *
 * @param <T>
 */
public interface EssentialEntityQueryService<T> {

	/**
	 * Count entities.
	 * 
	 * @param entity   Type of the Entity.
	 * @param rsql	   RSQL string.
	 * @return         The count of Entities the meet the rsql condition.
	 */
	public long countWhere(Class<T> entity, String rsql);
	
	/**
	 * Query a single with a partial result with a where (rsql) condition.
	 * 
	 * @param entity   	 Type of the Entity.
	 * @param properties List of the properties (data) to be returned.
	 * @param rsql	     RSQL string.
	 * @return			 A single result of the Entity with that properties filled that meet 
	 * 					 the rsql condition.
	 * @throws NonUniqueResultException 
	 * 				   When there are more than one Entity.
	 */
	public T querySingle(Class<T> entity, List<String> properties, String rsql);
	
	/**
	 * Query a entity with where (rsql) condition and paginated some properties filled.
	 * 
	 * @param entity     Type of the Entity.
	 * @param properties List of the properties (data) to be returned.
	 * @param rsql       RSQL string.
	 * @param page       Page number (starts with 1)
	 * @param pageSize   Page Size (starts with 1)
	 * @return	         A page of All Entities that meets that condition with partial result.
	 */
	public List<T> queryWhere(Class<T> entity, List<String> properties, String rsql, Integer page, Integer pageSize);
}
