package astLlvm;
public class LlTrue extends LlValue{
    public LlTrue(){
	type = LlPrimitiveType.I1;
    }
    public String toString(){
	return "1";
    }
}