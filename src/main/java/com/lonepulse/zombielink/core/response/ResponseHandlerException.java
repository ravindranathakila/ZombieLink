package com.lonepulse.zombielink.core.response;

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

import java.lang.reflect.Method;

import com.lonepulse.zombielink.core.ZombieLinkRuntimeException;

/**
 * <p>This runtime exception is thrown whenever a failure occurs in executing 
 * an HTTP request.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class ResponseHandlerException extends ZombieLinkRuntimeException {

	
	private static final long serialVersionUID = -7083028842706994616L;
	

	/**
	 * <p>Displays a detailed description along with the stacktrace. 
	 */
	public ResponseHandlerException(Method request, Class<?> endpoint, Throwable rootCause) {
		
		this("Failed to handle response for " + request.getName() + 
			 " on " + endpoint.getSimpleName(), rootCause);
	}
	
	/**
	 * See {@link RuntimeException#RuntimeException()}.
	 */
	public ResponseHandlerException() {
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String)}.
	 */
	public ResponseHandlerException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(Throwable)}.
	 */
	public ResponseHandlerException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String, Throwable)}.
	 */
	public ResponseHandlerException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
