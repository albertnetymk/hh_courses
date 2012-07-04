package syntaxtree;
import visitor.Visitor;

public class Not extends Exp {
  public Exp e;
  
  public Not(Exp ae) {
    e=ae; 
  }


    public <T,E>T accept(Visitor<T,E> v, E env) {
	return v.visit(this,env);
  }
}
