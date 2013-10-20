package com.lonepulse.zombielink.inject;

import static com.lonepulse.zombielink.util.Assert.assertNotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.lonepulse.zombielink.ValidationFailedException;
import com.lonepulse.zombielink.Validator;
import com.lonepulse.zombielink.annotation.Endpoint;
import com.lonepulse.zombielink.annotation.Request;

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


/**
 * <p>Mediates communication between all concrete implementation of {@link Validator}s and their sources.</p> 
 * 
 * <p>The following validators are available:</p>
 * 
 * <ul>
 * 	<li>{@link Validators#ENDPOINT}</li>
 * </ul>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
final class Validators {
	
	
	private Validators() {}
	
	
	/**
	 * <p>Validates and endpoint definition by reading the metadata contained on the interface {@link Class}.</p>
	 * 
	 * <p>The following validations are performed:</p>
	 * <ol>
	 * 	<li>Mandates interfaces for expressing endpoint definitions ({@link EndpointValidationFailedException})</li>
	 * 	<li>Checks the existence of an @{@link Endpoint} annotation ({@link MissingEndpointAnnotationException})</li>
	 * 	<li>Confirms the minimum requirement of a <b>host name</b> ({@link MissingEndpointHostException})</li>
	 * 	<li>Ensures that all method definitions are invokable requests ({@link StrayRequestException})</li>
	 * </ol>
	 * 
	 * @since 1.2.4
	 */
	public static final Validator<Class<?>> ENDPOINT = EndpointValidator.INSTANCE;
	
	
	private enum EndpointValidator implements Validator<Class<?>> {

		
		INSTANCE;
		
		
		@Override
		public void validate(Class<?> endpointDefinition) throws ValidationFailedException {

			assertNotNull(endpointDefinition);
			
			try {
				
				if(!endpointDefinition.isInterface()) {
					
					throw new EndpointValidationFailedException("The endpoint should be defined on an interface.");
				}
				
				if(!endpointDefinition.isAnnotationPresent(Endpoint.class)) {
					
					throw new MissingEndpointAnnotationException(endpointDefinition, Endpoint.class);
				}
				
				Endpoint endpoint = endpointDefinition.getAnnotation(Endpoint.class); 
				
				String value = endpoint.value();
				String host = (value == null || value.isEmpty())? endpoint.host() :value;
				
				if(host == null || host.isEmpty()) {
					
					throw new MissingEndpointHostException(endpointDefinition);
				}
				
				Method[] requestDefinitions = endpointDefinition.getMethods();
				
				List<Method> strayRequests = new ArrayList<Method>();
				
				for (Method requestDefinition : requestDefinitions) {
					
					if(!requestDefinition.isAnnotationPresent(Request.class)) {
						
						strayRequests.add(requestDefinition);
					}
				}
				
				if(!strayRequests.isEmpty()) {
					
					throw new StrayEndpointRequestException(strayRequests);
				}
			}
			catch(Exception e) {
				
				throw (e instanceof EndpointValidationFailedException)? 
						(EndpointValidationFailedException)e :new EndpointValidationFailedException(endpointDefinition, e);
			}
		}
	}
}