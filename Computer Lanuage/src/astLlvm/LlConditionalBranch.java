package astLlvm;
public  class LlConditionalBranch extends LlInstruction{
    public LlValue cond;
    public LlLabelValue brTrue, brFalse;

    public LlConditionalBranch(LlValue cond,  LlLabelValue brTrue, LlLabelValue brFalse){
	this.cond = cond;
	this.brTrue = brTrue;
	this.brFalse = brFalse;
    }

    public String toString(){
	return "  br i1 " +cond + ", label %" + brTrue + ", label %" + brFalse;
    }
}