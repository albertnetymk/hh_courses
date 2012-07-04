package syntaxtree;
import visitor.Visitor;


public class ArrayAssign extends Statement {
  public Identifier i;
  public Exp e1,e2;

  public ArrayAssign(Identifier ai, Exp ae1, Exp ae2) {
    i=ai; e1=ae1; e2=ae2;
  }


    public <T,E>T accept(Visitor<T,E> v, E env) {
	return v.visit(this,env);
  }

}

