package com.bg.paper;

import java.util.ArrayList;
import java.util.List;

public class HittingNode {
	public String word ;
	public int count = 0 ;
	public List<Integer> stepCount = new ArrayList<Integer>();

	public HittingNode(String word){
		this.word = word ;
	}
	
	public void add(int step){
		stepCount.add(step) ;
		count++ ;
	}
	public float score(){
		int sum = 0;
		for (int step:stepCount){
			sum+=step ;
		}
		return  (float)count/(float)sum ;
	}
}
