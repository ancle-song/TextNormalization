package com.bg.randomwalk;

public class Edge {
	public String name ;
	public int type = -1 ;
	public static final int OOV = 0;
	public static final int IV = 1;
	public float weight ;
	public float range1 = 0 ;
	public float range2 = 0 ;
	
	public Edge(String name ,float weight ,int type){
		this.name = name ;
		this.weight = weight ;
		this.type = type ;
	}
	
	public Edge(){
		
	}
	
	public String getType(){
		String str = "" ;
		if(type==0)
			str =  "OOV" ;
		else if(type ==1)
			str =  "IV" ;
		
		return str ;
	}
	public void setRange(float low ,float high){
		this.range1 = low ;
		this.range2 = high ;
	}
	
	public String getRange(){
		return range1+"~"+range2 ;
	}
	
	
}
