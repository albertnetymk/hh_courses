package syntaxtree;
import visitor.Visitor;

public abstract class Exp {
    public abstract <T,E>T accept(Visitor<T,E> v, E env);
}
