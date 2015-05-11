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

import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Default transformation parser.
 * 
 * @author Antonio Rabelo
 *
 * @param <T> Object 'to' that is the result.
 * @param <F> Object 'from' that is the base.
 */
public abstract class DefaultTransformation<T, F> implements TransformationStrategy<T, F> {
	
	/**
	 * Max depth the the recursive algorithm will descend into the object graph. 
	 */
	protected final int maxDepth;	
	
	/**
	 * Depth counter. 
	 */
	protected int depth = 1;
	
	/**
	 * Constructor.
	 */
	public DefaultTransformation() {
		this(0);
	}
	
	/**
	 * Constructor
	 * 
	 * @param maxDepth Max depth that the algorithm will descend in the object graph. 
	 */
	public DefaultTransformation(int maxDepth) {
		if (maxDepth < 0) {
			throw new IllegalArgumentException(
					"Parsing depth cannot be negative. Zero (0) means infinite, positive value means limitation.");
		}
		this.maxDepth = maxDepth;
	}
	
	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.data.TransformationStrategy#transform(java.lang.Object)
	 */
	public T transform(F from) {
		return (T)cachedTransform(from, new WeakHashMap<Object, Object>());
	}
	
	/**
	 * Caches transformation to evict dependency cycle.
	 *  
	 * @param from  Object instance to be parsed.
	 * @param cache Map cache to evict dependency cycle.
	 * @return Object transformed.
	 */
	protected Object cachedTransform(Object from, Map<Object, Object> cache) {
		Object result = null;
		if (cache.containsKey(System.identityHashCode(from))) {
			result = cache.get(System.identityHashCode(from));
		} else {
			Object to = createTargetFromContext(from);
			cache.put(System.identityHashCode(from), to);
			result = specificTransformation(to, from, cache);
		}
		return result;
	}
	
	/**
	 * Factory method that instantiate the target object.
	 * 
	 * @param from the context of the target is the object that we want to transform.
	 * @return Instance of the target object.
	 */
	protected abstract Object createTargetFromContext(Object from);

	/**
	 * Defines the specific transformation algorithm for parsing a single object.
	 * 
	 * @param to    Object where transformation will occur.
	 * @param from  Object to be transformed.
	 * @param cache Map cache to evict dependency cycle.
	 * @return      Object transformed.
	 */
	protected abstract Object specificTransformation(Object to, Object from, Map<Object, Object> cache);

	/**
	 * Verify in an object is able to be parsed.
	 * 
	 * @param object Object instance.
	 * @return true if it is able, false if not.
	 */
	protected boolean acceptParse(Object object) {
		return (object != null) && 
			   ((object.getClass().isArray()) ||
			   (object instanceof Collection) || 
			   (object instanceof Map) || 
			   isTypeAcceptable(object));
	}
	
	/**
	 * Parse an Object Sibling.
	 * 
	 * @param sibling Object sibling.
	 * @param cache   Map cache to evict dependency cycle.
	 * @return        Parsed Object.
	 * @throws ReflectiveOperationException
	 * 				  When the reflection goes wrong. 
	 */
	protected Object parseSiblings(Object sibling, Map<Object, Object> cache) throws ReflectiveOperationException  {
		Object result = null;
		if (acceptParse(sibling)) {
			if (this.maxDepth == 0 || depth < this.maxDepth) {
				if (sibling.getClass().isArray()) {
					result = parseSiblingsArray((Object[])sibling, cache);
				} else if (sibling instanceof Collection) {
					result = parseSiblingsCollection((Collection)sibling, cache);
				} else if (sibling instanceof Map) {
					result = parseSiblingsMap((Map<Object, Object>)sibling, cache);
				} else if (isTypeAcceptable(sibling)) {
					++depth;
					result = cachedTransform(sibling, cache);
					--depth;
				}
			}
		} else {
			result = sibling;
		}
		return result;
	}
	
	/**
	 * Parse all Array children.
	 * 
	 * @param sibling  Array siblings.
	 * @param cache    Map cache to evict dependency cycle.
	 * @return         Parsed Array.
	 * @throws ReflectiveOperationException
	 * 				  When the reflection goes wrong. 
	 */
	protected Object[] parseSiblingsArray(Object[] sibling, Map<Object, Object> cache) throws ReflectiveOperationException {
		Object[] result = new Object[sibling.length];
		for (int i = 0; i < sibling.length; i++) {
			result[i] = parseSiblings(sibling[i], cache);
		}
		return result;
	}
	
	/**
	 * Parse all Collection children.
	 * 
	 * @param sibling Collection siblings.
	 * @param cache   Map cache to evict dependency cycle.
	 * @return        Parsed Collection.
	 * @throws ReflectiveOperationException
	 * 				  When the reflection goes wrong.
	 */
	protected Collection<Object> parseSiblingsCollection(Collection sibling, Map<Object, Object> cache) throws ReflectiveOperationException {
		Collection<Object> result = sibling.getClass().newInstance();
		for (Object object : sibling) {
			result.add(parseSiblings(object, cache));
		}
		return result;
	}
	
	/**
	 * Parse all Map children.
	 * 
	 * @param sibling Map siblings.
	 * @param cache   Map cache to evict dependency cycle.
	 * @return        Parsed Map.
	 * @throws ReflectiveOperationException 
	 * 				  When the reflection goes wrong.
	 */
	protected Map<Object, Object> parseSiblingsMap(Map<Object, Object> sibling, Map<Object, Object> cache) throws ReflectiveOperationException {
		Map<Object, Object> result = sibling.getClass().newInstance();
		for (Map.Entry<Object, Object> entry : sibling.entrySet()) {
			result.put(parseSiblings(entry.getKey(), cache), parseSiblings(entry.getValue(), cache));
		}
		return result;
	}
}
