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
import javax.persistence.criteria.Predicate;

import com.github.tennaito.rsql.jpa.JpaPredicateVisitor;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;

/**
 * Decorate with RSQL string.
 * 
 * @author Antonio Rabelo
 *
 * @param <R> Result Type 
 * @param <T> Entity Type
 */
public abstract class AbstractCriteriaSnippetRsqlDecorator<R, T> extends AbstractCriteriaSnippetDecorator<R, T> {
	
	/**
	 * Rsql condition.
	 */
	protected final String rsql;

	/**
	 * Constructor.
	 * 
	 * @param snippet CriteriaSnippet to be decorated.
	 */
	public AbstractCriteriaSnippetRsqlDecorator(String rsql, CriteriaSnippet<R, T> snippet) {
		super(snippet);
		this.rsql = rsql;
	}
	
	/**
	 * Parse a rsql into it´s correspondent Predicate.
	 * 
	 * @param entity   Entity type.
	 * @param rsql     RSQL string.
	 * @param manager  EntityManager.
	 * @return         Predicate from the rsql.
	 */
	protected Predicate parseRsql(Class<T> entity, String rsql, EntityManager manager) {
		// Create the JPA Visitor for unknown entity
		RSQLVisitor<Predicate, EntityManager> visitor 
			= new JpaPredicateVisitor<T>((T[])Array.newInstance(entity, 0));

		// Parse a RSQL into a Node
		Node rootNode = new RSQLParser().parse(rsql);

		// Visit the node to retrieve CriteriaQuery
		return rootNode.accept(visitor, manager);
	}
}
