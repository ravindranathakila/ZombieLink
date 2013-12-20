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

import com.lonepulse.zombielink.request.Interceptor;

/**
 * <p>Identifies an {@link Interceptor} which will be used to process a request just before it's executed.</p>
 * <br>
 * <b>Usage:</b>
 * <br>
 * <br>
 * <ol>
 * <li>
 * <p>At <b>type-level</b> on an endpoint; attaches the interceptor(s) for all requests.</p>
 * <code>
 * <pre>@Endpoint("https://api.github.com")<b>
 *&#064;Intercept({CommonInterceptor1.class, CommonInterceptor2.class})</b><br>public interface GitHubEndpoint {<br>&nbsp;...<br>}</b>
 * </pre>
 * </code>
 * </li>
 * <li>
 * <p>At <b>method-level</b> on a request.</p>
 * <code>
 * <pre>@Deserialize(JSON)
 *&#064;GET("/users/{user}/gists")
 *<b>@Intercept(SpecificInterceptor.class)</b>
 *List&lt;Gist&gt; getGists(@PathParam("user") String user);</pre>
 * </code>
 * </li>
 * <li>
 * <p>As a <b>request parameter</b>.</p>
 * <code>
 * <pre>@Deserialize(JSON)</b>&nbsp;&nbsp;@GET("/users/{user}/gists")
 *List&lt;Gist&gt; getGists(@PathParam("user") String user, <b>Interceptor</b> interceptor);</b></b></pre>
 * </code>
 * </li>
 * </ol>
 * </p>
 * <br>
 * @version 1.1.2
 * <br><br>
 * @since 1.3.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Intercept {
	
	
	/**
	 * <p>The {@link Class}es which identify the custom {@link Interceptor}s which will be use on the 
	 * request before they are executed.</p> 
	 * 
	 * @return the {@link Class}es which identify the {@link Interceptor}s to be used
	 * <br><br>
	 * @since 1.3.0
	 */
	Class<? extends Interceptor>[] value();
}
