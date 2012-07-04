package astLlvm;
public  class LlAnd extends LlInstruction{
    public LlNamedValue lhs;
    public LlValue op1, op2;

    public LlAnd(LlNamedValue lhs, LlValue op1, LlValue op2){
	this.lhs = lhs;
	this.op1 = op1;
	this.op2 = op2;
    }

    public String toString(){
	return "  " +lhs + " = and i1 " + op1 + ", " + op2;
    }
}
