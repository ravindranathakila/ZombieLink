package com.lonepulse.zombielink.proxy;

/*
 * #%L
 * ZombieLink
 * %%
 * Copyright (C) 2013 Lonepulse
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.hamcrest.core.Is;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.lonepulse.zombielink.annotation.Endpoint;

/**
 * <p>Performs unit testing for <b>endpoint validation</b>.</p>
 * 
 * @version 1.1.1
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @category test
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class EndpointValidationTest {
	
	
	//represents an invalid endpoint definition with a missing @{@link Endpoint} annotation.
	private interface MissingEndpointAnnotationEndpoint {}
	
	//represents an invalid endpoint definition with a missing request metadata.
	@Endpoint("http://example.com")
	private interface StrayEndpointRequestEndpoint {
		
		void strayRequest();
	}
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	
	/**
	 * <p>Tests endpoint validation for a missing @{@link Endpoint} annotation.</p>
	 *  
	 * @since 1.3.0
	 */
	@Test @SuppressWarnings("unchecked") //safe case to Class<Throwable> from a known exception
	public final void testMissingEndpointAnnotation() throws ClassNotFoundException {
		
		expectedException.expect(Is.isA((Class<Throwable>) 
				Class.forName("com.lonepulse.zombielink.proxy.MissingEndpointAnnotationException")));
		
		Validators.ENDPOINT.validate(MissingEndpointAnnotationEndpoint.class);
	}
	
	/**
	 * <p>Tests endpoint validation for missing request metadata on methods.</p>
	 *  
	 * @since 1.3.0
	 */
	@Test @SuppressWarnings("unchecked") //safe case to Class<Throwable> from a known exception
	public final void testStrayRequest() throws ClassNotFoundException {
		
		expectedException.expect(Is.isA((Class<Throwable>) 
				Class.forName("com.lonepulse.zombielink.proxy.StrayEndpointRequestException")));
		
		Validators.ENDPOINT.validate(StrayEndpointRequestEndpoint.class);
	}
}
