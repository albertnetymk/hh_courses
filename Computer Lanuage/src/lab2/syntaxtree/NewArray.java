package syntaxtree;
import visitor.Visitor;

public class NewArray extends Exp {
  public Exp e;
  
  public NewArray(Exp ae) {
    e=ae; 
  }

    public <T,E>T accept(Visitor<T,E> v, E env) {
	return v.visit(this,env);
  }
}
