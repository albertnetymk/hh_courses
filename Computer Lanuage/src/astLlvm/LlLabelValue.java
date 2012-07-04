package astLlvm;
public class LlLabelValue extends LlValue{
    public String value;
    public LlLabelValue(String value){
	type = LlPrimitiveType.LABEL;
	this.value = value;
    }

    public String toString(){
	return ""+ value;
    }
}