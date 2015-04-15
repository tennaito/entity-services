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

import java.lang.reflect.Array;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

import com.github.tennaito.rsql.jpa.JpaCriteriaQueryVisitor;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;

/**
 * @author Antonio Rabelo
 *
 * @param <R> Result Type 
 * @param <T> Entity Type
 */
public class CriteriaSnippetRsqlDecorator<R, T> extends AbstractCriteriaSnippetDecorator<R, T> {
	
	private final String rsql;

	/**
	 * Constructor
	 * 
	 * @param snippet CriteriaSnippet to be decorated.
	 */
	public CriteriaSnippetRsqlDecorator(String rsql, CriteriaSnippet<R, T> snippet) {
		super(snippet);
		this.rsql = rsql;
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.snippet.CriteriaSnippetDecorator#modify(javax.persistence.criteria.CriteriaQuery, java.lang.Class, javax.persistence.EntityManager)
	 */
	public CriteriaQuery<R> modify(CriteriaQuery<R> criteria, Class<T> entity, EntityManager manager) {
		criteria = super.modify(criteria, entity, manager);
		if (this.rsql != null) {
			criteria = parseRsql(entity, rsql, manager);
		}
		return criteria;
	}
	
	protected CriteriaQuery parseRsql(Class<T> entity, String rsql, EntityManager manager) {
		// Create the JPA Visitor for unknown entity
		RSQLVisitor<CriteriaQuery<T>, EntityManager> visitor 
			= new JpaCriteriaQueryVisitor<T>((T[])Array.newInstance(entity, 0));

		// Parse a RSQL into a Node
		Node rootNode = new RSQLParser().parse(rsql);

		// Visit the node to retrieve CriteriaQuery
		return rootNode.accept(visitor, manager);
	}
}
