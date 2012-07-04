package astLlvm;
public class LlRet extends LlInstruction{
    public LlValue v;
    public LlRet(LlValue v){
	this.v = v;
    }

    public String toString(){
	return "  ret " + v.type + " " + v;
    }
}
