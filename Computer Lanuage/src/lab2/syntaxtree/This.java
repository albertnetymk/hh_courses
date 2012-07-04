package syntaxtree;
import visitor.Visitor;

public class This extends Exp {
    public <T,E>T accept(Visitor<T,E> v, E env) {
	return v.visit(this,env);
  }
}
