package astLlvm;

public class LlIntArrayType extends LlArray {
	public LlIntArrayType(int length) {
		super(length, LlPrimitiveType.I32);
	}

	public String toString() {
		return "{i32, [" + length + " x " + content + "]}";
	}
}
