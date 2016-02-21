package com.acerete.utils;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RandomUtils {

	// Numbers and lower case letters
	public static final String NUMBERS_AND_LOWER_LETTERS_REGEX = "^[a-z0-9]*$";
	
	// Thread safe
	private static final ThreadLocal<SecureRandom> RANDOM = new ThreadLocal<SecureRandom>() {
		@Override
		protected SecureRandom initialValue() {
			return new SecureRandom();
		}
	};
	
	/**
	 * Generates 'unique id' 
	 * @return id
	 */
	public static String generateUniqueId() {
		return new BigInteger(130, RANDOM.get()).toString(32);
	}
}
