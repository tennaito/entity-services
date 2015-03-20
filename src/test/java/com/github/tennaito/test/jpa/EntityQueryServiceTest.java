package com.github.tennaito.test.jpa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.tennaito.entity.service.EntityQueryService;
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
    	
    	Set<Item> items = new HashSet<Item>();
    	items.add(item1);
    	items.add(item2);
    	
    	list.setItems(items);
    	
    	entityManager.persist(list);

    	entityManager.getTransaction().commit();
	}
	
	@Test
	public void testSimple() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService service = new DefaultEntityQueryService(manager);
		System.out.println(service.count(InvoiceList.class));
		System.out.println(service.countWhere(InvoiceList.class, "items.id==2"));
	}
}
