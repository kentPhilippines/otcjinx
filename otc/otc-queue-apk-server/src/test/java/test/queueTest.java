package test;

import java.util.HashSet;
import java.util.Set;

public class queueTest {
	public static void main(String[] args) {
		Set set = new HashSet();
		Set set1 = new HashSet();
		set.add(1);
		set.add(2);
		set.add(3);
		set.add(4);
		set.add(5);
		set.add(6);
		set1.add('a');
		set1.add('b');
		set1.add('c');
		set1.add('d');
		set1.add('e');
		set1.add('f');
	 	System.arraycopy(set, 0, set1, set1.size(), set.size());
		for( Object o : set1)
			System.out.println(o);
		
		
		
		
		
		
		byte[]  srcBytes = new byte[]{2,4,0,0,0,0,0,10,15,50};
		          byte[] destBytes = new byte[5];
		         System.arraycopy(srcBytes, 0, destBytes, 0, 5); 
		         for(int i = 0;i< destBytes.length;i++){
		            System.out.print("-> " + destBytes[i]);
		         } 
		
		
		
		
		
		
	}
	
	
	

}
