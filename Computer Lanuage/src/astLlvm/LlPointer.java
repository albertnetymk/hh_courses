package astLlvm;
public class LlPointer extends LlType{
	public LlType content;

	public LlPointer(LlType content){
		this.content = content;
	}

	public String toString(){
		return content + " *";
	}
}
