class Test {
	public static void main(String[] args) {
		System.out.println(new A().f());
	}
}
class A {
	int a;
	public int f() {
		if( (3 < 2) && true) {
			a = 2;
		} else {
			a = 0;
		}
		return a;
	}
}
