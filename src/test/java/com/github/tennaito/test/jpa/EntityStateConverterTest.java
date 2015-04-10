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
		
		assertEquals(1, state.get("id"));
		assertEquals("Fruits", state.get("description"));
		assertEquals(3, state.<Set<EntityState>>get("items").size());
	}

	private EntityState createState() {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<InvoiceList> service = new DefaultEntityQueryService<InvoiceList>(manager);
		InvoiceList invoice = service.querySingle(InvoiceList.class, "id==1");
		
		EntityStateConverter<InvoiceList> converter = new DefaultEntityStateConverter<InvoiceList>();
		EntityState state   = converter.createState(invoice);
		return state;
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
}
