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
package com.github.tennaito.entity.service.data;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Map;

import javax.persistence.Entity;

/**
 * @author Antonio Rabelo
 *
 * @param <T> Entity type.
 */
public class EntityStateStrategy<T> extends DefaultTransformation<EntityState, T> implements TransformationStrategy<EntityState, T> {

	public EntityStateStrategy(int maxDepth) {
		super(maxDepth);
	}

	@Override
	protected Object specificTransformation(Object from, Map<Object, Object> cache) {
		if (!from.getClass().isAnnotationPresent(Entity.class)) {
			throw new IllegalArgumentException("Invalid type, instance must have @javax.persistence.Entity annotation.");
		}
		EntityState result = new EntityState(from.getClass());
		try {
			BeanInfo info = Introspector.getBeanInfo(from.getClass());
	        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
	        	if (pd.getReadMethod() != null) {
	        		result.put(pd.getName(), parseSiblings(pd.getReadMethod().invoke(from), cache));
	        	}
	        }
		} catch (IntrospectionException e) {
			throw new IllegalArgumentException(e);
		} catch (ReflectiveOperationException e) {
			throw new IllegalArgumentException(e);
		}		
		return result;
	}
	
	@Override
	protected boolean acceptType(Object object) {
		return object.getClass().isAnnotationPresent(Entity.class);
	}
}