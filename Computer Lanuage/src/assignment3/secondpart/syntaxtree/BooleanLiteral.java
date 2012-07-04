package syntaxtree;
import visitor.*;

public class BooleanLiteral extends Exp{
	public Boolean b;
	
	public BooleanLiteral(Boolean b){
		this.b=b;
	}
	
	public String toString(){
		return b.toString();
	}
	public <A,B> A accept(Visitor<A,B> v, B b){
		return v.visit(this,b);
	}
}
		
