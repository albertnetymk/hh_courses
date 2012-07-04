package astLlvm;
import java.util.*;

public  class LlCall extends LlInstruction{
    public LlNamedValue lhs;
    public LlType type;
    public LlPointer fnType;
    public String fnName;
    public List<LlValue> args;

    public LlCall(LlNamedValue lhs, LlType type, LlPointer fnType, String fnName, List<LlValue> args){
	this.lhs = lhs;
	this.type = type;
	this.fnType = fnType;
	this.fnName = fnName;
	this.args = args;
    }

    public String toString(){

	String arguments = "";
	for(int i = 0; i<args.size(); i++){
	    arguments = arguments + args.get(i).type + " " + args.get(i);
	    if(i+1<args.size()) 
		arguments = arguments + ", ";
	}	

	return "  " + lhs + " = " + "call " + type + " " + fnName +  "(" + arguments + ")"; 
    }
}
