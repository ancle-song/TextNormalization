package com.bg.paper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bg.randomwalk.Edge;

public class WordNode {
	
	public String name ;
	
	public int type = -1;
	
	public static final int OOV = 0;
	
	public static final int IV = 1;
	
	List<WordEdge> Edgelist = null ;
	
	public WordNode(String name ,int type){
		
		this.name = name ;
		
		this.type = type ;
		
		Edgelist = new ArrayList<WordEdge>();
		
	}
	
	public void addedge(WordEdge edge){
		
		Edgelist.add(edge);
		
	}
	
	public List<WordEdge> getEdgelist() {
		
		return Edgelist;
		
	}

	public String getType(){
		
		String str = "" ;
		
		if(type==0)
			
			str =  "OOV" ;
		
		else if(type ==1)
			
			str =  "IV" ;
		
		return str ;
	}
	
	public void setRangeX(String context){
		
		float temp = 0;  //分子
		
		float denominator = 0;    //分母
		
		for(WordEdge edge:Edgelist){
			
//			System.out.println("edge.context = "+edge.context);
			
//			System.out.println("edge.weight = "+edge.weight);
			
			if(!edge.context.equals(context))
				
				denominator+= edge.weight ;
			
		}
//		System.out.println("denominator = "+denominator);
		
		for(WordEdge edge:Edgelist){
			
			if(edge.context.equals(context)){
				
				edge.range1 = -1 ;
				
				edge.range2 = -1 ;
				
			}
			
			else{
				
				edge.weight = edge.weight/denominator ;
				
				edge.setRange(temp,temp+edge.weight);
				
				temp = edge.range2 ;
				
			}
		}
	}
	
	public void setRange(){
		
		float temp = 0;  //分子
		
		float denominator = 0;    //分母
		
		for(WordEdge edge:Edgelist){
			
//			System.out.println("edge.context = "+edge.context);
			
//			System.out.println("edge.weight = "+edge.weight);
			
			denominator+= edge.weight ;
			
		}
//		System.out.println("denominator = "+denominator);
		
		for(WordEdge edge:Edgelist){
			
			edge.weight = edge.weight/denominator ;
			
			edge.setRange(temp,temp+edge.weight);
			
			temp = edge.range2 ;
			
		}
	}
	
	public WordEdge getByRandom(){
		
		Random rand=new Random();
		
		float p=rand.nextFloat();
		
		WordEdge e = new WordEdge() ;
		
//		System.out.println("random = " + p);
		
		for(WordEdge edge:Edgelist){
			
			if(p>edge.range1 && p<=edge.range2){
				
				e = edge ;
				
				break ;
			}	
		}
		return e ;
	}
}
