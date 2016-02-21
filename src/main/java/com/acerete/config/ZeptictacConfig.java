package com.acerete.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.acerete.datamodel.DataModel;
import com.acerete.exceptions.ServerConfigException;
import com.acerete.vo.Field;

public class ZeptictacConfig {
	
	private static final String CONFIG_FILE_NAME = "zeptictac.properties";
	
	private static boolean isInitialized = false;
	private static Properties properties;
	private static boolean isTest;
	
	public static void init() throws ServerConfigException {
		init(false);
	}

	public static void init(boolean isTestIn) throws ServerConfigException {
		isInitialized = true;
		isTest = isTestIn;
		try {
			properties = new Properties();
			InputStream fis = ZeptictacConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
			properties.load(fis);
			// Force reading of properties to force failure
			getFieldSize();
			getPaginationLimit();
			getEmptySymbol();
			getPlayerSymbols();
		} catch (IOException e) {
			throw new ServerConfigException(e.getMessage());
		}
	}
	
	public static boolean isTest() {
		return isTest;
	}
	
	private static String getProperty(String key) throws ServerConfigException {
		if (!isInitialized) {
			throw new ServerConfigException("Config not initialized");
		}
		return properties.getProperty(key);
	}
	
	public static int getFieldSize() throws ServerConfigException {
		if (isTest) {
			return Field.MIN_DEFAULT_SIZE;
		}
		int fieldSize = Integer.parseInt(getProperty("field.size"));
		if (fieldSize < Field.MIN_DEFAULT_SIZE) {
			throw new ServerConfigException("Field size too low");
		}
		return fieldSize;
	}
	
	public static char getEmptySymbol() throws ServerConfigException {
		if (isTest) {
			return Field.DEFAULT_EMPTY_SYMBOL;
		}
		String symbol = getProperty("symbol.empty");
		if (symbol.length() != 1) {
			throw new ServerConfigException("Symbol should be one character only");
		}
		return symbol.charAt(0);
	}
	
	public static List<Character> getPlayerSymbols() throws ServerConfigException {
		if (isTest) {
			return Field.DEFAULT_PLAYER_SYMBOLS;
		}
		List<Character> symbols = new ArrayList<Character>();
		int i = 0;
		String newPlayerSymbol = "foo";
		while (newPlayerSymbol != null) {
			i++;
			newPlayerSymbol = getProperty("symbol.player."+i);
			if (newPlayerSymbol != null) {
				if (newPlayerSymbol.length() != 1) {
					throw new ServerConfigException("Symbol should be one character only");
				}
				symbols.add(newPlayerSymbol.charAt(0));
			}
		}
		if (symbols.size() < Field.DEFAULT_PLAYER_SYMBOLS.size()) {
			throw new ServerConfigException("Two players at least are required");
		}
		return symbols;
	}
	
	public static int getPaginationLimit() throws ServerConfigException {
		if (isTest) {
			return DataModel.PAGINATION_LIMIT_DEFAULT;
		}
		int paginationLimit = Integer.parseInt(getProperty("pagination.limit"));
		if (paginationLimit < DataModel.MIN_PAGINATION_LIMIT_DEFAULT) {
			throw new ServerConfigException("Pagination limit is too low");
		}
		if (paginationLimit > DataModel.MAX_PAGINATION_LIMIT_DEFAULT) {
			throw new ServerConfigException("Pagination limit is too high");
		}
		return paginationLimit; 
	}

}
