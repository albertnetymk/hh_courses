package astLlvm;
public class LlFalse extends LlValue{
    public LlFalse(){
	type = LlPrimitiveType.I1;
    }

    public String toString(){
	return "0";
    }
}