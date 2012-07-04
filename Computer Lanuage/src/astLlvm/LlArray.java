package astLlvm;

public class LlArray extends LlType {
	public int length;
	public LlType content;

	public LlArray(int length, LlType content) {
		this.length = length;
		this.content = content;
	}

	public String toString() {
		return "[" + length + " x " + content + "]";
	}
}