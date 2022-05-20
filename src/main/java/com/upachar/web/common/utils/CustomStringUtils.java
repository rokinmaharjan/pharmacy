package com.upachar.web.common.utils;

public class CustomStringUtils {
	
	private CustomStringUtils() {
		
	}

	public static String getImageNameFromUrl(String url) {
		String[] urlComponents = url.split("/");

		return urlComponents[urlComponents.length - 1];
	}
}
