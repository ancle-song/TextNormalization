package Test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.RefinedSoundex;

import com.bg.until.LCS;
import com.googlecode.phonet4java.DaitchMokotoff;

public class CommonsCode {
	public static void main(String[] args) throws EncoderException {
//		RefinedSoundex rs = new RefinedSoundex();
		String str1 = "effing" ;
		String str2 = "fucking" ;
//		int ds = rs.difference(str1, str2) ;//return the number of characters in two strings are the same
//		String encode1 = rs.encode(str1);
//		String encode2 = rs.encode(str2);
//		System.out.println(str1 +" : " + encode1);
//		System.out.println(str2 +" : " + encode2);
//		System.out.println(encode1.length());
//		System.out.println(encode2.length());
//		System.out.println(ds);
//		int len1 = encode1.length() ;
//		int len2 = encode2.length() ;
//		float sountSimilarity = (float)ds/(len1>len2?len1:len2) ;
//		System.out.println(str1 +" : " + str2+"\t"+sountSimilarity);
		
		float similarity = soundSimilarity(str1 ,str2);
		System.out.println("soundsimilarity = " + soundSimilarity(str1 ,str2));
		
	}
	
	
	public static float soundSimilarity(String str1 ,String str2) throws EncoderException{
		float similarity = 0 ;
		DaitchMokotoff phonet = new DaitchMokotoff();
		 String code1 = phonet.code(str1);
		 String code2 = phonet.code(str2);
		 System.out.println(code1);
		 System.out.println(code2);
		 float lcsr = LCS.getLCSR(code1, code2);
//		System.out.println(str1 +" : " + encode1);
//		System.out.println(str2 +" : " + encode2);
//		System.out.println(encode1.length());
//		System.out.println(encode2.length());
//		System.out.println(ds);
//		int len1 = code1.length() ;
//		int len2 = code2.length() ;
//		similarity  = lcsr/len1 ;

		return lcsr ;
	}
}
