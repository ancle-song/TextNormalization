package com.bg.until ;
import java.util.ArrayList;  
import java.util.List;  
  
//LCS算法实现 求两个字符串中间最长的公共字符串  
  
public class LCS{  
  
    public static void main(String[] args) {  
        LCS lcs = new LCS();  
        System.out.println(lcs.getLCSR("u", "y'all"));     
        
    }  
    
    public static float getLCSR(String str1 ,String str2){
    	float LCSR = 0 ;
    	List<String> commonStr = getSameStr(str1, str2) ;
    	int length =  str1.length()>str2.length()?str1.length():str2.length() ;
    	int LCS =  0;
    	for(String str:commonStr){
    		LCS+=str.length() ;
    	}
    	
    	LCSR = (float)LCS/length ;
//    	System.out.println("LCS = " +LCS);
//    	System.out.println("length = " +length);
//    	System.out.println("LCSR = "+LCSR);
    	return LCSR ;
    }
      
    public static  List<String> getSameStr(String str1, String str2){  
        char[] arrchar1 = str1.toCharArray();  
        char[] arrchar2 = str2.toCharArray();  
        int[][] arr = new int[arrchar1.length][arrchar2.length];  
        int len = arrchar1.length < arrchar2.length ? arrchar1.length:arrchar2.length;  
        int maxarr[] = new int[len];  
        int maxindex[] = new int[len];  
        for(int i = 0; i < arrchar1.length ; i++){  
            for(int j = 0; j < arrchar2.length ; j++){  
                if(arrchar2[j] == arrchar1[i]){  
                    if(i == 0 || j == 0){  
                        arr[i][j] = 1;  
                        if(maxarr[0] < 1){  
                            maxarr[0] = 1;  
                            maxindex[0] = i;  
                        }  
                    }else{  
                        arr[i][j] = arr[i-1][j-1] + 1;  
                        //如果当前求出的子串长度大于了maxarr中第一个数值 则清空maxarr数值 全部置0 同时替换第一个最大值  
                        if(maxarr[0] < arr[i][j]){  
                            maxarr[0] = arr[i][j];  
                            maxindex[0] = i;  
                            for(int num = 1; num < maxarr.length; num++){  
                                if(maxarr[num] == 0){  
                                    break;  
                                }else{  
                                    maxarr[num] = 0;  
                                    maxindex[num] = 0;  
                                }  
                            }  
                        }else if (maxarr[0] == arr[i][j]){  
                            //如果当前求出的子串长度跟maxarr中第一个一致 则保留  
                            int num = 0;  
                            for(int max : maxarr){  
                                if(max == 0){  
                                    maxarr[num] = arr[i][j];  
                                    maxindex[num] = i;  
                                    break;  
                                }  
                                num++;  
                            }  
                        }  
                    }  
                }else{  
                    arr[i][j] = 0;  
                }  
            }  
        }  
//        for(int i = 0;i < arr.length ; i++){  
//            for(int j = 0;j < arr[i].length;j++){  
//                System.out.print("   " + arr[i][j]);  
//            }  
//            System.out.println("");  
//        }  
          
        List<String> list = new ArrayList<String>();  
          
          
        for(int i = 0; i< maxarr.length ;i++){  
            if(maxarr[i] == 0){  
                break;  
            }  
            int num = maxindex[i] - (maxarr[i] -1);  
            String str = "";  
            for(int k = 0;k<maxarr[i];k++){  
                char tempchar = arrchar1[num];  
                str += String.valueOf(tempchar);  
                num++;  
            }  
//            System.out.println(str);  
            list.add(str);  
        }  
                  
        return list;  
    }  
}  
