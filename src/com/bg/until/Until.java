package com.bg.until;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public class Until {

	 public static void save_list(String filePath , List<String> result) throws IOException, InterruptedException, ExecutionException{
			
			   System.out.println("string process using multiThread...");
			   System.out.println("size = " + result.size());
			   long start = System.currentTimeMillis();
			  SubTaskx task = new SubTaskx(filePath ,result);
			  ForkJoinPool pool = new ForkJoinPool() ;
			  pool.invoke(task);
			  task.join();
			  long end = System.currentTimeMillis();
			 System.out.println("write file succeed,using :" + (end-start)/1000 + "s");
		 }
	
	public static void save_str(String filePath , String content) throws IOException{
		byte[] b = content.getBytes();
//	 	fos.write(b);
	 	FileChannel fc = new RandomAccessFile(filePath ,"rw").getChannel();
		ByteBuffer bb = ByteBuffer.wrap(content.getBytes());
		fc.position(fc.size()) ;
	 	fc.write(bb) ;
	}

}
