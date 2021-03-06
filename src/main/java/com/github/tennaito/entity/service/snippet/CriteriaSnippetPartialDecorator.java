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
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

/**
 * Decorates with partial properties list.
 * 
 * @author Antonio Rabelo
 *
 * @param <R> Result Type 
 * @param <T> Entity Type
 */
public class CriteriaSnippetPartialDecorator<R, T> extends AbstractCriteriaSnippetDecorator<R, T> {
	
	/**
	 * Properties list.
	 */
	private final List<String> properties;
	
	/**
	 * Constructor
	 * 
	 * @param snippet CriteriaSnippet to be decorated.
	 */
	public CriteriaSnippetPartialDecorator(List<String> properties, CriteriaSnippet<R, T> snippet) {
		super(snippet);
		this.properties = properties;
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.snippet.AbstractCriteriaSnippetDecorator#modify(javax.persistence.criteria.CriteriaQuery, java.lang.Class, java.lang.Class, javax.persistence.EntityManager)
	 */
	public CriteriaQuery<R> modify(CriteriaQuery<R> criteria, Class<R> resultClass, Class<T> entity, EntityManager manager) {
		criteria = super.modify(criteria, resultClass, entity, manager);
		if (this.properties != null) {
			criteria = criteria.multiselect(createSelectionList(criteria, entity, properties));
		}
		return criteria;
	}
	
	/**
	 * Creates a List of Selection from the properties list.
	 * 
	 * @param criteria   CriteriaQuery instance.
	 * @param entity     Entity class that has the properties.
	 * @param properties List of the selected properties.
	 * @return           List of Selections.
	 */
	private List<Selection<?>> createSelectionList(CriteriaQuery<R> criteria, Class<T> entity, List<String> properties) {
		Root<?> root = findRoot(criteria, entity);
		List<Selection<?>> selectedProperties = new ArrayList<Selection<?>>();
		for (String property : properties) {
			selectedProperties.add(root.get(property));
		}
		return selectedProperties;
	}
}
