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

import com.lonepulse.zombielink.inject.Zombie;

/**
 * <p>Identifies an alternate {@link Zombie.Configuration} which should be used on an endpoint for executing 
 * requests. Extend {@link Zombie.Configuration}, override template methods to provide a custom configuration 
 * and indicate that this configuration be used using its {@link Class}.</p> 
 * 
 * <b>Usage:</b>
 * <code>
 * <pre><br>@Endpoint("api.twitter.com")<br><b>@Configuration(TwitterZombieConfig.class)</b>
 *public interface TwitterEndpoint {<br>}</pre>
 * </code>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration {


	/**
	 * <p>The {@link Class} of the custom {@link Zombie.Configuration} extension with an alternate configuration 
	 * to be used on the endpoint.</p>
	 *
	 * @return the {@link Class} of the custom {@link Zombie.Configuration} extension
	 * 
	 * @since 1.2.4
	 */
	Class<? extends Zombie.Configuration> value();
}
