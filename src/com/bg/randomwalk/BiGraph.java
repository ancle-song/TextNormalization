package com.bg.randomwalk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.Set;

import com.bg.until.Until;

public class BiGraph {
	public HashMap<String ,Vertex> vertexs ;
	public List<String> oovList = new ArrayList<String>();
	
	public String oovFile = "./WW_oovlist.txt" ;	
	
	public BiGraph(String datafile) throws IOException{
	
		vertexs = new HashMap<String ,Vertex>();
		loadData(datafile);
	}
	
	public void loadData(String datafile) throws IOException{
		File file = new File(datafile);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line = "" ;
		int count = 1 ;
		while((line = br.readLine())!=null){
//			System.out.println(count+" : "+line);
			count++ ;
			String[] temp = line.split("\t") ;
			String[] root = temp[0].split(" ");
			int type = -1 ;
			if(root[1].equals("oov"))
				type = 0;
			else if(root[1].equals("iv"))
				type = 1 ;
			Vertex vertex = new Vertex(root[0] ,type);
			
			for(int i = 1 ;i<temp.length ;i++){
				String[] record = temp[i].split(" ");
				String name = record[0];
				float weight = Float.parseFloat(record[1]);
				if(record[2].equals("oov"))
					type = 0;
				else if(record[2].equals("iv"))
					type = 1 ;
				Edge edge = new Edge(name ,weight ,type);
				vertex.addedge(edge);
				
			}
			vertex.setRange();
			if(vertex.type==0)
				oovList.add(vertex.name);
			vertexs.put(vertex.name, vertex) ;
		}
	}

	public void saveOOV() throws IOException, InterruptedException, ExecutionException{
		File file = new File(oovFile);
		if(!file.exists()){
			Until.save_list(oovFile, oovList);
		}
	}
	
	public void print(){
		Set<Entry<String ,Vertex>> entryset  = vertexs.entrySet();
		for(Entry entry:entryset){
			Vertex vertex = (Vertex)entry.getValue() ;
			System.out.println(vertex.name+"\t"+vertex.getType());
			List<Edge> list = vertex.getEdgelist();
			 	for(Edge edge :list){
			 		System.out.println(edge.name +"\t"+edge.weight + "\t"+edge.getType()+"\t"+edge.getRange());
			 	}
		}
		
	}
}
