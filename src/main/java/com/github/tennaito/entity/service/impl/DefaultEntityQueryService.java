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

import java.lang.reflect.Array;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.github.tennaito.entity.service.EntityQueryService;
import com.github.tennaito.entity.service.data.EntityResult;
import com.github.tennaito.rsql.jpa.JpaCriteriaQueryVisitor;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;

public class DefaultEntityQueryService implements EntityQueryService {

	private final EntityManager manager;
	
	public DefaultEntityQueryService(EntityManager manager) {
		if(manager == null) {
			throw new IllegalArgumentException("Must define EntityManager instance");
		}
		this.manager = manager;
	}
	
	protected EntityManager getEntityManager() {
		return this.manager;
	}
	
	public EntityResult querySingle(Class<?> entity, List<String> properties, String rsql) {
		return queryWhere(entity, properties, rsql, 1, 1).get(0);
	}

	public long count(Class<?> entity) {
		return countWhere(entity, null);
	}

	public List<EntityResult> queryPartial(Class<?> entity, List<String> properties, int page, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<EntityResult> queryAll(Class<?> entity, int page, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	public long countWhere(Class<?> entity, String rsql) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<?> root = criteria.from(entity);
		if (rsql != null) {
			CriteriaQuery rsqlCriteria = parseRsql(entity, rsql);
			root = findRoot(rsqlCriteria, entity);
			criteria = rsqlCriteria;
		}
		criteria.select(builder.count(root));
		return getEntityManager().createQuery(criteria).getSingleResult();		
	}

	public List<EntityResult> queryWhere(Class<?> entity, List<String> properties, String rsql, int page, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected <T> CriteriaQuery<T> parseRsql(Class<? extends T> entity, String rsql) {
		// Create the JPA Visitor for unknown entity
		RSQLVisitor<CriteriaQuery<T>, EntityManager> visitor 
			= new JpaCriteriaQueryVisitor<T>((T[])Array.newInstance(entity, 0));

		// Parse a RSQL into a Node
		Node rootNode = new RSQLParser().parse(rsql);

		// Visit the node to retrieve CriteriaQuery
		return rootNode.accept(visitor, manager);
	}
	
	public static  <T> Root<T> findRoot(CriteriaQuery<?> query, Class<T> clazz) {

		for (Root<?> r : query.getRoots()) {
			if (clazz.equals(r.getJavaType())) {
				return (Root<T>) r.as(clazz);
			}
		}
		return (Root<T>) query.getRoots().iterator().next();
	}	

}
