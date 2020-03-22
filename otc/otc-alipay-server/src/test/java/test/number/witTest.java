
package test.number;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.ibatis.reflection.ArrayUtil;

public class witTest {

	public static void main(String[] args) {
		String witip  = "123123,312321,312312,3434,45,435,435,25,dasd";
		String[] split = witip.split(",");
		for(String a : split)
			System.out.println(a);
		List<String> asList = Arrays.asList(split);
		
	}
}
