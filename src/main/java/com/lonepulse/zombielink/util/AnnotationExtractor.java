package com.lonepulse.zombielink.util;

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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>This is a utility class which is used to extract annotations from different 
 * {@link Class}es.</p>
 * 
 * @version 1.1.2
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public final class AnnotationExtractor {

	
	/**
	 * <p>Constructor visibility restricted. Instantiation is nonsensical. 
	 */
	private AnnotationExtractor() {}
	
	/**
	 * <p>Extracts annotations of a given type from a {@link Method} and returns 
	 * them along with their arguments provided as an {@link Object} array.</p>
	 * 
	 * <p>These arguments maybe collected via a dynamic proxy.</p>
	 * 
	 * @param annotationType
	 * 			the {@link Class} of the annotation to extract
	 * 
	 * @param method
	 * 			the {@link Method} on which to look for annotations of the 
	 * 			given type
	 * 
	 * @param args
	 * 			the {@link Object} array of method argument collected via 
	 * 			the {@link Proxy}
	 * <br><br>
	 * @return
	 * 			the list of {@link Annotation}s along with their runtime 
	 * 			parameter values
	 * <br><br>
	 * @since 1.1.1
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Annotation> Map<Object, T> extractWithParameterValues(Class<T> annotationType, Method method, Object[] args) {
		
		Map<Object, T> argumentAndAnnotationList = new HashMap<Object, T>();
		
		Annotation[][] annotationsForAllParams = method.getParameterAnnotations();
		
		for (int i = 0; i < annotationsForAllParams.length; i++) {
			
			for (Annotation annotationForEachParam : annotationsForAllParams[i]) {
		
				if(annotationType.isAssignableFrom(annotationForEachParam.getClass())) {
					
					argumentAndAnnotationList.put(args[i], (T)annotationForEachParam);
				}
			}
		}
		
		return argumentAndAnnotationList;
	}
	
//	/**
//	 * <p>Extracts header annotations from a {@link Method} and returns them along with 
//	 * their arguments provided as an {@link Object} array. This makes use of 
//	 * {@link AnnotationExtractor#extractWithParameterValues(Class, Method, Object[])} 
//	 * with the annotation type parameter as {@link Header}.</p>
//	 * 
//	 * @param method
//	 * 			the {@link Method} on which to look for annotations of the given type
//	 * 
//	 * @param args
//	 * 			the {@link Object} array of method argument collected via the {@link Proxy}
//	 * <br><br>
//	 * @return
//	 * 			the list of variable header {@link Param}s along with their runtime 
//	 * 			parameter values
//	 * <br><br>
//	 * @since 1.1.2
//	 */
//	public static Map<StringBuilder, Header> extractHeaders(Method method, Object[] args) {
//		
//		Map<StringBuilder, Header> headerParams = new HashMap<StringBuilder, Header>();
//		
//		Annotation[][] annotationsForAllParams = method.getParameterAnnotations();
//		
//		for (int i = 0; i < annotationsForAllParams.length; i++) {
//			
//			for (Annotation annotationForEachParam : annotationsForAllParams[i]) {
//				
//				if(Header.class.isAssignableFrom(annotationForEachParam.getClass())) {
//					
//					try {
//						
//						headerParams.put((StringBuilder)args[i], (Header)annotationForEachParam);
//					}
//					catch (ClassCastException cce) {
//						
//						throw new Exception(args[i], method);
//					}
//				}
//			}
//		}
//		
//		return headerParams;
//	}
}
