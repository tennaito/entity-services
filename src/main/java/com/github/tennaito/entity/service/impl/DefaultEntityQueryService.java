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
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import com.github.tennaito.entity.service.EntityQueryService;
import com.github.tennaito.entity.service.snippet.CriteriaSnippet;
import com.github.tennaito.entity.service.snippet.CriteriaSnippetCountDecorator;
import com.github.tennaito.entity.service.snippet.CriteriaSnippetPaginationDecorator;
import com.github.tennaito.entity.service.snippet.CriteriaSnippetPartialDecorator;
import com.github.tennaito.entity.service.snippet.CriteriaSnippetWhereRsqlDecorator;

/**
 * DefaultEntityQueryService.
 * 
 * Responsible for provide a generic querying interface for any entity class.
 * 
 * @author Antonio Rabelo
 */
public class DefaultEntityQueryService<T> extends AbstractEntityQueryService<T> implements EntityQueryService<T> {

	/**
	 * Constructor.
	 * 
	 * @param manager An instance of the EntityManager.
	 */
	public DefaultEntityQueryService(EntityManager manager) {
		super(manager);
	}
	
	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.EssentialEntityQueryService#querySingle(java.lang.Class, java.util.List, java.lang.String)
	 */
	public T querySingle(Class<T> entity, List<String> properties, String rsql) {
		return this.buildEntityQuery(entity, properties, rsql, null, null).getSingleResult();
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.EssentialEntityQueryService#countWhere(java.lang.Class, java.lang.String)
	 */
	public long countWhere(Class<T> entity, String rsql) {
		return this.buildCountQuery(entity, rsql).getSingleResult();
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.EssentialEntityQueryService#queryWhere(java.lang.Class, java.util.List, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public List<T> queryWhere(Class<T> entity, List<String> properties, String rsql, Integer page, Integer pageSize) {
		return this.buildEntityQuery(entity, properties, rsql, page, pageSize).getResultList();
	}
	
	/**
	 * Build a entity query with where condition, partial properties and pagination.
	 * 
	 * @param entity     Entity class type.
	 * @param properties List of properties of the partial list.
	 * @param rsql       RSQL string for where clause.
	 * @param page       Page number (starts with 1).
	 * @param pageSize   Page size   (starts with 1).
	 * @return           Query with applied algorithms.
	 */
	protected TypedQuery<T> buildEntityQuery(Class<T> entity, List<String> properties, String rsql, Integer page, Integer pageSize) {
		CriteriaSnippetWhereRsqlDecorator<T, T> rsqlSnippet = new CriteriaSnippetWhereRsqlDecorator<T, T>(rsql, null);
		CriteriaSnippetPartialDecorator<T, T> partialSnippet = new CriteriaSnippetPartialDecorator<T, T>(properties, rsqlSnippet);
		CriteriaSnippetPaginationDecorator<T, T> paginationSnippet = new CriteriaSnippetPaginationDecorator<T, T>(page, pageSize, partialSnippet);
		return this.<T>buildQueryTemplateMethod(entity, entity, paginationSnippet);
	}
	
	/**
	 * Build a count query with where condition.
	 * 
	 * @param entity  Entity class type.
	 * @param rsql    RSQL string for where clause.
	 * @return        Query with applied algorithms.
	 */
	protected TypedQuery<Long> buildCountQuery(Class<T> entity, String rsql) {
		CriteriaSnippetWhereRsqlDecorator<Long, T> rsqlSnippet = new CriteriaSnippetWhereRsqlDecorator<Long, T>(rsql, null);
		CriteriaSnippetCountDecorator<T> countSnippet = new CriteriaSnippetCountDecorator<T>(rsqlSnippet);
		return this.<Long>buildQueryTemplateMethod(Long.class, entity, countSnippet);
	}

	/**
	 * Build a query template method.
	 * 
	 * @param resultClass Result class type.
	 * @param entity      Entity class type.
	 * @param snippet     Algorithm snippet.
	 * @return            Builded query defined by the algorithm snippet.
	 * @throws IllegalArgumentException 
	 * 					  When entity is null.
	 */
	protected <R> TypedQuery<R> buildQueryTemplateMethod(Class<R> resultClass, Class<T> entity, CriteriaSnippet<R,T> snippet) {
		TypedQuery<R> query = null;

		if (entity == null) {
			throw new IllegalArgumentException("Entity must be defined.");
		}

		if(snippet.validate()) {
			CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<R> criteria = builder.createQuery(resultClass);
			criteria = snippet.modify(criteria, entity, getEntityManager());
			query = getEntityManager().createQuery(criteria);
			query = snippet.configure(query);
			if (this.getQueryConfiguration() != null) {
				this.getQueryConfiguration().applyConfiguration(query);
			}
		}

		return query;
	}	
}
