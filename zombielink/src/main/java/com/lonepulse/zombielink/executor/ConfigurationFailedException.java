package com.lonepulse.zombielink.executor;

/*
 * #%L
 * ZombieLink
 * %%
 * Copyright (C) 2013 - 2014 Lonepulse
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

import com.lonepulse.zombielink.ZombieLinkRuntimeException;
import com.lonepulse.zombielink.proxy.Zombie;

/**
 * <p>This exception is thrown due to a failure in <b>managing</b> a {@link Zombie.Configuration}.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
class ConfigurationFailedException extends ZombieLinkRuntimeException {

	
	private static final long serialVersionUID = 1799332682414518776L;
	

	/**
	 * <p>Constructs a message which identifies the affected endpoint along with the root cause.</p>
	 * 
	 * @param endpointClass
	 * 			the {@link Class} of the endpoint interface whose configuration failed to be managed
	 * <br><br>
	 * @param rootCause
	 * 			the root {@link Throwable} cause which resulted in a failure to manage the configuration
	 * <br><br>
	 * @since 1.3.0
	 */
	public ConfigurationFailedException(Class<?> endpointClass, Throwable rootCause) {
		
		this(new StringBuilder("Failed to manage the configuration for endpoint <")
			 .append(endpointClass.getName()).append(">").toString(), rootCause);
	}
	
	/**
	 * See {@link ZombieLinkRuntimeException#ZombieLinkRuntimeException()}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public ConfigurationFailedException() {}

	/**
	 * See {@link ZombieLinkRuntimeException#ZombieLinkRuntimeException(String)}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public ConfigurationFailedException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link ZombieLinkRuntimeException#ZombieLinkRuntimeException(Throwable)}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public ConfigurationFailedException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link ZombieLinkRuntimeException#ZombieLinkRuntimeException(String, Throwable)}.
	 * <br><br>
	 * @since 1.3.0
	 */
	public ConfigurationFailedException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
