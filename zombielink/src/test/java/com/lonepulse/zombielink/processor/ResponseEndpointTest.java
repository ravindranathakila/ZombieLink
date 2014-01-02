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
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.gson.Gson;
import com.lonepulse.zombielink.annotation.Bite;
import com.lonepulse.zombielink.model.User;
import com.lonepulse.zombielink.proxy.InvocationException;
import com.lonepulse.zombielink.proxy.Zombie;

/**
 * <p>Performs unit testing on {@link ResponseEndpoint}.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @category test
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
public class ResponseEndpointTest {

	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	
	@Bite
	private ResponseEndpoint responseEndpoint;
	
	
	@Before
	public void setUp() throws Exception {
		
		Zombie.infect(this);
	}
	
	/**
	 * <p>Test for response failures.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testFailure() throws ParseException, IOException {
		
		String subpath = "/failure", content = "jabberwocky";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withBody(content)
				.withStatus(404)));
		
		try {
		
			responseEndpoint.failure();
			fail("Failed request did not throw a context aware <InvocationException>.");
		}
		catch(InvocationException error) {

			assertTrue(error.hasResponse());
			assertNotNull(error.getResponse());
			assertNotNull(error.getContext());
			assertEquals(content, EntityUtils.toString(error.getResponse().getEntity()));
		}
		
		verify(getRequestedFor(urlEqualTo(subpath)));
	}
	
	/**
	 * <p>Test for successful responses without any content.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testNoContent() {
		
		String subpath = "/nocontent";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(204)));
		
		String response = responseEndpoint.noContent();
		
		assertNull(response);
		
		verify(getRequestedFor(urlEqualTo(subpath)));
	}
	
	/**
	 * <p>Test for successful response indicating a content reset.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testResetContent() {
		
		String subpath = "/resetcontent";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(205)));
		
		String response = responseEndpoint.resetContent();
		
		assertNull(response);
		
		verify(getRequestedFor(urlEqualTo(subpath)));
	}

	/**
	 * <p>Test for a request which expects the raw {@link HttpResponse}.</p>
	 *
	 * @since 1.3.0
	 */
	@Test
	public final void testRawResponse() throws ParseException, IOException {
		
		String subpath = "/rawresponse",
			   body = "Welcome to the Republic of Genosha";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withBody(body)
				.withStatus(200)));
		
		Object response = responseEndpoint.rawResponse();
		
		assertNotNull(response);
		assertTrue(response instanceof HttpResponse);
		
		//ensures that the input stream is closed 
		String content = EntityUtils.toString(((HttpResponse)response).getEntity());
		
		assertEquals(content, body);
		verify(getRequestedFor(urlEqualTo(subpath)));
	}
	
	/**
	 * <p>Test for a request which expects the raw {@link HttpEntity}.</p>
	 *
	 * @since 1.3.0
	 */
	@Test
	public final void testRawEntity() throws ParseException, IOException {
		
		String subpath = "/rawentity",
			   body = "Hulk, make me a sandwich";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withBody(body)
				.withStatus(200)));
		
		Object response = responseEndpoint.rawEntity();
		
		assertNotNull(response);
		assertTrue(response instanceof HttpEntity);
		
		//ensures that the input stream is closed 
		String content = EntityUtils.toString(((HttpEntity)response));
		
		assertEquals(content, body);
		verify(getRequestedFor(urlEqualTo(subpath)));
	}
	
	/**
	 * <p>Test for a request which expects the raw {@link HttpEntity}.</p>
	 *
	 * @since 1.3.0
	 */
	@Test
	public final void testNoDeserializer() {
		
		String subpath = "/nodeserializer";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withBody(new Gson().toJson(new User(1, "Cain", "Marko", 37, false)))
				.withStatus(200)));

		try {
		
			responseEndpoint.noDeserializer();
			fail("Request succeeded in the absence of a deserializer.");
		}
		catch(InvocationException e) {
			
			Assert.assertTrue(e.hasResponse());
		}
		
		verify(getRequestedFor(urlEqualTo(subpath)));
	}
}
