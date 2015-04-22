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
 * Interface for querying purposes.
 * 
 * @author Antonio Rabelo
 *
 * @param <T>
 */
public interface EntityQueryService<T> extends EssentialEntityQueryService<T> {

	/**
	 * Count entities.
	 * 
	 * @param entity   Type of the Entity.
	 * @return         The count of Entities.
	 */
	public long count(Class<T> entity);
	
	/**
	 * Query and return a single result.
	 * 
	 * @param entity   Type of the Entity.
	 * @return 		   A single result of the Entity.
	 * @throws NonUniqueResultException 
	 * 				   When there are more than one Entity.
	 */
	public T querySingle(Class<T> entity);

	/**
	 * Query a single result with where condition (rsql).
	 *
	 * @param entity   Type of the Entity.
	 * @param rsql     RSQL string.
	 * @return 		   A single result of the Entity.
	 * @throws NonUniqueResultException 
	 * 				   When there are more than one Entity.
	 */
	public T querySingle(Class<T> entity, String rsql);

	/**
	 * Query a single with a partial result.
	 * 
	 * @param entity   	 Type of the Entity.
	 * @param properties List of the properties (data) to be returned.
	 * @return			 A single result of the Entity with that properties filled.
	 * @throws NonUniqueResultException 
	 * 				   When there are more than one Entity.
	 */
	public T querySingle(Class<T> entity, List<String> properties);

	/**
	 * List of Entities with a partial result.
	 * 
	 * @param entity     Type of the Entity.
	 * @param properties List of the properties (data) to be returned.
	 * @return			 List of entities with a partial result.
	 */
	public List<T> queryPartial(Class<T> entity, List<String> properties);	
	
	/**
	 * List of Entities with a partial result paginated.
	 * 
	 * @param entity     Type of the Entity.
	 * @param properties List of the properties (data) to be returned.
	 * @param page       Page number (starts with 1)
	 * @param pageSize   Page Size (starts with 1)
	 * @return			 List of entities with a partial result paginated.
	 */
	public List<T> queryPartial(Class<T> entity, List<String> properties, Integer page, Integer pageSize);
	
	/**
	 * List of All Entities.
	 * 
	 * @param entity   Type of the Entity.
	 * @return 		   List of All Entities.
	 */
	public List<T> queryAll(Class<T> entity);	
	
	/**
	 * List of All Entities paginated.
	 * 
	 * @param entity   Type of the Entity.
	 * @param page     Page number (starts with 1)
	 * @param pageSize Page Size (starts with 1)
	 * @return         A page of the All List Entities.
	 */
	public List<T> queryAll(Class<T> entity, Integer page, Integer pageSize);

	/**
	 * List of entities with a where condition.
	 * 
	 * @param entity   Type of the Entity.
	 * @param rsql     RSQL string.
	 * @return		   List of All Entities that meets that condition.
	 */
	public List<T> queryWhere(Class<T> entity, String rsql);
	
	/**
	 * List of entities with a where condition with partial result.
	 * 
	 * @param entity     Type of the Entity.
	 * @param properties List of the properties (data) to be returned.
	 * @param rsql       RSQL string.
	 * @return		     List of All Entities that meets that condition with partial result.
	 */
	public List<T> queryWhere(Class<T> entity, List<String> properties, String rsql);
	
	/**
	 * Query a entity with Where (rsql) condition and paginated.
	 * 
	 * @param entity   Type of the Entity.
	 * @param rsql     RSQL string.
	 * @param page     Page number (starts with 1)
	 * @param pageSize Page Size (starts with 1)
	 * @return	       A page of All Entities that meets that condition with partial result.
	 */
	public List<T> queryWhere(Class<T> entity, String rsql, Integer page, Integer pageSize);
}
