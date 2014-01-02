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
 * <p>Identifies an endpoint definition whose thread-safe proxy should be created and injected. Endpoint 
 * injection is performed with {@link Zombie#infect(Object, Object...)}.</p>
 * <br>
 * <b>Usage:</b>
 * <br><br>
 * <ul>
 * <li>
 * <h5>Property Injection</h5>
 * <pre>
 * <code><b>@Bite</b>
 * private GitHubEndpoint gitHubEndpoint;
 * {
 * &nbsp; &nbsp; Zombie.infect(this);
 * }
 * </code>
 * </pre>
 * </li>
 * <li>
 * <h5>Setter Injection</h5>
 * <pre>
 * <code><b>@Bite</b>
 * private GitHubEndpoint gitHubEndpoint;
 * {
 * &nbsp; &nbsp; Zombie.infect(this);
 * }
 * </code>
 * <code>
 * public void setGitHubEndpoint(GitHubEndpoint gitHubEndpoint) {
 * 
 * &nbsp; &nbsp; this.gitHubEndpoint = gitHubEndpoint;
 * }
 * </code>
 * </pre>
 * </li>
 * </ul>
 * <br>
 * @version 1.1.0
 * <br><br>
 * @since 1.1.0
 * <br><br>
 * @author <a href="http://sahan.me">Lahiru Sahan Jayasinghe</a>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface Bite {}
