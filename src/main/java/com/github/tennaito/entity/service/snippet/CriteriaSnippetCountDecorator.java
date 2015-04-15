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
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 * @author Antonio Rabelo
 *
 * @param <T> Entity Type
 */
public class CriteriaSnippetCountDecorator<T> extends AbstractCriteriaSnippetDecorator<Long, T> {
	
	/**
	 * Constructor
	 * 
	 * @param snippet CriteriaSnippet to be decorated.
	 */
	public CriteriaSnippetCountDecorator(CriteriaSnippet<Long, T> snippet) {
		super(snippet);
	}
	
	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.snippet.CriteriaSnippetDecorator#validate()
	 */
	public boolean validate() throws IllegalArgumentException {
		return super.validate();
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.snippet.CriteriaSnippetDecorator#modify(javax.persistence.criteria.CriteriaQuery, java.lang.Class, javax.persistence.EntityManager)
	 */
	public CriteriaQuery<Long> modify(CriteriaQuery<Long> criteria, Class<T> entity, EntityManager manager) {
		criteria = super.modify(criteria, entity, manager);
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		criteria.select(builder.count(findRoot(criteria, entity)));
		return criteria;
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.snippet.CriteriaSnippetDecorator#configure(javax.persistence.TypedQuery)
	 */
	public TypedQuery<Long> configure(TypedQuery<Long> query) {
		return super.configure(query);
	}
}
