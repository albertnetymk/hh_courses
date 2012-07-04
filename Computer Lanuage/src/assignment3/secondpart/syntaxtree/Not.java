package syntaxtree;
import visitor.*;

public class Not extends Exp{
	public Not(Exp ae){
		e=ae;
	}
	public Exp e;
	
	public String toString(){
		return "Not"+e;
	}
	public <A,B> A accept(Visitor<A,B> v, B b){
		return v.visit(this,b);
	}
}
		
