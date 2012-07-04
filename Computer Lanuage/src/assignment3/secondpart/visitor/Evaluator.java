package visitor;
import syntaxtree.*;
import java.util.HashMap;
public class Evaluator implements Visitor <Boolean, HashMap<String,Boolean>>{
	public Boolean visit(BooleanLiteral x, HashMap<String,Boolean> e){
		System.out.print(x.b);
		return x.b;
	}
	
	public Boolean visit(Id x, HashMap<String,Boolean> e){
		System.out.print(e.get(x.s));
//		return e.get(x.s);
		return true;
	}

	public Boolean visit(And x, HashMap<String,Boolean> e){
		System.out.print("(");
		boolean tmp1=x.e1.accept(this, e);
		System.out.print("&&");
		boolean tmp2=x.e2.accept(this, e);
		System.out.print(")");
		return tmp1 && tmp2;
		//return x.e1.accept(this, e) && x.e2.accept(this, e);
	}

	public Boolean visit(Or x, HashMap<String,Boolean> e){
		System.out.print("(");
		boolean tmp1=x.e1.accept(this, e);
		System.out.print("||");
		boolean tmp2=x.e2.accept(this, e);
		System.out.print(")");
		return tmp1 || tmp2;
		//return x.e1.accept(this, e) || x.e2.accept(this, e);
	}

	public Boolean visit(Not x, HashMap<String,Boolean> e){	
		System.out.print("(");
		boolean tmp1=x.e.accept(this, e);
		System.out.print(")");
		return !tmp1; 
		//return !(x.e.accept(this, e));
	}
}
