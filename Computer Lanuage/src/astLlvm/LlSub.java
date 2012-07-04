package astLlvm;
// will be used even for -x
public  class LlSub extends LlInstruction{
    public LlNamedValue lhs;
    public LlType type;
    public LlValue op1, op2;

    public LlSub(LlNamedValue lhs, LlType type, LlValue op1, LlValue op2){
	this.lhs = lhs;
	this.type = type;
	this.op1 = op1;
	this.op2 = op2;
    }

    public String toString(){
	return "  " +lhs + " = sub " + type + " " + op1 + ", " + op2;
    }
}