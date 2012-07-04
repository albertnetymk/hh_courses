package syntaxtree;
import visitor.Visitor;

public class IdentifierType extends Type {
  public String s;

  public IdentifierType(String as) {
    s=as;
  }

    public <T,E>T accept(Visitor<T,E> v, E env) {
	return v.visit(this,env);
  }
}
