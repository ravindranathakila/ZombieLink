package com.lonepulse.zombielink.executor;

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


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

import com.lonepulse.zombielink.annotation.Stateful;
import com.lonepulse.zombielink.inject.InvocationContext;

/**
 * <p>This is an implementation of {@link RequestExecutor} which isolates responsibilities that are common 
 * to all concrete {@link RequestExecutor}s.</p> 
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.1.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class BasicRequestExecutor implements RequestExecutor {
	
	
	private final ExecutionHandler executionHandler;
	
	
	/**
	 * <p>Creates a new instance of {@link BasicRequestExecutor} using the given {@link ExecutionHandler}.</p>
	 *
	 * @param executionHandler
	 * 			the instance of {@link ExecutionHandler} which will be invoked during request execution
	 * <br><br>
	 * @since 1.2.4
	 */
	BasicRequestExecutor(ExecutionHandler executionHandler) {
		
		this.executionHandler = executionHandler;
	}
	
	/**
	 * <p>Performs the actual request execution with the {@link HttpClient} to be used for the endpoint 
	 * (fetched using the {@link HttpClientDirectory}). See {@link HttpClient#execute(HttpUriRequest)}</p>
	 * 
	 * <p>If the endpoint is annotated with @{@link Stateful}, the relevant {@link HttpContext} from the 
	 * {@link HttpContextDirectory} is used. See {@link HttpClient#execute(HttpUriRequest, HttpContext)}</p>
	 *
	 * @param request
	 * 			the {@link HttpRequestBase} to be executed using the endpoint's {@link HttpClient}
	 * <br><br>
	 * @param context
	 * 			the {@link InvocationContext} used to discover information about the proxy invocation
	 * <br><br>
	 * @return the {@link HttpResponse} which resulted from the execution
	 * <br><br>
	 * @since 1.2.4
	 */
	protected HttpResponse fetchResponse(HttpRequestBase request, InvocationContext context) {

		try {
		
			Class<?> endpoint = context.getEndpoint();
			
			HttpClient httpClient = HttpClientDirectory.INSTANCE.lookup(endpoint);
			
			return endpoint.isAnnotationPresent(Stateful.class)? 
					httpClient.execute(request, HttpContextDirectory.INSTANCE.lookup(endpoint)) 
					:httpClient.execute(request);
		}
		catch(Exception e) {
			
			throw new RequestExecutionException(context.getRequest(), context.getEndpoint(), e);
		}
	}
	
	/**
	 * <p>Determines whether the {@link HttpResponse} signifies a successful request execution or not.</p>
	 *
	 * @param response
	 * 			the {@link HttpResponse} whose success status is to be determined
	 * <br><br>
	 * @return {@code true} if the {@link HttpResponse} signifies a successful request execution 
	 * <br><br>
	 * @since 1.2.4
	 */
	protected boolean isSuccessful(HttpResponse response) {
		
		int status = response.getStatusLine().getStatusCode();
		return status > 199 && status < 300;
	}
	
	
	/**
	 * <p>Executes an {@link HttpRequestBase} using the endpoint's {@link HttpClient} and handles the 
	 * resulting {@link HttpResponse} using this executor's {@link ExecutionHandler}.</p>
	 * 
	 * <p>See {@link #fetchResponse(HttpRequestBase, InvocationContext)}</p>
	 * 
	 * <p>See {@link #isSuccessful(HttpResponse)}</p>
	 * 
	 * @param request
	 * 			the {@link HttpRequestBase} to be executed using the endpoint's {@link HttpClient}
	 * <br><br>
	 * @param context
	 * 			the {@link InvocationContext} used to discover information about the proxy invocation
	 * <br><br>
	 * @throws RequestExecutionException
	 * 			if the HTTP request execution failed
	 * <br><br>
	 * @since 1.1.0
	 */
	@Override
	public HttpResponse execute(HttpRequestBase request, InvocationContext context) 
	throws RequestExecutionException {
		
		HttpResponse response = null;
		
		try {
			
			response = fetchResponse(request, context);
			
			if(isSuccessful(response)) {
				
				executionHandler.onSuccess(response, context);
			}
			else {
				
				executionHandler.onFailure(response, context);
			}
			
			return response;
		}
		catch (Exception error) {
			
			try {
				
				executionHandler.onError(context, error);
			}
			catch(Exception e) {
				
				throw RequestExecutionException.wrap(context.getRequest(), context.getEndpoint(), e);
			}
			
			throw RequestExecutionException.wrap(context.getRequest(), context.getEndpoint(), error);
		}
	}
}
