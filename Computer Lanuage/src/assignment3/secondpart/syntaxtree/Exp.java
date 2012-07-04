package syntaxtree;

import visitor.*;

public abstract class Exp{
	public abstract <A,B> A accept(Visitor<A,B> v, B b);
}
