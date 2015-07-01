package com.bg.paper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.bg.randomwalk.Edge;
import com.bg.randomwalk.Vertex;



public class RandomWalk {
	
public static int MAXSTEP = 4;
	
	public static void main(String[] args) throws IOException {
		
//		String datafile = "./data/CW_2013_tweets2009-06.txt" ;
		
		String datafile = "./data/CW_2013_tweets2009-06.txt" ;
		
		System.out.println("Construct Bipartite Graph...");
		
		long begin = System.currentTimeMillis() ;
		
		BiGraph graph = new BiGraph(datafile);
		
		long end = System.currentTimeMillis() ;
		
		System.out.println("Construct Bipartite Graph succeed! spend:"+ (end-begin)/1000+"s");
		
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
		
		int flag = -1 ;
		
		while(step<MAXSTEP){
			
				if(flag==1)
					
					break ;

				WordNode wordNode = graph.WordNodelist.get(oov);
				
				if(wordNode!=null){
						
					WordEdge wordEdge =wordNode.getByRandom();
					
					String context = wordEdge.context ;
	
					//	System.out.println((step+1) + ":" +wordEdge.context);
					
					step ++ ;
	
					//	System.out.println("step = " + step);
					
					ContextNode contextNode = graph.ContextNodelist.get(context);
					
					if(contextNode==null ){
						
	//					System.out.println("contextNode = = null");
						
						continue ;
						
					}
					
					else if(contextNode!=null){
						
								int count = 0 ;
								
								while(true){
									
										ContextEdge contextEdge = contextNode.getByRandom();
										
										count++ ;
										
										if(count>20){
											
											iv="" ;
											
											flag = 1;
											
											break ;
											
										}
										
										if(contextEdge.type==1){
											
											iv = contextEdge.name ;
											
											flag = 1 ;
											
											break ;
											
										}
										else if(contextEdge.type==0 && step<=MAXSTEP){
											
											if(contextNode.Edgelist.size()==1){
												
												iv = "" ;
												
												flag = 1 ;		
												
												break ;
												
											}
											
											else if(contextNode.Edgelist.size()>1){
												
													String temp = contextEdge.name ;
													
													if(temp.equals(oov))
														
														continue ;
													
													else{
														
														oov = temp ;
														
//													System.out.println("oov = " + oov +"	"+contextNode.Edgelist.size());
														
//													for(ContextEdge edge:contextNode.Edgelist)
														
//														System.out.print(edge.name+"    ");
														
//													System.out.println();
														
													break ;
													
													}
											}
										}
										
										else if(contextEdge.type==0 && step>MAXSTEP){
											iv = "" ;
											
											break ;
											
										}
									}
							}
					}
				
				else if(wordNode==null){
					
//					System.out.println("No OOV in OOV wordNode!");
					
					break ;
					
				}
		}
		
		if(iv=="" || iv.equals(oov))
			
			return "" ;
		
		else 
			
			return iv ;
	}
	
	
	public static String normalizationA(String oov ,BiGraph graph){
		
		String iv = "" ;
		
		int step = 0;
		
		while(true){
			
				WordNode wordNode = graph.WordNodelist.get(oov);
				
				if(wordNode!=null){
				
				WordEdge wordEdge =wordNode.getByRandom();
				
				String context = wordEdge.context ;

				ContextNode contextNode = graph.ContextNodelist.get(context);
				
				if(contextNode==null)
					
					continue ;
				
				contextNode.setRangeX(oov);
				
				ContextEdge contextEdge = contextNode.getByRandom();
				
				if(contextEdge.type==1){
					
					iv = contextEdge.name ;
					
					break ;
					
				}
				
				else if(contextEdge.type==0 && step<=MAXSTEP){
					
					String temp = contextEdge.name ;
					
					if(!temp.equals(oov))
						
//					System.out.println("oov = " + oov +"\t"+"temp = " + temp);
						
					oov = temp ;
					
//					WordNode wordNodex = graph.WordNodelist.get(oov);
					
//					wordNodex.setRangeX(context);
					
					step ++ ;
					
				}
				
				else if(contextEdge.type==0 && step>MAXSTEP){
					
					iv = "" ;
					
					break ;
					
				}
				
			}
				
				else {
					
//					System.out.println("No OOV in OOV wordNode!");
					
					break ;
				
				}
		}
		
		if(iv=="" || iv.equals(oov))
			
			return "" ;
		
		else 
			
			return iv ;
	}
}
