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

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.lonepulse.zombielink.annotation.Bite;
import com.lonepulse.zombielink.inject.InvocationContext;
import com.lonepulse.zombielink.inject.Zombie;
import com.lonepulse.zombielink.request.Interceptor;

/**
 * <p>Performs unit testing on {@link InterceptorEndpoint}.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @category test
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class InterceptorEndpointTest {

	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	
	@Bite
	private InterceptorEndpoint interceptorEndpoint;
	
	
	@Before
	public void setUp() {
		
		Zombie.infect(this);
	}
	
	/**
	 * <p>Test for {@link Interceptor}s defined at an endpoint level.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testEndpointInterceptor() {
		
		String subpath = "/endpoint";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		interceptorEndpoint.endpointInterceptor();
		
		verify(getRequestedFor(urlEqualTo(subpath))
				.withHeader("X-Header", equalTo("endpoint")));
	}
	
	/**
	 * <p>Test for {@link Interceptor}s defined at an endpoint level.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testMetadataInterceptor() {
		
		String subpath = "/metadata";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		interceptorEndpoint.metadataInterceptor();
		
		verify(getRequestedFor(urlEqualTo(subpath))
				.withHeader("X-Header", equalTo("X-Value")));
	}
	
	/**
	 * <p>Test for {@link Interceptor}s defined at an request level.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testRequestInterceptor() {
		
		String subpath = "/request";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		interceptorEndpoint.requestInterceptor();
		
		verify(getRequestedFor(urlEqualTo(subpath))
			   .withHeader("X-Header", equalTo("endpoint"))
			   .withHeader("X-Header", equalTo("request")));
	}
	
	/**
	 * <p>Test for {@link Interceptor}s passed as a request parameters.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testParamInterceptor() {
		
		String subpath = "/param";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		final String value = "param";
		
		interceptorEndpoint.paramInterceptor(new Interceptor() {
			
			@Override
			public void intercept(InvocationContext context, HttpRequestBase request) {
				
				request.addHeader("X-Header", value);
			}
		});
		
		verify(getRequestedFor(urlEqualTo(subpath))
			   .withHeader("X-Header", equalTo("endpoint"))
			   .withHeader("X-Header", equalTo("request"))
			   .withHeader("X-Header", equalTo(value)));
	}
	
	/**
	 * <p>Test for detaching {@link Interceptor}s.</p>
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testDetachInterceptor() {
		
		String subpath = "/detach";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)));
		
		interceptorEndpoint.detachInterceptor();
		
		verify(getRequestedFor(urlEqualTo(subpath))
			   .withoutHeader("X-Header"));
	}
}
