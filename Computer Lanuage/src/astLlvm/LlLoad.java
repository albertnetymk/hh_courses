package astLlvm;
public class LlLoad extends LlInstruction{
    public LlValue lhs;
    public LlNamedValue address; // includes its type

    public LlLoad(LlValue lhs, LlNamedValue address){
	this.lhs=lhs;
	this.address=address;
    }
    
    public String toString(){
	return "  " + lhs + " = load " + address.type + " " + address;
    }
}