package com.bg.paper;

public class WordEdge {
	
	public String context ;
	
	public float weight ;
	
	public float range1 = 0 ;
	
	public float range2 = 0 ;
	
	public WordEdge(String context ,float weight){
		
		this.context = context ;
		
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
