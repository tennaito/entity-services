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

import com.github.tennaito.entity.service.data.EntityResult;

public interface EntityQueryService {

	public EntityResult querySingle(Class<?> entity, List<String> properties, String rsql);
	
	public long count(Class<?> entity);
	public List<EntityResult> queryPartial(Class<?> entity, List<String> properties, Integer page, Integer pageSize);
	public List<EntityResult> queryAll(Class<?> entity, Integer page, Integer pageSize);

	public long countWhere(Class<?> entity, String rsql);
	public List<EntityResult> queryWhere(Class<?> entity, List<String> properties, String rsql, Integer page, Integer pageSize);
}
