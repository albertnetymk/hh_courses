package astLlvm;
import java.util.*;
public  class LlAlloca extends LlInstruction{
    public LlNamedValue lhs;
    public LlType type;
    public List<LlValue> numbers;

    public LlAlloca(LlNamedValue lhs, LlType type, List<LlValue> numbers){
	this.lhs = lhs;
	this.type = type;
	this.numbers = numbers;
    }

    public String toString(){
	String nrs = "";
	for(LlValue v : numbers)
	    nrs = nrs + ", " + v.type + " " + v;
	return "  " + lhs + " = alloca " + type + nrs;
    }
}