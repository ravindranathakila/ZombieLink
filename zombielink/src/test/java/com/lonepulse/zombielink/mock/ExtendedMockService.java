package com.lonepulse.zombielink.mock;

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

import com.lonepulse.zombielink.altpkg.ThirdPartyMockService;
import com.lonepulse.zombielink.annotation.Bite;
import com.lonepulse.zombielink.proxy.MockEndpoint;

/**
 * <p>Emulates a type which should be searched for endpoint injection and its super-type should be excluded.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @category test
 * <br><br> 
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
public class ExtendedMockService extends ThirdPartyMockService {
	
	
	@Bite
	private MockEndpoint extendedEndpoint;
	
	
	public MockEndpoint getExtendedEndpoint() {
		
		return extendedEndpoint;
	}
}
