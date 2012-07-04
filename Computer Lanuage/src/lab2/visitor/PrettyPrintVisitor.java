package visitor;

import syntaxtree.*;

public class PrettyPrintVisitor implements Visitor<Object,Object> {
    
    private String indent = "";
    
    // MainClass m;
    // ClassDeclList cl;
    public Object visit(Program n, Object e) {
	n.m.accept(this, e);
	for ( int i = 0; i < n.cl.size(); i++) {
	    System.out.println();
	    n.cl.get(i).accept(this, e);
	}
    return null;}
    
    // Identifier i1,i2;
    // Statement s;
    public Object visit(MainClass n, Object e) {
	String old = indent;
	System.out.print("class ");
	n.i1.accept(this, e);
	System.out.println(" {");
	indent=indent+"  ";
	System.out.print(indent+"public static void main (String [] ");
	n.i2.accept(this, e);
	System.out.println(") {");
	
	indent = indent + "  ";
	System.out.print(indent);
	n.s.accept(this, e);
	indent = old;
	System.out.println("  }");
	System.out.println("}");
    return null;}
    
    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public Object visit(ClassDeclSimple n, Object e) {
	System.out.print("class ");
	n.i.accept(this, e);
	System.out.println(" { ");
	String old = indent;
	indent = old + "  ";
	
	for ( int i = 0; i < n.vl.size(); i++ ) {
	    System.out.print(indent);
	    n.vl.get(i).accept(this, e);
	    if ( i+1 < n.vl.size() ) { System.out.println(); }
	}
	for ( int i = 0; i < n.ml.size(); i++ ) {
	    System.out.println();
	    indent=old + "  ";
	    n.ml.get(i).accept(this, e);
	}
	indent = old;
	System.out.println();
	System.out.println("}");
    return null;}
    
    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public Object visit(ClassDeclExtends n, Object e) {
	System.out.print("class ");
	n.i.accept(this, e);
	System.out.println(" extends ");
	n.j.accept(this, e);
	System.out.println(" { ");
	String old = indent;
	indent = old + "  ";
	for ( int i = 0; i < n.vl.size(); i++ ) {
	    System.out.print(indent);
	    n.vl.get(i).accept(this, e);
	    if ( i+1 < n.vl.size() ) { System.out.println(); }
	}
	for ( int i = 0; i < n.ml.size(); i++ ) {
	    System.out.println();
	    indent = old+"  ";
	    n.ml.get(i).accept(this, e);
	}
	indent = old;
	System.out.println();
	System.out.println("}");
    return null;}
    
    // Type t;
    // Identifier i;
    public Object visit(VarDecl n, Object e) {
	n.t.accept(this, e);
	System.out.print(" ");
	n.i.accept(this, e);
	System.out.print(";");
    return null;}
    
    // Type t;
    // Identifier i;
    // FormalList fl;
    // VarDeclList vl;
    // StatementList sl;
    // Exp e;
    public Object visit(MethodDecl n, Object e) {
	String old = indent;
	System.out.print(indent+"public ");
	n.t.accept(this, e);
	System.out.print(" ");
	n.i.accept(this, e);
	System.out.print(" (");
	for ( int i = 0; i < n.fl.size(); i++ ) {
	    n.fl.get(i).accept(this, e);
	    if (i+1 < n.fl.size()) { System.out.print(", "); }
	}
	System.out.println(") { ");
	indent = indent + "  ";
	for ( int i = 0; i < n.vl.size(); i++ ) {
	    System.out.print(indent);
	    n.vl.get(i).accept(this, e);
	    System.out.println("");
	}
	for ( int i = 0; i < n.sl.size(); i++ ) {
	    indent = old+"  ";
	    System.out.print(indent);
	    n.sl.get(i).accept(this, e);
	    if ( i < n.sl.size() ) { System.out.println(""); }
	}
	System.out.print(indent + "return ");
	n.e.accept(this, e);
	indent = old;
	System.out.println(";");
	System.out.print("  }");
    return null;}
    
    // Type t;
    // Identifier i;
    public Object visit(Formal n, Object e) {
	n.t.accept(this, e);
	System.out.print(" ");
	n.i.accept(this, e);
    return null;}
    
    public Object visit(IntArrayType n, Object e) {
	System.out.print("int []");
    return null;}
    
    public Object visit(BooleanType n, Object e) {
	System.out.print("boolean");
    return null;}
    
    public Object visit(IntegerType n, Object e) {
	System.out.print("int");
    return null;}
    
    // String s;
    public Object visit(IdentifierType n, Object e) {
	System.out.print(n.s);
    return null;}
    
    // StatementList sl;
    public Object visit(Block n, Object e) {
	System.out.println("{ ");
	String old = indent;
	indent = indent + "  ";
	for ( int i = 0; i < n.sl.size(); i++ ) {
	    System.out.print(indent);
	    n.sl.get(i).accept(this, e);
	    System.out.println();
	}
	indent = old;
	System.out.print(indent+"} ");
    return null;}
    
    // Exp e;
    // Statement s1,s2;
    public Object visit(If n, Object e) {
	String old = indent;
	indent = indent + "  ";
	System.out.print("if (");
	n.e.accept(this, e);
	System.out.println(") ");
	System.out.print(indent);
	n.s1.accept(this, e);
	System.out.println();
	System.out.println(old + "else ");
	System.out.print(indent);
	n.s2.accept(this, e);
	indent=old;
    return null;}
    
    // Exp e;
    // Statement s;
    public Object visit(While n, Object e) {
	String old=indent;
	indent = indent + "  ";
	System.out.print("while (");
	n.e.accept(this, e);
	System.out.print(") ");
	n.s.accept(this, e);
	indent = old;
    return null;}
    
    // Exp e;
    public Object visit(Print n, Object e) {
	System.out.print("System.out.println(");
	n.e.accept(this, e);
	System.out.print(");");
    return null;}
    
    // Identifier i;
    // Exp e;
    public Object visit(Assign n, Object e) {
	n.i.accept(this, e);
	System.out.print(" = ");
	n.e.accept(this, e);
	System.out.print(";");
    return null;}
    
    // Identifier i;
    // Exp e1,e2;
    public Object visit(ArrayAssign n, Object e) {
	n.i.accept(this, e);
	System.out.print("[");
	n.e1.accept(this, e);
	System.out.print("] = ");
	n.e2.accept(this, e);
	System.out.print(";");
    return null;}
    
    // Exp e1,e2;
    public Object visit(And n, Object e) {
	System.out.print("(");
	n.e1.accept(this, e);
	System.out.print(" && ");
	n.e2.accept(this, e);
	System.out.print(")");
    return null;}
    
    // Exp e1,e2;
    public Object visit(LessThan n, Object e) {
	System.out.print("(");
	n.e1.accept(this, e);
	System.out.print(" < ");
	n.e2.accept(this, e);
	System.out.print(")");
    return null;}
    
    // Exp e1,e2;
    public Object visit(Plus n, Object e) {
	System.out.print( "(");
	n.e1.accept(this, e);
	System.out.print(" + ");
	n.e2.accept(this, e);
	System.out.print(")");
    return null;}
    
    // Exp e1,e2;
    public Object visit(Minus n, Object e) {
	System.out.print("(");
	n.e1.accept(this, e);
	System.out.print(" - ");
	n.e2.accept(this, e);
	System.out.print(")");
    return null;}
    
    // Exp e1,e2;
    public Object visit(Times n, Object e) {
	System.out.print("(");
	n.e1.accept(this, e);
	System.out.print(" * ");
	n.e2.accept(this, e);
	System.out.print(")");
    return null;}
    
    // Exp e1,e2;
    public Object visit(ArrayLookup n, Object e) {
	n.e1.accept(this, e);
	System.out.print("[");
	n.e2.accept(this, e);
	System.out.print("]");
    return null;}
    
    // Exp e;
    public Object visit(ArrayLength n, Object e) {
	n.e.accept(this, e);
	System.out.print(".length");
    return null;}
    
    // Exp e;
    // identifier i;
    // ExpList el;
    public Object visit(Call n, Object e) {
	n.e.accept(this, e);
	System.out.print(".");
	n.i.accept(this, e);
	System.out.print("(");
	for ( int i = 0; i < n.el.size(); i++ ) {
	    n.el.get(i).accept(this, e);
	    if ( i+1 < n.el.size() ) { System.out.print(", "); }
	}
	System.out.print(")");
    return null;}
    
    // int i;
    public Object visit(IntegerLiteral n, Object e) {
	System.out.print( n.i);
    return null;}
    
    public Object visit(True n, Object e) {
	System.out.print( "true");
    return null;}
    
    public Object visit(False n, Object e) {
	System.out.print( "false");
    return null;}
    
    // String s;
    public Object visit(IdentifierExp n, Object e) {
	System.out.print( n.s);
    return null;}
    
    public Object visit(This n, Object e) {
	System.out.print( "this");
    return null;}
    
    // Exp e;
    public Object visit(NewArray n, Object e) {
	System.out.print("new int [");
	n.e.accept(this, e);
	System.out.print("]");
    return null;}
    
    // Identifier i;
    public Object visit(NewObject n, Object e) {
	System.out.print( "new ");
	System.out.print(n.i.s);
	System.out.print("()");
    return null;}
    
    // Exp e;
    public Object visit(Not n, Object e) {
	System.out.print("!");
	n.e.accept(this, e);
    return null;}
    
    // String s;
    public Object visit(Identifier n, Object e) {
	System.out.print(n.s);
    return null;}
}
