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

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.github.tennaito.entity.service.data.EntityState;
import com.github.tennaito.entity.service.data.EntityStateStrategy;
import com.github.tennaito.test.pojo.Level;

/**
 * @author Antonio Rabelo
 *
 */
public class TransformationTest extends AbstractEntityServicesTest {
	
	@Test
	public void testLimitedDepth() {
		Level leaf = new Level("leaf", null);
		Level branch2 = new Level("branch2", leaf);
		Level branch1 = new Level("branch1",branch2);
		Level branch0 = new Level("branch0", branch1);
		Level trunk = new Level("trunk", branch0);
		
		EntityStateStrategy<Level> strategy = new EntityStateStrategy<Level>(1);
		EntityState state = strategy.transform(trunk);
		assertEquals("trunk", state.get("name"));		
		assertEquals(null, state.<EntityState>get("level"));
		
		strategy = new EntityStateStrategy<Level>(2);
		state = strategy.transform(trunk);
		assertEquals("trunk", state.get("name"));		
		assertEquals("branch0", state.<EntityState>get("level").get("name"));
		
		strategy = new EntityStateStrategy<Level>(3);
		state = strategy.transform(trunk);
		assertEquals("trunk", state.get("name"));		
		assertEquals("branch1", state.<EntityState>get("level")
								     .<EntityState>get("level").get("name"));
		
		strategy = new EntityStateStrategy<Level>(4);
		state = strategy.transform(trunk);
		assertEquals("trunk", state.get("name"));		
		assertEquals("branch2", state.<EntityState>get("level")
								     .<EntityState>get("level")
								     .<EntityState>get("level").get("name"));
		
		strategy = new EntityStateStrategy<Level>(5);
		state = strategy.transform(trunk);
		assertEquals("trunk", state.get("name"));		
		assertEquals("leaf", state.<EntityState>get("level")
								  .<EntityState>get("level")
								  .<EntityState>get("level")
								  .<EntityState>get("level").get("name"));
		
		strategy = new EntityStateStrategy<Level>(6);
		state = strategy.transform(trunk);
		assertEquals("trunk", state.get("name"));		
		assertEquals(null, state.<EntityState>get("level")
								.<EntityState>get("level")
								.<EntityState>get("level")
								.<EntityState>get("level")
								.<EntityState>get("level"));		
	}
	
	@Test
	public void testCycles() {
		Level leaf = new Level("leaf", null);
		Level branch = new Level("branch", leaf);
		Level trunk = new Level("trunk", branch);
		// this is crazy!!!
		leaf.setLevel(trunk);
		
		EntityStateStrategy<Level> strategy = new EntityStateStrategy<Level>();
		EntityState state = strategy.transform(trunk);
		assertEquals("trunk", state.get("name"));
		assertEquals("trunk", state.<EntityState>get("level")
								   .<EntityState>get("level")
								   .<EntityState>get("level").get("name"));		
	}
}
