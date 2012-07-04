package syntaxtree;
import visitor.Visitor;

public class BooleanType extends Type {
    public <T,E>T accept(Visitor<T,E> v, E env) {
	return v.visit(this,env);
  }
}
