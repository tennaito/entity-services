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
package com.github.tennaito.entity.service;

import java.util.List;

/**
 * @author Antonio Rabelo
 *
 * @param <T>
 */
public interface EntityQueryService<T> extends EssentialEntityQueryService<T> {

	public long count(Class<T> entity);
	
	public T querySingle(Class<T> entity);
	public T querySingle(Class<T> entity, String rsql);
	public T querySingle(Class<T> entity, List<String> properties);

	public List<T> queryPartial(Class<T> entity, List<String> properties);	
	public List<T> queryPartial(Class<T> entity, List<String> properties, Integer page, Integer pageSize);
	public List<T> queryAll(Class<T> entity);	
	public List<T> queryAll(Class<T> entity, Integer page, Integer pageSize);

	public List<T> queryWhere(Class<T> entity, String rsql);
	public List<T> queryWhere(Class<T> entity, List<String> properties, String rsql);
	public List<T> queryWhere(Class<T> entity, String rsql, Integer page, Integer pageSize);
}
