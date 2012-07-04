package astLlvm;
public class LlLabel extends LlInstruction{
    public LlLabelValue label;
    public LlLabel(LlLabelValue label){this.label = label;}
    public String toString(){
	return label+":";
    }
}