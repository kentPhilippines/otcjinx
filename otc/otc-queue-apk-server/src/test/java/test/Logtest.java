package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.log.Log;


public class Logtest {
	static Logger log= LoggerFactory.getLogger(Logtest.class);
	public static void main(String[] args) {
		log.info("asdasd", "[asdsa]");
		log.info("asda");
	}
}
