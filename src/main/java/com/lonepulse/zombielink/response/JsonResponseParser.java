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

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.lonepulse.zombielink.inject.InvocationContext;

/**
 * <p>This is an extension of {@link AbstractResponseParser} which parses b>JSON response content</b> 
 * to an instance of the model specified on the endpoint definition.</p>
 * 
 * <p><b>Note</b> that this parser requires the <a href="http://code.google.com/p/google-gson">GSON</a> 
 * library to be available on the classpath to be active. If GSON is not detected, this parser will 
 * be disabled and any attempt to use it will result in an {@link IllegalStateException}.</p>
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.1.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
final class JsonResponseParser extends AbstractResponseParser<Object> {
	
	
	private static final Logger LOGGER = Logger.getLogger(JsonResponseParser.class.getName());
	
	private static final String ERROR_CONTEXT_UNAVAILABLE = new StringBuilder()
	.append("\n\nGSON (gson-2.2.4.jar) was not detected on the classpath. ")
	.append("To enable JSON response parsing with @Parser(ParserType.JSON) ")
	.append("add the following dependency to your build configuration.\n\n")
	.append("Maven:\n")
	.append("<dependency>\n")
	.append("  <groupId>com.google.code.gson</groupId>\n")
	.append("  <artifactId>gson</artifactId>\n")
	.append("  <version>2.2.4</version>\n")
	.append("</dependency>\n\n")
	.append("Scala SBT:\n")
	.append("libraryDependencies += \"com.google.code.gson\" % \"gson\" % \"2.2.4\"\n\n")
	.append("Gradle:\n")
	.append("compile 'com.google.code.gson:gson:2.2.4'\n\n")
	.append("...or grab the JAR from ")
	.append("http://code.google.com/p/google-gson/downloads/list \n\n").toString();
	
	private static final String ERROR_CONTEXT_INCOMPATIBLE = new StringBuilder()
	.append("\n\nFailed to initialize JsonResponseParser; use of @Parser(ParserType.JSON) is disabled.\n")
	.append("Please make sure that you are using version 2.2.4 of GSON.\n\n").toString();
	
	
	private static Class<?> Gson;
	private static Class<?> TypeToken;
	
	private static Method Gson_fromJson;
	private static Method TypeToken_GET;
	private static Method TypeToken_getType;
	
	private static Object gson; //thread-safe, as proven by http://goo.gl/RUyPdn
	
	private static boolean unavailable;
	private static boolean incompatible;
	
	static {
		
		try {
			
			Gson = Class.forName("com.google.gson.Gson");
			Gson_fromJson = Gson.getDeclaredMethod("fromJson", String.class, Type.class);
			
			TypeToken = Class.forName("com.google.gson.reflect.TypeToken");
			TypeToken_GET = TypeToken.getDeclaredMethod("get", Class.class);
			TypeToken_getType = TypeToken.getDeclaredMethod("getType");
			
			gson = Gson.newInstance();
		}
		catch (ClassNotFoundException cnfe) { 
			
			unavailable = true;
			LOGGER.log(Level.WARNING, ERROR_CONTEXT_UNAVAILABLE);
		}
		catch(Exception e) {
			
			incompatible = true;
			LOGGER.log(Level.WARNING, ERROR_CONTEXT_INCOMPATIBLE);
		}
	}
	
	
	/**
	 * <p>Creates a new instance of {@link JsonResponseParser} and register the generic type {@link Object} 
	 * as the entity which results from its <i>parse</i> operation.</p>
	 *
	 * @since 1.1.0
	 */
	public JsonResponseParser() {
		
		super(Object.class);
	}
	
	/**
     * <p>Parses the JSON String in the {@link HttpResponse} using <b>GSON</b> and returns the entity modeled 
     * by the JSON data.</p>
     * 
     * <p>See {@link AbstractResponseParser#processResponse(HttpResponse, InvocationContext)}.
     * 
	 * @param httpResponse
	 * 				the {@link HttpResponse} which contains the JSON content to be parsed to a model
	 * <br><br>
	 * @param context
	 * 				the {@link InvocationContext} which is used to discover further information regarding 
	 * 				the proxy invocation
     * <br><br>
	 * @return the model which was parsed from the JSON response content
	 * <br><br>
	 * @throws IllegalStateException 
	 * 				if the <b>GSON library</b> was not found on the classpath or if an incompatible version 
	 * 				of the library is being used
	 * <br><br>
	 * @throws Exception 
	 * 				if the JSON content failed to be parsed to the specified model
	 * <br><br>
	 * @since 1.1.0
	 */
	@Override
	protected Object processResponse(HttpResponse httpResponse, InvocationContext context) throws Exception {
		
		if(unavailable || incompatible) {
			
			throw new IllegalStateException(unavailable? ERROR_CONTEXT_UNAVAILABLE :ERROR_CONTEXT_INCOMPATIBLE);
		}
		
		String jsonString = EntityUtils.toString(httpResponse.getEntity());
		
		Object typeToken = TypeToken_GET.invoke(null, context.getRequest().getReturnType());
		
		return Gson_fromJson.invoke(gson, jsonString, TypeToken_getType.invoke(typeToken));
	}
}
