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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import com.github.tennaito.entity.service.EntityQueryService;
import com.github.tennaito.entity.service.data.EntityState;
import com.github.tennaito.rsql.jpa.JpaCriteriaQueryVisitor;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;

/**
 * DefaultEntityQueryService.
 * 
 * Responsible for provide a generic querying interface for any entity class.
 * 
 * @author Antonio Rabelo
 */
public class DefaultEntityQueryService implements EntityQueryService {

	/**
	 * EntityManager instance.
	 */
	private final EntityManager manager;
	
	/**
	 * Constructor.
	 * 
	 * @param manager An instance of the EntityManager.
	 */
	public DefaultEntityQueryService(EntityManager manager) {
		if(manager == null) {
			throw new IllegalArgumentException("Must define EntityManager instance");
		}
		this.manager = manager;
	}
	
	/**
	 * Returns an instance of the EntityManager.
	 * 
	 * @return EntityManager instance.
	 */
	protected EntityManager getEntityManager() {
		return this.manager;
	}
	
	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.EntityQueryService#querySingle(java.lang.Class, java.util.List, java.lang.String)
	 */
	public EntityState querySingle(Class<?> entity, List<String> properties, String rsql) {
		return parseSingleResult(buildQueryWhere(entity, properties, rsql, null, null).getSingleResult());
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.EntityQueryService#count(java.lang.Class)
	 */
	public long count(Class<?> entity) {
		return countWhere(entity, null);
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.EntityQueryService#queryPartial(java.lang.Class, java.util.List, java.lang.Integer, java.lang.Integer)
	 */
	public List<EntityState> queryPartial(Class<?> entity, List<String> properties, Integer page, Integer pageSize) {
		return queryWhere(entity, properties, null, page, pageSize);
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.EntityQueryService#queryAll(java.lang.Class, java.lang.Integer, java.lang.Integer)
	 */
	public List<EntityState> queryAll(Class<?> entity, Integer page, Integer pageSize) {
		return queryWhere(entity, null, null, page, pageSize);
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.EntityQueryService#countWhere(java.lang.Class, java.lang.String)
	 */
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

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.EntityQueryService#queryWhere(java.lang.Class, java.util.List, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	public List<EntityState> queryWhere(Class<?> entity, List<String> properties, String rsql, Integer page, Integer pageSize) {
		return parseResultList(buildQueryWhere(entity, properties, rsql, page, pageSize).getResultList());
	}

	protected Query buildQueryWhere(Class<?> entity, List<String> properties, String rsql, Integer page, Integer pageSize) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity must be defined.");
		}
		
		if (page != null && page < 1) {
			page = 1;
		}
		if (pageSize != null && pageSize < 1) {
			pageSize = 1;
		}
		
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery criteria = builder.createQuery(entity);
		Root<?> root = criteria.from(entity);
		if (rsql != null) {
			CriteriaQuery rsqlCriteria = parseRsql(entity, rsql);
			root = findRoot(rsqlCriteria, entity);
			criteria = rsqlCriteria;
		}
		
		if(properties != null) {
			criteria.multiselect(createSelectionList(properties, root));
		}

		Query query = getEntityManager().createQuery(criteria);
		
		if (page != null) {
			query.setFirstResult((page - 1)*pageSize);
		}
		
		if (pageSize != null) {
			query.setMaxResults(pageSize);
		}
		
		return query;
	}
	
	private List<EntityState> parseResultList(List<Object> resultList) {
		List<EntityState> result = new ArrayList<EntityState>();
		for (Object element : resultList) {
			result.add(parseSingleResult(element));
		}
		return result;
	}
	
	private EntityState parseSingleResult(Object singleResult) {
		EntityState result = new EntityState(singleResult.getClass().getName());
		try {
			BeanInfo info = Introspector.getBeanInfo(singleResult.getClass());
	        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
	        	result.put(pd.getName(), parseSiblings(pd.getReadMethod().invoke(singleResult)));
	        }
		} catch (IntrospectionException e) {
			throw new IllegalArgumentException(e);
		} catch (ReflectiveOperationException e) {
			throw new IllegalArgumentException(e);
		}
		return result;
	}
	
	private boolean acceptParse(Object object) {
		return (object.getClass().isArray()) ||
			   (object instanceof Collection) || 
			   (object instanceof Map) || 
			   (object.getClass().isAnnotationPresent(Entity.class));
	}
	
	private Object parseSiblings(Object sibling) {
		Object result = sibling;
		if (acceptParse(sibling)) {
			if (sibling.getClass().isArray()) {
				result = parseSiblingsArray((Object[])sibling);
			} else if (sibling instanceof Collection) {
				result = parseSiblingsCollection((Collection)sibling);
			} else if (sibling instanceof Map) {
				result = parseSiblingsMap((Map<Object, Object>)sibling);
			} else if (sibling.getClass().isAnnotationPresent(Entity.class)) {
				result = parseSingleResult(sibling);
			}
		}
		return result;
	}
	
	private Object[] parseSiblingsArray(Object[] sibling) {
		Object[] result = new Object[sibling.length];
		for (int i = 0; i < sibling.length; i++) {
			result[i] = parseSiblings(sibling[i]);
		}
		return result;
	}
	
	private Collection<Object> parseSiblingsCollection(Collection sibling) {
		Collection<Object> result = new ArrayList<Object>();
		for (Object object : sibling) {
			result.add(parseSiblings(object));
		}
		return result;
	}
	
	private Map<Object, Object> parseSiblingsMap(Map<Object, Object> sibling) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		for (Map.Entry<Object, Object> entry : sibling.entrySet()) {
			result.put(parseSiblings(entry.getKey()), parseSiblings(entry.getValue()));
		}
		return result;
	}

	private List<Selection<?>> createSelectionList(List<String> properties, Root<?> root) {
		List<Selection<?>> selectedProperties = new ArrayList<Selection<?>>();
		for (String property : properties) {
			selectedProperties.add(root.get(property));
		}
		return selectedProperties;
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
	
	protected static  <T> Root<T> findRoot(CriteriaQuery<?> query, Class<T> clazz) {
		for (Root<?> r : query.getRoots()) {
			if (clazz.equals(r.getJavaType())) {
				return (Root<T>) r.as(clazz);
			}
		}
		return (Root<T>) query.getRoots().iterator().next();
	}
}
