package com.lonepulse.zombielink.executor;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.http.client.HttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.lonepulse.zombielink.inject.MockEndpoint;

/**
 * <p>Performs <b>Unit Testing</b> on the implementation of {@link ConfigurationManager}.</p>
 * 
 * @category test
 * <br><br>
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public class ConfigurationManagerTest {
	

	@Rule
	public WireMockRule wireMockRule = new WireMockRule();
	
	private ConfigurationManager configurationManager;
	
	
	/**
	 * <p>Sets up the test case by instantiating three instances of the default {@link HttpClient} configuration</p>. 
	 */
	@Before
	public void setUp() {
	
		this.configurationManager = new ConfigurationService();
	}
	
	/**
	 * <p>Test for endpoints that do not use a custom configured {@link HttpClient}.</p>
	 *  
	 * @since 1.2.4
	 */
	@Test
	public final void testDefaultClient() {
		
		configurationManager.register(MockEndpoint.class);
		assertTrue(HttpClientDirectory.INSTANCE.lookup(MockEndpoint.class) == HttpClientDirectory.DEFAULT);
	}
	
	/**
	 * <p>Test for endpoints that use a custom {@link HttpClient}.</p>
	 *  
	 * @since 1.2.4
	 */
	@Test
	public final void testCustomClient() {
		
		configurationManager.register(ConfigEndpoint.class);
		
		HttpClient httpClient = HttpClientDirectory.INSTANCE.lookup(ConfigEndpoint.class);
		assertEquals(2 * 1000, httpClient.getParams().getIntParameter(HttpConnectionParams.SO_TIMEOUT, 0));
		
		configurationManager.register(ConfigEndpoint.class);
		assertTrue(HttpClientDirectory.INSTANCE.lookup(ConfigEndpoint.class) == httpClient);
	}
}
