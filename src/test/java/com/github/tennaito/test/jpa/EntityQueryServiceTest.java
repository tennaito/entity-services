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

import static junit.framework.Assert.assertEquals;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.github.tennaito.entity.service.EntityQueryService;
import com.github.tennaito.entity.service.impl.DefaultEntityQueryService;
import com.github.tennaito.test.jpa.entity.InvoiceList;
import com.github.tennaito.test.jpa.entity.Item;

/**
 * @author Antonio Rabelo
 *
 */
public class EntityQueryServiceTest extends AbstractEntityServicesTest {

	@Test
	public void testCount() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService service = new DefaultEntityQueryService(manager);
		assertEquals(1, service.count(InvoiceList.class));
	}
	
	@Test
	public void testCountWhere() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService service = new DefaultEntityQueryService(manager);
		assertEquals(1, service.countWhere(InvoiceList.class, "id==1"));
	}

	@Test
	public void testQueryAll() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService service = new DefaultEntityQueryService(manager);
		assertEquals(3, service.queryAll(Item.class, null, null).size());
	}
	
	@Test
	public void testQueryAllPaginated() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<Item> service = new DefaultEntityQueryService<Item>(manager);
		assertEquals("strawberry", service.queryAll(Item.class, 2, 1).get(0).getDescription());
	}	
	
	@Test
	public void testQuerySingle() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<InvoiceList> service = new DefaultEntityQueryService<InvoiceList>(manager);
		assertEquals("Fruits", service.querySingle(InvoiceList.class, null, null).getDescription());
		assertEquals(3, service.querySingle(InvoiceList.class, null, null).getItems().size());
	}
}
