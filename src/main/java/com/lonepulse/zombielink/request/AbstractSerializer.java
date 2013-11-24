package com.lonepulse.zombielink.request;

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

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;

import com.lonepulse.zombielink.inject.InvocationContext;
import com.lonepulse.zombielink.util.Entities;
import com.lonepulse.zombielink.util.EntityResolutionFailedException;

/**
 * <p>An abstract implementation of {@link Serializer} which directs <i>serialization</i> for 
 * all {@link Serializer}s. To create a custom {@link Serializer} extend this class and override 
 * {@link #serialize(Object, InvocationContext)}.</p> 
 * 
 * <p><b>Note</b> that all implementations should produce an output type which can be translated 
 * to an {@link HttpEntity} type specified on {@link Entities#resolve(Class)}.</p>
 * 
 * <p><b>Note</b> that all implementations are expected to be <b>stateless</b>.</p>
 * 
 * @version 1.1.0
 * <br><br>
 * @since 1.2.4
 * <br><br>
 * @author <a href="mailto:sahan@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public abstract class AbstractSerializer<INPUT, OUTPUT> implements Serializer<INPUT, OUTPUT> {

	
	/**
	 * <p>Initializes a new {@link AbstractSerializer} with the given {@link Class} which 
	 * represents this serializer's <i>output</i> type.</p>
	 *
	 * @param outputType
	 * 			the {@link Class} of the <i>output</i> type for this serializer
	 * <br><br>
	 * @throws SerializerInstantiationException
	 * 			if the ouput type of this {@link Serializer} cannot be translated to an 
	 * 			{@link HttpEntity} type specified on {@link Entities#resolve(Class)} 
	 * <br><br>
	 * @since 1.2.4
	 */
	public AbstractSerializer(Class<OUTPUT> outputType) {
		
		try {
		
			Entities.resolve(outputType);
		}
		catch(EntityResolutionFailedException erfe) {
			
			throw new SerializerInstantiationException(getClass(), erfe);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final OUTPUT run(INPUT input, InvocationContext context) {
		
		try {
			
			return serialize(input, context);
		}
		catch(Exception e) {
		
			throw new SerializerException(e);
		}
	}
	
	/**
	 * <p>Takes a model which conforms to the <i>input</i> type of the {@link Serializer} and converts 
	 * it to a content type suitable for network transmission.</p>
	 * 
	 * <p><b>Note</b> that all <i>output</i>s are translated to a corresponding {@link HttpEntity} for 
	 * enclosure within an {@link HttpRequest}. Therefore, all output instances should comply to a type 
	 * specified at {@link Entities#resolve(Object)}.</p> 
	 * 
	 * @param input
	 * 			the <i>input</i> model to be serialized to a transmittable format
	 * 
	 * @param context
	 * 			the {@link InvocationContext} which supplies information on the proxy invocation
     * <br><br>
	 * @return the serialized <i>output</i> which will be translated to an {@link HttpEntity}   
	 * <br><br>
	 * @throws Exception 
	 * 			any errors which might occur during serialization are caught, wrapped in an instance 
	 * 			of {@link SerializerException} (cause preserved) and allowed to bubble up
	 * <br><br>
	 * @since 1.2.4
	 */
	protected abstract OUTPUT serialize(INPUT input, InvocationContext context) throws Exception;
}