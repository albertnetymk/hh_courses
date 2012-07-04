package visitor;

import syntaxtree.*;

public interface Visitor<T,Env> {
    public T visit(Program n, Env e);
    public T visit(MainClass n, Env e);
    public T visit(ClassDeclSimple n, Env e);
    public T visit(ClassDeclExtends n, Env e);
    public T visit(VarDecl n, Env e);
    public T visit(MethodDecl n, Env e);
    public T visit(Formal n, Env e);
    public T visit(IntArrayType n, Env e);
    public T visit(BooleanType n, Env e);
    public T visit(IntegerType n, Env e);
    public T visit(IdentifierType n, Env e);
    public T visit(Block n, Env e);
    public T visit(If n, Env e);
    public T visit(While n, Env e);
    public T visit(Print n, Env e);
    public T visit(Assign n, Env e);
    public T visit(ArrayAssign n, Env e);
    public T visit(And n, Env e);
    public T visit(LessThan n, Env e);
    public T visit(Plus n, Env e);
    public T visit(Minus n, Env e);
    public T visit(Times n, Env e);
    public T visit(ArrayLookup n, Env e);
    public T visit(ArrayLength n, Env e);
    public T visit(Call n, Env e);
    public T visit(IntegerLiteral n, Env e);
    public T visit(True n, Env e);
    public T visit(False n, Env e);
    public T visit(IdentifierExp n, Env e);
    public T visit(This n, Env e);
    public T visit(NewArray n, Env e);
    public T visit(NewObject n, Env e);
    public T visit(Not n, Env e);
    public T visit(Identifier n, Env e);
}
