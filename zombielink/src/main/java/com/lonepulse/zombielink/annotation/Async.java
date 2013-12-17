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
 * <p>Identifies requests which should be executed <b>asynchronously</b>.</p>
 * <br>
 * <b>Usage:</b>
 * <br>
 * <br>
 * <ol>
 * <li>
 * At <b>type-level</b> on an endpoint; marks all requests as asynchronous.<br>
 * <code>
 * <pre><b>@Async</b>&nbsp;@Endpoint(scheme = "https", host = "api.github.com")<br>public interface GithubEndpoint {<br>&nbsp;&nbsp;...<br>}</pre>
 * </code>
 * </li>
 *  
 * <li>
 * At <b>method-level</b> on a request.<br>
 * <code>
 * <pre><b>@Async</b>&nbsp;@GET("/users/{user}/repos")<br>Set&lt;Repo&gt; getRepos(@PathParam("user") String user);</pre>
 * </code>
 * </li>
 * </ol>
 * </p>
 * <br>
 * @version 1.1.0
 * <br><br>
 * @since 1.1.0
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Async {}
