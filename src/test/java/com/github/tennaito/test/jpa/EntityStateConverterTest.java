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

import java.util.Collection;
import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.github.tennaito.entity.service.EntityQueryService;
import com.github.tennaito.entity.service.data.DefaultEntityStateConverter;
import com.github.tennaito.entity.service.data.EntityState;
import com.github.tennaito.entity.service.data.EntityStateConverter;
import com.github.tennaito.entity.service.impl.DefaultEntityQueryService;
import com.github.tennaito.test.jpa.entity.InvoiceList;
import com.github.tennaito.test.jpa.entity.Item;

/**
 * @author Antonio Rabelo
 *
 */
public class EntityStateConverterTest extends AbstractEntityServicesTest {
	
	@Test
	public void testCreateState() {
		EntityState state = createState();
		
		assertEquals("InvoiceList", state.getName());
		assertEquals(1, state.get("id"));
		assertEquals("Fruits", state.get("description"));
		assertEquals(3, state.<Set<EntityState>>get("items").size());
	}
	
	@Test
	public void testCreateStateList() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<InvoiceList> service = new DefaultEntityQueryService<InvoiceList>(manager);
		InvoiceList invoice = service.querySingle(InvoiceList.class, "id==1");
		
		EntityStateConverter<Item> converter = new DefaultEntityStateConverter<Item>();
		
		Collection<EntityState> states = converter.createStateList(invoice.getItems());
		
		assertEquals(3, states.size());
	}
	
	@Test
	public void testCreateEntityList() {
		EntityState state = createState();
		
		EntityStateConverter<Item> converter = new DefaultEntityStateConverter<Item>();
		
		Collection<Item> states = converter.createEntityList(state.<Set<EntityState>>get("items"));
		
		assertEquals(3, states.size());
	}
	
	
	@Test
	public void testStateEquals() {
		EntityState state1 = createState();
		EntityState state2 = createState();
		
		assertTrue(state1.equals(state2));
	}
	
	@Test
	public void testCreateEntity() {
		EntityStateConverter<InvoiceList> converter = new DefaultEntityStateConverter<InvoiceList>();
		EntityState state = createState();
		InvoiceList entity = converter.createEntity(state);
		
		assertEquals(1, (Object) entity.getId());
		assertEquals("Fruits", entity.getDescription());
		assertEquals(3, entity.getItems().size());
		for (Object element : entity.getItems()) {
			assertTrue(Item.class.isAssignableFrom(element.getClass()));			
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidCreateEntity() {
		EntityStateConverter<InvoiceList> converter = new DefaultEntityStateConverter<InvoiceList>();
		converter.createEntity(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidCreateState() {
		EntityStateConverter<InvoiceList> converter = new DefaultEntityStateConverter<InvoiceList>();
		converter.createState(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidDepth() {
		new DefaultEntityStateConverter<InvoiceList>(-1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEntityStateInvalidProperty() {
		EntityState state   = createState();
		state.get("invalid");
	}
	
	private EntityState createState() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<InvoiceList> service = new DefaultEntityQueryService<InvoiceList>(manager);
		InvoiceList invoice = service.querySingle(InvoiceList.class, "id==1");
		
		EntityStateConverter<InvoiceList> converter = new DefaultEntityStateConverter<InvoiceList>();
		EntityState state   = converter.createState(invoice);
		return state;
	}
}
