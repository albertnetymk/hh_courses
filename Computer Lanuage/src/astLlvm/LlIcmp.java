package astLlvm;
public  class LlIcmp extends LlInstruction{
    public LlNamedValue lhs;
    public int conditionCode;
    public LlType type;
    public LlValue op1, op2;

    
    public LlIcmp(LlNamedValue lhs,  int conditionCode, LlType type, LlValue op1, LlValue op2){
	this.lhs = lhs;
	this.conditionCode = conditionCode;
	this.type = type;
	this.op1 = op1;
	this.op2 = op2;
    }

	public String toString(){
		String cond = "";
		switch (conditionCode){
			case EQ  : {cond = "eq";break;}
			case NE  : {cond = "ne";break;}
			case UGT : {cond = "ugt";break;}
			case UGE : {cond = "uge";break;}
			case ULT : {cond = "ult";break;}
			case ULE : {cond = "ule";break;}
			case SGT : {cond = "sgt";break;}
			case SGE : {cond = "sge";break;}
			case SLT : {cond = "slt";break;}
			case SLE : {cond = "sle";break;}
		}
		return "  " +lhs + " = icmp " + cond + " " + type + " " + op1 + ", " + op2;
	}

	public static final int EQ  = 0;
	public static final int NE  = 1;
	public static final int UGT = 2;
	public static final int UGE = 3;
	public static final int ULT = 4;
	public static final int ULE = 5;
	public static final int SGT = 6;
	public static final int SGE = 7;
	public static final int SLT = 8;
	public static final int SLE = 9;
}
