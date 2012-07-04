package astLlvm;

public class LlBitcast extends LlInstruction {
	public LlNamedValue lhs;
	public LlType oldType;
	public LlValue value;
	public LlType newType;

	public LlBitcast(LlNamedValue lhs, LlValue value, LlType newType) {
		this.lhs = lhs;
		this.value = value;
		this.oldType = value.type;
		this.newType = newType;
	}

	public String toString() {
		return "  " + lhs + " = " + "bitcast " + oldType + " " + value + " to "
				+ newType;
	}
}
