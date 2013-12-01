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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.lonepulse.zombielink.inject.InvocationContext;

/**
 * <p>This is an extension of {@link AbstractDeserializer} which allows an <b>XML response content</b> 
 * to an instance of the model specified on the endpoint definition.</p>
 * 
 * <p><b>Note</b> that this deserializer requires the <a href="http://simple.sourceforge.net">Simple-XML</a> 
 * library to be available on the classpath to be active. If Simple-XML is not detected, this deserializer will 
 * be disabled and any attempt to use it will result in an {@link IllegalStateException}.</p>
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
final class XmlDeserializer extends AbstractDeserializer<Object> {

	
	private static final Logger LOGGER = Logger.getLogger(XmlDeserializer.class.getName());
	
	private static final String ERROR_CONTEXT_UNAVAILABLE = new StringBuilder()
	.append("\n\nSimple-XML (simple-xml-2.7.1.jar) was not detected on the classpath. ")
	.append("To enable XML deserialization with @Deserializer(ContentType.XML) ")
	.append("add the following dependency to your build configuration.\n\n")
	.append("Maven:\n")
	.append("<dependency>\n")
	.append("  <groupId>org.simpleframework</groupId>\n")
	.append("  <artifactId>simple-xml</artifactId>\n")
	.append("  <version>2.7.1</version>\n")
	.append("</dependency>\n\n")
	.append("Scala SBT:\n")
	.append("libraryDependencies += \"org.simpleframework\" % \"simple-xml\" % \"2.7.1\"\n\n")
	.append("Gradle:\n")
	.append("compile 'org.simpleframework:simple-xml:2.7.1'\n\n")
	.append("...or grab the JAR from ")
	.append("http://simple.sourceforge.net/download.php \n\n").toString();
	
	private static final String ERROR_CONTEXT_INCOMPATIBLE = new StringBuilder()
	.append("\n\nFailed to initialize XmlDeserializer; use of @Deserializer(ContentType.XML) is disabled.\n")
	.append("Please make sure that you are using version 2.7.1 of Simple-XML.\n\n").toString();
	

	private static Class<?> Persister; 
	private static Method Persister_read;
	
	private static Object persister; //thread-safe, as specified at http://goo.gl/WSXO5z
	
	private static boolean unavailable;
	private static boolean incompatible;
	
	static {
		
		try {
			
			Persister = Class.forName("org.simpleframework.xml.core.Persister");
			Persister_read = Persister.getDeclaredMethod("read", Class.class, String.class);
			
			persister = Persister.newInstance(); 
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
	 * <p>Creates a new instance of {@link XmlDeserializer} and register the generic type {@link Object} 
	 * as the entity which results from its <i>parse</i> operation.</p>
	 *
	 * @since 1.2.4
	 */
	public XmlDeserializer() {
		
		super(Object.class);
	}
	
	/**
     * <p>Parses the XML content returned by the {@link HttpResponse} entity using <b>Simple-XML</b> into 
     * the model specified on the endpoint definition.</p>
     * 
     * <p>See {@link AbstractDeserializer#deserialize(HttpResponse, InvocationContext)}.
     * 
	 * @param httpResponse
	 * 				the {@link HttpResponse} which contains the XML content to be deserialized to a model
	 * <br><br>
	 * @param context
	 * 				the {@link InvocationContext} which is used to discover further information regarding 
	 * 				the proxy invocation
     * <br><br>
	 * @return the model which was deserialized from the XML response content, else {@code null} if the 
	 * 		   given {@link HttpResponse} did not contain an {@link HttpEntity}
	 * <br><br>
	 * @throws IllegalStateException 
	 * 				if the <b>Simple-XML library</b> was not found on the classpath or if an incompatible 
	 * 				version of the library is being used
	 * <br><br>
	 * @throws DeserializerException
	 * 			if XML deserialization failed for the given entity using the Simple-XML library 
	 * <br><br>
	 * @since 1.2.4
	 */
	@Override
	protected Object deserialize(HttpResponse httpResponse, InvocationContext context) {
		
		if(unavailable || incompatible) {
			
			throw new IllegalStateException(unavailable? ERROR_CONTEXT_UNAVAILABLE :ERROR_CONTEXT_INCOMPATIBLE);
		}
		
		try {
			
			HttpEntity entity = httpResponse.getEntity();
			
			return entity == null? null :Persister_read.invoke(persister, 
					context.getRequest().getReturnType(), EntityUtils.toString(entity));
		} 
		catch(Exception e) {
			
			throw new DeserializerException(new StringBuilder("XML deserialization failed for request <")
			.append(context.getRequest().getName())
			.append("> on endpoint <")
			.append(context.getEndpoint().getName())
			.append(">").toString(), e);
		}
	}
}
