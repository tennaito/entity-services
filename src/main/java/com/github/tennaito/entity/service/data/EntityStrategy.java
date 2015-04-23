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

/**
 * EntityStrategy.
 * 
 * Responsible to transform an EntityState object into the former Entity/Pojo object.
 * 
 * @author Antonio Rabelo
 *
 * @param <T> EntityState type.
 */
public class EntityStrategy<T> extends DefaultTransformation<T, EntityState> implements TransformationStrategy<T, EntityState> {

	/**
	 * Constructor.
	 */
	public EntityStrategy() {
		super();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param maxDepth Max depth the recursion can go when parsing an object.
	 */
	public EntityStrategy(int maxDepth) {
		super(maxDepth);
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.data.DefaultTransformation#specificTransformation(java.lang.Object, java.util.Map)
	 */
	@Override
	protected Object specificTransformation(Object from, Map<Object, Object> cache) {
		if (!this.acceptType(from)) {
			throw new IllegalArgumentException(
					"Invalid type, instance must be assignable to com.github.tennaito.entity.service.data.EntityState class.");
		}
		
		EntityState state = (EntityState)from;
		Object     result = null;
		try {
			result = state.getOriginalType().newInstance();
			BeanInfo info = Introspector.getBeanInfo(result.getClass());
	        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
	        	if (pd.getWriteMethod() != null) {
	        		pd.getWriteMethod().invoke(result, parseSiblings(state.get(pd.getName()), cache));
	        	}
	        }
		} catch (IntrospectionException e) {
			throw new IllegalArgumentException(e);
		} catch (ReflectiveOperationException e) {
			throw new IllegalArgumentException(e);
		}		
		return result;
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.data.DefaultTransformation#acceptType(java.lang.Object)
	 */
	@Override
	protected boolean acceptType(Object object) {
		return object != null && EntityState.class.isAssignableFrom(object.getClass());		
	}
}
