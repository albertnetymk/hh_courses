package astLlvm;
public class LlNamedValue extends LlValue{
    public String name;
    public LlNamedValue(String name, LlType type){
	this.type = type;
	this.name = name;
    }
    public String toString(){
	return name; 
    }
}