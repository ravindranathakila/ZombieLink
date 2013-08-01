package com.lonepulse.zombielink.test.endpoint;

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
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.findAll;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.lonepulse.zombielink.core.annotation.Bite;
import com.lonepulse.zombielink.core.annotation.Header;
import com.lonepulse.zombielink.core.annotation.HeaderSet;
import com.lonepulse.zombielink.core.annotation.Stateful;
import com.lonepulse.zombielink.core.inject.Zombie;

/**
 * <p>Performs <b>Unit Testing</b> on the proxy of {@link MockEndpoint}.
 * 
 * @category test
 * <br><br>
 * @version 1.1.1
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class MockEndpointTest {

	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	
	@Bite
	private MockEndpoint mockEndpoint;
	
	
	/**
	 * <p>Sets up the test case by performing endpoint injection on {@link #mockEndpoint}.
	 * 
	 * @throws java.lang.Exception
	 * 			if the test case setup or endpoint injection failed
	 */
	@Before
	public void setUp() throws Exception {
		
		Zombie.infect(this);
	}
	
	/**
	 * <p>Test for the request method POST.
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testPostMethod() {
		
		String path = "/postrequest";
		
		stubFor(post(urlEqualTo(path)).willReturn(aResponse().withStatus(200)));
		
		String name = "DoctorWho", age = "953", location = "Tardis";
		
		mockEndpoint.postRequest(name, age, location);
		
		List<LoggedRequest> requests = findAll(postRequestedFor(urlMatching(path)));
		assertFalse(requests == null);
		assertFalse(requests.isEmpty());
		
		LoggedRequest request = requests.get(0);
		assertTrue(request.getMethod().equals(RequestMethod.POST));
		
		String body = request.getBodyAsString();
		assertTrue(body.contains("name=" + name));
		assertTrue(body.contains("age=" + age));
		assertTrue(body.contains("location=" + location));
	}
	
	/**
	 * <p>Test for the request method PUT.
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testPutMethod() {
		
		String path = "/putrequest";
		
		stubFor(put(urlEqualTo(path))
				.willReturn(aResponse().withStatus(200)));
		
		String name = "DoctorWho", age = "953", location = "Tardis";
		
		mockEndpoint.putRequest(name, age, location);
		
		List<LoggedRequest> requests = findAll(putRequestedFor(urlMatching(path)));
		assertFalse(requests == null);
		assertFalse(requests.isEmpty());
		
		LoggedRequest request = requests.get(0);
		assertTrue(request.getMethod().equals(RequestMethod.PUT));
		
		String body = request.getBodyAsString();
		assertTrue(body.contains("name=" + name));
		assertTrue(body.contains("age=" + age));
		assertTrue(body.contains("location=" + location));
	}
	
	/**
	 * <p>Test for the request method DELETE.
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testDeleteMethod() {
		
		String path = "/deleterequest/timelord/doctorwho";
		
		stubFor(delete(urlEqualTo(path)).willReturn(aResponse().withStatus(200)));
		
		mockEndpoint.deleteRequest();
		
		List<LoggedRequest> requests = findAll(deleteRequestedFor(urlMatching(path)));
		assertFalse(requests == null);
		assertFalse(requests.isEmpty());
		
		LoggedRequest request = requests.get(0);
		assertTrue(request.getMethod().equals(RequestMethod.DELETE));
	}
	
	/**
	 * <p>Test for a request {@link Header}.
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testRequestHeader() {
		
		stubFor(get(urlEqualTo("/requestheader"))
				.willReturn(aResponse().withStatus(200)));
		
		String header = "mobile";
		
		mockEndpoint.requestHeader(new StringBuilder(header));
		
		verify(getRequestedFor(urlMatching("/requestheader"))
				.withHeader("User-Agent", matching(header)));
	}
	
	/**
	 * <p>Test for {@link HeaderSet} and {@link HeaderSet.Header}.
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testHeaderSet() {
		
		stubFor(get(urlEqualTo("/headerset"))
				.willReturn(aResponse().withStatus(200)));
		
		mockEndpoint.headerSet();
		
		verify(getRequestedFor(urlMatching("/headerset"))
				.withHeader("Accept", matching("application/json"))
				.withHeader("Accept-Charset", matching("utf-8")));
	}
	
	/**
	 * <p>Test for {@link Stateful}.
	 * 
	 * @since 1.2.4
	 */
	@Test
	public final void testState() {
		
		String cookie = "JSESSIONID=1111";
		
		stubFor(get(urlEqualTo("/stateful"))
				.willReturn(aResponse().withStatus(200)
				.withHeader("Set-Cookie", cookie)
				.withBody("empty")));
		
		StringBuilder cookieHeader = new StringBuilder();
		
		mockEndpoint.stateful(cookieHeader);
		assertEquals(cookie, cookieHeader.toString()); //the server should return a cookie to store 
		
		mockEndpoint.stateful(new StringBuilder()); //a second request should submit the cookie
		
		verify(getRequestedFor(urlMatching("/stateful"))
			   .withHeader("Cookie", matching(cookie)));
	}
}
