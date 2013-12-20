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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.lonepulse.zombielink.proxy.Zombie;

/**
 * <p>Identifies an alternate {@link Zombie.Configuration} which should be used on an endpoint for 
 * executing requests. Extend {@link Zombie.Configuration}, override the template methods to provide 
 * a custom configuration and indicate that this configuration be used using its {@link Class}.</p>
 * <br>
 * <b>Usage:</b>
 * <br>
 * <br>
 * <code>
 * <pre><b>@Config(GitHubConfig.class)</b><br>@Endpoint("https://api.github.com")
 *public interface GitHubEndpoint {<br>&nbsp;&nbsp;...<br>}</pre>
 * </code>
 * <br>
 * @version 1.1.0
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {


	/**
	 * <p>The {@link Class} of the custom {@link Zombie.Configuration} extension with an alternate 
	 * configuration to be used on the endpoint.</p>
	 *
	 * @return the {@link Class} of the custom {@link Zombie.Configuration} extension
	 * 
	 * @since 1.3.0
	 */
	Class<? extends Zombie.Configuration> value();
}
