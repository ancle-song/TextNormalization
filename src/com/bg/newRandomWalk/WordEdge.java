package com.bg.newRandomWalk;

public class WordEdge {
	public String context ;
	public int type = -1 ;
	public static final int CW = 0;//context_word
	public static final int CC = 1;//context_context
	public float weight ;
	public float range1 = 0 ;
	public float range2 = 0 ;
	
	public WordEdge(String context ,float weight){
		this.context = context ;
		this.weight = weight ;
	}
	
	public WordEdge(String context,int type ,float weight){
		this.context = context ;
		this.type = type ;
		this.weight = weight ;
	}
	public WordEdge(){
		
	}

	public void setRange(float low ,float high){
		this.range1 = low ;
		this.range2 = high ;
	}
	
	public String getRange(){
		return range1+"~"+range2 ;
	}
	
	
}
