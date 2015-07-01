package com.bg.newRandomWalk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

public class BiGraph {
	
public HashMap<String ,WordNode> WordNodelist;
public HashMap<String ,ContextNode> ContextNodelist;
public HashMap<String,Integer> SingleContext ;//用来存储那些孤立的上下文
	
	public BiGraph() throws IOException{
		WordNodelist = new HashMap<String ,WordNode>();
		ContextNodelist =new HashMap<String,ContextNode>();
		SingleContext = new HashMap<String,Integer>();
//		loadDatax(datafile);
		loadCC("./data/CC_tweets2009-06.txt");
		loadCW("./data/CW_2013_tweets2009-06.txt");
	}
	
	public static void main(String[] args) throws IOException {
		BiGraph biGraph = new BiGraph();
	}
	
	//load Context_Context_map
		public void loadCC(String CC) throws IOException{
			System.out.println("load CC...");
			File file = new File(CC);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = "" ;
			int count  = 1 ;
			while((line = br.readLine())!=null){
				System.out.println(count+" : " + line);
				count++ ;
				String[] temp = line.split("\t") ;
				String context = temp[0];
//				System.out.println(context);
				SingleContext.put(context, 1);
				ContextNode contextNode = new ContextNode(context,1);
				for(int i = 1 ;i<temp.length ;i++){
//					System.out.println(temp[i]);
					String[] record = temp[i].split(" ");
					String c = record[0] ;
					for(int j = 1 ;j<4;j++)
						c+=" "+record[j] ;
					float weight = Float.parseFloat(record[4])*Float.parseFloat(record[5])*Float.parseFloat(record[6]);
					ContextEdge contextEdge = new ContextEdge(c ,weight);
					contextNode.addedge(contextEdge);	
				}
				ContextNodelist.put(context, contextNode);
			}
		}
	
		public void loadCW(String CW) throws IOException{
			System.out.println("load CW...");
			File file = new File(CW);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = "" ;
			int count = 1 ;
			while((line = br.readLine())!=null){
				System.out.println(count+" : " + line);
				count++ ;
				System.out.println(line);
				String[] temp = line.split("\t") ;
				String context = temp[0];
				ContextNode contextNode = new ContextNode(context,0);
				
					 List<String> wordlist = new ArrayList<String>();
					 //handle the contexts
					 for(int i = 1 ;i<temp.length ;i++){
						String[]record = temp[i].split(" ");
						String word = record[0];
						
						float weight =Float.parseFloat(record[1]);
						int type = -1 ;
						if(record[2].equals("oov"))
							type = 0;
						else if(record[2].equals("iv"))
							type = 1 ;
						
						if(!wordlist.contains(word)){
							ContextEdge contextEdge = new ContextEdge(word ,weight ,type);
							contextNode.addedge(contextEdge);
							WordNode wordNode = new WordNode(word ,type);
							WordEdge wordEdge = new WordEdge(context ,weight);
							wordNode.addedge(wordEdge);
							if(WordNodelist.containsKey(word)){
								WordNode tempNode =  WordNodelist.get(word);
								tempNode.addedge(wordEdge);
								WordNodelist.put(word, tempNode);
								tempNode = null ;
							}
							else WordNodelist.put(word, wordNode);
						}
						
						else if(wordlist.contains(word)){
								 for(ContextEdge edge:contextNode.getEdgelist()){
									if(edge.name.equals(word))
										edge.weight = edge.weight+weight ;
								}
								 WordNode tempNode = WordNodelist.get(word);
								for(WordEdge edge:tempNode.getEdgelist()){
									if(edge.context.equals(context))
										edge.weight +=weight ;
								}
							}
						wordlist.add(word);
					}
					 if(!SingleContext.containsKey(context))
					ContextNodelist.put(context, contextNode);
				
			}
			//setRange:setting  the random walk probably
			Set<Entry<String ,ContextNode>> entryset  = ContextNodelist.entrySet();
			for(Entry entry:entryset){
				ContextNode contextNode = (ContextNode)entry.getValue();
				contextNode.setRange();
			}
			
			Set<Entry<String ,WordNode>> entrysetx  = WordNodelist.entrySet();
			for(Entry entry:entrysetx){
				WordNode wordNode = (WordNode)entry.getValue();
				wordNode.setRange();
			}
		} 
		
	//load Context_Context_map
	public void loaddata(String datafile) throws IOException{
		System.out.println("load data...");
		File file = new File(datafile);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line = "" ;
		while((line = br.readLine())!=null){
			System.out.println(line);
			String[] temp = line.split("\t") ;
			String context = temp[0];
			ContextNode contextNode = new ContextNode(context,1);
			List<String> wordlist = new ArrayList<String>();
			//handle the contexts
			for(int i = 1 ;i<temp.length ;i++){
				String[]record = temp[i].split(" ");
				String word = record[0];
				
				float weight =Float.parseFloat(record[1]);
				int type = -1 ;
				if(record[2].equals("oov"))
					type = 0;
				else if(record[2].equals("iv"))
					type = 1 ;
				
				if(!wordlist.contains(word)){
					ContextEdge contextEdge = new ContextEdge(word ,weight ,type);
					contextNode.addedge(contextEdge);
					WordNode wordNode = new WordNode(word ,type);
					WordEdge wordEdge = new WordEdge(context ,weight);
					wordNode.addedge(wordEdge);
					if(WordNodelist.containsKey(word)){
						WordNode tempNode =  WordNodelist.get(word);
						tempNode.addedge(wordEdge);
						WordNodelist.put(word, tempNode);
						tempNode = null ;
					}
					else WordNodelist.put(word, wordNode);
				}
				
				else if(wordlist.contains(word)){
						 for(ContextEdge edge:contextNode.getEdgelist()){
							if(edge.name.equals(word))
								edge.weight = edge.weight+weight ;
						}
						 WordNode tempNode = WordNodelist.get(word);
						for(WordEdge edge:tempNode.getEdgelist()){
							if(edge.context.equals(context))
								edge.weight +=weight ;
						}
					}
				wordlist.add(word);
			}
//			contextNode.setRange();
			ContextNodelist.put(context, contextNode);
		}
		//setRange:setting  the random walk probably
		Set<Entry<String ,ContextNode>> entryset  = ContextNodelist.entrySet();
		for(Entry entry:entryset){
			ContextNode contextNode = (ContextNode)entry.getValue();
			contextNode.setRange();
		}
		
		Set<Entry<String ,WordNode>> entrysetx  = WordNodelist.entrySet();
		for(Entry entry:entrysetx){
			WordNode wordNode = (WordNode)entry.getValue();
			wordNode.setRange();
		}
	}
	
	//load Context_word_map
	public void loadDatax(String datafile) throws IOException{
		File file = new File(datafile);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line = "" ;
		while((line = br.readLine())!=null){
			String[] temp = line.split("\t") ;
			String context = temp[0];
			ContextNode contextNode = new ContextNode(context);
			List<String> wordlist = new ArrayList<String>();
			//handle the words
			for(int i = 1 ;i<temp.length ;i++){
				String[]record = temp[i].split(" ");
				String word = record[0];
				
				float weight =Float.parseFloat(record[1]);
				int type = -1 ;
				if(record[2].equals("oov"))
					type = 0;
				else if(record[2].equals("iv"))
					type = 1 ;
				
				if(!wordlist.contains(word)){
					ContextEdge contextEdge = new ContextEdge(word ,weight ,type);
					contextNode.addedge(contextEdge);
					WordNode wordNode = new WordNode(word ,type);
					WordEdge wordEdge = new WordEdge(context ,weight);
					wordNode.addedge(wordEdge);
					if(WordNodelist.containsKey(word)){
						WordNode tempNode =  WordNodelist.get(word);
						tempNode.addedge(wordEdge);
						WordNodelist.put(word, tempNode);
						tempNode = null ;
					}
					else WordNodelist.put(word, wordNode);
				}
				
				else if(wordlist.contains(word)){
						 for(ContextEdge edge:contextNode.getEdgelist()){
							if(edge.name.equals(word))
								edge.weight = edge.weight+weight ;
						}
						 WordNode tempNode = WordNodelist.get(word);
						for(WordEdge edge:tempNode.getEdgelist()){
							if(edge.context.equals(context))
								edge.weight +=weight ;
						}
					}
				wordlist.add(word);
			}
//			contextNode.setRange();
			ContextNodelist.put(context, contextNode);
		}
		//setRange:setting  the random walk probably
		Set<Entry<String ,ContextNode>> entryset  = ContextNodelist.entrySet();
		for(Entry entry:entryset){
			ContextNode contextNode = (ContextNode)entry.getValue();
			contextNode.setRange();
		}
		
		Set<Entry<String ,WordNode>> entrysetx  = WordNodelist.entrySet();
		for(Entry entry:entrysetx){
			WordNode wordNode = (WordNode)entry.getValue();
			wordNode.setRange();
		}
	}

	public void print(){
//		System.out.println("print WordNode:");
//		Set<Entry<String ,WordNode>> entryset  = WordNodelist.entrySet();
//		for(Entry entry:entryset){
//			WordNode wordNode = (WordNode)entry.getValue() ;
//			System.out.println(wordNode.name+"\t"+wordNode.getType());
//			List<WordEdge> list = wordNode.getEdgelist();
//			 	for(WordEdge edge :list){
//			 		System.out.println(edge.context +"\t"+edge.weight +"\t"+edge.getRange());
//			 	}
//		}
		System.out.println("size = "+ContextNodelist.size());
		System.out.println("\nprint ContextNode:");
		Set<Entry<String ,ContextNode>> entrysetx  = ContextNodelist.entrySet();
		for(Entry entry:entrysetx){
			
			ContextNode contextNode = (ContextNode)entry.getValue();
			List<ContextEdge> list = contextNode.getEdgelist();
			System.out.println(entry.getKey());
			for(ContextEdge edge:list)
				System.out.println(edge.name +"\t"+edge.weight+"\t"+edge.type +"\t"+edge.getRange());
		}
		
		
	}

}
