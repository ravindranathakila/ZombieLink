package com.lonepulse.zombielink.request;

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


import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.zombielink.annotation.Request.RequestMethod;
import com.lonepulse.zombielink.inject.InvocationContext;
import com.lonepulse.zombielink.processor.Processor;

/**
 * <p>This is an abstract implementation of {@link Processor} which specifies a template for processing the 
 * <i>data</i> and <i>metadata</i> on a proxy endpoint <b>request invocation</b>. It includes an implementation 
 * of {@link Processor#run(Object...)} that checks the preconditions for executing 
 * {@link #process(HttpRequestBase, InvocationContext)}.</p>
 * 
 * <p>All implementations must be aware of the {@link InvocationContext} which can be used to discover 
 * information about the endpoint and the request declaration. This information can be queried based on the 
 * <i>targeting criteria</i> for this request processor and the resulting information should be used to <i>build 
 * upon</i> the given {@link HttpRequest}.</p>
 * 
 * <p>It is advised to adhere to <a href="www.w3.org/Protocols/rfc2616/rfc2616.html‎">RFC 2616</a> of <b>HTTP 1.1</b> 
 * when designing an implementation.</p>
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
abstract class AbstractRequestProcessor implements Processor<HttpRequestBase, RequestProcessorException> {

	
	/**
	 * <p>Accepts an {@link HttpRequestBase} and a {@link InvocationContext}, validates all preconditions 
	 * and uses the metadata contained within the configuration to process and subsequently build upon the request. 
	 * Any implementations that wish to check additional preconditions or those that wish to alter this basic approach 
	 * should override this method.</p>
	 * 
	 * <p><b>Note</b> that this method is expected to return the {@link HttpRequestBase} which was processed using 
	 * the implementation of {@link #process(HttpRequestBase, InvocationContext)}.</p> 
	 * 
	 * <p>Delegates to {@link #process(HttpRequestBase, InvocationContext)}.</p>
	 * 
	 * <p>See {@link Processor#run(Object...)}.</p>
	 *
	 * @param args
	 * 			a array of <b>length 2</b> with an {@link HttpRequestBase} and a {@link InvocationContext} 
	 * 			in that <b>exact order</b> 
	 * <br><br>
	 * @return the {@link HttpRequestBase} processed using {@link #process(HttpRequestBase, InvocationContext)}
	 * <br><br>
	 * @throws IllegalArgumentException
	 * 			if the supplied arguments array is {@code null} or if the number of arguments does not equal 2, 
	 * 			or if the arguments are not of the expected type 
	 * <br><br>
	 * @throws RequestProcessorException
	 * 			if {@link #process(HttpRequestBase, InvocationContext)} failed for the given {@link HttpRequestBase} 
	 * 			and {@link InvocationContext}
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	public HttpRequestBase run(Object... args) throws RequestProcessorException {

		if(args == null || args.length != 2) {
			
			StringBuilder errorContext = new StringBuilder("An ")
			.append(AbstractRequestProcessor.class.getName())
			.append(" requires exactly two arguments: the ")
			.append(HttpRequestBase.class.getName())
			.append(" which it should process and the ")
			.append(InvocationContext.class.getName())
			.append(" which provides the data and metadata for processing. ");
			
			throw new IllegalArgumentException(errorContext.toString());
		}
		
		StringBuilder accumulatedContext = new StringBuilder();
		boolean hasIllegalArguments = false;
		
		if(args[0] == null || !(args[0] instanceof HttpRequestBase)) {
			
			accumulatedContext.append("The first argument should be an instance of ")
			.append(HttpRequestBase.class.getName())
			.append(" which cannot be <null>. ");
			
			hasIllegalArguments = true;
		}
		
		if(args[1] == null || !(args[1] instanceof InvocationContext)) {
			
			accumulatedContext.append("The second argument to should be an instance of ")
			.append(InvocationContext.class.getName())
			.append(" which cannot be <null>. ");
			
			hasIllegalArguments = true;
		}
		
		if(hasIllegalArguments) {
			
			throw new IllegalArgumentException(accumulatedContext.toString());
		}
		
		return process((HttpRequestBase)args[0], (InvocationContext)args[1]);
	}
	
	/**
	 * <p>Takes the {@link InvocationContext} for the given {@link HttpRequestBase} and uses the metadata 
	 * contained within the configuration to <i>build upon</i> the request.</p>
	 * 
	 * <p>The provided {@link HttpRequestBase} will be a concrete implementation which coincides with one of 
	 * the {@link RequestMethod}s, such as {@link HttpGet} or {@link HttpPut}. It would be sensible to check 
	 * the type of the request-method so that you treat each request in a way that complies with 
	 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html">Section 9</a> of the <b>HTTP 1.1</b> 
	 * RFC when designing an implementation.</p>
	 * 
	 * <p><b>Note</b> that all implementations should process the original instance of {@link HttpRequestBase} 
	 * without recreating or reusing a separate instance with similar properties.</p>
	 * 
	 * @param httpRequestBase
	 * 			a concrete implementation of {@link HttpRequestBase}, such as {@link HttpGet} which should 
	 * 			be used to grow on based on the targeting criteria for this request processor
	 * <br><br>
	 * @param config
	 * 			the {@link InvocationContext} which is used to discover the request's 
	 * 			{@link RequestMethod} and any annotated metadata along with the invocation arguments  
 	 * <br><br>
 	 * @return the <b>same instance</b> of {@link HttpRequestBase} which was passed in for processing 
 	 * <br><br>
	 * @throws RequestProcessorException
	 * 			if the processor finds an {@link HttpRequestBase} <i>which it should act upon</i> and yet 
	 * 			fails to perform the necessary processing 
	 * <br><br>
	 * @since 1.2.4
	 */
	protected abstract HttpRequestBase process(HttpRequestBase httpRequestBase, InvocationContext config)
	throws RequestProcessorException;
}
