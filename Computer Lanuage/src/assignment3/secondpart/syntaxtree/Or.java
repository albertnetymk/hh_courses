package syntaxtree;
import visitor.*;

public class Or extends Exp{
	public Or(Exp ae1, Exp ae2){
		e1=ae1;
		e2=ae2;
	}
	public Exp e1,e2;

	public String toString(){
		return e1+"Or"+e2;
	}
	public <A,B> A accept(Visitor<A,B> v, B b){
		return v.visit(this,b);
	}
}
		
