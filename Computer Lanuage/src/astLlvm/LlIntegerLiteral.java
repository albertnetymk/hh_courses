package astLlvm;
public class LlIntegerLiteral extends LlValue{
    public int value;
    public LlIntegerLiteral(int value){
	type = LlPrimitiveType.I32;
	this.value = value;
    }

    public String toString(){
	return ""+ value;
    }
}