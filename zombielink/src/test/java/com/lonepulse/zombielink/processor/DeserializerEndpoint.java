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

import static com.lonepulse.zombielink.annotation.Entity.ContentType.JSON;
import static com.lonepulse.zombielink.annotation.Entity.ContentType.PLAIN;
import static com.lonepulse.zombielink.annotation.Entity.ContentType.XML;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.lonepulse.zombielink.annotation.Deserialize;
import com.lonepulse.zombielink.annotation.Detach;
import com.lonepulse.zombielink.annotation.Endpoint;
import com.lonepulse.zombielink.annotation.GET;
import com.lonepulse.zombielink.model.User;
import com.lonepulse.zombielink.proxy.InvocationContext;
import com.lonepulse.zombielink.response.AbstractDeserializer;

/**
 * <p>An endpoint with request method definitions that use various pre-fabricated and custom deserializers.</p>
 * 
 * @version 1.1.1
 * <br><br> 
 * @since 1.3.0
 * <br><br> 
 * @category test
 * <br><br> 
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
@Deserialize(JSON)
@Endpoint("http://0.0.0.0:8080")
public interface DeserializerEndpoint {
	
	/**
	 * <p>A mock request which receives a response with a code that signals a failure. 
	 * Expects a domain specific exception to be thrown rather than the deserialized result.</p>  
	 *
	 * @return the deserialized response content, which in this case should not be available
	 * 
	 * @since 1.3.0
	 */
	@Deserialize(PLAIN)
	@GET("/responseerror")
	String responseError();
	
	/**
	 * <p>A mock request which receives a JSON response that is deserialized to its model.</p>
	 * 
	 * @return the deserialized response entity
	 * 
	 * @since 1.3.0
	 */
	@GET("/json")
	User deserializeJson();
	
	/**
	 * <p>A mock request which receives an XML response that is deserialized to its model.</p>
	 * 
	 * @return the deserialized response entity
	 * 
	 * @since 1.3.0
	 */
	@GET("/xml") @Deserialize(XML)
	User deserializeXml();
	
	/**
	 * <p>A mock request which does not use an @{@link Deserialize} definition and defers to 
	 * the <i>raw deserializer</i> which simple retrieves the response content as a String.</p>
	 *
	 * @return the deserializer <b>raw</b> response content
	 * 
	 * @since 1.3.0
	 */
	@GET("/raw") @Deserialize(PLAIN)
	String plain();
	
	
	static final class Redactor extends AbstractDeserializer<User> {
		
		
		public Redactor() {
			
			super(User.class);
		}

		@Override
		protected User deserialize(InvocationContext context, HttpResponse response) {

			try {
				
				String json = EntityUtils.toString(response.getEntity());
				
				User user = new Gson().fromJson(json, User.class);
				user.setFirstName("<redacted>");
				user.setLastName("<redacted>");
				
				return user;
			}
			catch (Exception e) {
			
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * <p>A mock request with a response which should be deserialized by a custom deserializer.</p>
	 * 
	 * @return the deserialized response entity
	 * 
	 * @since 1.3.0
	 */
	@GET("/custom")
	@Deserialize(type = Redactor.class) 
	User deserializeCustom();
	
	/**
	 * <p>Sends a request which detaches the inherited deserializer defined on the endpoint.</p>
	 *
	 * @return the response which should not be processed by a deserializer
	 *
	 * @since 1.3.0
	 */
	@GET("/detach") @Detach(Deserialize.class)
	String detachDeserializer();
	
	
	static final class UninstantiableDeserializer extends AbstractDeserializer<String> {
		
		public UninstantiableDeserializer(String illegalParam) { //illegal parameterized constructor
			
			super(String.class);
		}
		
		@Override
		protected String deserialize(InvocationContext context, HttpResponse response) {
			
			return "deserialized";
		}
	}
	
	/**
	 * <p>A mock request which uses a custom deserializer that cannot be instantiated.</p>
	 * 
	 * @return mock content which will never be available due to a deserialization failure
	 * 
	 * @since 1.3.0
	 */
	@GET("/uninstantiabledeserializer")
	@Deserialize(type = UninstantiableDeserializer.class)
	String uninstantiableDeserializer();
	
	
	static final class IllegalDeserializer extends AbstractDeserializer<Object> {
		
		public IllegalDeserializer(String param) {
			
			super(Object.class); //illegal type
		}
		
		@Override
		protected String deserialize(InvocationContext context, HttpResponse response) {
			
			return "deserialized";
		}
	}
	
	/**
	 * <p>A mock request which uses a custom deserializer that cannot be instantiated.</p>
	 * 
	 * @param user
	 * 			the model which should be serialized using a custom serializer
	 * 
	 * @since 1.3.0
	 */
	@GET("/illegaldeserializer")
	@Deserialize(type = IllegalDeserializer.class)
	User illegalDeserializer();
}
