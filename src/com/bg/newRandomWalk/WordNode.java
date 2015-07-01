package com.bg.newRandomWalk;

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
	
	public void setRange(){
		float temp = 0;  //分子
		float denominator = 0;    //分母
//		System.out.println("word = " + name);
		for(WordEdge edge:Edgelist){

			denominator+= edge.weight ;
		}
//		System.out.println("denominator = "+denominator);
		for(WordEdge edge:Edgelist){
			edge.weight = edge.weight/denominator ;
//			System.out.println("edge.context = "+edge.context);
//			System.out.println("edge.weight = "+edge.weight);
//			System.out.println("range:"+temp+"-"+(temp+edge.weight));
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
//		System.out.println(e.context);
		return e ;
	}
	

}
