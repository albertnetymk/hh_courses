package astLlvm;
public  class LlBranch extends LlInstruction{
    public LlLabelValue label;

    public LlBranch(LlLabelValue label){
	this.label = label;
    }

    public String toString(){
	return "  br label %" + label;
    }
}