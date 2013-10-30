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

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;

import com.lonepulse.zombielink.annotation.Configuration;
import com.lonepulse.zombielink.inject.Zombie;

/**
 * <p>A registry of {@link HttpClient}s which are configured to be used for a specific endpoint.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
enum HttpClientRegistry {
	
	/**
	 * <p>The instance of {@link HttpClientRegistry} which caches {@link HttpClient}s that are uniquely configured 
	 * for each endpoint. An {@link HttpClient} is stored or accessed using an endpoint {@link Class}.</p>
	 * 
	 * @since 1.2.4
	 */
	INSTANCE;
	
	
	/**
	 * <p>The default configuration for an {@link HttpClient} which will be used to executing endpoint requests if 
	 * no specialized configuration is provided.</p>
	 * 
	 * @since 1.2.4
	 */
	public static final HttpClient DEFAULT;
	
	
	private static final Map<String, HttpClient> DIRECTORY  = new HashMap<String, HttpClient>();
	
	private static final Map<String, String> ENDPOINT_CONFIGS = new HashMap<String, String>();
	
	
	static {
		
		DEFAULT = new Zombie.Configuration(){}.httpClient();
		DIRECTORY.put(Zombie.Configuration.class.getName(), DEFAULT);
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				synchronized (DIRECTORY) {
					
					for (HttpClient httpClient : DIRECTORY.values()) {
						
						try {
							
							httpClient.getConnectionManager().shutdown();
						}
						catch(Exception e) {}
					}
				}
			}
		}));
	}
	
	
	/**
	 * <p>Registers an instance of {@link HttpClient} under the given {@link Class} of the endpoint definition. 
	 * If an {@link HttpClient} already exists under the given endpoint, <i>no attempt will be made to replace 
	 * the existing instance</i>.</p>
	 * 
	 * @param endpoint
	 * 			the {@link Class} of the endpoint whose {@link HttpClient} is to be added to the directory
	 * <br><br>
	 * @param httpClient
	 * 			the {@link HttpClient} to be registered under the given endpoint definition
	 * <br><br>
	 * @return the {@link HttpClient} which was registered under the given endpoint definition
	 * <br><br> 
	 * @since 1.2.4
	 */
	public synchronized HttpClient bind(Class<?> endpoint, HttpClient httpClient) {
		
		String configClassName = endpoint.isAnnotationPresent(Configuration.class)?
			endpoint.getAnnotation(Configuration.class).value().getName() :Zombie.Configuration.class.getName();
			
		String endpointClassName = endpoint.getName();
			
		if(!DIRECTORY.containsKey(configClassName)) {
			
			DIRECTORY.put(configClassName, httpClient);
		}
		
		if(!ENDPOINT_CONFIGS.containsKey(endpointClassName)) {
			
			ENDPOINT_CONFIGS.put(endpointClassName, configClassName);
		}
		
		return lookup(endpoint);
	}

	/**
	 * <p>Retrieves the {@link HttpClient} which was added under the given endpoint. If no instance was registered 
	 * for this endpoint {@link Class}, the {@link #DEFAULT} instance is returned.</p>
	 *  
	 * @param endpointClass
	 * 			the {@link Class} of the endpoint definition whose {@link HttpClient} is to be retrieved
	 * <br><br>
	 * @return the {@link HttpClient} which was registered under the given endpoint, else the pre-configured 
	 * 		   {@link #DEFAULT} instance if no existing {@link HttpClient} was found
	 * <br><br>
	 * @since 1.2.4
	 */
	public synchronized HttpClient lookup(Class<?> endpointClass) {
		
		HttpClient httpClient = DIRECTORY.get(ENDPOINT_CONFIGS.get(endpointClass.getName()));
		
		return httpClient == null? DEFAULT :httpClient;
	}
}
