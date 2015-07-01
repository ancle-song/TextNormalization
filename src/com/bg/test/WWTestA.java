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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.RefinedSoundex;

import com.bg.randomwalk.BiGraph;
import com.bg.randomwalk.RandomWalk;
import com.bg.until.EditDistance;
import com.bg.until.LCS;
import com.bg.until.LetterExtraction;
import com.bg.until.Until;


public class WWTestA {
	
	public static String[] content = new String[549] ;
	public static HashMap<String,Integer> oov_map = new HashMap<String,Integer>();
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException, EncoderException {
		String datafile = "./data/5gram250_word2wordx.txt" ;
	//String datafile = "./data/5gram_word2word.txt" ;

		System.out.println("Construct Bipartite Graph...");
		long begin = System.currentTimeMillis() ;
		BiGraph graph = new BiGraph(datafile);
//		graph.saveOOV();
		long end = System.currentTimeMillis() ;
		System.out.println("Construct Bipartite Graph succeed! spend:"+ (end-begin)/1000+"s");
//		graph.print();
		System.out.println("read oov words...");
		HashMap<String,HashMap<String,Float>> word_map = new HashMap<String,HashMap<String,Float>>();
		HashMap<String ,Integer> dic = loaddic();
	List<String> oovlist =  readoovx();
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

		for(String record:oovlist){
			String oov = record.split(" ")[0] ;
			oov = LetterExtraction.extractionx(oov);
			String correct = record.split(" ")[2] ;
			HashMap<String ,Integer> ivList = new HashMap<String,Integer>();

		for(int i = 0 ;i<1000 ;i++){
			String iv = RandomWalk.normalization(oov ,graph);
			if(iv!="" &&( !iv.equals(oov))&&dic.containsKey(iv)){
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
			  float sound_similarity = soundSimilarity(oov ,word);
			  float editSimilarity =1-(float) EditDistance.levenshtein(oov, word)/oov.length()>word.length()?oov.length():word.length() ;
			  float similarity = (float)(context_similarity+lexical_similarity+sound_similarity)/2 ;
			  float lcsSimilarity = (float)LCS.getLCSR(oov, word)/oov.length()<word.length()?oov.length():word.length() ;
			  float similarity1 = (float)(context_similarity+editSimilarity)/2 ;
			  if(dic.containsKey(word)&& editSimilarity>0.5)
				  resultmap.put(word,similarity) ;
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
	     
	     int counter =1;
	     int flag = -1 ;
	     int find = count_correct ;
	     int iv_count = 1 ;
	     if(!word_map.containsKey(record)){
	    	 word_map.put(record, new HashMap<String,Float>()) ;
	    	 HashMap<String,Float> map = word_map.get(record) ;
//	    	 map.put(oov, (float) 0);
//	    	 word_map.put(record, map);
	     }
	      while(iterator.hasNext())
	        {  
	    	  Map.Entry<String,Float> entry= (Map.Entry<String,Float>)iterator.next() ;
	    	  String key = entry.getKey() ;
	    	  float score = entry.getValue() ;
	    	  		counter++ ;
	    	  		HashMap<String,Float> map = word_map.get(record) ;
	    	  		if(iv_count<N&&!map.containsKey(key)){
	    	  			map.put(key, score);
	    	  			word_map.put(record, map) ;
	    	  			count++ ;
	    	  		}
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
//	      if(find == count_correct)
//	    	  System.out.println(oov +"("+correct+")");
//	      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//	      br.readLine();
		}
			float precision = (float)count_correct/count_part ;
			float recall =  (float)count_correct/count_all ;
			float fscore = (float)(2*precision*recall)/(precision+recall);
			System.out.println("count_all = "+count_all);
			System.out.println("count_part = "+count_part);
			System.out.println("count_correct = "+count_correct);
			System.out.println("top"+N+":");
			System.out.println("precision = " + precision);
			System.out.println("recall = " + recall);
			System.out.println("fscore = " + fscore);
			
			Set<Map.Entry<String,HashMap<String,Float>>> entrySet =  word_map.entrySet();
			for(Map.Entry<String,HashMap<String,Float>> entry:entrySet){
				String key = entry.getKey();
				HashMap<String,Float> map = entry.getValue() ;
				   List<Map.Entry<String,Float>> list=new ArrayList<>();  
				   class ValueComparator implements Comparator<Map.Entry<String,Float>>  
			        {  
			            public int compare(Map.Entry<String,Float> mp1, Map.Entry<String,Float> mp2)   
			            {  
			            	
			                return  ( (mp1.getValue()-mp2.getValue()==0)?0: (mp1.getValue()-mp2.getValue()<0)?1:-1) ;
			            }  
			        }  
			        list.addAll(map.entrySet());  
			       ValueComparator vc=new ValueComparator();  
			        Collections.sort(list,vc);  
				String str =key ;
				for(Map.Entry<String, Float> iv:list)
					str+="\t"+iv.getKey()+" : "+iv.getValue() ;
//					System.out.println(str);

			}
			

			System.out.println("construct lattice:");
			HashMap<Integer ,List<String>>  lattice = new HashMap<Integer ,List<String>>();
			
			 for(int i = 0;i<content.length ;i++){
				 lattice.put(i, new ArrayList<String>()) ;
				 String str = content[i] ;
				 String[] temp = content[i].split("\t");
				 for(int j = 0;j<temp.length ;j++){
					 if(word_map.containsKey(temp[j])){
						 Set<String> iv_list = word_map.get(temp[j]).keySet() ;
						 for(String word:iv_list){
							 String path = str.replace(temp[j], word) ;
							 List<String> list = lattice.get(i);
							 if(!path.contains(" OOV ")){
							 list.add(path.replaceAll("\t", " ")) ;
							 lattice.put(i, list) ;
							 }
						 }
					 }
				 }
			 }
			 
			 System.out.println("print lattice:");
			 int count = 1 ;
			 Set<Map.Entry<Integer, List<String>>> resultSet = lattice.entrySet() ;
			 for(Map.Entry<Integer, List<String>> entry:resultSet){
				 int key = entry.getKey() ;
				 List<String> list = entry.getValue() ;
				
				 if(list.size()>0){
//					 System.out.println("key = "+key);
					 String lines = content[key] ;
				 for(String str:list){
					 lines+="\n"+str ;
//					 System.out.println(count+" : " +str);
					 count++ ;
				 }
//				 Until.save_str("./lattice_path.txt", lines+"\n\n");
				 }
			 }

	}
		
	}
	
	
	public static List<String> readoov() throws IOException{
		
		List<String> oovlist = new ArrayList<String>();
		HashMap<String,String> word_context_map = new HashMap<String,String>();
		File file = new File("/home/songyajun/Java/dataset/规范化标准测试集/lexnorm_v1.2/corpus.v1.2.tweet");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = "" ;
		int count = 0;
//		String[] content = new String[] ;
		while((line = br.readLine())!=null){

			String[] temp = line.split("	");
//			System.out.println("temp_length = " + temp.length);
			if(temp.length!=3){
				count++ ;
//				System.out.println(count+" : " + line);
				continue ;
			}
			if(temp.length==3){
				String type = temp[1];
//				System.out.println("type = " + type);
				if(type.equals("OOV")&&(!temp[0].equals(temp[2]))){
//					System.out.println("line = "+line);
					oovlist.add(line);
				}
			}
		}
		return oovlist ;
	}
	public static List<String> readoovx() throws IOException{
		List<String> oovlist = new ArrayList<String>();
		HashMap<String,String> word_context_map = new HashMap<String,String>();
		
		File file = new File("/home/songyajun/Java/dataset/规范化标准测试集/lexnorm_v1.2/corpus.v1.2.tweet");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = "" ;
		int count = -1;
		
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

	public static float lexicalSimilarity(String str1 ,String str2){
		int editdistance = EditDistance.levenshtein(str1, str2);
		float LCSR = LCS.getLCSR(str1, str2);
		float lexicalSimilarity = (float)LCSR/editdistance ;
//		System.out.println("lexicalSimilarity = "+lexicalSimilarity);
		return  lexicalSimilarity ;
	}
	
	public static float soundSimilarity(String str1 ,String str2) throws EncoderException{
		float similarity = 0 ;
		RefinedSoundex rs = new RefinedSoundex();
		int ds = rs.difference(str1, str2) ;//return the number of characters in two strings are the same
		String encode1 = rs.encode(str1);
		String encode2 = rs.encode(str2);
//		System.out.println(str1 +" : " + encode1);
//		System.out.println(str2 +" : " + encode2);
//		System.out.println(encode1.length());
//		System.out.println(encode2.length());
//		System.out.println(ds);
		int len1 = encode1.length() ;
		int len2 = encode2.length() ;
		similarity  = (float)ds/(len1>len2?len1:len2) ;
		
		return similarity ;
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
