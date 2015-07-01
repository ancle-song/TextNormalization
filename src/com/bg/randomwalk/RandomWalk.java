package com.bg.randomwalk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import com.bg.until.EditDistance;
import com.bg.until.LCS;


public class RandomWalk {
	
	public static int MAXSTEP = 4;
	
	public static void main(String[] args) throws IOException {
		String datafile = "./data/tweets2009-06_5gram_word2word.txt" ;
		System.out.println("Construct Bipartite Graph...");
		long begin = System.currentTimeMillis() ;
		BiGraph graph = new BiGraph(datafile);
		long end = System.currentTimeMillis() ;
		System.out.println("Construct Bipartite Graph succeed! spend:"+ (end-begin)/1000+"s");
//		graph.print();
		while(true){
			System.out.println("normalization begin,please input oov:");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String oov = br.readLine();
			String iv = normalization(oov ,graph);
			System.out.println(oov+">>"+iv);
		}
	}
	
	public static String normalization(String oov ,BiGraph graph){
		String x = oov ;  
		String iv ="" ;
		int step = 0;
		while(true){
			Vertex vertex = graph.vertexs.get(oov);
			if(vertex!=null){
				vertex.setRangeX(oov);
			Edge edge = vertex.getByRandom();
			
//			System.out.println((step+1) + ":" +edge.name);
			
			if(edge.type==1){
				iv = edge.name ;
				break ;
			}
			else if(edge.type==0 && step<=MAXSTEP){
				oov = edge.name ;
				step ++ ;
			}
			else if(edge.type==0 && step>MAXSTEP){
				iv = "" ;
				break ;
			}
			}
			else{
//				System.out.println("No OOV in OOV wordNode!");
				break ;
			}
		}
		float similarity =  lexicalSimilarity(x ,iv);
//		if(similarity>0)
//			System.out.println(x+" : "+iv+"\t"+similarity);
//		if(similarity>0.1)
//			return iv ;
//		else return "" ;
		return iv ;
	}
	
	public static float lexicalSimilarity(String str1 ,String str2){
		int editdistance = EditDistance.levenshtein(str1, str2);
		float LCSR = LCS.getLCSR(str1, str2);
		
		float lexicalSimilarity = (float)LCSR/editdistance ;
//		System.out.println(str1+" : " + str2 +"\t" + LCSR+"\t" +editdistance+"\t" + lexicalSimilarity);
//		System.out.println("lexicalSimilarity = "+lexicalSimilarity);
		return  lexicalSimilarity ;
	}

}
