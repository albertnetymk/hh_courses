package astLlvm;
public  class LlAdd extends LlInstruction{
    public LlNamedValue lhs;
    public LlType type;
    public LlValue op1, op2;

    public LlAdd(LlNamedValue lhs, LlType type, LlValue op1, LlValue op2){
	this.lhs = lhs;
	this.type = type;
	this.op1 = op1;
	this.op2 = op2;
    }

    public String toString(){
	return "  " +lhs + " = add " + type + " " + op1 + ", " + op2;
    }
}