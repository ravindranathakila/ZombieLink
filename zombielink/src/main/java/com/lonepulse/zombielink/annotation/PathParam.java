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

/**
 * <p>This annotation marks a RESTful path parameter which should replace a placeholder on the request 
 * path with the same name.</p> 
 * <br>
 * <br>
 * <b>Usage:</b>
 * <br>
 * <br>
 * <br>
 * <br>
 * <p>
 * <code>
 * <pre>@GET("<b>/repos/{user}/{repo}/events</b>")
 *List&lt;Event&gt; getRepoEvents(<b>@PathParam("user") String user, &#064;PathParam("repo") String repo</b>);
 * </pre>
 * </code>
 * </p>
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface PathParam {

	
	/**
	 * <p>The name of the RESTful request's path parameter.</p>
	 * 
	 * @return the name of the request parameter
	 * <br><br>
	 * @since 1.1.1
	 */
	String value();
}
