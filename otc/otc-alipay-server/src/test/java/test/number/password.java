package test.number;

public class password {

	public static void main(String[] args) {
		new TestM().OuterMethod();
	}


	public void methodA() {
		methodB();
	}

	public void methodB() {
		methodC();
	}

	public void methodC() {
		StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
		for (StackTraceElement s : stacks) {
			System.out.println("-------" + s.getMethodName() + " : " + s);
		}
	}
}

class TestM {
	public void OuterMethod() {
		new password().methodA();
	}
}