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
package com.github.tennaito.test.jpa;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.junit.Test;

import com.github.tennaito.entity.service.snippet.AbstractCriteriaSnippetDecorator;
import com.github.tennaito.entity.service.snippet.CriteriaSnippet;
import com.github.tennaito.test.jpa.entity.Item;

/**
 * @author Antonio Rabelo
 */
public class CriteriaSnippetDecoratorTest extends AbstractEntityServicesTest {

	@Test(expected=NullPointerException.class)
	public void testNoRoot() {
		CriteriaSnippet<Long, Item> snippet = new AbstractCriteriaSnippetDecorator<Long, Item>(null) {
			@Override
			public CriteriaQuery<Long> modify(CriteriaQuery<Long> criteria,
					Class<Long> resultClass, Class<Item> entity,
					EntityManager manager) {
				CriteriaBuilder builder = manager.getCriteriaBuilder();
				// will cause error because cant find root (null).
				criteria.select(builder.count(findRoot(criteria, Integer.class)));
				return criteria;
			}
		};
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		CriteriaQuery<Long> criteria = manager.getCriteriaBuilder().createQuery(Long.class);
		criteria.from(Item.class);
		snippet.modify(criteria, Long.class, Item.class, manager);
	}
}
