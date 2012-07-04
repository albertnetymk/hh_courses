package astLlvm;
public class LlPrimitiveType extends LlType{
    public static final LlType I1         = new LlPrimitiveType();
    public static final LlType I8         = new LlPrimitiveType();
    public static final LlType I32        = new LlPrimitiveType();
    public static final LlType VOID       = new LlPrimitiveType();
    public static final LlType LABEL      = new LlPrimitiveType();
    public static final LlType DOTDOTDOT  = new LlPrimitiveType();

    public String toString(){
	if(this == I1) return "i1";
	if(this == I8) return "i8";
	if(this == I32) return "i32";
	if(this == VOID) return "void";
	if(this == LABEL) return "label";
	if(this == DOTDOTDOT) return "...";
	return null;
    }
}