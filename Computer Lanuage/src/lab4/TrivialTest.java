class TrivialTest {
	public static void main(String[] argv) {
		System.out.println((new Test()).f());
	}
}

class Test {
	public int f() {
		int a;
		a=0;
		a=a+2;
		return a;
	}
}
