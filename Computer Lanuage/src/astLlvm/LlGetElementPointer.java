package astLlvm;
import  java.util.*;
public class LlGetElementPointer extends LlInstruction{
    public LlNamedValue lhs;
    public LlNamedValue source;
    public List<LlValue> places;

    public LlGetElementPointer(LlNamedValue lhs, LlNamedValue source, List<LlValue> places){
	this.lhs = lhs;
	this.source = source;
	this.places = places;
    }
    
    public String toString(){
	String ps = "";
	for(int i = 0; i<places.size(); i++){
	    ps = ps + places.get(i).type + " " + places.get(i);
	    if(i+1<places.size()) 
		ps = ps + ", ";

	}
	return "  " + lhs + " = getelementptr " + source.type + " " + source +", " + ps;
    }

}
