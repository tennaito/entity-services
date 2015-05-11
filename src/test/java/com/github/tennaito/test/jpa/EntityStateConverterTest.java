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
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.tennaito.entity.service.EntityQueryService;
import com.github.tennaito.entity.service.data.DefaultEntityStateConverter;
import com.github.tennaito.entity.service.data.EntityState;
import com.github.tennaito.entity.service.data.EntityStateConverter;
import com.github.tennaito.entity.service.data.EntityStateStrategy;
import com.github.tennaito.entity.service.data.EntityStrategy;
import com.github.tennaito.entity.service.data.TransformationStrategy;
import com.github.tennaito.entity.service.impl.DefaultEntityQueryService;
import com.github.tennaito.test.jpa.entity.InvoiceList;
import com.github.tennaito.test.jpa.entity.Item;
import com.github.tennaito.test.pojo.Level;

/**
 * @author Antonio Rabelo
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Introspector.class, EntityStateStrategy.class, BeanInfo.class, PropertyDescriptor.class, Method.class})
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
	public void testStateEquals() throws Exception {
		EntityState state1 = createState();
		EntityState state2 = createState();
		
		// same reference
		assertTrue(state1.equals(state1));
		// same object
		assertTrue(state1.equals(state2));
		// not the same...
		assertFalse(state1.equals(null));
		assertFalse(state1.equals("teste"));
		
		String name = state1.getName();
		Field f = state1.getClass().getDeclaredField("name");
		f.setAccessible(true);
		f.set(state1, null);
		
		assertFalse(state1.equals(state2));
		f.set(state1, name);
		f.setAccessible(false);		
		
		String description = state1.get("description");
		state1.put("description", "fail");
		
		assertFalse(state1.equals(state2));
		
		state1.put("description", description);
		
		Field p = state1.getClass().getDeclaredField("properties");
		p.setAccessible(true);
		Object value = p.get(state1);
		p.set(state1, null);
		
		assertFalse(state1.equals(state2));
		p.set(state1, value);
		p.setAccessible(false);				
		
		EntityState otherState = createStateFromId1(Item.class);
		assertFalse(state1.equals(otherState));
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
		return createStateFromId1(InvoiceList.class);
	}
	
	private <T> EntityState createStateFromId1(Class<T> type) {
		EntityManager manager = EntityManagerFactoryInitializer.getEntityManagerFactory().createEntityManager();
		EntityQueryService<T> service = new DefaultEntityQueryService<T>(manager);
		T object = service.querySingle(type, "id==1");
		
		EntityStateConverter<T> converter = new DefaultEntityStateConverter<T>();
		EntityState state   = converter.createState(object);
		return state;
	}	
	
	@Test
	public void testNotPojoWhenIntrospectionException() throws Exception {
		Level level = new Level("teste", null);
		PowerMockito.mockStatic(Introspector.class);
		Mockito.when(Introspector.getBeanInfo(Level.class)).thenThrow(new IntrospectionException(""));
		EntityStateStrategy<Level> strategy = new EntityStateStrategy<Level>(0);
		assertFalse(strategy.isTypeAcceptable(level));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCannotCreateTargetFromContext() throws Exception {
//		Level level = new Level();
//		EntityState state = Mockito.spy(new EntityState(Level.class));
//		EntityStrategy<Level> strategy = Mockito.spy(new EntityStrategy<Level>());
//		Mockito.when(state.getOriginalType()).thenReturn(clazz);
//		Mockito.when(clazz.newInstance()).thenThrow(new InstantiationException());
//		strategy.transform(state);
	}
	

	@Test(expected=IllegalArgumentException.class)
	public void testEntityStateStrategyIllegalArgumentWhenIntrospectionException() throws Exception {
		this.<EntityState, Level>doIllegalArgumentWhenIntrospectionException(new Level("teste", null), new EntityStateStrategy<Level>());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testEntityStrategyIllegalArgumentWhenIntrospectionException() throws Exception {
		this.<Level, EntityState>doIllegalArgumentWhenIntrospectionException(new EntityState(Level.class), new EntityStrategy<Level>());
	}

	private <T, F> void doIllegalArgumentWhenIntrospectionException(F from, TransformationStrategy<T, F> transformation)
			throws IntrospectionException {
		PowerMockito.mockStatic(Introspector.class);
		Mockito.when(Introspector.getBeanInfo(from.getClass())).thenThrow(new IntrospectionException(""));		
		TransformationStrategy<T, F> strategy = Mockito.spy(transformation);
		Mockito.doReturn(true).when(strategy).isTypeAcceptable(from);
		strategy.transform(from);
		PowerMockito.verifyStatic();
	}	
	
	@Test(expected=IllegalArgumentException.class)
	public void testEntityStateStrategyIllegalArgumentWhenReflectiveOperationException() throws Exception {
		Level level = new Level("teste", null);
		this.<EntityState, Level>doIllegalArgumentWhenReflectiveOperationExceptionOnTransformation(level, level, "level", new EntityStateStrategy<Level>());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEntityStrategyIllegalArgumentWhenReflectiveOperationException() throws Exception {
		this.<Level, EntityState>doIllegalArgumentWhenReflectiveOperationExceptionOnTransformation(new EntityState(Level.class), new Level("teste", null), "level", new EntityStrategy<Level>());
	}

	private <T,F> void doIllegalArgumentWhenReflectiveOperationExceptionOnTransformation(F from, Object object, String property, TransformationStrategy<T, F> transformation)
			throws IntrospectionException, IllegalAccessException,
			InvocationTargetException {
		PowerMockito.mockStatic(Introspector.class);
		BeanInfo info = PowerMockito.mock(BeanInfo.class);		
		PropertyDescriptor prop = PowerMockito.spy(new PropertyDescriptor(property, object.getClass()));
		Method method = PowerMockito.spy(prop.getReadMethod());		
		Mockito.when(method.invoke(prop)).thenThrow(new InvocationTargetException(new Exception()));
		Mockito.when(prop.getReadMethod()).thenReturn(method);
		Mockito.when(info.getPropertyDescriptors()).thenReturn(new PropertyDescriptor[]{prop});
		Mockito.when(Introspector.getBeanInfo(object.getClass())).thenReturn(info);
		
		TransformationStrategy<T, F> strategy = Mockito.spy(transformation);
		Mockito.doReturn(true).when(strategy).isTypeAcceptable(object);
		strategy.transform(from);
	}
}
