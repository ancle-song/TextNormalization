package com.bg.randomwalk;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Vertex {
	public String name ;
	public int type = -1;
	public static final int OOV = 0;
	public static final int IV = 1;
	List<Edge> Edgelist = null ;
	
	public Vertex(String name ,int type){
		this.name = name ;
		this.type = type ;
		Edgelist = new ArrayList<Edge>();
	}
	
	public void addedge(Edge edge){
		Edgelist.add(edge);
	}
	
	public List<Edge> getEdgelist() {
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
	
	public void setRangeX(String oov){
		float temp = 0;  //分子
		float denominator = 0;    //分母
		
		for(Edge edge:Edgelist){
			if(!edge.name.equals(oov))
				denominator+= edge.weight ;
			
				
		}
		for(Edge edge:Edgelist){
			 if(edge.name.equals(oov)){
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
		
		for(Edge edge:Edgelist)
			denominator+= edge.weight ;
		for(Edge edge:Edgelist){
			edge.weight = edge.weight/denominator ;
			edge.setRange(temp,temp+edge.weight);
			temp = edge.range2 ;
		}
	}
	public Edge getByRandom(){
		Random rand=new Random();
		float p=rand.nextFloat();
		Edge e = new Edge() ;
//		System.out.println("random = " + p);
		for(Edge edge:Edgelist){
			if(p>edge.range1 && p<=edge.range2){
				e = edge ;
				break ;
			}	
		}
		return e ;
	}
	
	
}
