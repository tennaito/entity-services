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
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * @author Antonio Rabelo
 *
 * @param <R> Result Type 
 * @param <T> Entity Type
 */
public abstract class AbstractCriteriaSnippetDecorator<R, T> implements CriteriaSnippet<R, T> {

	/**
	 * CriteriaSnippet to be decorated.
	 */
	protected final CriteriaSnippet<R, T> snippetToBeDecorated;
	
	/**
	 * Constructor
	 * 
	 * @param snippet CriteriaSnippet to be decorated.
	 */
	public AbstractCriteriaSnippetDecorator(CriteriaSnippet<R, T> snippet) {
		if (snippet == null) {
			this.snippetToBeDecorated = new CriteriaSnippet<R, T>() {

				public boolean validate() throws IllegalArgumentException {
					return true;
				}
				public CriteriaQuery<R> modify(CriteriaQuery<R> criteria,
						Class<T> entity, EntityManager manager) {
					return criteria;
				}
				public TypedQuery<R> configure(TypedQuery<R> query) {
					return query;
				}
			};
		} else {
			this.snippetToBeDecorated = snippet;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.snippet.CriteriaSnippet#validate()
	 */
	public boolean validate() throws IllegalArgumentException {
		return this.snippetToBeDecorated.validate();
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.snippet.CriteriaSnippet#modify(javax.persistence.criteria.CriteriaQuery, java.lang.Class, javax.persistence.EntityManager)
	 */
	public CriteriaQuery<R> modify(CriteriaQuery<R> criteria, Class<T> entity, EntityManager manager) {
		return this.snippetToBeDecorated.modify(criteria, entity, manager);
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.snippet.CriteriaSnippet#configure(javax.persistence.TypedQuery)
	 */
	public TypedQuery<R> configure(TypedQuery<R> query) {
		return this.snippetToBeDecorated.configure(query);
	}

	protected static Root<?> findRoot(CriteriaQuery<?> query, Class<?> clazz) {
		Root root = null;
		if (query.getRoots().isEmpty()) {
			root = query.from(clazz);
		} else {
			for (Root<?> r : query.getRoots()) {
				if (clazz.equals(r.getJavaType())) {
					root = (Root)r.as(clazz);
				}
			}
		}
		return root;
	}	
}
