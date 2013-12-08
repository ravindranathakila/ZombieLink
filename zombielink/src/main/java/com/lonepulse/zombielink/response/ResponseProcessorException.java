package com.lonepulse.zombielink.response;

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

import java.util.Arrays;

import org.apache.http.HttpResponse;

import com.lonepulse.zombielink.ZombieLinkRuntimeException;
import com.lonepulse.zombielink.inject.InvocationContext;

/**
 * <p>This runtime exception is thrown when an {@link AbstractResponseProcessor} fails to execute 
 * successfully for a given {@link InvocationContext} and {@link HttpResponse}.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class ResponseProcessorException extends ZombieLinkRuntimeException {

	
	private static final long serialVersionUID = -7772538141198806201L;
	

	/**
	 * <p>Displays a detailed description with information on the failed processor and content.</p>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} for the failed {@link AbstractResponseProcessor}
	 * <br><br>
	 * @param responseProcessorClass
	 * 			the {@link Class} of the {@link AbstractResponseProcessor} implementation which failed
	 * <br><br>
	 * @since 1.2.4
	 */
	public ResponseProcessorException(Class<?> responseProcessorClass, 
									  InvocationContext context) {
	
		this(new StringBuilder(responseProcessorClass.getName())
			 .append(" failed to process the response for the request [")
			 .append(context.getRequest().getName())
			 .append("] with arguments ")
			 .append(Arrays.toString(context.getArguments().toArray())).toString());
	}
	
	/**
	 * <p>Displays a detailed description with information on the failed processor and content, while 
	 * preserving the stacktrace.</p>
	 * 
	 * @param context
	 * 			the {@link InvocationContext} for the failed {@link AbstractResponseProcessor}
	 * <br><br>
	 * @param responseProcessorClass
	 * 			the {@link Class} of the {@link AbstractResponseProcessor} implementation which failed
	 * <br><br>
	 * @param rootCause
	 * 			the parent exception which caused the {@link AbstractResponseProcessor} to fail
	 * <br><br>
	 * @since 1.2.4
	 */
	public ResponseProcessorException(Class<?> responseProcessorClass, 
									  InvocationContext config, 
									  Throwable rootCause) {
		
		this(new StringBuilder(responseProcessorClass.getName())
			 .append(" failed to process the response for the request [")
			 .append(config.getRequest().getName())
			 .append("] with arguments ")
			 .append(Arrays.toString(config.getArguments().toArray())).toString(), rootCause);
	}
	
	/**
	 * See {@link ZombieLinkRuntimeException#ZombieLinkRuntimeException()}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public ResponseProcessorException() {}

	/**
	 * See {@link ZombieLinkRuntimeException#ZombieLinkRuntimeException(String)}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public ResponseProcessorException(String detailMessage) {
		
		super(detailMessage);
	}

	/**
	 * See {@link ZombieLinkRuntimeException#ZombieLinkRuntimeException(Throwable)}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public ResponseProcessorException(Throwable throwable) {
		
		super(throwable);
	}

	/**
	 * See {@link ZombieLinkRuntimeException#ZombieLinkRuntimeException(String, Throwable)}.
	 * <br><br>
	 * @since 1.2.4
	 */
	public ResponseProcessorException(String detailMessage, Throwable throwable) {

		super(detailMessage, throwable);
	}
}
