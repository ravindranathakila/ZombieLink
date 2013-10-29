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


import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.entity.StringEntity;

import com.lonepulse.zombielink.annotation.Entity;
import com.lonepulse.zombielink.annotation.FormParam;
import com.lonepulse.zombielink.annotation.Header;
import com.lonepulse.zombielink.annotation.HeaderSet;
import com.lonepulse.zombielink.annotation.PathParam;
import com.lonepulse.zombielink.annotation.QueryParam;
import com.lonepulse.zombielink.annotation.Request;
import com.lonepulse.zombielink.annotation.Request.RequestMethod;
import com.lonepulse.zombielink.inject.InvocationContext;

/**
 * <p>This utility class offers some common operations which are used in building requests - most commonly 
 * using the information contained within a {@link InvocationContext}.
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @category utility
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 * 
 * TODO revise utilities isolate a common algorithm for annotated params extraction 
 */
final class RequestUtils {
	
	
	/**
	 * <p>Constructor visibility restricted. Instantiation is nonsensical. 
	 */
	private RequestUtils() {}
	
	
	/**
	 * <p>Finds all <b><i>constant</i> request parameters</b> in the given {@link InvocationContext}.</p> 
	 * <p>Constant request parameters are introduced with @{@link Request.Param} at <b>request level</b> using 
	 * the @{@link Request} annotation.</p>
	 *
	 * @param config
	 * 			the {@link InvocationContext} from which all {@link Request.Param} annotations applied 
	 * 			on the endpoint method will be extracted
	 * 
	 * @return an <b>unmodifiable</b> {@link List} which aggregates all the @{@link Request.Param} annotations 
	 * `	   found on the {@link Request} annotation 
	 * 
	 * @throws IllegalArgumentException
	 * 			if the supplied {@link InvocationContext} was {@code null}
	 * 
	 * @since 1.2.4
	 */
	 static List<Request.Param> findConstantRequestParams(InvocationContext config) {
		
		if(config == null) {
			
			new IllegalArgumentException("The supplied Proxy Invocation Configuration cannot be <null>.");
		}
		
		Method request = config.getRequest();
		Request.Param[] requestParams = request.getAnnotation(Request.class).params();
		
		return Collections.unmodifiableList(Arrays.asList(requestParams));
	}
	
	/**
	 * <p>Finds all <b>query parameters</b> in the given {@link InvocationContext}.</p> 
	 * 
	 * <p>Query parameters are introduced with @{@link QueryParam} on the arguments to a request method.</p>
	 *
	 * @param config
	 * 			the {@link InvocationContext} from which all @{@link QueryParam} annotations applied 
	 * 			on the endpoint method arguments will be extracted
	 * 
	 * @return an <b>unmodifiable</b> {@link Map} in the form {@code Map<name, value>} which aggregates all the 
	 * 		   param names coupled with the value of the linked method argument annotated with @{@link QueryParam}
	 * 
	 * @throws IllegalArgumentException
	 * 			if the supplied {@link InvocationContext} was {@code null} 
	 * 
	 * @since 1.2.4
	 */
	 static Map<String, Object> findQueryParams(InvocationContext config) {
		
		if(config == null) {
			
			new IllegalArgumentException("The supplied Proxy Invocation Configuration cannot be <null>.");
		}
		
		Map<String, Object> paramMap = new LinkedHashMap<String, Object>(); 
		
		Method request = config.getRequest();
		List<Object> paramValues = config.getArguments();
		
		Annotation[][] annotationsForAllParams = request.getParameterAnnotations();
		
		for (int i = 0; i < annotationsForAllParams.length; i++) {
			
			for (Annotation annotation : annotationsForAllParams[i]) {
				
				if(QueryParam.class.isAssignableFrom(annotation.getClass())) {
					
					paramMap.put(((QueryParam)annotation).value(), paramValues.get(i));
					break; //only one @QueryParam annotation is expected per endpoint method argument
				}
			}
		}
		
		return Collections.unmodifiableMap(paramMap);
	}
	
	/**
	 * <p>Finds all <a href="http://en.wikipedia.org/wiki/POST_(HTTP)#Use_for_submitting_web_forms">
	 * form-urlencoded</a> parameters in the given {@link InvocationContext}.</p> 
	 *
	 * <p>Form parameters are introduced with @{@link FormParam} on the arguments to a request method.</p>
	 *
	 * @param config
	 * 			the {@link InvocationContext} from which all @{@link FormParam} annotations applied 
	 * 			on the endpoint method arguments will be extracted
	 * 
	 * @return an <b>unmodifiable</b> {@link Map} in the form {@code Map<name, value>} which aggregates all the 
	 * 		   param names coupled with the value of the linked method argument
	 * 
	 * @throws IllegalArgumentException
	 * 			if the supplied {@link InvocationContext} was {@code null} 
	 * 
	 * @since 1.2.4
	 */
	 static Map<String, Object> findFormParams(InvocationContext config) {
		
		if(config == null) {
			
			new IllegalArgumentException("The supplied Proxy Invocation Configuration cannot be <null>.");
		}
		
		Map<String, Object> paramMap = new LinkedHashMap<String, Object>(); 
		
		Method request = config.getRequest();
		List<Object> paramValues = config.getArguments();
		
		Annotation[][] annotationsForAllParams = request.getParameterAnnotations();
		
		for (int i = 0; i < annotationsForAllParams.length; i++) {
			
			for (Annotation annotation : annotationsForAllParams[i]) {
				
				if(FormParam.class.isAssignableFrom(annotation.getClass())) {
					
					paramMap.put(((FormParam)annotation).value(), paramValues.get(i));
					break; //only one @FormParam annotation is expected per endpoint method argument
				}
			}
		}
		
		return Collections.unmodifiableMap(paramMap);
	}
	
	/**
	 * <p>Discovers which concrete implementation of {@link HttpEntity} is suitable for wrapping the given object. 
	 * This discovery proceeds in the following order by checking the runtime-type of the generic object:</p> 
	 *
	 * <ol>
	 * 	<li>org.apache.http.{@link HttpEntity} --&gt; returned as-is.</li> 
	 * 	<li>{@code byte[]}, {@link Byte}[] --&gt; {@link ByteArrayEntity}</li> 
	 *  <li>java.io.{@link File} --&gt; {@link FileEntity}</li>
	 * 	<li>java.io.{@link InputStream} --&gt; {@link BufferedHttpEntity}</li>
	 * 	<li>{@link CharSequence} --&gt; {@link StringEntity}</li>
	 * 	<li>java.io.{@link Serializable} --&gt; {@link SerializableEntity} (with an internal buffer)</li>
	 * </ol>
	 *
	 * @param genericEntity
	 * 			a generic reference to an object whose concrete {@link HttpEntity} is to be resolved 
	 * 
	 * @return the resolved concrete {@link HttpEntity} implementation
	 * 
	 * @throws IllegalArgumentException
	 * 			if the supplied generic entity was null
	 * 
	 * @throws EntityResolutionFailedException
	 * 			if a specific {@link HttpEntity} implementation failed to be resolved
	 * 
	 * @since 1.2.4
	 */
	 static HttpEntity resolveHttpEntity(Object genericEntity) {
		
		if(genericEntity == null) {
			
			new IllegalArgumentException("The supplied generic entity cannot be <null>.");
		}
		
		try {
		
			if(genericEntity instanceof HttpEntity) {
				
				return (HttpEntity)genericEntity;
			}
			else if(byte[].class.isAssignableFrom(genericEntity.getClass())) {
				
				return new ByteArrayEntity(((byte[])genericEntity));
			}
			else if(Byte[].class.isAssignableFrom(genericEntity.getClass())) {
				
				Byte[] wrapperBytes = (Byte[])genericEntity;
				byte[] primitiveBytes = new byte[wrapperBytes.length];
				
				for (int i = 0; i < wrapperBytes.length; i++) {
					
					primitiveBytes[i] = wrapperBytes[i].byteValue();
				}
				
				return new ByteArrayEntity(primitiveBytes);
			}
			else if(genericEntity instanceof File) {
				
				return new FileEntity((File)genericEntity);
			}
			else if(genericEntity instanceof InputStream) {
				
				BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
				basicHttpEntity.setContent((InputStream)genericEntity);
				
				return new BufferedHttpEntity(basicHttpEntity);
			}
			else if(genericEntity instanceof CharSequence) {
				
				return new StringEntity(((CharSequence)genericEntity).toString());
			}
			else if(genericEntity instanceof Serializable) {
				
				return new SerializableEntity((Serializable)genericEntity, true);
			}
			else {
				
				throw new EntityResolutionFailedException(genericEntity);
			}
		}
		catch(Exception e) {

			throw (e instanceof EntityResolutionFailedException)?
					(EntityResolutionFailedException)e :new EntityResolutionFailedException(genericEntity, e);
		}
	}
	
	/**
	 * <p>Finds the single <b>entity parameter</b> in the given {@link InvocationContext} which 
	 * is identified by placing an @{@link Entity} annotation on an endpoint method argument. If found, the 
	 * corresponding {@link HttpEntity} is resolved via {@link #resolveHttpEntity(Object)} and returned.</p> 
	 * 
	 * <p>Only one such entity is expected to be found, if multiple @{@link Entity} annotations are discovered 
	 * a {@link MultipleEntityException} is thrown. If no @{@link Entity} annotation is discovered a
	 * {@link MissingEntityException} is thrown. These might be caught and recovered from if preferred.</p>
	 *
	 * @param config
	 * 			the {@link InvocationContext} whose single entity is to be discovered and resolved
	 * 
	 * @return the resolved {@link HttpEntity} which was discovered as an argument annotated with @{@link Entity}
	 * 
	 * @throws IllegalArgumentException
	 * 			if the {@link InvocationContext} was {@code null}
	 * 
	 * @throws MultipleEntityException
	 * 			if many arguments were found to be annotated with @{@link Entity} 
	 * 
	 * @throws MissingEntityException
	 * 			if no arguments were found to be annotated with @{@link Entity}
	 * 
	 * @throws EntityResolutionFailedException
	 * 			if a specific {@link HttpEntity} implementation failed to be resolved
	 * 
	 * @since 1.2.4
	 */
	static HttpEntity findAndResolveEntity(InvocationContext config) {
		
		List<Object> paramValues = config.getArguments();
		Annotation[][] annotationsForAllParams = config.getRequest().getParameterAnnotations();
		
		List<Object> entities = new ArrayList<Object>();
		
		for (int i = 0; i < annotationsForAllParams.length; i++) {
			
			for (Annotation annotation : annotationsForAllParams[i]) {

				if(Entity.class.isAssignableFrom(annotation.getClass())) {
					
					entities.add(paramValues.get(i));
					break; //only one @Entity annotation is expected per endpoint method argument
				}
			}
		}
		
		if(entities.isEmpty()) {
			
			throw new MissingEntityException(config);
		}
		
		if(entities.size() > 1) {
			
			throw new MultipleEntityException(config);
		}
		
		return RequestUtils.resolveHttpEntity(entities.get(0));
	}
	
	/**
	 * <p>Finds all <b>path parameters</b> in the given {@link InvocationContext}. Path parameters 
	 * are introduced with @{@link PathParam} on the arguments to a request method.</p>
	 * 
	 * @param config
	 * 			the {@link InvocationContext} from which all @{@link PathParam} annotations applied 
	 * 			on the endpoint method arguments will be extracted
	 * 
	 * @return an <b>unmodifiable</b> {@link Map} in the form {@code Map<name, value>} which aggregates all the 
	 * 		   param names coupled with the value of the linked method argument annotated with @{@link PathParam}
	 * 
	 * @throws IllegalArgumentException
	 * 			if the supplied {@link InvocationContext} was {@code null}
	 * 
	 * @since 1.2.4
	 */
	static Map<String, Object> findPathParams(InvocationContext config) {
		
		if(config == null) {
			
			new IllegalArgumentException("The supplied Proxy Invocation Configuration cannot be <null>.");
		}
		
		Map<String, Object> paramMap = new LinkedHashMap<String, Object>(); 
		
		Method request = config.getRequest();
		List<Object> paramValues = config.getArguments();
		
		Annotation[][] annotationsForAllParams = request.getParameterAnnotations();
		
		for (int i = 0; i < annotationsForAllParams.length; i++) {
			
			for (Annotation annotation : annotationsForAllParams[i]) {
				
				if(PathParam.class.isAssignableFrom(annotation.getClass())) {
					
					paramMap.put(((PathParam)annotation).value(), paramValues.get(i));
					break; //only one @PathParam annotation is expected per endpoint method argument
				}
			}
		}
		
		return Collections.unmodifiableMap(paramMap);
	}
	
	/**
	 * <p>Finds all <b>static and dynamic headers</b> in the given {@link InvocationContext} and 
	 * returns an unmodifiable {@link List} of {@link Map.Entry} instances with the header <i>name</i> as the 
	 * key and the runtime argument as the <i>value</i>. <b>Note</b> that this implementation of 
	 * {@link Map.Entry#setValue(Object)} throws an {@link UnsupportedOperationException}. This list may contain 
	 * multiple entries with the <i>same name</i> as these headers are meant to be <b>added</b> ant not overwritten 
	 * for a request.</p>
	 * 
	 * <p><i>Static-headers</i> are specified at request-level with @{@link HeaderSet} and <i>dynamic-headers</i> 
	 * are specified at argument-level with @{@link Header}.</p>
	 * 
	 * <br><br>
	 * @param config
	 * 			the {@link InvocationContext} from which all static and dynamic headers will be discovered
	 * <br><br>
	 * @return an <b>unmodifiable</b> {@link List} of {@link Map.Entry} instances with the header <i>name</i> as the 
	 * 		   key and the runtime argument as the <i>value</i>; <b>note</b> that this implementation of 
	 * 		   {@link Map.Entry#setValue(Object)} throws an {@link UnsupportedOperationException} 
	 * <br><br>
	 * @throws IllegalArgumentException
	 * 			if the supplied {@link InvocationContext} was {@code null}
	 * <br><br>
	 * @since 1.2.4
	 */
	static List<Map.Entry<String, Object>> findHeaders(InvocationContext config) {
		
		if(config == null) {
			
			new IllegalArgumentException("The supplied Proxy Invocation Configuration cannot be <null>.");
		}
		
		Method request = config.getRequest();
		HeaderSet headerSet = request.getAnnotation(HeaderSet.class);
		
		List<Map.Entry<String, Object>> headers = new ArrayList<Map.Entry<String, Object>>();
		
		if(headerSet != null && headerSet.value() != null && headerSet.value().length > 0) {
		
			List<HeaderSet.Header> staticHeaders = Arrays.asList(headerSet.value());
			
			for (final HeaderSet.Header staticHeader : staticHeaders) {
				
				headers.add(new Map.Entry<String, Object>() {

					@Override
					public String getKey() {
						return staticHeader.name();
					}

					@Override
					public Object getValue() {
						return staticHeader.value();
					}

					@Override
					public Object setValue(Object value) {
						throw new UnsupportedOperationException();
					}
				});
			}
		}
		
		List<Object> paramValues = config.getArguments();
		
		Annotation[][] annotationsForAllParams = request.getParameterAnnotations();
		
		for (int i = 0; i < annotationsForAllParams.length; i++) {
			
			for (Annotation annotation : annotationsForAllParams[i]) {
				
				if(Header.class.isAssignableFrom(annotation.getClass())) {
					
					final Header header = (Header)annotation;
					final Object paramValue = paramValues.get(i);
					
					headers.add(new Map.Entry<String, Object>() {

						@Override
						public String getKey() {
							return header.value();
						}

						@Override
						public Object getValue() {
							return paramValue;
						}

						@Override
						public Object setValue(Object value) {
							throw new UnsupportedOperationException();
						}
					});
					
					break; //only one @Header annotation is expected per endpoint method argument
				}
			}
		}
		
		return Collections.unmodifiableList(headers);
	}
	
	/**
	 * <p>Retrieves the proper extension of {@link HttpRequestBase} for the given {@link InvocationContext}. This 
	 * implementation is solely dependent upon the {@link RequestMethod} property in the annotated metdata of the 
	 * endpoint method definition.</p>
	 *
	 * @param context
	 * 			the {@link InvocationContext} for which a {@link HttpRequestBase} is to be generated 
	 * 
	 * @return the {@link HttpRequestBase} translated from the {@link InvocationContext}'s {@link RequestMethod} 
	 * <br><br>
	 * @since 1.2.4
	 */
	static final HttpRequestBase translateRequestMethod(InvocationContext context) {
		
		RequestMethod requestMethod = context.getRequest().getAnnotation(Request.class).method();
		
		switch (requestMethod) {
		
			case POST: return new HttpPost();
			case PUT: return new HttpPut();
			case DELETE: return new HttpDelete();
			case HEAD: return new HttpHead();
			case TRACE: return new HttpTrace();
			case OPTIONS: return new HttpOptions();
			
			case GET: default: return new HttpGet();
		}
	}
}
