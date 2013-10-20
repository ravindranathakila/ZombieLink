package com.lonepulse.zombielink.inject;

import com.lonepulse.zombielink.annotation.Endpoint;

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
 * <p>This runtime exception is thrown when the @{@link Endpoint} annotation is missing on an 
 * endpoint definition.</p>
 * 
 * @version 1.1.1
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class MissingEndpointAnnotationException extends EndpointValidationFailedException {

	
	private static final long serialVersionUID = 4087362624687849076L;

	
	/**
	 * <p>Displays a detailed description with information about the endpoint definition and the missing 
	 * annotation, along with the stacktrace.</p>
	 * 
	 * @param endpoint
	 * 			the {@link Class} of the endpoint definition interface
	 * 
	 * @param missingAnnotation
	 * 			the required annotation which was not found on the endpoint definition
	 * <br><br>
	 * @since 1.2.4
	 */
	public MissingEndpointAnnotationException(Class<?> endpoint, Class<?> missingAnnotation) {
		
		this(new StringBuilder("Missing annotation ").append(missingAnnotation.getName())
			 .append( " on endpoint ").append(endpoint == null? "<null>" :endpoint.getName()).toString());
	}
	
	/**
	 * See {@link RuntimeException#RuntimeException()}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public MissingEndpointAnnotationException() {}

	/**
	 * See {@link RuntimeException#RuntimeException(String)}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public MissingEndpointAnnotationException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(Throwable)}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public MissingEndpointAnnotationException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String, Throwable)}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public MissingEndpointAnnotationException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}