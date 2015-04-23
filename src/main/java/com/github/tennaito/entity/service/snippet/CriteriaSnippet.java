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
package com.github.tennaito.entity.service.snippet;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

/**
 * CriteriaSnippet.
 * 
 * Interface to modify a criteria query.
 * 
 * @author Antonio Rabelo
 *
 * @param <R> Result class
 * @param <T> Entity class
 */
public interface CriteriaSnippet<R, T> {
	/**
	 * Validate the query against some conditions.
	 * 
	 * @return true is conditions are satisfied, false otherwise.
	 * 
	 * @throws IllegalArgumentException
	 * 				When conditions are not satisfied.
	 */
	public boolean validate() throws IllegalArgumentException;
	
	/**
	 * Modify a CriteriaQuery with an specific algorithm.
	 * 
	 * @param criteria    CriteriaQuery to be modified.
	 * @param resultClass ResultClass.
	 * @param entity      Entity class.
	 * @param manager     EntityManager instance.
	 * @return		      Modified CriteriaQuery.
	 */
	public CriteriaQuery<R> modify(CriteriaQuery<R> criteria, Class<R> resultClass, Class<T> entity, EntityManager manager);
	
	/**
	 * Configure the TypedQuery that is the result of the criteria query.
	 * 
	 * @param query TypedQuery derived from the CriteriaQuery.
	 * @return      Modified TypedQuery.
	 */
	public TypedQuery<R> configure(TypedQuery<R> query);
}
