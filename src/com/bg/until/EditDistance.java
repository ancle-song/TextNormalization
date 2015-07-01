package com.bg.until ;

public class EditDistance {

	public static void main(String[] args) {
		String str1 = "my brother tellin father" ;
		String str2 = "my brother telling father" ;
		long start = System.currentTimeMillis();
		levenshtein(str1,str2);
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.print("time :"+ time);

	}

	/**
	 * 
	 * 
	 * @createTime 2012-1-12
	 */
	public static int levenshtein(String str1,String str2) {
	  int distance = 0 ;
		int len1 = str1.length();
		int len2 = str2.length();
	
		int[][] dif = new int[len1 + 1][len2 + 1];
	
		for (int a = 0; a <= len1; a++) {
			dif[a][0] = a;
		}
		for (int a = 0; a <= len2; a++) {
			dif[0][a] = a;
		}
		int temp;
		for (int i = 1; i <= len1; i++) {
			for (int j = 1; j <= len2; j++) {
				if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
					temp = 0;
				} else {
					temp = 1;
				}
				
				dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,
						dif[i - 1][j] + 1);
			}
		}
//		System.out.println(str1+":"+str2);	
//		System.out.println("edit distance:"+dif[len1][len2]);
	
		float similarity =1 - (float) dif[len1][len2] / Math.max(str1.length(), str2.length());
//		if(similarity>=0.9)
//			System.out.println("similarity:"+similarity);
		distance = dif[len1][len2] ;
		return distance ;
	}
	
	private static int min(int... is) {
		int min = Integer.MAX_VALUE;
		for (int i : is) {
			if (min > i) {
				min = i;
			}
		}
		return min;
	}

}
