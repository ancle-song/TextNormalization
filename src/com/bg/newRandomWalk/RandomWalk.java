package com.bg.newRandomWalk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.bg.randomwalk.Edge;
import com.bg.randomwalk.Vertex;



public class RandomWalk {
public static int MAXSTEP = 4;
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("Construct Bipartite Graph...");
		BiGraph graph = new BiGraph();
		System.out.println("Construct Bipartite Graph succeed!");
//		graph.print();
		while(true){
			System.out.println("normalization begin,please input oov:");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String oov = br.readLine();
			String iv = normalization(oov ,graph);
			System.out.println(oov+">>"+iv);
		}
	}
	
	public static String normalization(String oov ,BiGraph graph){
	
		String iv = "" ;
		int step = 0;
		while(true){
			System.out.println("oov = " + oov);
			WordNode wordNode = graph.WordNodelist.get(oov);
			 
			if(wordNode!=null){
					WordEdge wordEdge =wordNode.getByRandom();
					String context = wordEdge.context ;
//					System.out.println((step+1) + ":" +wordEdge.context);
					ContextNode contextNode = graph.ContextNodelist.get(context);
					if(contextNode==null)
						continue ;
//					System.out.println("context_type = " + contextNode.type);
					if(contextNode.type==1){
						String c = contextNode.getByRandom().name ;
						ContextNode contextNodex = graph.ContextNodelist.get(c);
						if(contextNodex==null)
							continue ;
						contextNodex.setRangeX(oov);
						ContextEdge contextEdge = contextNodex.getByRandom();
						if(contextEdge.type==1){
							iv = contextEdge.name ;
							step++ ;
							break ;
						}
						else if(contextEdge.type==0 && step<=MAXSTEP){
									String temp = contextEdge.name ;
									if(!temp.equals(oov))
										System.out.println("oov = " + oov +"\t"+"temp = " + temp);
										oov = temp ;
									step++ ;
									continue ;
								
								}
				else if(contextEdge.type==0 && step>MAXSTEP){
									iv = "" ;
									break ;
					}
				}
					
				else if(contextNode.type==0){
							contextNode.setRangeX(oov);
							ContextEdge contextEdge = contextNode.getByRandom();
							if(contextEdge.type==1){
								iv = contextEdge.name ;
								break ;
							}
							else if(contextEdge.type==0 && step<=MAXSTEP){
										String temp = contextEdge.name ;
										if(!temp.equals(oov))
											System.out.println("oov = " + oov +"\t"+"temp = " + temp);
											oov = temp ;
										step++ ;
										continue ;
										
							}
							else if(contextEdge.type==0 && step>MAXSTEP){
								iv = "" ;
								break ;
							}
						}
				}
		else {
			//				System.out.println("No OOV in OOV wordNode!");
					break ;
				}
			}	
		
		if(iv=="" || iv.equals(oov))
			return "" ;
		
		else 
			return iv;
	}
}
