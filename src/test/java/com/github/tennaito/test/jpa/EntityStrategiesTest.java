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
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.tennaito.entity.service.data.DefaultEntityStateConverter;
import com.github.tennaito.entity.service.data.DefaultTransformation;
import com.github.tennaito.entity.service.data.EntityState;
import com.github.tennaito.entity.service.data.EntityStateConverter;
import com.github.tennaito.entity.service.data.EntityStateStrategy;
import com.github.tennaito.entity.service.data.EntityStrategy;
import com.github.tennaito.entity.service.data.TransformationStrategy;
import com.github.tennaito.test.pojo.Level;

/**
 * @author Antonio Rabelo
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Introspector.class, BeanInfo.class, PropertyDescriptor.class, Method.class})
public class EntityStrategiesTest extends AbstractEntityServicesTest {
	
	@Test
	@PrepareForTest(EntityStateStrategy.class)
	public void testNotPojoWhenIntrospectionException() throws Exception {
		Level level = new Level("teste");
		PowerMockito.mockStatic(Introspector.class);
		Mockito.when(Introspector.getBeanInfo(Level.class)).thenThrow(new IntrospectionException(""));
		EntityStateStrategy<Level> strategy = new EntityStateStrategy<Level>(0);
		assertFalse(strategy.isTypeAcceptable(level));
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testCannotCreateTargetFromContext() throws Exception {
		Level level = new Level();
		EntityState state = Mockito.spy(new EntityState(Level.class));
		EntityStrategy<Level> strategy = Mockito.spy(new EntityStrategy<Level>());

		// create an InstantiationException
		Class instrumentedClass = int[].class;
		Mockito.when(state.getOriginalType()).thenReturn(instrumentedClass);
		
		strategy.transform(state);
	}

	@Test(expected=UnsupportedOperationException.class)
	@PrepareForTest({EntityStateStrategy.class, DefaultTransformation.class})	
	public void testEntityStateStrategyUnsupportedWhenIntrospectionException() throws Exception {
		PowerMockito.mockStatic(Introspector.class);
		// transformation expect Level.class both for getting properties (EntityStateStrategy) or setting properties (EntityStrategy)
		Mockito.when(Introspector.getBeanInfo(Level.class)).thenCallRealMethod();
		Mockito.when(Introspector.getBeanInfo(Level.class)).thenThrow(new IntrospectionException(""));		
		
		new EntityStateStrategy<Level>().transform(new Level("teste"));
		PowerMockito.verifyStatic();
	}

	@Test(expected=UnsupportedOperationException.class)
	@PrepareForTest({EntityStrategy.class, DefaultTransformation.class})
	public void testEntityStrategyUnsupportedWhenIntrospectionException() throws Exception {
		PowerMockito.mockStatic(Introspector.class);
		// transformation expect Level.class both for getting properties (EntityStateStrategy) or setting properties (EntityStrategy)
		Mockito.when(Introspector.getBeanInfo(Level.class)).thenThrow(new IntrospectionException(""));		
		
		new EntityStrategy<Level>().transform(new EntityState(Level.class));
		
		PowerMockito.verifyStatic();
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testEntityStateStrategyUnsupportedWhenReflectiveOperationException() throws Exception {
		Level from = new Level("level");
		TransformationStrategy<EntityState, Level> transformation = new EntityStateStrategy<Level>() {
			@Override
			protected Object parseSiblings(Object sibling,
					Map<Object, Object> cache)
					throws ReflectiveOperationException {
				throw new ReflectiveOperationException();
			}
		};
		transformation.transform(from);		
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testEntityStrategyUnsupportedWhenReflectiveOperationException() throws Exception {
		EntityStateConverter<Level> converter = new DefaultEntityStateConverter<Level>();
		EntityState from = converter.createState(new Level("level"));
		TransformationStrategy<Level, EntityState> transformation = new EntityStrategy<Level>() {
			@Override
			protected Object parseSiblings(Object sibling,
					Map<Object, Object> cache)
					throws ReflectiveOperationException {
				throw new ReflectiveOperationException();
			}
		};
		transformation.transform(from);		
	}
}
