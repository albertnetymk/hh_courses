package astLlvm;

import java.util.List;

public class LlStructure extends LlType {
	public String name;

	public LlStructure(String name) {
		this.name = name;
	}

	public String toString() {
		return "%"+name;
	}
}
