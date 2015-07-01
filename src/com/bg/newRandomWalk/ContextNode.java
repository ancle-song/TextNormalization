package com.bg.newRandomWalk;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class ContextNode {
	
	public String context ;
	public int type = -1 ;
	public static final int CW = 0;//context_word
	public static final int CC = 1;//context_context
	List<ContextEdge> Edgelist = null;
	
	public ContextNode(String context ){
		this.context = context ;
		Edgelist = new ArrayList<ContextEdge>();
	}
	
	public ContextNode(String context ,int type){
		this.context = context ;
		this.type = type ;
		Edgelist = new ArrayList<ContextEdge>();
	}
	
	public void addedge(ContextEdge edge){
		Edgelist.add(edge);
	}
	
	public List<ContextEdge> getEdgelist() {
		return Edgelist;
	}
	
	public void setRangeX(String oov){
		float temp = 0; 
		float denominator = 0;    //分母
//		System.out.println("context = " + context);
		if(Edgelist.size()<=1){
			for(ContextEdge edge:Edgelist){
//				System.out.println("edge.word = "+edge.name);
//				System.out.println("edge.weight = "+edge.weight);
				denominator+= edge.weight ;
			}
//			System.out.println("denominator = "+denominator);
			
			for(ContextEdge edge:Edgelist){
				edge.weight = edge.weight/denominator ;
				edge.setRange(temp,temp+edge.weight);
				temp = edge.range2 ;
			}
		}
		if(Edgelist.size()>1){
		for(ContextEdge edge:Edgelist){
//			System.out.println("edge.word = "+edge.name);
//			System.out.println("edge.weight = "+edge.weight);

				if(!edge.name.equals(oov))
					denominator+= edge.weight ;
		}
//		System.out.println("denominator = "+denominator);
		
		for(ContextEdge edge:Edgelist){
			
			if(edge.name.equals(oov)){
				edge.range1 = -1;
				edge.range2 =  -1 ;
			}
			else{	
				edge.weight = edge.weight/denominator ;
				edge.setRange(temp,temp+edge.weight);
				temp = edge.range2 ;
		}
		}
		}
	}
	
	public void setRange(){
		float temp = 0; 
		float denominator = 0;    //分母
//		System.out.println("context = " + context);
		for(ContextEdge edge:Edgelist){
//			System.out.println("edge.word = "+edge.name);
//			System.out.println("edge.weight = "+edge.weight);
			denominator+= edge.weight ;
		}
//		System.out.println("denominator = "+denominator);
		for(ContextEdge edge:Edgelist){
			edge.weight = edge.weight/denominator ;
//			System.out.println("edge.context = "+edge.name);
//			System.out.println("edge.weight = "+edge.weight);
//			System.out.println("range:"+temp+"-"+(temp+edge.weight));
			edge.setRange(temp,temp+edge.weight);
			temp = edge.range2 ;
		}
	}
	
	public ContextEdge getByRandom(){
		Random rand=new Random();
		float p=rand.nextFloat();
		ContextEdge e = new ContextEdge() ;
//		System.out.println("random = " + p);
		for(ContextEdge edge:Edgelist){
			if(p>edge.range1 && p<=edge.range2){
				e = edge ;
				break ;
			}	
		}
//		System.out.println(e.name);
		return e ;
	}
	

}
