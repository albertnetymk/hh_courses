package syntaxtree;
import visitor.Visitor;

public class IntArrayType extends Type {

    public <T,E>T accept(Visitor<T,E> v, E env) {
	return v.visit(this,env);
  }
}
