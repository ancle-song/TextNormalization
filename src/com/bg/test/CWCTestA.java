package com.bg.test;

import java.io.BufferedReader;
import java.io.File;
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
import java.util.concurrent.ExecutionException;

import com.bg.newRandomWalk.BiGraph;
import com.bg.newRandomWalk.RandomWalk;
import com.bg.until.EditDistance;
import com.bg.until.LCS;
import com.bg.until.LetterExtraction;

public class CWCTestA {
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
	
		System.out.println("Construct Bipartite Graph...");
		long begin = System.currentTimeMillis() ;
		BiGraph graph = new BiGraph();
//		graph.saveOOV();
		long end = System.currentTimeMillis() ;
		System.out.println("Construct Bipartite Graph succeed! spend:"+ (end-begin)/1000+"s");
//		graph.print();
		System.out.println("read oov words...");
	List<String> oovlist =  readoov();
	System.out.println("read oov words succeed!");
		System.out.println();
		
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
		for(int i = 0 ;i<100 ;i++){
			String iv = RandomWalk.normalization(oov ,graph);
			
//			System.out.println("temp= " +temp);
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
//			  float lexical_similarity = lexicalSimilarity(oov ,word);
			  int max = oov.length()>word.length()?oov.length():word.length() ;
			  float lexical_similarity = 1-(float)EditDistance.levenshtein(oov, word)/(float)max;
			  float similarity = (float)(context_similarity+lexical_similarity)/2 ;
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
	      
//	      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//	      br.readLine();
		}
			float precision = (float)count_correct/count_part ;
			float recall =  (float)count_correct/count_all ;
			float fscore = (float)(2*precision*recall)/(precision+recall);
			System.out.println("oov_count = "+oovlist.size());
			System.out.println("un_recall = " + unrecall) ;
			System.out.println("count_part = " + count_part) ;
			System.out.println("correct_count = " + count_correct) ;
			
			System.out.println("top+"+N+" : ");
			System.out.println("precision = " + precision);
			System.out.println("recall = " + recall);
			System.out.println("fscore = " + fscore);
	}
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
}
