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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.github.tennaito.test.jpa.entity.InvoiceList;
import com.github.tennaito.test.jpa.entity.Item;

/**
 * @author Antonio Rabelo
 *
 */
public abstract class AbstractEntityServicesTest {
	
	private static boolean loaded = false;
	
	@BeforeClass
	public static void setUpBefore() throws Exception {
		if (!loaded) {
			EntityManager entityManager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
	    	entityManager.getTransaction().begin();

	    	InvoiceList list = new InvoiceList();
	    	list.setId(1);
	    	list.setDescription("Fruits");

	    	Item item1 = new Item();
	    	item1.setDescription("blueberry");
	    	item1.setPrice(0.5d);
	    	item1.setQuantity(2000);
	    	entityManager.persist(item1);
	    	
	    	Item item2 = new Item();
	    	item2.setDescription("strawberry");
	    	item2.setPrice(1.0d);
	    	item2.setQuantity(100);
	    	entityManager.persist(item2);
	    	
	    	Item item3 = new Item();
	    	item3.setDescription("raspberry");
	    	item3.setPrice(0.75d);
	    	item3.setQuantity(300);
	    	entityManager.persist(item3);
	    	
	    	Set<Item> items = new HashSet<Item>();
	    	items.add(item1);
	    	items.add(item2);
	    	items.add(item3);
	    	
	    	list.setItems(items);
	    	
	    	entityManager.persist(list);

	    	entityManager.getTransaction().commit();
			loaded = true;
		}
	}
}
