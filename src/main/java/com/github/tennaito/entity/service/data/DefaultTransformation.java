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
 * @author Antonio Rabelo
 *
 * @param <T> Object 'to' that is the result.
 * @param <F> Object 'from' that is the base.
 */
public abstract class DefaultTransformation<T, F> implements TransformationStrategy<T, F> {
	protected final int maxDepth;	
	protected int depth = 0;
	
	public DefaultTransformation() {
		this(0);
	}
	
	public DefaultTransformation(int maxDepth) {
		if (maxDepth < 0) {
			throw new IllegalArgumentException(
					"Parsing depth cannot be negative. Zero (0) means infinite, positive value means limitation.");
		}
		this.maxDepth = maxDepth;
	}
	
	public T transform(F from) {
		return (T)cachedTransform(from, new WeakHashMap<Object, Object>());
	}
	
	protected Object cachedTransform(Object from, Map<Object, Object> cache) {
		Object result = null;
		if (cache.containsKey(from)) {
			result = cache.get(from);
		} else {
			result = specificTransformation(from, cache);
			cache.put(from, result);
		}
		return result;
	}

	protected abstract Object specificTransformation(Object from, Map<Object, Object> cache);
	
	protected abstract boolean acceptType(Object object);

	protected boolean acceptParse(Object object) {
		return (object.getClass().isArray()) ||
			   (object instanceof Collection) || 
			   (object instanceof Map) || 
			   acceptType(object);
	}
	
	protected Object parseSiblings(Object sibling, Map<Object, Object> cache) throws ReflectiveOperationException  {
		Object result = null;
		++depth;
		if (this.maxDepth == 0 || depth <= this.maxDepth) {
			result = sibling;
			if (acceptParse(sibling)) {
				if (sibling.getClass().isArray()) {
					result = parseSiblingsArray((Object[])sibling, cache);
				} else if (sibling instanceof Collection) {
					result = parseSiblingsCollection((Collection)sibling, cache);
				} else if (sibling instanceof Map) {
					result = parseSiblingsMap((Map<Object, Object>)sibling, cache);
				} else if (acceptType(sibling)) {
					result = cachedTransform(sibling, cache);
				}
			}
		}
		--depth;
		return result;
	}
	
	protected Object[] parseSiblingsArray(Object[] sibling, Map<Object, Object> cache) throws ReflectiveOperationException {
		Object[] result = new Object[sibling.length];
		for (int i = 0; i < sibling.length; i++) {
			result[i] = parseSiblings(sibling[i], cache);
		}
		return result;
	}
	
	protected Collection<Object> parseSiblingsCollection(Collection sibling, Map<Object, Object> cache) throws ReflectiveOperationException {
		Collection<Object> result = sibling.getClass().newInstance();
		for (Object object : sibling) {
			result.add(parseSiblings(object, cache));
		}
		return result;
	}
	
	protected Map<Object, Object> parseSiblingsMap(Map<Object, Object> sibling, Map<Object, Object> cache) throws ReflectiveOperationException {
		Map<Object, Object> result = sibling.getClass().newInstance();
		for (Map.Entry<Object, Object> entry : sibling.entrySet()) {
			result.put(parseSiblings(entry.getKey(), cache), parseSiblings(entry.getValue(), cache));
		}
		return result;
	}
}
