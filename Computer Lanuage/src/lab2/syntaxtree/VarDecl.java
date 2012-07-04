package syntaxtree;
import visitor.Visitor;

public class VarDecl {
  public Type t;
  public Identifier i;
  
  public VarDecl(Type at, Identifier ai) {
    t=at; i=ai;
  }


    public <T,E>T accept(Visitor<T,E> v, E env) {
	return v.visit(this,env);
  }
}
