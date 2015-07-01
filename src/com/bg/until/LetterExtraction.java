package com.bg.until;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LetterExtraction {
	static Pattern p = Pattern.compile("([a-zA-Z0-9]+)");
	
	
	public static String extraction(String source){
		String result = "" ;
		Matcher m = p.matcher(source);
		while(m.find()){
			result+=m.group();
		}
		return result ;
	}

	public static String extractionx(String source){
		
		Pattern px = Pattern.compile("([0-9a-zA-Z])\\1{2}");
		
		int flag = 0;
		while(flag==0){
			Matcher mx = px.matcher(source);
			if(mx.find()){
				String temp = mx.group();
//				System.out.println("temp = " + temp);
				source= source.replace(temp, temp.substring(0, temp.length()-1)) ;
			}
			else flag = 1 ;
		}
		return source ;
	}
	
	public static void main(String[] args){
		String result =  LetterExtraction.extraction("meeeeee,eeeee.ee88eee..eeeeeeee");
		result =  LetterExtraction.extractionx(result);
		if(result!="")
			System.out.println(result);
		else 
			System.out.println("No letter at all!");
	}
}
