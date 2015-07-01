package com.bg.paper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import com.bg.until.Until;

//Bipartite Graph representation of words and it`s contexts
public class BiGraph {
	
public HashMap<String ,WordNode> WordNodelist;//word node 

public HashMap<String ,ContextNode> ContextNodelist;//context node

public List<String> oovList = new ArrayList<String>();//oov = out of vocabulary word

public String oovFile = "./CW_oovlist.txt" ;	

	public BiGraph(String datafile) throws IOException{
		
		WordNodelist = new HashMap<String ,WordNode>();
		
		ContextNodelist =new HashMap<String,ContextNode>();
		
		loadData(datafile);
	}
	
	//load data from file and construct the graph
	public void loadData(String datafile) throws IOException{
		
		File file = new File(datafile);
		
		FileReader fr = new FileReader(file);
		
		BufferedReader br = new BufferedReader(fr);
		
		String line = "" ;
		
		int count = 1;
		
		while((line = br.readLine())!=null){
			
			System.out.println(count+" : "+line);
			
			count++ ;
			
			//line ="that is what i	makin 121 oov	making 331 iv	makinng 38 oov	mking 75 oov	taking 290 iv	getting 163 iv"
			String[] temp = line.split("\t") ;
			
			String context = temp[0];
			
			ContextNode contextNode = new ContextNode(context);
			
			HashMap<String,Integer> wordlist = new HashMap<String,Integer>();
			
			for(int i = 1 ;i<temp.length ;i++){
				
				String[]record = temp[i].split(" ");
				
				String word = record[0];
				
				float weight =Float.parseFloat(record[1]);
				
				int type = -1 ;//word type(iv , oov)
				
				if(record[2].equals("oov"))
					type = 0;
				else if(record[2].equals("iv"))
					type = 1 ;
				
				if(!wordlist.containsKey(word)){
					
					ContextEdge contextEdge = new ContextEdge(word ,weight ,type);
					
					contextNode.addedge(contextEdge);
					
					WordNode wordNode = new WordNode(word ,type);
					
					WordEdge wordEdge = new WordEdge(context ,weight);
					
					wordNode.addedge(wordEdge);
					
					if(WordNodelist.containsKey(word)){
						
						WordNode tempNode =  WordNodelist.get(word);
						
						tempNode.addedge(wordEdge);
						
						WordNodelist.put(word, tempNode);
						
					}
					
					else WordNodelist.put(word, wordNode);
					
				}
				
				else if(wordlist.containsKey(word)){
					
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
				wordlist.put(word, 1);
			}
			ContextNodelist.put(context, contextNode);
		}
		
		Set<Entry<String ,ContextNode>> entryset  = ContextNodelist.entrySet();
		
		for(Entry entry:entryset){
			
			ContextNode contextNode = (ContextNode)entry.getValue();
			
			
			contextNode.setRange();//
		}
		
		Set<Entry<String ,WordNode>> entrysetx  = WordNodelist.entrySet();
		
		for(Entry entry:entrysetx){
			
			WordNode wordNode = (WordNode)entry.getValue();
			
			if(wordNode.type ==0)
				
				oovList.add(wordNode.name);
			
			wordNode.setRange();
		}
	}
	
	public void saveOOV() throws IOException, InterruptedException, ExecutionException{
		
		File file = new File(oovFile);
		
		if(!file.exists()){
			
			Until.save_list(oovFile, oovList);
			
		}
		
	}
	
	public void loadDatax(String datafile) throws IOException{
		
	 	RandomAccessFile rafi = new RandomAccessFile(datafile, "r");  
	 	
	 	byte[] buf = new byte[ 500000000];  
	 	
	 	long start = System.currentTimeMillis();  
	 	
	 	int c = rafi.read(buf); 
	 	
	 	rafi.close();
	 	
	 	String content = new String(buf);
	 	
		String[] lines = content.split("\n") ;

		for(String line:lines){
			
			System.out.println(line);
			
			String[] temp = line.split("\t") ;
			
			String context = temp[0];
			
			ContextNode contextNode = new ContextNode(context);
			
			List<String> wordlist = new ArrayList<String>();
			
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
						
//						tempNode = null ;
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
			
			ContextNodelist.put(context, contextNode);
			
		}
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
		
		lines = null ;
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
