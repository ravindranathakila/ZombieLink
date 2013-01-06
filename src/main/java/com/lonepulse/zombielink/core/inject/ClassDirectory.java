/**
 * 
 */
package com.lonepulse.zombielink.core.inject;

/**
 * <p>This interface determines the policy of a <i>directory service</i> which can 
 * register and retrieve entities by their {@link Class} representations.
 * 
 * @version 1.1.1
 * <br><br>
 * @author <a href="mailto:lahiru@lonepulse.com">Lahiru Sahan Jayasinghe</a>
 */
public interface ClassDirectory {

	
	/**
	 * <p>Adds an entry to the class directory. If an entry is already defined 
	 * with this {@link Class} <i>the existing entry is retained</i> and the 
	 * <i>new entry is silently ignored</i>.  
	 * 
	 * @param entryKey
	 * 			the endpoint {@link Class} representation used as the <b>key</b>
	 * 
	 * @param entryValue
	 * 			the <b>proxy</b> instance to be registered in the directory
	 * <br><br>
	 * @since 1.1.1
	 */
	public void put(Class<?> entryKey, Object entryValue);
	
	/**
	 * <p>Adds an entry to the endpoint directory. If an entry is already defined 
	 * with this {@link Class} <i>the existing proxy is <b>replaced</b> and 
	 * <b>returned</b></i>.  
	 * 
	 * @param entryKey
	 * 			the endpoint {@link Class} representation used as the <b>key</b>
	 * 
	 * @param entryValue
	 * 			the <b>proxy</b> instance to be registered in the directory
	 * 
	 * @return the previous entry's value
	 * <br><br>
	 * @since 1.1.1
	 */
	public Object post(Class<?> entryKey, Object entryValue);

	/**
	 * <p>Retrieves an entry from the directory using the {@link Class} key.
	 * 
	 * @param entryKey
	 * 			the {@link Class} which identifies the proxy to retrieve
	 * 
	 * @return the entry <b>value</b> or <b>null</b> if the endpoint {@link Class} key is missing
	 * <br><br>
	 * @since 1.1.1
	 */
	public Object get(Class<?> entryKey);
	
	/**
	 * <p>Deletes an entry from the directory using the {@link Class} key.
	 * 
	 * @param entryKey
	 * 			the {@link Class} which identifies the proxy to delete
	 * 
	 * @return the deleted entry <b>value</b> or <b>null</b> if the endpoint {@link Class} key is missing
	 * <br><br>
	 * @since 1.1.1
	 */
	public Object delete(Class<?> entryKey);
}
