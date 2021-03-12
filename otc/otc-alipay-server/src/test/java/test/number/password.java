package test.number;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class password {
	public static void main(String[] args) {
		Map<String, Long> map = new ConcurrentHashMap<>();
		Long put = map.put("1", Long.valueOf(2));

		System.out.println(put);
	}

}
