package visitor;

import syntaxtree.*;

public interface Visitor < A, Env >{
    A visit(BooleanLiteral x, Env e);
    A visit(Id x, Env e);
    A visit(And x, Env e);
    A visit(Or x, Env e);
    A visit(Not x, Env e);
}

