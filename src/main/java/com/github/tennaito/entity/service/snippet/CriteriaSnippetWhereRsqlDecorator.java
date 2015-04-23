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
package com.github.tennaito.entity.service.snippet;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

/**
 * Decorate Where clause with RSQL string.
 * 
 * @author Antonio Rabelo
 *
 * @param <R> Result Type 
 * @param <T> Entity Type
 */
public class CriteriaSnippetWhereRsqlDecorator<R, T> extends AbstractCriteriaSnippetRsqlDecorator<R, T> {

	/**
	 * Constructor.
	 * 
	 * @param snippet CriteriaSnippet to be decorated.
	 */
	public CriteriaSnippetWhereRsqlDecorator(String rsql, CriteriaSnippet<R, T> snippet) {
		super(rsql, snippet);
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.snippet.AbstractCriteriaSnippetDecorator#modify(javax.persistence.criteria.CriteriaQuery, java.lang.Class, java.lang.Class, javax.persistence.EntityManager)
	 */
	public CriteriaQuery<R> modify(CriteriaQuery<R> criteria, Class<R> resultClass, Class<T> entity, EntityManager manager) {
		criteria = super.modify(criteria, resultClass, entity, manager);
		if (this.rsql != null) {
			criteria = buildRsqlWhereClause(resultClass, entity, rsql, manager);
		}
		return criteria;
	}
	
	/**
	 * Build a Where clause from Rsql.
	 * 
	 * @param resultClass Result class type.
	 * @param entity      Entity type.
	 * @param rsql        RSQL string.
	 * @param manager     EntityManager.
	 * @return            CriteriaQuery from the rsql.
	 */
	protected CriteriaQuery<R> buildRsqlWhereClause(Class<R> resultClass, Class<T> entity, String rsql, EntityManager manager) {
		Predicate predicate = parseRsql(entity, rsql, manager);
    	CriteriaBuilder builder = manager.getCriteriaBuilder();
    	CriteriaQuery<R> criteria = builder.createQuery(resultClass);
    	return criteria.where(predicate);
	}
}
