package com.lonepulse.zombielink.core.processor.validator;

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


import com.lonepulse.zombielink.core.processor.ProxyInvocationConfiguration;

/**
 * <p>Exposes all available {@link Validator}s and delegates communication. 
 * 
 * @version 1.1.0
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public enum Validators implements Validator<Object> {

	
	/**
	 * See {@link EndpointValidator}.
	 * 
	 * @since 1.1.0
	 */
	ENDPOINT(new EndpointValidator());
	
	
	/**
	 * The exposed instance of {@link Validator}.
	 */
	private Validator<? extends Object> validator;

	
	/**
	 * <p>Instantiates {@link #validator} with the give instance of 
	 * {@link Validator}.
	 * 
	 * @param requestExecutor
	 * 			the associated instance of {@link RequestExecutor}
	 */
	private Validators(Validator<? extends Object> validator) {
		
		this.validator = validator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object validate(ProxyInvocationConfiguration config) 
	throws ValidationFailedException {
		
		return validator.validate(config);
	}
}
