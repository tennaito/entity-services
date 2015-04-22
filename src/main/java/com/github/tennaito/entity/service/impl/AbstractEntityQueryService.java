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
package com.github.tennaito.entity.service.impl;

import java.util.List;

import javax.persistence.EntityManager;

import com.github.tennaito.entity.service.EntityQueryService;

/**
 * AbstractEntityQueryService.
 * 
 * Template for all derived sub-types operations from essential entity query service.
 * 
 * @author Antonio Rabelo
 */
public abstract class AbstractEntityQueryService<T> implements EntityQueryService<T> {

	/**
	 * EntityManager instance.
	 */
	private final EntityManager manager;
	
	/**
	 * QueryConfiguration instance.
	 */
	private QueryConfiguration queryConfiguration;

	/**
	 * Constructor.
	 * 
	 * @param manager An instance of the EntityManager.
	 */
	public AbstractEntityQueryService(EntityManager manager) {
		if(manager == null) {
			throw new IllegalArgumentException("Must define EntityManager instance");
		}
		this.manager = manager;
	}
	
	/**
	 * Configure the EntityQueryService with some query configuration logic. 
	 * 
	 * @param queryConfiguration QueryConfiguration instance.
	 * @return the owner object.
	 */
	public AbstractEntityQueryService<T> configure(QueryConfiguration queryConfiguration) {
		this.queryConfiguration = queryConfiguration;
		return this;
	}
	
	/**
	 * Returns an instance of the EntityManager.
	 * 
	 * @return EntityManager instance.
	 */
	protected EntityManager getEntityManager() {
		return this.manager;
	}
	
	/**
	 * Returns the query configuration object.
	 * 
	 * @return QueryConfiguration instance.
	 */
	protected QueryConfiguration getQueryConfiguration() {
		return this.queryConfiguration;
	}
	
	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.EntityQueryService#querySingle(java.lang.Class)
	 */
	public T querySingle(Class<T> entity) {
		return querySingle(entity, null, null);
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.EntityQueryService#querySingle(java.lang.Class, java.lang.String)
	 */
	public T querySingle(Class<T> entity, String rsql) {
		return querySingle(entity, null, rsql);
	}
	
	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.EntityQueryService#querySingle(java.lang.Class, java.util.List)
	 */
	public T querySingle(Class<T> entity, List<String> properties) {
		return querySingle(entity, properties, null);
	}
	
	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.EntityQueryService#count(java.lang.Class)
	 */
	public long count(Class<T> entity) {
		return countWhere(entity, null);
	}
	
	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.EntityQueryService#queryPartial(java.lang.Class, java.util.List)
	 */
	public List<T> queryPartial(Class<T> entity, List<String> properties) {
		return queryPartial(entity, properties, null, null);
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.EntityQueryService#queryPartial(java.lang.Class, java.util.List, java.lang.Integer, java.lang.Integer)
	 */
	public List<T> queryPartial(Class<T> entity, List<String> properties, Integer page, Integer pageSize) {
		return queryWhere(entity, properties, null, page, pageSize);
	}
	
	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.EntityQueryService#queryAll(java.lang.Class)
	 */
	public List<T> queryAll(Class<T> entity) {
		return queryAll(entity, null, null);
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.EntityQueryService#queryAll(java.lang.Class, java.lang.Integer, java.lang.Integer)
	 */
	public List<T> queryAll(Class<T> entity, Integer page, Integer pageSize) {
		return queryWhere(entity, null, null, page, pageSize);
	}
	
	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.EntityQueryService#queryWhere(java.lang.Class, java.lang.String)
	 */
	public List<T> queryWhere(Class<T> entity, String rsql) {
		return queryWhere(entity, null, rsql, null, null);
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.EntityQueryService#queryWhere(java.lang.Class, java.util.List, java.lang.String)
	 */
	public List<T> queryWhere(Class<T> entity, List<String> properties, String rsql) {
		return queryWhere(entity, properties, rsql, null, null);
	}
	
	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.EntityQueryService#queryWhere(java.lang.Class, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public List<T> queryWhere(Class<T> entity, String rsql, Integer page, Integer pageSize) {
		return queryWhere(entity, null, rsql, page, pageSize);
	}
}
