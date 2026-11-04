package com.jaiganesh.urlshortener.service;

import org.springframework.stereotype.Service;

@Service
public class Base62Service {

	private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public String encode(long number) {
		if(number == 0) {
			return "0";
		}
	
	   StringBuilder sb = new StringBuilder();
	
    	while(number > 0) {
		  int remainder = (int) (number % 62);
		  sb.append(BASE62.charAt(remainder));
		  number = number / 62;
	    }
    	return sb.reverse().toString();
	}
	
	public long decode(String shortCode) {
		long number = 0;
		for(int i = 0 ; i < shortCode.length() ; i++) {
			number = number * 62 + BASE62.indexOf(shortCode.charAt(i));
		}
		return number;
	}
}
