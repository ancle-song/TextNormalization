package com.bg.until;
	
	import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

public class SubTaskx extends RecursiveTask <String>{  
	  
    private static final long serialVersionUID = 1L;  
    private static List<String> resultlist = new ArrayList<String>();
      
    private List<String> recordlist = new ArrayList<String>();  
    private int Threadshold =10000 ; 
      private String savePath ;
    public SubTaskx(String savePath ,List<String> recordlist) {  
        super();  
        this.recordlist = recordlist;  
        this.savePath = savePath ;
    }  
  
    @Override  
    protected String compute() {  
    	String content = "" ;
    	System.out.println("list_size = " + recordlist.size());
    	if(recordlist.size() <=Threadshold){
			try {
				content = process(recordlist);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	else{
            int mid =recordlist.size() / 2;  
            SubTaskx t1 = new SubTaskx(savePath ,recordlist.subList(0 , mid-1));  
         
            SubTaskx t2 = new SubTaskx(savePath ,recordlist.subList(mid, recordlist.size()-1));  
           
//            invokeAll(t1,t2);
            t1.fork();
            t2.fork();
            content = t1.join() + t2.join() ;
    	}
            return "" ;

        }  

	public String process(List<String> recordlist) throws IOException{
		System.out.println("SubTaskx_process_list...");
		String content = "" ;
		Object[] array =  recordlist.toArray() ;
	    for(int i= 0 ;i<array.length;i++){
	    	content += array[i]+ "\n" ;

        }
//	    String savePath = "/home/songyajun/Java/dataset/en_5gram/" +"en_5gram_count.txt" ;
//	    IOtool.saveResult(savePath, content);
	    save_content(savePath ,content);
	    System.out.println("SubTaskx_process_list succeed!");
	    content = "" ;
	    return "" ;
	}
	
	public void save_content(String savePath , String content) throws IOException{
		System.out.println("write data...");
		
	 	byte[] b = content.getBytes();
	 	FileChannel fc = new RandomAccessFile(savePath ,"rw").getChannel();
		ByteBuffer bb = ByteBuffer.wrap(content.getBytes());
		
		fc.position(fc.size()) ;
	 	fc.write(bb) ;
	 	fc.close();
	 	content = "" ;
	 	
	 	System.out.println("write data succeed!");	

	}
	
	public List<String> getResultlist (){
		return resultlist  ;
	}
	
	public int getSize(){
		return resultlist.size();
	}
	
	public void resultlistClear(){
		resultlist.clear();
	}
}