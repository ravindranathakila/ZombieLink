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
 * <p>Models the header associated with a request and groups a constant 
 * set of {@link Headers.Header}s which is to be populated.</p>
 * 
 * <b>Usage:</b>
 * <br>
 * <br>
 * <p>
 * <code>
 * <pre><b>
 * &#064;Headers({&#064;Headers.Header(name = "</b>Accept<b>", value = "</b>text/plain<b>"),
 * 	    &#064;Headers.Header(name = "</b>Accept-Charset<b>", value = "</b>utf-8<b>")})</b>
 * public abstract String getRSSFeed();
 * </pre>
 * </code>
 * </p>
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Headers {

	
	/**
	 * <p>Marks a <b>constant</b> header parameter for a particular request.</p> 
	 * 
	 * @version 1.1.1
	 * <br><br>
	 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
	 */
	public @interface Header {
		
		/**
		 * <p>The name of the header parameter.</p>
		 * 
		 * @return the name of the header parameter
		 * <br><br>
		 * @since 1.1.1
		 */
		public String name();
		
		/**
		 * <p>The value of the header parameter.</p>
		 * 
		 * @return the value for the header parameter
		 * <br><br>
		 * @since 1.1.1
		 */
		public String value();
	}

	
	/**
	 * <p>The parameters to be included in this header.</p>
	 * 
	 * @return the array of {@link Headers.Header}s
	 * <br><br>
	 * @since 1.1.1
	 */
	public Headers.Header[] value();
}
