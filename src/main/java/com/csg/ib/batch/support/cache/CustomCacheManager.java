package com.csg.ib.batch.support.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomCacheManager {

	private CustomCacheManager() {
		throw new IllegalStateException("Unable To Instantiate");
	}

	public static Map<String, List<String>> reportingCache = new ConcurrentHashMap<>();
	public static List<String> reportingTableCache = new ArrayList<>();
	
	public static List<String> matchedTableCache = new ArrayList<>();

}
