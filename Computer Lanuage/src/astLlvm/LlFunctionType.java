package astLlvm;
import java.util.*;
public class LlFunctionType extends LlType{
	public LlType resultType;
	public List<LlType> parametersTypes;

	public LlFunctionType(LlType resultType, List<LlType> parametersTypes){
		this.resultType = resultType;
		this.parametersTypes = parametersTypes;
	}


}
