package com.bg.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Test {
	public static void main(String[] args) throws IOException {
		readoovx();
	}
	
	public static List<String> readoovx() throws IOException{
		List<String> oovlist = new ArrayList<String>();
		HashMap<String,String> word_context_map = new HashMap<String,String>();
		HashMap<String,Integer> oov_map = new HashMap<String,Integer>();
		File file = new File("/home/songyajun/Java/dataset/规范化标准测试集/lexnorm_v1.2/corpus.v1.2.tweet");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = "" ;
		int count = -1;
		String[] content = new String[549] ;
		for(int i = 0;i<content.length ;i++)
			content[i]="";
		while((line = br.readLine())!=null){

			String[] temp = line.split("	");
//			System.out.println("temp_length = " + temp.length);
			if(temp.length!=3){
//				System.out.println(count+" : " + line);
				count++ ;
			}
			if(temp.length==3){
				String oov = temp[0];
				String type = temp[1];
				String iv = temp[2] ;
				String record = "" ;
				if(type.equals("OOV")&&(!temp[0].equals(temp[2])))
					 record = oov+" " + type+" " + iv ;
				else 
					record = oov ;
				if(!type.equals("NO")){
//					System.out.println("count = " + count);
					content[count]+=record+"\t" ;
				}
				
//				System.out.println("type = " + type);
				if(type.equals("OOV")&&(!temp[0].equals(temp[2]))){
//					System.out.println("line = "+line);
					oov_map.put(line.replaceAll("\t", " "), 1);
					oovlist.add(line.replaceAll("\t", " "));
				}
			}
		}
		
		for(int i = 0;i<549;i++){
			String[] temp = content[i].split("\t");
			System.out.println(i + " : "+content[i]);
//			for(int j = 0;j<temp.length ;j++){
//				if(oov_map.containsKey(temp[j])){
//					int low = j-2>0?j-2:0 ;
//					int high = temp.length-1>(j+2)?(j+2):temp.length-1 ;
//					String context = "";
//					System.out.println("low = "+low +"\t" +"j = "+ j+"\t"+"high = "+high);
//					for(int k =low ;k<=high;k++){
//						if(k!=j){
//							String word = temp[k].split(" ")[0];
//							context+=word+"\t";
//						}
//						else
//							context+=temp[k]+"\t";
//					}
//					String record = temp[j]+"\t\t"+context ;
//					System.out.println(j+" : " + record);
//				}
//			}
//			System.out.println(i+" : " + content[i]);
		}
		return oovlist ;
	}

}
