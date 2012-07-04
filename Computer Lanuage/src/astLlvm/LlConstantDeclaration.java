package astLlvm;
public class LlConstantDeclaration extends LlInstruction{
	public String name;
	public String rhs;
	public LlConstantDeclaration(String name, String rhs){
		this.name = name;
		this.rhs = rhs;
	}

	public String toString(){
		return name + " = " + rhs;
	}
}
