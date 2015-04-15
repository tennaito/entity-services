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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

/**
 * @author Antonio Rabelo
 *
 * @param <R> Result Type 
 * @param <T> Entity Type
 */
public class CriteriaSnippetPaginationDecorator<R, T> extends AbstractCriteriaSnippetDecorator<R, T> {
	
	private final Integer page;
	private final Integer pageSize;

	/**
	 * Constructor
	 * 
	 * @param snippet CriteriaSnippet to be decorated.
	 */
	public CriteriaSnippetPaginationDecorator(Integer page, Integer pageSize, CriteriaSnippet<R, T> snippet) {
		super(snippet);
		this.page = page;
		this.pageSize = pageSize;
	}
	
	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.snippet.CriteriaSnippetDecorator#validate()
	 */
	public boolean validate() throws IllegalArgumentException {
		boolean val = super.validate();
		
		if (this.page != null && this.page < 1) {
			throw new IllegalArgumentException("Page must be a non-zero positive integer.");
		}
		
		if (this.pageSize != null && this.pageSize < 1) {
			throw new IllegalArgumentException("PageSize must be a non-zero positive integer.");
		}

		return val;
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.snippet.CriteriaSnippetDecorator#modify(javax.persistence.criteria.CriteriaQuery, java.lang.Class, javax.persistence.EntityManager)
	 */
	public CriteriaQuery<R> modify(CriteriaQuery<R> criteria, Class<T> entity, EntityManager manager) {
		return super.modify(criteria, entity, manager);
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.snippet.CriteriaSnippetDecorator#configure(javax.persistence.TypedQuery)
	 */
	public TypedQuery<R> configure(TypedQuery<R> query) {
		query = super.configure(query);
		
		if (this.page != null) {
			query.setFirstResult((page - 1)*pageSize);
		}
		
		if (this.pageSize != null) {
			query.setMaxResults(pageSize);
		}
		
		return query;
	}
	
	private List<Selection<?>> createSelectionList(CriteriaQuery<R> criteria, Class<T> entity, List<String> properties) {
		Root<?> root = findRoot(criteria, entity);
		List<Selection<?>> selectedProperties = new ArrayList<Selection<?>>();
		for (String property : properties) {
			selectedProperties.add(root.get(property));
		}
		return selectedProperties;
	}
}
