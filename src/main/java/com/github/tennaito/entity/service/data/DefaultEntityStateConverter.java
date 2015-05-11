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

import java.util.ArrayList;
import java.util.Collection;

/**
 * Default converter for Entity Types to EntityState and vice-versa.
 * 
 * @author Antonio Rabelo
 *
 * @param <T> Entity Type.
 */
public class DefaultEntityStateConverter<T> implements EntityStateConverter<T> {
	
	/**
	 * Strategy to transform an Entity type into an EntityState.
	 */
	protected final TransformationStrategy<EntityState, T> toEntityState;
	
	/**
	 * Strategy to transform an EntityState into an Entity Type.
	 */
	protected final TransformationStrategy<T, EntityState> toEntity;
	
	/**
	 * Constructor. 
	 */
	public DefaultEntityStateConverter() {
		this(0);
	}

	/**
	 * Constructor
	 * 
	 * @param maxDepth Max depth that the algorithm will descend in the object graph.
	 */
	public DefaultEntityStateConverter(int maxDepth) {
		this(new EntityStateStrategy<T>(maxDepth), new EntityStrategy<T>(maxDepth));
	}

	/**
	 * Constructor
	 * 
	 * @param toEntityState Transformation Strategy to convert an Entity to EntityState
	 * @param toEntity      Transformation Strategy to convert an EntityState to Entity 
	 */
	public DefaultEntityStateConverter(TransformationStrategy<EntityState, T> toEntityState,
                                       TransformationStrategy<T, EntityState> toEntity) {
		this.toEntityState = toEntityState;
		this.toEntity = toEntity;
	}
	
	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.data.EntityStateConverter#createState(java.lang.Object)
	 */
	public EntityState createState(T entity) {
		return this.toEntityState.transform(entity);
	}

	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.data.EntityStateConverter#createStateList(java.util.Collection)
	 */
	public Collection<EntityState> createStateList(Collection<T> entityList) {
		Collection<EntityState> result = new ArrayList<EntityState>();
		for (T element : entityList) {
			result.add(createState(element));
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.data.EntityStateConverter#createEntity(com.github.tennaito.entity.service.data.EntityState)
	 */
	public T createEntity(EntityState state) {
		return this.toEntity.transform(state);
	}
	
	/* (non-Javadoc)
	 * @see com.github.tennaito.entity.service.data.EntityStateConverter#createEntityList(java.util.Collection)
	 */
	public Collection<T> createEntityList(Collection<EntityState> stateList) {
		Collection<T> result = new ArrayList<T>();
		for (EntityState element : stateList) {
			result.add(createEntity(element));
		}
		return result;
	}	
}
