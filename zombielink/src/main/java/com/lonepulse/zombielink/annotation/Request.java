package com.lonepulse.zombielink.annotation;

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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;

/**
 * <p>This annotation is used to mark a method which initiates an HTTP request. It can be used 
 * as a meta-annotation to identify the HTTP method for custom annotations along the lines of 
 * <code>@GET</code> or <code>@PUT</code>. For such custom annotations, be sure to include the 
 * property {@code String value() default "";} for accepting the request path.</p>
 * 
 * <b>Usage:</b>
 * <br>
 * <br>
 * <p>
 * <code>
 * <code>
 * <pre><b>@Request(path = "/repos/{user}/{repo}/events")</b>
 *List&lt;Event&gt; getRepoEvents(@PathParam("user") String user, &#064;PathParam("repo") String repo);
 * </pre>
 * </code>
 * </p>
 * 
 * @version 1.2.0
 * <br><br>
 * @since 1.1.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface Request {
	
	
	/**
	 * <p>Identifies the <i>method</i> of a request as specified in <a href="">Section 9</a> 
	 * of the HTTP 1.1 RFC.</p>
	 * 
	 * @version 1.1.0
	 * <br><br>
	 * @since 1.2.4
	 * <br><br>
	 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
	 */
	public static enum RequestMethod {

		/**
		 * <p>Identifies an {@link HttpGet} request.</p>
		 * 
		 * @since 1.2.4
		 */
		GET,
		
		/**
		 * <p>Identifies an {@link HttpPost} request.</p>
		 * 
		 * @since 1.2.4
		 */
		POST,
		
		/**
		 * <p>Identifies an {@link HttpPut} request.</p>
		 * 
		 * @since 1.2.4
		 */
		PUT,
		
		/**
		 * <p>Identifies an {@link HttpDelete} request.</p>
		 * 
		 * @since 1.2.4
		 */
		DELETE,
		
		/**
		 * <p>Identifies an {@link HttpHead} request.</p>
		 * 
		 * @since 1.2.4
		 */
		HEAD,
		
		/**
		 * <p>Identifies an {@link HttpTrace} request.</p>
		 * 
		 * @since 1.2.4
		 */
		TRACE,
		
		/**
		 * <p>Identifies an {@link HttpOptions} request.</p>
		 * 
		 * @since 1.2.4
		 */
		OPTIONS;
	}
	
	/**
	 * <p>The HTTP request method indicated using {@link RequestMethod}.</p>
	 * 
	 * <p>The default method type is {@link RequestMethod#GET}.</p>
	 * 
	 * @return the HTTP request method
	 * <br><br>
	 * @since 1.1.0
	 */
	RequestMethod method() default RequestMethod.GET;
	
	/**
	 * <p>A sub-path which which continues from the root hierarchy of the URI. If no sub path is given, 
	 * the resource is assumed to be found at the root path given on the endpoint.</p>    
	 * 
	 * @return the sub-path on which the resource is located 
	 * <br><br>
	 * @since 1.1.0
	 */
	String path() default "";
}
