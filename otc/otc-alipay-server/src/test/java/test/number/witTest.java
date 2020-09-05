
package test.number;

import java.lang.reflect.Method;

public class witTest {
	public static void main(String[] args) {
		new witTest().run();
	}

	public void run() {
		// TODO Auto-generated method stub
		System.out.println("测试========》");
		String s1 = this.getClass().getName();//类名
		System.out.println(s1);
		System.out.println(Thread.currentThread().getStackTrace()[1].getClassName());
		Method[] methods = this.getClass().getMethods();
		for (Method md : methods)
			System.out.println(md.getName());
	}

}
