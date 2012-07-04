class ArrayTest{
	public static void main(String[] a){
		System.out.println(new A().sum(100));
	}
}

class A {
	int [] a;
	int aSize;
	public int sum(int size){
		int i;
		int y;
		i = 0;
		aSize = size;
		a = new int[size];
		while(i<size){
			a[i] = i;
			i = i+1;
		}

		y = 0;
		i = 0;
		while(i<aSize){
			y = y + a[i];
			i = i + 1;
		}
		return y;
	}

}
