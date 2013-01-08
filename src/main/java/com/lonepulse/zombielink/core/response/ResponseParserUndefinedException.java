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
import com.lonepulse.zombielink.core.annotation.Endpoint;
import com.lonepulse.zombielink.core.annotation.Parser;
import com.lonepulse.zombielink.core.annotation.Request;

/**
 * <p>This runtime exception is thrown when a {@link ResponseParser} to be used 
 * for a particular {@link Endpoint}, or {@link Request} thereof, has 
 * not been declared using {@link Parser}.
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class ResponseParserUndefinedException extends ZombieLinkRuntimeException {

	
	private static final long serialVersionUID = -7605261918947538557L;

	
	/**
	 * <p>Displays a detailed description along with the stacktrace. 
	 */
	public ResponseParserUndefinedException(Class<?> endpoint, Method method) {
		
		this("Cannot parse the response for request " + method.getName() + " on endpoint " + endpoint.getName() +
			 ": a " + ResponseParser.class.getName() + " has not been defined via the " + Parser.class.getName() + " annotation.");
	}

	/**
	 * <p>Displays a detailed description along with the stacktrace. 
	 */
	public ResponseParserUndefinedException() {
		
		this("Cannot parse the request response: a " + ResponseParser.class.getName() + 
			  " has not been defined via the " + Parser.class.getName() + " annotation.");
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String)}.
	 */
	public ResponseParserUndefinedException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(Throwable)}.
	 */
	public ResponseParserUndefinedException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link RuntimeException#RuntimeException(String, Throwable)}.
	 */
	public ResponseParserUndefinedException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
