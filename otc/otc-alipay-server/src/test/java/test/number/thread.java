package test.number;

import cn.hutool.core.thread.ThreadUtil;

public class thread {
	public static void main(String[] args) {
		StackTraceElement[] stackTrace = ThreadUtil.getStackTrace();
		for(StackTraceElement sss:stackTrace) {
			String className = sss.getClassName();
			String fileName = sss.getFileName();
			String methodName = sss.getMethodName();
			int lineNumber = sss.getLineNumber();//代码行数
			System.out.println(className);
			System.out.println(fileName);
		System.out.println(methodName);
		System.out.println(lineNumber);
		}
	}

}
