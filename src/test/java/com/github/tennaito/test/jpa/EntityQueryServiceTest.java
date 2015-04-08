package com.github.tennaito.test.jpa;

import static junit.framework.Assert.assertEquals;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tennaito.entity.service.EntityQueryService;
import com.github.tennaito.entity.service.data.EntityResult;
import com.github.tennaito.entity.service.impl.DefaultEntityQueryService;
import com.github.tennaito.test.jpa.entity.InvoiceList;
import com.github.tennaito.test.jpa.entity.Item;

public class EntityQueryServiceTest {
	
	@BeforeClass
	public static void setUpBefore() throws Exception {
    	EntityManager entityManager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
    	entityManager.getTransaction().begin();

    	InvoiceList list = new InvoiceList();
    	list.setId(1);
    	list.setDescription("Fruits");

    	Item item1 = new Item();
    	item1.setDescription("blueberry");
    	item1.setPrice(0.5d);
    	item1.setQuantity(2000);
    	
    	Item item2 = new Item();
    	item2.setDescription("strawberry");
    	item2.setPrice(1.0d);
    	item2.setQuantity(100);

    	Item item3 = new Item();
    	item3.setDescription("raspberry");
    	item3.setPrice(0.75d);
    	item3.setQuantity(300);
    	
    	Set<Item> items = new HashSet<Item>();
    	items.add(item1);
    	items.add(item2);
    	items.add(item3);
    	
    	list.setItems(items);
    	
    	entityManager.persist(list);

    	entityManager.getTransaction().commit();
	}
	
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
	
//	@Test
//	public void testQueryAll() {
//		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
//		EntityQueryService service = new DefaultEntityQueryService(manager);
//		assertEquals(3, service.queryAll(Item.class, null, null).size());
//	}
	
//	@Test
//	public void testQueryAllPaginated() {
//		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
//		EntityQueryService service = new DefaultEntityQueryService(manager);
//		assertEquals("strawberry", service.queryAll(Item.class, 2, 1).get(0).get("name"));
//	}	
	
	@Test
	public void testQuerySingle() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService service = new DefaultEntityQueryService(manager);
		assertEquals("Fruits", service.querySingle(InvoiceList.class, null, null).get("description"));
		List<EntityResult> items = service.querySingle(InvoiceList.class, null, null).get("items");
		assertEquals("blueberry", items.get(0).get("description"));
	}	
}
