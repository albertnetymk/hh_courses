package syntaxtree;
import visitor.Visitor;

public class Identifier {
  public String s;

  public Identifier(String as) { 
    s=as;
  }


    public <T,E>T accept(Visitor<T,E> v, E env) {
	return v.visit(this,env);
  }
  public String toString(){
    return s;
  }
}
