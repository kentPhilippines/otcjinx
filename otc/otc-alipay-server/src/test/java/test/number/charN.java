package test.number;

public class charN {
	public static void main(String[] args) {
		String username = "B2adshasdasdjk13";
		char[] charArray = username.toCharArray();
		char a = charArray[0];
		char d = charArray[charArray.length-1];
		System.out.println(a);
		System.out.println(d);
		int o = a - d ;
		int ss = (o < 0) ? -o : o;
		System.out.println(ss);
		
		
		
		
	}

}
