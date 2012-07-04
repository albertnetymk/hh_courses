package astLlvm;
import java.util.*;
public class LlDefine extends LlInstruction{
    public String name;
    public LlType resultType;
    public List<LlNamedValue> args;
    
    public LlDefine(String name, LlType resultType, List<LlNamedValue> args){
	this.name = name;
	this.resultType = resultType;
	this.args = args;
    }

    public String toString(){
	String arguments = "";
	for(int i = 0; i<args.size(); i++){
	    arguments = arguments + args.get(i).type + " " + args.get(i);
	    if(i+1<args.size()) 
		arguments = arguments + ", ";
	    
	}
	return "define " + resultType + " " + name + "(" + arguments + ") {";
    }
}