package syntaxtree;
import visitor.*;

public class Id extends Exp{
	public String s;
	
	public Id(String as){
		s=as;
	}
	
	public String toString(){
		return s;
	}
	public <A,B> A accept(Visitor<A,B> v, B b){
		return v.visit(this,b);
	}
}
