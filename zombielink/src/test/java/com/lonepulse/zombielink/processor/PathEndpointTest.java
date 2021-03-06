package com.lonepulse.zombielink.processor;

/*
 * #%L
 * ZombieLink
 * %%
 * Copyright (C) 2013 - 2014 Lonepulse
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
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.lonepulse.zombielink.annotation.Bite;
import com.lonepulse.zombielink.annotation.PathParam;
import com.lonepulse.zombielink.annotation.Request;
import com.lonepulse.zombielink.proxy.InvocationException;
import com.lonepulse.zombielink.proxy.Zombie;

/**
 * <p>Performs unit testing on {@link PathEndpoint}.</p>
 * 
 * @version 1.1.1
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @category test
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
public class PathEndpointTest {

	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Bite
	private PathEndpoint pathEndpoint;
	
	
	@Before
	public void setUp() throws Exception {
		
		Zombie.infect(this);
	}
	
	/**
	 * <p>Test for a {@link Request} with a subpath.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testSubpath() {
		
		String subpath = "/subpath", body = "hello";
		
		stubFor(get(urlEqualTo(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withBody(body)));
		
		assertEquals(body, pathEndpoint.subpath());
		verify(getRequestedFor(urlMatching(subpath)));
	}
	
	/**
	 * <p>Test for a RESTful {@link Request} with a subpath having {@link PathParam}s.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testRestfulSubpathWithParams() {
		
		String subpath = "/restfulsubpathwithparam/\\S+", body = "hello", 
			   id = "doctorwho", url = "/restfulsubpathwithparam/" + id;
		
		stubFor(get(urlMatching(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withBody(body)));
		
		assertEquals(body, pathEndpoint.restfulSubpathWithParam(id));
		verify(getRequestedFor(urlEqualTo(url)));
	}
	
	/**
	 * <p>Test for {@link PathEndpoint#restfulSubpathWithIllegalParamType(Long)}.</p>
	 * 
	 * @since 1.3.0
	 */
	@Test
	public final void testRestfulSubpathWithIllegalParamType() {
		
		String subpath = "/restfulsubpathwithillegalparamtype/\\S+", body = "hello";
		
		stubFor(get(urlMatching(subpath))
				.willReturn(aResponse()
				.withStatus(200)
				.withBody(body)));
		
		expectedException.expect(Is.isA(InvocationException.class));
		
		assertNull(pathEndpoint.restfulSubpathWithIllegalParamType(1L));
	}
}
