package com.lonepulse.zombielink.processor;

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

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import com.lonepulse.zombielink.annotation.Entity;
import com.lonepulse.zombielink.annotation.FormParam;
import com.lonepulse.zombielink.annotation.PathParam;
import com.lonepulse.zombielink.annotation.QueryParam;
import com.lonepulse.zombielink.proxy.InvocationContext;
import com.lonepulse.zombielink.proxy.InvocationException;
import com.lonepulse.zombielink.request.RequestProcessorChain;
import com.lonepulse.zombielink.response.ResponseProcessorChain;

/**
 * <p>This enum aggregates all <i>thread-safe</i> processor-chains and exposes the services common to each 
 * chain and may even offer services which encompass all the chains as a whole. Any swap between chains 
 * (e.g. a deprecated implementation for its enhancement) for the same target criteria occurs transparently.</p>
 * 
 * <p>All processor-chains are ensured to be independent of an internal (or an external object's) state and 
 * is thread-safe. If a state is incurred, proper {@link ThreadLocal} management will be performed.</p>
 *  
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public enum Processors {

	
	/**
	 * <p>This is a concrete implementation of {@link AbstractProcessorChain} which creates a sequentially 
	 * executed series of <b>request processors</b> responsible for building the {@link HttpRequest} for a 
	 * request invocation.</p>
	 * 
	 * <p>This chain consists of the <b>request processors</b> listed below in the given order:  
	 * 
	 * <ol>
	 * 	<li>{@link UriProcessor} - builds the complete URI from the root-path and the sub-path</li>
	 * 	<li>{@link HeaderProcessor} - populates all static and dynamic HTTP headers</li>
	 *  <li>{@link PathParamProcessor} - replaces path parameter placeholders in the URI with any @{@link PathParam}s</li>
	 *  <li>{@link QueryParamProcessor} - appends a query-string formulated for any @{@link QueryParam}s</li>
	 *  <li>{@link FormParamProcessor} - inserts a form-url-encoded query-string for any @{@link FormParam}s</li>
	 *  <li>{@link EntityProcessor} - inserts the {@link HttpEntity} identified using @{@link Entity}</li>
	 *  <li>{@link InterceptionProcessor} - runs hooks for custom request processing just before execution</li>
	 * </ol>
	 * 
	 * <p><b>Note</b> that this processor-chain requires a single {@link InvocationContext} to be fed into 
	 * {@link #run(Object...)}} and returns the {@link HttpRequestBase} which was processed by the chain.</p>
	 * 
	 * <p><b>Note</b> that a chain-wide failure is <b>NOT recoverable</b>. All failures are of type 
	 * <b>RequestProcessorException</b> and may be thrown from any arbitrary {@link ProcessorChainLink}. 
	 * Any changes made on the arguments to the chain are <b>NOT rolled back</b>.</p> 
	 * 
	 * @version 1.2.0
	 * <br><br>
	 * @since 1.3.0
	 */
	REQUEST(new RequestProcessorChain()),
	
	/**
	 * <p>This is a concrete implementation of {@link AbstractProcessorChain} which creates a sequentially 
	 * executed series of <b>response processors</b> responsible for handling the {@link HttpResponse} 
	 * which was returned for a request execution.</p>
	 * 
	 * <p>This chain consists of the <b>response processors</b> listed below in the given order:  
	 * 
	 * <ol>
	 * 	<li>{@link HeaderProcessor} - retrieves the response headers and makes them available</li>
	 * 	<li>{@link EntityProcessor} - parses and returns the content of the response body</li>
	 * </ol>
	 * 
	 * <p><b>Note</b> that this processor-chain <b>may or may not</b> return the deserialized response 
	 * entity depending on the availability of response content.</p>
	 * 
	 * <p><b>Note</b> that a chain-wide failure is <b>NOT recoverable</b>. All failures are of type 
	 * <b>ResponseProcessorException</b> and may be thrown from any arbitrary {@link ProcessorChainLink}. 
	 * Any changes made on the arguments to the chain are <b>NOT rolled back</b>.</p> 
	 * 
	 * @version 1.1.0
	 * <br><br>
	 * @since 1.3.0
	 */
	RESPONSE(new ResponseProcessorChain());
	
	
	
	private AbstractProcessorChain<?, ? extends Throwable> processorChain;
	
	
	private Processors(AbstractProcessorChain<?, ? extends Throwable> processorChain) {
	
		this.processorChain = processorChain;
	}

	/**
	 * <p>Accepts the arguments which will be used by the processor-chain and invokes the root link which 
	 * will in-turn invoke all successive links with the arguments and return the final result.</p>
	 * 
	 * <p>If a chain-wide failure for the current processor-chain is recoverable, ensure that you handle 
	 * the failure of the appropriate type and take any steps for recovery. For example, a processor chain 
	 * which acts on the input arguments and yet does not rollback the changes on failure could be mitigated 
	 * by reverting to a deep-clone of the original arguments.</p>
	 * 
	 * <p>See {@link AbstractProcessorChain#run(Object...)}.</p>
	 * 
	 * @param args
	 * 			the arguments to the processor-chain which will be used by the root link and all successive 
	 * 			links that follow; see {@link AbstractProcessorChain#onInitiate(ProcessorChainLink, Object...)} 
	 * <br><br>
	 * @return the final result produced by the tail of the chain which may or may not have been processed 
	 * 		   for terminal conditions by {@link AbstractProcessorChain#onTerminate(Object, Object...)}; 
	 * 		   ensure that you <i>cast</i> this to the type which is expected from the processor-chain 
	 * <br><br>
	 * @throws InvocationException
	 * 			if a chain-wide failure occurred in either the request or response processor chains 
	 * <br><br>
	 * @since 1.3.0
	 */
	public Object run(Object... args) {
		
		try {
		
			return this.processorChain.run(args);
		}
		catch(Exception e) {
			
			throw this.equals(REQUEST)? 
				InvocationException.newInstance((InvocationContext)args[0], e):
				InvocationException.newInstance((InvocationContext)args[0], (HttpResponse)args[1], e);
		}
	}
}
