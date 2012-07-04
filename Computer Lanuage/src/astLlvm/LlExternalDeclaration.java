package astLlvm;
import java.util.*;
public class LlExternalDeclaration extends LlInstruction{
    public String name;
    public LlType resultType;
    public List<LlType> parameterTypes;

    public LlExternalDeclaration(String name,LlType resultType, List<LlType> parameterTypes){
	this.name = name;
	this.resultType = resultType;
	this.parameterTypes = parameterTypes;
    }
    
    public String toString(){
	String argTypes = "";
	for(int i = 0; i<parameterTypes.size(); i++){
	    argTypes = argTypes + parameterTypes.get(i);
	    if(i+1<parameterTypes.size()) 
		argTypes = argTypes + ", ";

	}
	return "declare " + resultType + " " + name + " (" + argTypes + ")";
    }
}