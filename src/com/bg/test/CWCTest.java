package com.bg.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.bg.newRandomWalk.BiGraph;
import com.bg.newRandomWalk.RandomWalk;

public class CWCTest {
public static void main(String[] args) throws IOException {
		
		System.out.println("Construct Bipartite Graph...");
		BiGraph graph = new BiGraph();
		System.out.println("Construct Bipartite Graph succeed!");
//		graph.print();
		while(true){
			System.out.println("normalization begin,please input oov:");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String oov = br.readLine();
			String iv = RandomWalk.normalization(oov ,graph);
			System.out.println(oov+">>"+iv);
		}
	}
}
