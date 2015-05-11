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
import java.io.Serializable;
import java.util.Map;

/**
 * EntityStateStrategy.
 * 
 * Responsible to transform an Entity/Pojo object type into EntityState.
 * 
 * @author Antonio Rabelo
 *
 * @param <T> Entity type.
 */
public class EntityStateStrategy<T> extends DefaultTransformation<EntityState, T> implements TransformationStrategy<EntityState, T> {

	/**
	 * Constructor.
	 */
	public EntityStateStrategy() {
		super();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param maxDepth Max depth the recursion can go when parsing an object.
	 */
	public EntityStateStrategy(int maxDepth) {
		super(maxDepth);
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.data.DefaultTransformation#specificTransformation(java.lang.Object, java.lang.Object, java.util.Map)
	 */
	@Override
	protected Object specificTransformation(Object to, Object from, Map<Object, Object> cache) {
		if (to == null || !this.isTypeAcceptable(from)) {
			throw new IllegalArgumentException("Invalid type, to and from instance must be a Pojo and not null.");
		}
		EntityState result = (EntityState)to;
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
	
	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.data.TransformationStrategy#acceptType(java.lang.Object)
	 */
	public boolean isTypeAcceptable(Object object) {
		return object != null && this.isPojo(object);
	}
	
	/**
	 * Verifies if the object is a Pojo.
	 * 
	 * @param object Object instance.
	 * @return true if it is not primitive, not java.lang, if it is serializable and has som properties.
	 * 		   false otherwise. 
	 */
	protected boolean isPojo(Object object) {
		boolean result = false;
		try {
			result = // is not primitive
					 (!object.getClass().isPrimitive()) && 
					 // is serializable
					 (Serializable.class.isAssignableFrom(object.getClass())) &&
					 // is not a java.lang
					 (!object.getClass().getName().startsWith("java.lang")) &&					 
					 // has properties
					 (Introspector.getBeanInfo(object.getClass()).getPropertyDescriptors().length > 0);
		} catch (IntrospectionException e) {
			result = false;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.data.DefaultTransformation#createTargetFromContext(java.lang.Object)
	 */
	@Override
	protected Object createTargetFromContext(Object from) {
		Object state = null;
		if (from != null) {
			state = new EntityState(from.getClass());
		}
		return state;
	}
}
