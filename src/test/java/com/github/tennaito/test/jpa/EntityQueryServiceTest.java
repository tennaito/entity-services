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
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.eclipse.persistence.jpa.JpaQuery;
import org.junit.Test;

import com.github.tennaito.entity.service.EntityQueryService;
import com.github.tennaito.entity.service.impl.DefaultEntityQueryService;
import com.github.tennaito.entity.service.impl.QueryConfiguration;
import com.github.tennaito.test.jpa.entity.InvoiceList;
import com.github.tennaito.test.jpa.entity.Item;

/**
 * @author Antonio Rabelo
 *
 */
public class EntityQueryServiceTest extends AbstractEntityServicesTest {

	@Test(expected=IllegalArgumentException.class)
	public void testEntityQueryServiceInstantiation() {
		new DefaultEntityQueryService<InvoiceList>(null);
	}
	
	@Test
	public void testCount() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<InvoiceList> service = new DefaultEntityQueryService<InvoiceList>(manager);
		assertEquals(1, service.count(InvoiceList.class));
	}
	
	@Test
	public void testCountWhere() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<InvoiceList> service = new DefaultEntityQueryService<InvoiceList>(manager);
		assertEquals(1, service.countWhere(InvoiceList.class, "id==1"));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testCountWhereHint() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<Item> service = new DefaultEntityQueryService<Item>(manager)
			.configure(new QueryConfiguration() {
				public void applyConfiguration(Query query) {
					throw new UnsupportedOperationException();
				}
			});
		assertEquals(3, service.countWhere(Item.class,"description==*berry*"));
	}
	
	@Test
	public void testQueryWhereItems() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<Item> service = new DefaultEntityQueryService<Item>(manager);
		List<Item> items = service.queryWhere(Item.class, "description==*a*");
		assertEquals(2, items.size());
		for (Item item : items) {
			if ("blueberry".equals(item.getDescription())) {
				fail();
				break;
			}
			assertTrue(item.getDescription() != null);
			assertTrue(item.getId() != null);
			assertTrue(item.getPrice() != null);
			assertTrue(item.getQuantity() != null);
		}
	}		
	
	@Test
	public void testQueryWherePaginatedItems() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<Item> service = new DefaultEntityQueryService<Item>(manager);
		List<Item> items = service.queryWhere(Item.class, "description==*a*", 1, 1);
		assertEquals(1, items.size());
		for (Item item : items) {
			if ("blueberry".equals(item.getDescription())) {
				fail();
				break;
			}
			assertTrue(item.getDescription() != null);
			assertTrue(item.getId() != null);
			assertTrue(item.getPrice() != null);
			assertTrue(item.getQuantity() != null);
		}
	}
	
	@Test
	public void testQueryWhereSpecificPropertiesItems() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<Item> service = new DefaultEntityQueryService<Item>(manager)
				.configure(new QueryConfiguration() {
					public void applyConfiguration(Query query) {
						((JpaQuery)query).getDatabaseQuery().dontMaintainCache();
					}
				});
		List<String> properties = new ArrayList<String>();
		properties.add("description");
		List<Item> items = service.queryWhere(Item.class, properties, "description==*a*");
		assertEquals(2, items.size());
		for (Item item : items) {
			if ("blueberry".equals(item.getDescription())) {
				fail();
				break;
			}
			assertTrue(item.getDescription() != null);
			assertTrue(item.getId() == null);
			assertTrue(item.getPrice() == null);
			assertTrue(item.getQuantity() == null);
		}
	}	
	
	@Test
	public void testQueryWhereSpecificPropertiesPaginatedItems() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<Item> service = new DefaultEntityQueryService<Item>(manager)
				.configure(new QueryConfiguration() {
					public void applyConfiguration(Query query) {
						((JpaQuery)query).getDatabaseQuery().dontMaintainCache();
					}
				});
		List<String> properties = new ArrayList<String>();
		properties.add("description");
		List<Item> items = service.queryWhere(Item.class, properties, "description==*a*", 1, 1);
		assertEquals(1, items.size());
		for (Item item : items) {
			if ("blueberry".equals(item.getDescription())) {
				fail();
				break;
			}
			assertTrue(item.getDescription() != null);
			assertTrue(item.getId() == null);
			assertTrue(item.getPrice() == null);
			assertTrue(item.getQuantity() == null);
		}
	}	

	@Test
	public void testQueryPartialSpecificPropertiesItems() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<Item> service = new DefaultEntityQueryService<Item>(manager)
				.configure(new QueryConfiguration() {
					public void applyConfiguration(Query query) {
						((JpaQuery)query).getDatabaseQuery().dontMaintainCache();
					}
				});
		List<String> properties = new ArrayList<String>();
		properties.add("description");
		List<Item> items = service.queryPartial(Item.class, properties);
		assertEquals(3, items.size());
		for (Item item : items) {
			assertTrue(item.getDescription() != null);
			assertTrue(item.getId() == null);
			assertTrue(item.getPrice() == null);
			assertTrue(item.getQuantity() == null);
		}
	}	
	
	@Test
	public void testQueryPartialSpecificPropertiesPaginatedItems() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<Item> service = new DefaultEntityQueryService<Item>(manager)
				.configure(new QueryConfiguration() {
					public void applyConfiguration(Query query) {
						((JpaQuery)query).getDatabaseQuery().dontMaintainCache();
					}
				});
		List<String> properties = new ArrayList<String>();
		properties.add("description");
		List<Item> items = service.queryPartial(Item.class, properties, 1, 2);
		assertEquals(2, items.size());
		for (Item item : items) {
			assertTrue(item.getDescription() != null);
			assertTrue(item.getId() == null);
			assertTrue(item.getPrice() == null);
			assertTrue(item.getQuantity() == null);
		}
	}	

	@Test
	public void testQueryAll() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<Item> service = new DefaultEntityQueryService<Item>(manager);
		assertEquals(3, service.queryAll(Item.class).size());
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
		InvoiceList invoice = service.querySingle(InvoiceList.class);
		assertEquals("Fruits", invoice.getDescription());
		assertEquals(3, invoice.getItems().size());
	}
	
	@Test
	public void testQuerySingleNonUniqueItem() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<Item> service = new DefaultEntityQueryService<Item>(manager);
		try {
			service.querySingle(Item.class);
			fail();
		} catch (NonUniqueResultException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testQuerySingleSpecificItem() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<Item> service = new DefaultEntityQueryService<Item>(manager);
		assertEquals("blueberry",service.querySingle(Item.class, "id==1").getDescription());
	}

	@Test
	public void testQuerySinglePropertiesItem() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<InvoiceList> service = new DefaultEntityQueryService<InvoiceList>(manager)
				.configure(new QueryConfiguration() {
					public void applyConfiguration(Query query) {
						((JpaQuery)query).getDatabaseQuery().dontMaintainCache();
					}
				});
		List<String> properties = new ArrayList<String>();
		properties.add("description");
		InvoiceList invoice = service.querySingle(InvoiceList.class, properties);
		assertTrue(invoice.getDescription() != null);
		assertTrue(invoice.getId() == null);
		assertTrue(invoice.getItems() == null);
	}
	
	@Test
	public void testQuerySingleSpecificPropertiesItem() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<Item> service = new DefaultEntityQueryService<Item>(manager)
				.configure(new QueryConfiguration() {
					public void applyConfiguration(Query query) {
						((JpaQuery)query).getDatabaseQuery().dontMaintainCache();
					}
				});
		List<String> properties = new ArrayList<String>();
		properties.add("description");
		Item item = service.querySingle(Item.class, properties, "id==1");
		assertEquals("blueberry",item.getDescription());		
		assertTrue(item.getDescription() != null);
		assertTrue(item.getId() == null);
		assertTrue(item.getPrice() == null);
		assertTrue(item.getQuantity() == null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testQueryInvalidPagination() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<Item> service = new DefaultEntityQueryService<Item>(manager);
		service.queryAll(Item.class, 0, 0);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testQueryInvalidEntity() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<Item> service = new DefaultEntityQueryService<Item>(manager);
		service.queryAll(null);
	}	
}
