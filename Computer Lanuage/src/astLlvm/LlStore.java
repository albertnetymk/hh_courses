package astLlvm;

public class LlStore extends LlInstruction {
	public LlValue content;
	public LlNamedValue address;

	public LlStore(LlValue content, LlNamedValue address) {
		this.content = content;
		this.address = address;
	}

	public String toString() {
		return "  store " + content.type + " " + content + ", " + address.type
				+ " " + address;
	}
}