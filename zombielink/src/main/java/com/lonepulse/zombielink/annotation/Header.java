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

/**
 * <p>Identifies a header which should be sent with the request. If this annotation is placed on an 
 * instance of {@link StringBuilder}, its content will be replaced by any available <b>response</b> 
 * header with the same name.</p>
 * <br>
 * <b>Usage:</b>
 * <br>
 * <br>
 * <ol>
 * <li>
 * <h5>Request Headers</h5>
 * <code>
 * <pre>
 *&#064;GET("/repos/{user}/{repo}/git/blobs/{sha}")
 *Blob getBlob(<b>@Header</b>("Accept") String mediaType, ... );
 * </pre>
 * </code>
 * </li>
 * <li>
 * <h5>Response Headers</h5>
 * <code>
 * <pre>
 *&#064;GET("/users/{user}/repos")
 *List&lt;Repo&gt; getRepos(<b>@Header</b>("X-RateLimit-Remaining") <b>StringBuilder</b> rateLimitRemaining, ... );</pre>
 * </code>
 * </li>
 * </ol>
 * </p>
 * <br>
 * @version 1.1.1
 * <br><br>
 * @since 1.1.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Header {
	
	/**
	 * <p>The name of the header.</p>
	 * 
	 * @return the header name
	 * <br><br>
	 * @since 1.1.1
	 */
	String value();
}
