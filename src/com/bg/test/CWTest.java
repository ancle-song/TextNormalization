package com.bg.test;

import java.io.BufferedReader;
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

import com.bg.paper.BiGraph;
import com.bg.paper.RandomWalk;

public class CWTest {
	
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
//		String datafile = "./data/CW_2013_tweets2009-06.txt" ;
		String datafile = "./data/new_CW_2013_tweets2009-06.txt" ;
		System.out.println("Construct Bipartite Graph...");
		long begin = System.currentTimeMillis() ;
		BiGraph graph = new BiGraph(datafile);
//		graph.saveOOV();
		long end = System.currentTimeMillis() ;
		System.out.println("Construct Bipartite Graph succeed! spend:"+ (end-begin)/1000+"s");
//		graph.print();
	
		while(true){
			HashMap<String ,Integer> ivList = new HashMap<String,Integer>();
			System.out.println("normalization begin,please input oov:");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String oov = br.readLine();
		for(int i = 0 ;i<100 ;i++){
			String iv = RandomWalk.normalization(oov ,graph);

			if(iv!=""){
//				System.out.println(oov+">>"+iv);
				if(!ivList.containsKey(iv))
					ivList.put(iv,1) ;
				else if(ivList.containsKey(iv))
					ivList.put(iv,  ivList.get(iv)+1);
			}
		}
		  System.out.println();
	        class ValueComparator implements Comparator<Map.Entry<String,Integer>>  
	        {  
	            public int compare(Map.Entry<String,Integer> map1, Map.Entry<String,Integer> map2)   
	            {  
	                return  (map2.getValue()-map1.getValue()) ;
	            }  
	        }  
	        
	        List<Map.Entry<String,Integer>> list=new ArrayList<Map.Entry<String,Integer>>();  
	        list.addAll(ivList.entrySet());  
	       ValueComparator compatator=new ValueComparator();  
	        Collections.sort(list,compatator);  
	        Iterator<Entry<String, Integer>> it = list.iterator() ;
	      while(it.hasNext())
	        {  
	            System.out.println(it.next());  
	        }  
		}
	}

}
