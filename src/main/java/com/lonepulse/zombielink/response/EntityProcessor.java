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

import static com.lonepulse.zombielink.util.Is.async;
import static com.lonepulse.zombielink.util.Is.detached;
import static com.lonepulse.zombielink.util.Is.status;
import static com.lonepulse.zombielink.util.Is.successful;

import java.lang.reflect.Method;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.lonepulse.zombielink.annotation.Deserializer;
import com.lonepulse.zombielink.annotation.Entity.ContentType;
import com.lonepulse.zombielink.annotation.Header;
import com.lonepulse.zombielink.inject.InvocationContext;

/**
 * <p>This is a concrete implementation of {@link AbstractResponseProcessor} which retrieves the {@link HttpEntity} 
 * of an {@link HttpResponse} and parses it using the defined {@link ContentType}. {@link ContentType}s are defined 
 * using @{@link Deserializer} either at the endpoint level or at the request level. All endpoint request declarations 
 * which defined a return type should be associated with a deserializer. Custom deserializers may be used by extending 
 * {@link AbstractDeserializer} and defining its type on {@link Deserializer#type()}.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
class EntityProcessor extends AbstractResponseProcessor {

	
	/**
	 * <p>Accepts the {@link InvocationContext} along with the {@link HttpResponse} plus the results-map 
	 * and retrieves the {@link HttpEntity} form the response. This is then fed all HTTP response headers which 
	 * are discovered in the {@link HttpResponse}. These are then injected into their matching {@link StringBuilder} 
	 * which are identified by @{@link Header} on the endpoint request definition. The HTTP response headers and the 
	 * in-out parameters are matched using the header name and all parameters with a runtime value of {@code null} 
	 * will be ignored.</p> 
	 * 
	 * @param response
	 * 			the instance of {@link HttpResponse} whose headers are to be retrieves and injected in the in-out 
	 * 			{@link StringBuilder} parameters found on the request definition
	 * <br><br>
	 * @param context
	 * 			an immutable instance of {@link InvocationContext} which is used to discover any 
	 * 			@{@link Header} metadata in its <i>request</i> and <i>args</i> 
	 * <br><br>
	 * @return the deserialized response entity which conforms to the expected type
	 * <br><br> 
	 * @throws ResponseProcessorException
	 * 			if the response-header retrieval or injection failed due to an unrecoverable error
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	protected Object process(HttpResponse response, InvocationContext context, Object content) {

		if(response.getEntity() == null) {
			
			return content;
		}
		
		HttpEntity entity = response.getEntity();
		
		Method request = context.getRequest();
		Class<?> responseType = request.getReturnType();
		
		try {
			
			if(successful(response) && !status(response, 204, 205)) { //omit successful status codes without response content 
				
				if(HttpResponse.class.isAssignableFrom(responseType)) {
					
					return response;
				}
				
				if(HttpEntity.class.isAssignableFrom(responseType)) {
					
					return response.getEntity();
				}
			
				boolean responseExpected = !(responseType.equals(void.class) || responseType.equals(Void.class));
				boolean handleAsync = async(context);
				
				if(handleAsync || responseExpected) {
					
					Class<?> endpoint = context.getEndpoint();
					AbstractDeserializer<?> deserializer = null;
			
					Deserializer metadata = (metadata = 
						request.getAnnotation(Deserializer.class)) == null? 
							endpoint.getAnnotation(Deserializer.class) :metadata;
					
					if(metadata != null & !detached(context, Deserializer.class)) {
						
						deserializer = (metadata.value() == ContentType.UNDEFINED)? 
							Deserializers.resolve(metadata.type()) :Deserializers.resolve(metadata.value()); 
					}
					else if(handleAsync || CharSequence.class.isAssignableFrom(responseType)) {
						
						deserializer = Deserializers.resolve(ContentType.PLAIN);     
					}
					else {
						
						throw new DeserializerUndefinedException(endpoint, request);
					}
					
					return deserializer.run(response, context);
				}
			}
		}
		catch(Exception e) {
			
			throw (e instanceof ResponseProcessorException)? 
					(ResponseProcessorException)e :new ResponseProcessorException(getClass(), context, e);
		}
		finally {
			
			if(!(HttpResponse.class.isAssignableFrom(responseType) ||
				 HttpEntity.class.isAssignableFrom(responseType))) {
			
				EntityUtils.consumeQuietly(entity);
			}
		}
		
		return content;
	}
}
