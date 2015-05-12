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

import static junit.framework.Assert.assertFalse;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.tennaito.entity.service.data.EntityState;
import com.github.tennaito.entity.service.data.EntityStateStrategy;
import com.github.tennaito.entity.service.data.EntityStrategy;
import com.github.tennaito.entity.service.data.TransformationStrategy;
import com.github.tennaito.test.pojo.Level;

/**
 * @author Antonio Rabelo
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Introspector.class, EntityStateStrategy.class, EntityStrategy.class, BeanInfo.class, PropertyDescriptor.class, Method.class})
public class EntityStrategiesTest extends AbstractEntityServicesTest {
	
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
		Level level = new Level();
		EntityState state = Mockito.spy(new EntityState(Level.class));
		EntityStrategy<Level> strategy = Mockito.spy(new EntityStrategy<Level>());

		// create an InstantiationException
		Class instrumentedClass = int[].class;
		Mockito.when(state.getOriginalType()).thenReturn(instrumentedClass);
		
		strategy.transform(state);
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
		Mockito.when(Introspector.getBeanInfo(Level.class)).thenThrow(new IntrospectionException(""));		
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
