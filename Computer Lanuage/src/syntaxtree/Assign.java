package syntaxtree;
import visitor.Visitor;

public class Assign extends Statement {
  public Identifier i;
  public Exp e;

  public Assign(Identifier ai, Exp ae) {
    i=ai; e=ae; 
  }

    public <T,E>T accept(Visitor<T,E> v, E env) {
	return v.visit(this,env);
  }
}

