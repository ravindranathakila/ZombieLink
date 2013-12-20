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

import org.apache.http.HttpResponse;

import com.lonepulse.zombielink.annotation.Endpoint;
import com.lonepulse.zombielink.annotation.GET;
import com.lonepulse.zombielink.annotation.Header;
import com.lonepulse.zombielink.annotation.Headers;

/**
 * <p>An endpoint with mock which processes request and response headers.</p>
 * 
 * @version 1.1.1
 * <br><br> 
 * @since 1.3.0
 * <br><br> 
 * @category test
 * <br><br> 
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Endpoint("http://0.0.0.0:8080")
public interface HeaderEndpoint {
	
	/**
	 * <p>A mock request which inserts a request header.</p>
	 * 
	 * @param userAgent
	 * 			a variable header - <i>User-Agent</i> in this case
	 * 
	 * @return the textual content of the {@link HttpResponse} body
	 * 
	 * @since 1.3.0
	 */
	@GET("/requestheader")
	public String requestHeader(@Header("User-Agent") String userAgent);
	
	/**
	 *<p>Retrieves a response header from a request using {@link Header}.</p>
	 * 
	 * @param server
	 * 			the {@link StringBuilder} which is annotated with {@code @Header} 
	 * 			to treat it as an in-out variable for retrieving the response header
	 * 
	 * @return a response whose header was retrieved 
	 * 
	 * @since 1.3.0
	 */
	@GET("/responseheader")
	public String responseHeader(@Header("Server") StringBuilder server);
	
	/**
	 * <p>A mock request which expects a request header value but instead receives 
	 * none form the current invocation.</p>
	 * 
	 * @param userAgent
	 * 			a variable header - <i>From</i> in this case
	 * 
	 * @return the textual content of the {@link HttpResponse} body
	 * 
	 * @since 1.3.0
	 */
	@GET("/requestheaderskip")
	public String requestHeaderSkip(@Header("From") String email);
	
	/**
	 * <p>A mock request which expects a response header value form the server 
	 * but instead receives none.</p>
	 * 
	 * @param server
	 * 			the {@link StringBuilder} which is annotated with {@code @Header} 
	 * 			to treat it as an in-out variable for retrieving the response header
	 * 
	 * @return a response whose header was expected to be retrieved 
	 * 
	 * @since 1.3.0
	 */
	@GET("/responseheaderskip")
	public String responseHeaderSkip(@Header("Expires") String expires);
	
	/**
	 * <p>A mock request which inserts a header that of an illegal type. This invocation 
	 * should be unsuccessful and should result in an error.</p>
	 * 
	 * @param contentLength
	 * 			a variable header of the illegal type {@code int} 
	 * 
	 * @return the deserialized response content, which in this case should not be available
	 * 
	 * @since 1.3.0
	 */
	@GET("/requestheadertypeerror")
	public String requestHeaderTypeError(@Header("Content-Length") int contentLength);
	
	/**
	 * <p>A mock request which inserts a constant set of headers.</p>
	 * 
	 * @return the textual content of the {@link HttpResponse} body
	 * 
	 * @since 1.3.0
	 */
	@GET("/headerset")
	@Headers({@Headers.Header(name = "Accept", value = "application/json"),
			  @Headers.Header(name = "Accept-Charset", value = "utf-8")})
	public String headerSet();
}
