package lab3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import syntaxtree.And;
import syntaxtree.ArrayAssign;
import syntaxtree.ArrayLength;
import syntaxtree.ArrayLookup;
import syntaxtree.Assign;
import syntaxtree.Block;
import syntaxtree.BooleanType;
import syntaxtree.Call;
import syntaxtree.ClassDecl;
import syntaxtree.ClassDeclExtends;
import syntaxtree.ClassDeclSimple;
import syntaxtree.Exp;
import syntaxtree.ExpList;
import syntaxtree.False;
import syntaxtree.Formal;
import syntaxtree.Identifier;
import syntaxtree.IdentifierExp;
import syntaxtree.IdentifierType;
import syntaxtree.If;
import syntaxtree.IntArrayType;
import syntaxtree.IntegerLiteral;
import syntaxtree.IntegerType;
import syntaxtree.LessThan;
import syntaxtree.MainClass;
import syntaxtree.MethodDecl;
import syntaxtree.Minus;
import syntaxtree.NewArray;
import syntaxtree.NewObject;
import syntaxtree.Not;
import syntaxtree.Plus;
import syntaxtree.Print;
import syntaxtree.Program;
import syntaxtree.Statement;
import syntaxtree.This;
import syntaxtree.Times;
import syntaxtree.True;
import syntaxtree.Type;
import syntaxtree.VarDecl;
import syntaxtree.VarDeclList;
import syntaxtree.While;
import visitor.Visitor;

public class TypeChecker implements Visitor<Type, SymbolTable> {
	private static String thisClass;
	private Type intArrayType = new IntArrayType();
	private Type intType = new IntegerType();
	private Type booleanType = new BooleanType();

	public static void typeCheck(Program p) {
		SymbolTable env = new SymbolTable();

		// Add the main class and the rest of the classes to form the
		// global environment. Local variables are not included in this
		// environment.
		env = p.accept(new DecElaborator(), env);

		// type check all class declarations!
		p.accept(new TypeChecker(), env);
	}

	public Type visit(Program n, SymbolTable env) {
		n.m.accept(this, env);
		for (ClassDecl c : n.cl)
			c.accept(this, env);
		return null;
	}

	public Type visit(MainClass n, SymbolTable env) {
		thisClass = n.i1.s;
		n.s.accept(this, env);
		thisClass = "";
		return null;
	}

	public Type visit(ClassDeclSimple n, SymbolTable env) {
		env.addLocalScope();
		DecElaborator de = new DecElaborator();
		// add fields to this local scope
		for (VarDecl v : n.vl) {
			env = v.accept(de, env);
		}
		thisClass = n.i.s;
		for (MethodDecl m : n.ml) {
			// add one local scope and typecheck this method
			m.accept(de, env);
		}
		thisClass = "";
		env.removeLocalScope();
		return null;
	}

	public Type visit(ClassDeclExtends n, SymbolTable env) {
		env.addLocalScope();
		DecElaborator de = new DecElaborator();
		Structure _interface = env.getClass(n.i.s);
		// add fields to this local scope
		while (_interface != null) {
			for (VarDecl v : _interface.fields) {
				env = v.accept(de, env);
			}
			_interface = env.getClass(_interface.parent);
		}

		thisClass = n.i.s;
		for (MethodDecl m : n.ml) {
			// add one local scope and typecheck this method
			m.accept(de, env);
		}
		thisClass = "";
		env.removeLocalScope();
		return null;

	}

	public Type visit(VarDecl n, SymbolTable env) {
		// nothing to do!
		return null;
	}

	public Type visit(MethodDecl n, SymbolTable env) {
		System.out.println("method decl: " + n.i.s);
		for (Statement s : n.sl) {
			s.accept(this, env);
		}
		Type t = n.e.accept(this, env);
		if (!Types.subType(t, n.t, env)) {
			System.out.println("Type error: expected " + Types.toString(n.t)
					+ " and found " + Types.toString(t));
			System.exit(1);
		}
		return null;
	}

	public Type visit(Formal n, SymbolTable env) {
		// nothing to do!
		return null;
	}

	public Type visit(IntArrayType n, SymbolTable env) {
		return n;
	}

	public Type visit(BooleanType n, SymbolTable env) {
		return n;
	}

	public Type visit(IntegerType n, SymbolTable env) {
		return n;
	}

	public Type visit(IdentifierType n, SymbolTable env) {
		return n;
	}

	public Type visit(Block n, SymbolTable env) {
		for (Statement s : n.sl) {
			s.accept(this, env);
		}
		return null;
	}

	public Type visit(If n, SymbolTable env) {
		System.out.println("If");
		Type t = n.e.accept(this, env);
		if (!Types.subType(t, booleanType, env)) {
			System.out.println("Type error: expected "
					+ Types.toString(booleanType) + " and found "
					+ Types.toString(t));
			System.exit(1);
		}
		if (n.s1 != null) {
			n.s1.accept(this, env);
		}
		if (n.s2 != null) {
			n.s2.accept(this, env);
		}
		return null;
	}

	public Type visit(While n, SymbolTable env) {
		System.out.println("While");
		Type tmp = n.e.accept(this, env);
		if (!Types.subType(tmp, booleanType, env)) {
			System.out.println("Type error: expected "
					+ Types.toString(booleanType) + " and found "
					+ Types.toString(tmp));
			System.exit(1);
		}
		n.s.accept(this, env);
		return null;
	}

	public Type visit(Print n, SymbolTable env) {
		System.out.println("Print");
		Type tmp = n.e.accept(this, env);
		if (!Types.subType(tmp, intType, env)) {
			System.out.println("Type error: expected "
					+ Types.toString(intType) + " and found "
					+ Types.toString(tmp));
			System.exit(1);
		}
		return null;
	}

	public Type visit(Assign n, SymbolTable env) {
		System.out.println("Assign");
		Type left = n.i.accept(this, env);
		Type right = n.e.accept(this, env);
		if (!Types.subType(right, left, env)) {
			System.out.println("Type error: expected " + Types.toString(left)
					+ " and found " + Types.toString(right));
			System.exit(1);
		}
		return null;
	}

	public Type visit(ArrayAssign n, SymbolTable env) {
		System.out.println("ArrayAssign");
		Type left = n.i.accept(this, env);

		Type index = n.e1.accept(this, env);
		Type right = n.e2.accept(this, env);

		if (!Types.subType(left, intArrayType, env)) {
			System.out.println("Type error: expected "
					+ Types.toString(intArrayType) + " and found "
					+ Types.toString(left));
			System.exit(1);
		}
		if (!Types.subType(index, intType, env)) {
			System.out.println("Type error: expected "
					+ Types.toString(intType) + " and found "
					+ Types.toString(index));
			System.exit(1);
		}
		if (!Types.subType(right, intType, env)) {
			System.out.println("Type error: expected "
					+ Types.toString(intType) + " and found "
					+ Types.toString(right));
			System.exit(1);
		}
		return null;
	}

	public Type visit(And n, SymbolTable env) {
		Type left = n.e1.accept(this, env);
		Type right = n.e2.accept(this, env);
		if (!Types.subType(left, booleanType, env)) {
			System.out.println("Type error: expected "
					+ Types.toString(booleanType) + " and found "
					+ Types.toString(left));
			System.exit(1);
		}
		if (!Types.subType(right, booleanType, env)) {
			System.out.println("Type error: expected "
					+ Types.toString(booleanType) + " and found "
					+ Types.toString(right));
			System.exit(1);
		}
		return left;
	}

	public Type visit(LessThan n, SymbolTable env) {
		Type left = n.e1.accept(this, env);
		Type right = n.e2.accept(this, env);
		if (!Types.subType(left, intType, env)) {
			System.out.println("Type error: expected "
					+ Types.toString(intType) + " and found "
					+ Types.toString(left));
			System.exit(1);
		}
		if (!Types.subType(right, intType, env)) {
			System.out.println("Type error: expected "
					+ Types.toString(intType) + " and found "
					+ Types.toString(right));
			System.exit(1);
		}
		return booleanType;
	}

	public Type visit(Plus n, SymbolTable env) {
		Type left = n.e1.accept(this, env);
		Type right = n.e2.accept(this, env);
		if (!Types.subType(right, intType, env)) {
			System.out.println("Type error: expected "
					+ Types.toString(intType) + " and found "
					+ Types.toString(right));
			System.exit(1);
		}
		if (!Types.subType(left, intType, env)) {
			System.out.println("Type error: expected "
					+ Types.toString(intType) + " and found "
					+ Types.toString(left));
			System.exit(1);
		}
		return intType;
	}

	public Type visit(Minus n, SymbolTable env) {
		Type left = n.e1.accept(this, env);
		Type right = n.e2.accept(this, env);
		if (!Types.subType(right, intType, env)) {
			System.out.println("Type error: expected "
					+ Types.toString(intType) + " and found "
					+ Types.toString(right));
			System.exit(1);
		}
		if (!Types.subType(left, intType, env)) {
			System.out.println("Type error: expected "
					+ Types.toString(intType) + " and found "
					+ Types.toString(left));
			System.exit(1);
		}
		return intType;
	}

	public Type visit(Times n, SymbolTable env) {
		Type left = n.e1.accept(this, env);
		Type right = n.e2.accept(this, env);
		if (!Types.subType(right, intType, env)) {
			System.out.println("Type error: expected "
					+ Types.toString(intType) + " and found "
					+ Types.toString(right));
			System.exit(1);
		}
		if (!Types.subType(left, intType, env)) {
			System.out.println("Type error: expected "
					+ Types.toString(intType) + " and found "
					+ Types.toString(left));
			System.exit(1);
		}
		return intType;
	}

	public Type visit(ArrayLookup n, SymbolTable env) {
		Type t1 = n.e1.accept(this, env);
		Type t2 = n.e2.accept(this, env);
		if (!Types.subType(t1, intArrayType, env)) {
			System.out.println("Type error: expected "
					+ Types.toString(intArrayType) + " and found "
					+ Types.toString(t1));
			System.exit(1);
		}
		if (!Types.subType(t2, intType, env)) {
			System.out.println("Type error: expected "
					+ Types.toString(intType) + " and found "
					+ Types.toString(t2));
			System.exit(1);
		}
		return intType;
	}

	public Type visit(ArrayLength n, SymbolTable env) {
		Type t = n.e.accept(this, env);
		if (!Types.subType(t, intArrayType, env)) {
			System.out.println("Type error: expected "
					+ Types.toString(intArrayType) + " and found "
					+ Types.toString(t));
			System.exit(1);
		}
		return intType;
	}

	public Type visit(Call n, SymbolTable env) {
		// class name
		String caller = Types.toString(n.e.accept(this, env));
		if (!env.classes.containsKey(caller)) {
			System.out.println("can't find " + caller);
			System.exit(1);
		}
		Structure childClass = env.classes.get(caller);
		while (true) {
			if (childClass == null) {
				System.out.println("can't find " + n.i + " for "
						+ Types.toString(n.e.accept(this, env)));
				System.exit(1);
			}
			if (childClass.getMethod(n.i.s) != null) {
				if (signatureCompare(n.el, childClass.getMethod(n.i.s).formalTypes,
							env)) {
					return childClass.getMethod(n.i.s).returnType;
				}
			}
			childClass = env.getClass(childClass.parent);
		}
	}

	private boolean signatureCompare(ExpList el, List<Type> formalTypes, SymbolTable env) {
		// check if el is subtype of formal type
		List<Type> list = new ArrayList<Type>();
		for (Exp e : el) {
			list.add(e.accept(this, env));
		}
		for (int i = 0, j = 0; j < formalTypes.size(); i++, j++) {
			if (!Types.subType(list.get(i), formalTypes.get(j), env))
				return false;
		}
		return true;
	}

	public Type visit(IntegerLiteral n, SymbolTable env) {
		return intType;
	}

	public Type visit(True n, SymbolTable env) {
		return booleanType;
	}

	public Type visit(False n, SymbolTable env) {
		return booleanType;
	}

	public Type visit(IdentifierExp n, SymbolTable env) {
		Type tmp = env.getVariable(n.s);
		if (tmp == null) {
			System.out.println("undefined variable" + n.s);
			System.exit(1);
		}
		return env.getVariable(n.s);
	}

	public Type visit(This n, SymbolTable env) {
		return new IdentifierType(thisClass);
	}

	public Type visit(NewArray n, SymbolTable env) {
		return intArrayType;
	}

	public Type visit(NewObject n, SymbolTable env) {
		return new IdentifierType(n.i.s);
	}

	public Type visit(Not n, SymbolTable env) {
		return booleanType;
	}

	public Type visit(Identifier n, SymbolTable env) {
		if (env.getVariable(n.s) == null) {
			System.out.println("undefined variable" + n.s);
			System.exit(1);
		}
		return env.getVariable(n.s);
	}

}

class DecElaborator implements Visitor<SymbolTable, SymbolTable> {

	public SymbolTable visit(Program n, SymbolTable env) {
		env = n.m.accept(this, env);
		for (ClassDecl c : n.cl) {
			env = c.accept(this, env);
		}
		return env;
	}

	public SymbolTable visit(MainClass n, SymbolTable env) {
		Structure _main = new Structure(null, null);
		_main.addMethod("main", new Signature());
		env.addClass(n.i1.s, _main);
		return env;
	}

	public SymbolTable visit(ClassDeclSimple n, SymbolTable env) {
		Structure _structure = new Structure(null, n.vl);
		for (MethodDecl m : n.ml) {
			_structure.addMethod(m.i.s, new Signature(m));
		}
		env.addClass(n.i.s, _structure);
		return env;
	}

	public SymbolTable visit(ClassDeclExtends n, SymbolTable env) {
		Structure _structure = new Structure(n.j.s, n.vl);
		for (MethodDecl m : n.ml) {
			_structure.addMethod(m.i.s, new Signature(m));
		}
		env.addClass(n.i.s, _structure);
		return env;
	}

	public SymbolTable visit(VarDecl n, SymbolTable env) {
		env.addVariable(n.i.s, n.t);
		return env;
	}

	public SymbolTable visit(MethodDecl n, SymbolTable env) {
		// start a local scope for typechecking and then remove it!
		env.addLocalScope();
		for (Formal f : n.fl) {
			env = f.accept(this, env);
		}
		for (VarDecl v : n.vl) {
			env = v.accept(this, env);
		}
		n.accept(new TypeChecker(), env);
		env.removeLocalScope();
		return env;
	}

	public SymbolTable visit(Formal n, SymbolTable env) {
		env.addVariable(n.i.s, n.t);
		return env;
	}

	public SymbolTable visit(IntArrayType n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(BooleanType n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(IntegerType n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(IdentifierType n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(Block n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(If n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(While n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(Print n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(Assign n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(ArrayAssign n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(And n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(LessThan n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(Plus n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(Minus n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(Times n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(ArrayLookup n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(ArrayLength n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(Call n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(IntegerLiteral n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(True n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(False n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(IdentifierExp n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(This n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(NewArray n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(NewObject n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(Not n, SymbolTable env) {
		return null;
	}

	public SymbolTable visit(Identifier n, SymbolTable env) {
		return null;
	}
}

class Signature {
	// the "type" of methods in minijava
	Type returnType;
	List<Type> formalTypes;

	public Signature() {
		returnType = new IdentifierType("Void");
		formalTypes = new ArrayList<Type>();
	}

	public Signature(MethodDecl m) {
		returnType = m.t;
		formalTypes = new ArrayList<Type>();
		for (Formal f : m.fl) {
			formalTypes.add(f.t);
		}
	}

	public String toString() {
		String sig = "";
		for (Type t : formalTypes) {
			sig = sig + Types.toString(t) + "  ";
		}
		sig = sig + " -> " + Types.toString(returnType);
		return sig;
	}

}

class Structure {
	// the "type" of a class in minijava
	String parent;
	VarDeclList fields;
	Map<String, Signature> methods;

	public Structure(String p, VarDeclList f) {
		parent = p;
		fields = f;
		methods = new HashMap<String, Signature>();

	}
	public void addMethod(String methodName, Signature methodSignature) {
		// TODO allow overloading
		if (methods.containsKey(methodName)) {
			System.out.println("<< " + methodName + " >> already defined");
			System.exit(1);
		}
		methods.put(methodName, methodSignature);
	}

	public Signature getMethod(String methodName) {
		return methods.get(methodName);
	}

}

/**
 * One symbol table is constructed for one file.
 */
class SymbolTable {
	Map<String, Structure> classes = new HashMap<String, Structure>();
	LinkedList<Map<String, Type>> localScopes = new LinkedList<Map<String, Type>>();

	public void addClass(String className, Structure classStructure) {
		if (classes.containsKey(className)) {
			System.out.println("<< " + className + " >> already defined");
			System.exit(1);
		}
		classes.put(className, classStructure);
	}

	public Structure getClass(String className) {
		return classes.get(className);
	}

	public void addLocalScope() {
		localScopes.add(new HashMap<String, Type>());
	}

	public void removeLocalScope() {
		localScopes.removeLast();
	}

	public void addVariable(String varName, Type t) {
		// these variables are local variables inside one method
		Map<String, Type> localScope = localScopes.getLast();
		if (localScope.containsKey(varName)) {
			System.out.println("<< " + varName
					+ " >> already defined in local scope");
			System.exit(1);
		}
		localScope.put(varName, t);
	}

	public Type getVariable(String varName) {
		// the local variable, that has the same name with one field, will hide the field
		Iterator<Map<String, Type>> iterator = localScopes.descendingIterator();
		Map<String, Type> m;
		while (iterator.hasNext()) {
			m = iterator.next();
			if (m.containsKey(varName))
				return m.get(varName);
		}
		return null;
	}
}

class Types {

	public static String toString(Type t) {
		if (t instanceof IntegerType)
			return "int";
		if (t instanceof BooleanType)
			return "bool";
		if (t instanceof IntArrayType)
			return "[int]";
		if (t instanceof IdentifierType)
			return ((IdentifierType) t).s;
		return "void";

	}

	/**
	 * check if t1 is one subtype of t2
	 */
	public static boolean subType(Type t1, Type t2, SymbolTable env) {
		if (t1 instanceof IntegerType && t2 instanceof IntegerType)
			return true;
		if (t1 instanceof BooleanType && t2 instanceof BooleanType)
			return true;
		if (t1 instanceof IntArrayType && t2 instanceof IntArrayType)
			return true;
		if (t1 instanceof IdentifierType && t2 instanceof IdentifierType) {
			String s1 = ((IdentifierType) t1).s;
			String s2 = ((IdentifierType) t2).s;
			if (s1.equals(s2))
				return true;
			String parent = env.classes.get(s1).parent;
			if (parent == null)
				return false;
			return subType(new IdentifierType(parent), t2, env);
		}
		return false;
	}

}
