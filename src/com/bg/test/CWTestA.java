package com.bg.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.RefinedSoundex;

import com.bg.paper.BiGraph;
import com.bg.paper.HittingNode;
import com.bg.paper.RandomWalk;
import com.bg.until.EditDistance;
import com.bg.until.LCS;
import com.bg.until.LetterExtraction;
import com.googlecode.phonet4java.DaitchMokotoff;


public class CWTestA {
	
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException, EncoderException {
//		String datafile = "./data/new_CW_2013_tweets2009-06.txt" ;
		String datafile = "./data/CW_2013_tweets2009-06.txt" ;
		
		System.out.println("Construct Bipartite Graph...");
		long begin = System.currentTimeMillis() ;
		BiGraph graph = new BiGraph(datafile);
//		graph.saveOOV();
		long end = System.currentTimeMillis() ;
		System.out.println("Construct Bipartite Graph succeed! spend:"+ (end-begin)/1000+"s");
//		graph.print();
		System.out.println("read oov words...");
	List<String> oovlist =  readoov();
	System.out.println("read oov words succeed!");
		System.out.println();
		HashMap<String ,Integer> dic = loaddic();
		while(true){
			System.out.println("input N:");
		      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		      String input = br.readLine() ;
		      if(input.equals("exit"))
		    	  break ;
		      int N = Integer.parseInt(input);
			int count_all = oovlist.size() ;
		    int count_part = 0;
		    int count_correct = 0 ;   
		    int unrecall = 0 ;
		for(String record:oovlist){
			String oov = record.split("\t")[0] ;
			oov = LetterExtraction.extractionx(oov);
			String correct = record.split("\t")[2] ;
			HashMap<String ,Integer> ivList = new HashMap<String,Integer>();
		for(int i = 0 ;i<200 ;i++){
			String iv = RandomWalk.normalizationA(oov ,graph);
			
			if(iv!="" &&( !iv.equals(oov))){
//				System.out.println(oov+">>"+iv);
				if(!ivList.containsKey(iv))
					ivList.put(iv,1) ;
				else if(ivList.containsKey(iv))
					ivList.put(iv,  ivList.get(iv)+1);
			}
		}
//		  System.out.println();
		  HashMap<String,Float> resultmap = new  HashMap<String,Float>();
		 
		  int count = 0 ;
		  Iterator it = ivList.entrySet().iterator() ;
		  while(it.hasNext()){
			  Map.Entry entry =( Map.Entry)it.next();
			  count+=(int)entry.getValue() ;
		  }
		  
		  Iterator iter = ivList.entrySet().iterator() ;
		  while(iter.hasNext()){
			  Map.Entry entry =( Map.Entry)iter.next();
			  String word = (String)entry.getKey() ;
			  int value =  (int)entry.getValue() ;
			  float context_similarity =(float)value/count ;
			  
			  float lexical_similarity = lexicalSimilarity(oov ,word);
			  
//			  float sound_similarity = soundSimilarity(oov,word);
//			  int max = oov.length()>word.length()?oov.length():word.length() ;
//			  float lexical_similarity = 1-(float)EditDistance.levenshtein(oov, word)/(float)max;
			  float similarity = (float)(context_similarity+lexical_similarity)/2 ;
//			  System.out.println(oov+" : " + word +"\t"+context_similarity +"\t"+lexical_similarity+"\t"+similarity );
			  if(dic.containsKey(word))
				  resultmap.put(word, similarity) ;
		  }
		  
		  class ValueComparator implements Comparator<Map.Entry<String,Float>>  
	        {  
	            public int compare(Map.Entry<String,Float> mp1, Map.Entry<String,Float> mp2)   
	            {  
	            	
	                return  ( (mp1.getValue()-mp2.getValue()==0)?0: (mp1.getValue()-mp2.getValue()<0)?1:-1) ;
	            }  
	        }  
	        List<Map.Entry<String,Float>> list=new ArrayList<>();  
	        list.addAll(resultmap.entrySet());  
	       ValueComparator vc=new ValueComparator();  
	        Collections.sort(list,vc);  
	      
	     Iterator   iterator  = list.iterator() ;
	     if(list.size()==0){
//	    	  System.out.println(oov +"("+correct+")");
	    	  unrecall+=1 ;
	      }
	     int counter =1;
	     int flag = -1 ;
	     
	     int find = count_correct;
	      while(iterator.hasNext())
	        {  
	    	  Map.Entry<String,Float> entry= (Map.Entry<String,Float>)iterator.next() ;
	    	  String key = entry.getKey() ;
	    	  float score = entry.getValue() ;
	    	  
	    	  		counter++ ;
	    		  if(key.equals(correct)){
	    			  count_correct++ ;
//	    			  System.out.println(oov +"("+correct+")"+":");
//	    			  System.out.println(key + "\t" +score);  
	    		  }
	    		  if(counter>1 &&flag<0){
	    			  count_part++ ;
	    			  flag = 1 ; 
	    		  }
	    		  if(counter>N)
	    			  break ;
	        }  
	   
//	      Iterator   iterx  = list.iterator() ;
//	      count = 0 ;
//	      System.out.println(oov+">"+correct+" : ");
//	      while(iterx.hasNext()&&count<=N){
//	    	  System.out.println(iterx.next());
//	    	  count++ ;
//	      }
//	      BufferedReader brx = new BufferedReader(new InputStreamReader(System.in));
//	      brx.readLine();
		}
			float precision = (float)count_correct/count_part ;
			float recall =  (float)count_correct/count_all ;
			float fscore = (float)(2*precision*recall)/(precision+recall);
			System.out.println("oov_count = "+oovlist.size());
			System.out.println("un_recall = " + unrecall) ;
			System.out.println("correct_count = " + count_correct) ;
			
			System.out.println("top+"+N+" : ");
			System.out.println("precision = " + precision);
			System.out.println("recall = " + recall);
			System.out.println("fscore = " + fscore);
			   
			
	}
	}
	
	
	
	public static float soundSimilarity(String str1 ,String str2) throws EncoderException{
		float similarity = 0 ;
		DaitchMokotoff phonet = new DaitchMokotoff();
		 String code1 = phonet.code(str1);
		 String code2 = phonet.code(str2);
		 float lcsr = LCS.getLCSR(code1, code2);
//		System.out.println(str1 +" : " + encode1);
//		System.out.println(str2 +" : " + encode2);
//		System.out.println(encode1.length());
//		System.out.println(encode2.length());
//		System.out.println(ds);
		int len1 = code1.length() ;
		int len2 = code2.length() ;
//		similarity  = lcsr/len1 ;
		
		
		return lcsr ;
	}
	
	public static List<String> readoov() throws IOException{
		List<String> oovlist = new ArrayList<String>();
		File file = new File("/home/songyajun/Java/dataset/规范化标准测试集/lexnorm_v1.2/corpus.v1.2.tweet");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = "" ;
		while((line = br.readLine())!=null){
			//sagittarius	IV	sagittarius
//			System.out.println(line);
			
			String[] temp = line.split("	");
//			System.out.println("temp_length = " + temp.length);
			if(temp.length==3){
				String type = temp[1];
//				System.out.println("type = " + type);
				if(type.equals("OOV")&&(!temp[0].equals(temp[2]))){
					System.out.println("line = "+line);
					oovlist.add(line);
				}
			}
		}
		return oovlist ;
	}
	
	public static float lexicalSimilarity(String str1 ,String str2){
		int editdistance = EditDistance.levenshtein(str1, str2);
		float LCSR = LCS.getLCSR(str1, str2);
		float lexicalSimilarity = (float)LCSR/editdistance ;
//		System.out.println("lexicalSimilarity = "+lexicalSimilarity);
		return  lexicalSimilarity ;
	}
	
	public static HashMap<String ,Integer> loaddic() throws IOException{
		HashMap<String ,Integer> oov_map = new HashMap<String ,Integer>();
		File file = new File("./mydic.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = "" ;
		while((line = br.readLine())!=null){
			if(line!="" && !oov_map.containsKey(line))
				oov_map.put(line, 1);
		}
		return oov_map;
	}
}
