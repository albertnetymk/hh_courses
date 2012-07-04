package lab4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import syntaxtree.While;
import visitor.Visitor;
import astLlvm.LlAdd;
import astLlvm.LlAlloca;
import astLlvm.LlAnd;
import astLlvm.LlArray;
import astLlvm.LlBitcast;
import astLlvm.LlBranch;
import astLlvm.LlCall;
import astLlvm.LlCloseDefinition;
import astLlvm.LlConditionalBranch;
import astLlvm.LlConstantDeclaration;
import astLlvm.LlDefine;
import astLlvm.LlExternalDeclaration;
import astLlvm.LlFalse;
import astLlvm.LlFunctionType;
import astLlvm.LlGetElementPointer;
import astLlvm.LlIcmp;
import astLlvm.LlInstruction;
import astLlvm.LlIntArrayType;
import astLlvm.LlIntegerLiteral;
import astLlvm.LlLabel;
import astLlvm.LlLabelValue;
import astLlvm.LlLoad;
import astLlvm.LlMul;
import astLlvm.LlNamedValue;
import astLlvm.LlPointer;
import astLlvm.LlPrimitiveType;
import astLlvm.LlRet;
import astLlvm.LlStore;
import astLlvm.LlStructure;
import astLlvm.LlSub;
import astLlvm.LlTrue;
import astLlvm.LlType;
import astLlvm.LlValue;

public class CodeGenerator implements Visitor<LlValue, CodeSymbolTable> {
	private static String thisClass;

	private List<LlInstruction> assembler;
	private int tmpNr;
	private int LabelNr;

	public static void translate(Program p) {
		// create the symbol table
		CodeSymbolTable env = new CodeSymbolTable();
		// fill out this symbol table use CodeElaborator
		env = p.accept(new CodeDecElaborator(), env);

		CodeGenerator codeGenerator = new CodeGenerator();
		codeGenerator.assembler.add(new LlConstantDeclaration(
				"@.formatting.string",
				"private constant [4 x i8] c\"%d\\0A\\00\""));

		// declaration of all classes
		Set<String> classes = env.classSet();
		for(String c : classes) {
			codeGenerator.assembler.add(new LlConstantDeclaration("%"+c,
						"type " + env.getClass(c).getFieldsType(env)));
		}

		// generate code for the complete program
		// collects a list of assembler instructions in the field assembler
		p.accept(codeGenerator, env);

		// the last instruction in assembler is the declaration of the
		// library functions
		// printf
		List<LlType> pts = new LinkedList<LlType>();
		pts.add(new LlPointer(LlPrimitiveType.I8));
		pts.add(LlPrimitiveType.DOTDOTDOT);
		codeGenerator.assembler.add(new LlExternalDeclaration("@printf",
				LlPrimitiveType.I32, pts));
		// malloc
		pts.clear();
		pts.add(LlPrimitiveType.I32);
		codeGenerator.assembler.add(new LlExternalDeclaration("@malloc",
				new LlPointer(LlPrimitiveType.I8), pts));

		// for testing,
		// print the list of assembler instructions
		for (LlInstruction instr : codeGenerator.assembler) {
			System.out.println(instr);
		}
	}

	public CodeGenerator() {
		assembler = new LinkedList<LlInstruction>();
		tmpNr = 0;
		LabelNr = 0;

	}

	public LlValue visit(Program n, CodeSymbolTable env) {
		n.m.accept(this, env);
		for (ClassDecl c : n.cl)
			c.accept(this, env);
		return null;
	}

	public LlValue visit(MainClass n, CodeSymbolTable env) {
		// add the definition of the main function:
		LlNamedValue returnValue = new LlNamedValue("%retval", new LlPointer(
				LlPrimitiveType.I32));
		assembler.add(new LlDefine("@main", LlPrimitiveType.I32,
				new LinkedList<LlNamedValue>()));
		assembler.add(new LlLabel(new LlLabelValue("entry")));
		assembler.add(new LlAlloca(returnValue, LlPrimitiveType.I32,
				new LinkedList<LlValue>()));
		assembler.add(new LlStore(new LlIntegerLiteral(0), returnValue));

		// generate the assembler instructions for the statement in main
		n.s.accept(this, env);

		assembler.add(new LlLoad(new LlNamedValue("%0", LlPrimitiveType.I32),
				returnValue));
		assembler.add(new LlRet(new LlNamedValue("%0", LlPrimitiveType.I32)));
		assembler.add(new LlCloseDefinition());
		return null;
	}

	public LlValue visit(ClassDeclSimple n, CodeSymbolTable e) {
		thisClass = n.i.s;
		for (MethodDecl m : n.ml) {
			m.accept(this, e);
		}
		return null;
	}

	public LlValue visit(ClassDeclExtends n, CodeSymbolTable e) {
		thisClass = n.i.s;
		for (MethodDecl m : n.ml) {
			m.accept(this, e);
		}
		return null;
	}

	public LlValue visit(MethodDecl n, CodeSymbolTable e) {
		e.addLocalScope();
		List<LlNamedValue> parameters = new ArrayList<LlNamedValue>();
		LlNamedValue value;

		// add the first parameter, which is the object
		parameters.add(new LlNamedValue("%object" + "_para",
					new LlPointer(e.getClass(thisClass).toLlType())));
		// add the ordinary parameter
		for (Formal f : n.fl) {
			parameters.add(new LlNamedValue("%" + f.i.s + "_para", ConvertType
					.convertType(f.t, e)));
		}
		// definition of the method
		assembler.add(new LlDefine("@" + thisClass + "." + n.i.s, ConvertType
				.convertType(n.t, e), parameters));
		assembler.add(new LlLabel(new LlLabelValue("entry")));

		// TODO need to be improved
		value = new LlNamedValue("%object" + "_para",
				new LlPointer(e.getClass(thisClass).toLlType()));
		e.addVariable(value.toString(), value);
		for (Formal p : n.fl) {
			p.accept(this, e);
		}
		for (VarDecl v : n.vl) {
			v.accept(this, e);
		}
		for (Statement s : n.sl) {
			s.accept(this, e);
		}
		assembler.add(new LlRet(n.e.accept(this, e)));
		assembler.add(new LlCloseDefinition());
		e.removeLocalScope();
		return null;
	}

	public LlValue visit(VarDecl n, CodeSymbolTable e) {
		// local variables
		if (n.t instanceof IntArrayType || n.t instanceof IdentifierType) {
			// reference variables
			LlNamedValue pointer = new LlNamedValue("%" + n.i.s + "_addr",
					new LlPointer(new LlPointer(ConvertType.convertType(n.t, e))));
			assembler.add(new LlAlloca(pointer, new LlPointer(ConvertType
					.convertType(n.t, e)), new LinkedList<LlValue>()));
			e.addVariable(pointer.toString(), pointer);
		} else {
			// value variables
			LlNamedValue pointer = new LlNamedValue("%" + n.i.s + "_addr",
					new LlPointer(ConvertType.convertType(n.t, e)));
			assembler.add(new LlAlloca(pointer, ConvertType.convertType(n.t, e),
					new LinkedList<LlValue>()));
			e.addVariable(pointer.toString(), pointer);
		}
		return null;
	}

	public LlValue visit(Formal n, CodeSymbolTable e) {
		LlNamedValue pointer = new LlNamedValue("%" + n.i.s + "_addr",
				new LlPointer(ConvertType.convertType(n.t, e)));
		LlNamedValue value = new LlNamedValue("%" + n.i.s + "_para",
				ConvertType.convertType(n.t, e));
		e.addVariable(pointer.toString(), pointer);
		assembler.add(new LlAlloca(pointer, ConvertType.convertType(n.t, e),
				new LinkedList<LlValue>()));
		assembler.add(new LlStore(value, pointer));
		return null;
	}

	public LlValue visit(IntArrayType n, CodeSymbolTable e) {
		return null;
	}

	public LlValue visit(BooleanType n, CodeSymbolTable e) {
		return null;
	}

	public LlValue visit(IntegerType n, CodeSymbolTable e) {
		return null;
	}

	public LlValue visit(IdentifierType n, CodeSymbolTable e) {
		return null;
	}

	public LlValue visit(Block n, CodeSymbolTable e) {
		for (Statement s : n.sl) {
			s.accept(this, e);
		}
		return null;
	}

	public LlValue visit(If n, CodeSymbolTable e) {
		LlValue cond = n.e.accept(this, e);
		LlLabelValue ifThen = new LlLabelValue("then" + (LabelNr));
		LlLabelValue ifElse = new LlLabelValue("else" + (LabelNr));
		LlLabelValue ifEnd = new LlLabelValue("end" + (LabelNr++));

		assembler.add(new LlConditionalBranch(cond, ifThen, ifElse));

		assembler.add(new LlLabel(ifThen));
		n.s1.accept(this, e);
		assembler.add(new LlBranch(ifEnd));

		assembler.add(new LlLabel(ifElse));
		n.s2.accept(this, e);
		assembler.add(new LlBranch(ifEnd));

		assembler.add(new LlLabel(ifEnd));
		return null;
	}

	public LlValue visit(While n, CodeSymbolTable e) {
		LlLabelValue whileCond = new LlLabelValue("cond" + (LabelNr));
		LlLabelValue whileThen = new LlLabelValue("then" + (LabelNr));
		LlLabelValue whileEnd = new LlLabelValue("end" + (LabelNr++));
		assembler.add(new LlBranch(whileCond));

		assembler.add(new LlLabel(whileCond));
		LlValue cond = n.e.accept(this, e);
		assembler.add(new LlConditionalBranch(cond, whileThen, whileEnd));

		assembler.add(new LlLabel(whileThen));
		n.s.accept(this, e);
		assembler.add(new LlBranch(whileCond));

		assembler.add(new LlLabel(whileEnd));

		return null;
	}

	public LlValue visit(Print n, CodeSymbolTable env) {
		LlValue v = n.e.accept(this, env);
		// for getelementptr:
		LlNamedValue lhs = new LlNamedValue("%tmp" + (tmpNr++), new LlPointer(
				LlPrimitiveType.I8));
		LlNamedValue src = new LlNamedValue("@.formatting.string",
				new LlPointer(new LlArray(4, LlPrimitiveType.I8)));
		List<LlValue> places = new LinkedList<LlValue>();
		places.add(new LlIntegerLiteral(0));
		places.add(new LlIntegerLiteral(0));
		List<LlType> pts = new LinkedList<LlType>();
		pts.add(new LlPointer(LlPrimitiveType.I8));
		List<LlValue> args = new LinkedList<LlValue>();
		args.add(lhs);
		args.add(v);
		assembler.add(new LlGetElementPointer(lhs, src, places));

		// calling printf:
		assembler
				.add(new LlCall(new LlNamedValue("%tmp" + (tmpNr++),
						LlPrimitiveType.I32), LlPrimitiveType.I32,
						new LlPointer(new LlFunctionType(LlPrimitiveType.I32,
								pts)), "@printf", args));
		return null;
	}

	public LlValue visit(Assign n, CodeSymbolTable e) {
		LlValue exp = n.e.accept(this, e);
		LlNamedValue pointer = (LlNamedValue) n.i.accept(this, e);
		assembler.add(new LlStore(exp, pointer));
		return null;
	}

	public LlValue visit(ArrayAssign n, CodeSymbolTable e) {
		LlNamedValue pointerToArray = (LlNamedValue) n.i.accept(this, e);
		LlNamedValue _array = new LlNamedValue("%tmp" + (tmpNr++), ((LlPointer) pointerToArray.type).content);
		LlValue index = n.e1.accept(this, e);
		LlValue value = n.e2.accept(this, e);
		LlNamedValue pointer = new LlNamedValue("%tmp" + (tmpNr++), new LlPointer(
				LlPrimitiveType.I32));
		List<LlValue> list = new LinkedList<LlValue>();
		list.add(new LlIntegerLiteral(0));
		list.add(new LlIntegerLiteral(1));
		list.add(index);
		assembler.add(new LlLoad(_array, pointerToArray));
		assembler.add(new LlGetElementPointer(pointer, _array, list));
		assembler.add(new LlStore(value, pointer));
		return null;
	}

	// maybe this is internal implementation of "and"
	/*
	public LlValue visit(And n, CodeSymbolTable e) {
		LlValue v1 = n.e1.accept(this, e);
		LlValue v2 = n.e2.accept(this, e);
		LlNamedValue lhs = new LlNamedValue("%tmp" + (tmpNr++),
				LlPrimitiveType.I1);
		LlNamedValue pointer = new LlNamedValue("%tmp" + (tmpNr++),
				new LlPointer(LlPrimitiveType.I1));
		assembler.add(new LlAlloca(pointer, LlPrimitiveType.I1,
				new ArrayList<LlValue>()));
		LlLabelValue andThen = new LlLabelValue("then" + (LabelNr));
		LlLabelValue andElse = new LlLabelValue("else" + (LabelNr));
		LlLabelValue andEnd = new LlLabelValue("end" + (LabelNr++));
		assembler.add(new LlConditionalBranch(v1, andThen, andElse));
		assembler.add(new LlLabel(andThen));
		LlLabelValue andandThen = new LlLabelValue("then" + (LabelNr));
		LlLabelValue andandElse = new LlLabelValue("else" + (LabelNr));
		LlLabelValue andandEnd = new LlLabelValue("end" + (LabelNr++));

		assembler.add(new LlConditionalBranch(v2, andandThen, andandElse));
		assembler.add(new LlLabel(andandThen));
		assembler.add(new LlStore(v1, pointer));
		assembler.add(new LlBranch(andandEnd));

		assembler.add(new LlLabel(andandElse));
		assembler.add(new LlStore(v2, pointer));
		assembler.add(new LlBranch(andandEnd));

		assembler.add(new LlLabel(andandEnd));

		assembler.add(new LlBranch(andEnd));

		assembler.add(new LlLabel(andElse));
		assembler.add(new LlStore(v1, pointer));

		assembler.add(new LlBranch(andEnd));

		assembler.add(new LlLabel(andEnd));
		assembler.add(new LlLoad(lhs, pointer));
		return lhs;
	}
	*/
	public LlValue visit(And n, CodeSymbolTable e) {
		LlValue v1 = n.e1.accept(this, e);
		LlValue v2 = n.e2.accept(this, e);
		LlNamedValue lhs = new LlNamedValue("%tmp" + (tmpNr++),
				LlPrimitiveType.I1);
		assembler.add(new LlAnd(lhs, v1, v2));
		return lhs;
	}

	public LlValue visit(LessThan n, CodeSymbolTable e) {
		LlValue v1 = n.e1.accept(this, e);
		LlValue v2 = n.e2.accept(this, e);
		LlNamedValue lhs = new LlNamedValue("%tmp" + (tmpNr++),
				LlPrimitiveType.I1);
		assembler.add(new LlIcmp(lhs, LlIcmp.SLT, LlPrimitiveType.I32, v1, v2));
		return lhs;
	}

	public LlValue visit(Plus n, CodeSymbolTable e) {
		LlValue v1 = n.e1.accept(this, e);
		LlValue v2 = n.e2.accept(this, e);
		LlNamedValue lhs = new LlNamedValue("%tmp" + (tmpNr++),
				LlPrimitiveType.I32);
		assembler.add(new LlAdd(lhs, LlPrimitiveType.I32, v1, v2));
		return lhs;
	}

	public LlValue visit(Minus n, CodeSymbolTable e) {
		LlValue v1 = n.e1.accept(this, e);
		LlValue v2 = n.e2.accept(this, e);
		LlNamedValue lhs = new LlNamedValue("%tmp" + (tmpNr++),
				LlPrimitiveType.I32);
		assembler.add(new LlSub(lhs, LlPrimitiveType.I32, v1, v2));
		return lhs;
	}

	public LlValue visit(Times n, CodeSymbolTable e) {
		LlValue v1 = n.e1.accept(this, e);
		LlValue v2 = n.e2.accept(this, e);
		LlNamedValue lhs = new LlNamedValue("%tmp" + (tmpNr++),
				LlPrimitiveType.I32);
		assembler.add(new LlMul(lhs, LlPrimitiveType.I32, v1, v2));
		return lhs;
	}

	public LlValue visit(ArrayLookup n, CodeSymbolTable e) {
		LlNamedValue _array = (LlNamedValue) n.e1.accept(this, e);
		LlValue v2 = n.e2.accept(this, e);
		LlNamedValue lhs = new LlNamedValue("%tmp" + (tmpNr++),
				LlPrimitiveType.I32);
		LlNamedValue pointer = new LlNamedValue("%tmp" + (tmpNr++),
				new LlPointer(LlPrimitiveType.I32));
		List<LlValue> position = new LinkedList<LlValue>();
		position.add(new LlIntegerLiteral(0));
		position.add(new LlIntegerLiteral(1));
		position.add(v2);
		assembler.add(new LlGetElementPointer(pointer, _array, position));
		assembler.add(new LlLoad(lhs, pointer));
		return lhs;
	}

	public LlValue visit(ArrayLength n, CodeSymbolTable e) {
		LlNamedValue _array = ((LlNamedValue)n.e.accept(this, e));
		LlNamedValue lhs = new LlNamedValue("%tmp" + (tmpNr++),
				LlPrimitiveType.I32);
		LlNamedValue pointer = new LlNamedValue("%tmp" + (tmpNr++),
				new LlPointer(LlPrimitiveType.I32));
		List<LlValue> position = new LinkedList<LlValue>();
		position.add(new LlIntegerLiteral(0));
		position.add(new LlIntegerLiteral(0));
		assembler.add(new LlGetElementPointer(pointer, _array, position));
		assembler.add(new LlLoad(lhs, pointer));
		return lhs;
	}

	public LlValue visit(Call n, CodeSymbolTable e) {
		LlValue caller = n.e.accept(this, e);
		// because of the type checker, the down casting is right
		LlNamedValue method = e.getClass(((LlStructure) ((LlPointer) caller.type).content).name)
				.getMethod(n.i.s);
		// return type of this method
		LlType returnType = ((LlFunctionType) method.type).resultType;

		List<LlValue> parameters = new ArrayList<LlValue>();
		parameters.add(caller);
		for (Exp f : n.el) {
			parameters.add(f.accept(this, e));
		}

		LlNamedValue lhs = new LlNamedValue("%tmp" + (tmpNr++),
				returnType);
		assembler.add(new LlCall(lhs, returnType, null, method.name,
				parameters));
		return lhs;
	}

	public LlValue visit(IntegerLiteral n, CodeSymbolTable e) {
		return new LlIntegerLiteral(n.i);
	}

	public LlValue visit(True n, CodeSymbolTable e) {
		return new LlTrue();
	}

	public LlValue visit(False n, CodeSymbolTable e) {
		return new LlFalse();
	}

	public LlValue visit(IdentifierExp n, CodeSymbolTable e) {
		LlNamedValue pointer = e.getVariable("%" + n.s + "_addr");
		if (pointer != null) {
			// local variable
			LlNamedValue lhs = new LlNamedValue("%tmp" + (tmpNr++),
					((LlPointer) pointer.type).content);
			assembler.add(new LlLoad(lhs, pointer));
			return lhs;
		} else {
			// field
			LlNamedValue _object = e.getVariable("%object" + "_para");
			Structure _class = e.getClass(((LlStructure) ((LlPointer) _object.type).content).name);
			List<LlValue> position = new LinkedList<LlValue>();
			int offset = _class.getOffset(n.s);
			position.add(new LlIntegerLiteral(0));
			position.add(new LlIntegerLiteral(offset));

			LlType type = ConvertType.convertType(_class.getFieldType(offset), e);
			if (type instanceof LlPrimitiveType) {
				LlNamedValue lhs = new LlNamedValue("%tmp" + (tmpNr++), type);
				pointer = new LlNamedValue("%tmp" + (tmpNr++), new LlPointer(type));
				assembler.add(new LlGetElementPointer(pointer, _object, position));
				assembler.add(new LlLoad(lhs, pointer));
				return lhs;
			} else {
				LlNamedValue lhs = new LlNamedValue("%tmp" + (tmpNr++), new LlPointer(type));
				LlNamedValue tmp = new LlNamedValue("%tmp" + (tmpNr++), new LlPointer(type));
				pointer = new LlNamedValue("%tmp" + (tmpNr++), new LlPointer(new LlPointer(type)));
				assembler.add(new LlGetElementPointer(tmp, _object, position));
				assembler
						.add(new LlBitcast(pointer, tmp, new LlPointer(new LlPointer(type))));
				assembler.add(new LlLoad(lhs, pointer));
				return lhs;
			}
		}
	}

	public LlValue visit(This n, CodeSymbolTable e) {
		LlNamedValue pointer = e.getVariable("%object" + "_para");
		return pointer;
	}

	public LlValue visit(NewArray n, CodeSymbolTable e) {
		LlNamedValue length = (LlNamedValue) n.e.accept(this, e);
		// storage needed merely for this array
		LlNamedValue bytes = new LlNamedValue("%tmp" + (tmpNr++), LlPrimitiveType.I32);
		// storage needed for this array and its length
		LlNamedValue size = new LlNamedValue("%tmp" + (tmpNr++), LlPrimitiveType.I32);
		// this return value of malloc
		LlNamedValue pointer = new LlNamedValue("%tmp" + (tmpNr++), new LlPointer(LlPrimitiveType.I8));
		LlNamedValue lhs = new LlNamedValue("%tmp" + (tmpNr++), new LlPointer(new LlIntArrayType(0)));
		LlNamedValue lengthAddress = new LlNamedValue("%tmp" + (tmpNr++), new LlPointer(LlPrimitiveType.I32));
		List<LlValue> position = new ArrayList<LlValue>();
		position.add(new LlIntegerLiteral(0));
		position.add(new LlIntegerLiteral(0));

		List<LlValue> parameters = new ArrayList<LlValue>();
		parameters.add(size);
		assembler.add(new LlMul(bytes, LlPrimitiveType.I32, length, new LlIntegerLiteral(4)));
		assembler.add(new LlAdd(size, LlPrimitiveType.I32, bytes, new LlIntegerLiteral(4)));
		assembler.add(new LlCall(pointer, new LlPointer(LlPrimitiveType.I8), null,
				"@malloc", parameters));
		assembler.add(new LlBitcast(lhs, pointer, new LlPointer(new LlIntArrayType(0))));
		assembler.add(new LlGetElementPointer(lengthAddress, lhs, position));
		assembler.add(new LlStore(length, lengthAddress));

		return lhs;
	}

	public LlValue visit(NewObject n, CodeSymbolTable e) {
		Structure name = e.getClass(n.i.s); 		
		LlNamedValue tmp = new LlNamedValue("%tmp" + (tmpNr++), new LlPointer(
					LlPrimitiveType.I8));
		LlNamedValue object = new LlNamedValue("%tmp" + (tmpNr++),
				new LlPointer(name.toLlType()));

		List<LlValue> parameters = new LinkedList<LlValue>();
		parameters.add(name.getObjectSize());
		assembler.add(new LlCall(tmp, new LlPointer(LlPrimitiveType.I8), null,
					"@malloc", parameters));
		assembler.add(new LlBitcast(object, tmp, new LlPointer(name.toLlType())));
		return object;
	}

	public LlValue visit(Not n, CodeSymbolTable e) {
		LlValue v = n.e.accept(this, e);
		LlNamedValue lhs = new LlNamedValue("%tmp" + (tmpNr++),
				LlPrimitiveType.I1);
		LlNamedValue pointer = new LlNamedValue("%tmp" + (tmpNr++),
				new LlPointer(LlPrimitiveType.I1));
		assembler.add(new LlAlloca(pointer, LlPrimitiveType.I1,
					new ArrayList<LlValue>()));
		LlLabelValue notThen = new LlLabelValue("then" + (LabelNr));
		LlLabelValue notElse = new LlLabelValue("else" + (LabelNr));
		LlLabelValue notEnd = new LlLabelValue("end" + (LabelNr++));

		assembler.add(new LlConditionalBranch(v, notThen, notElse));
		assembler.add(new LlLabel(notThen));
		assembler.add(new LlStore(new LlFalse(), pointer));
		assembler.add(new LlBranch(notEnd));

		assembler.add(new LlLabel(notElse));
		assembler.add(new LlStore(new LlTrue(), pointer));
		assembler.add(new LlBranch(notEnd));

		assembler.add(new LlLabel(notEnd));
		assembler.add(new LlLoad(lhs, pointer));
		return lhs;
	}

	public LlValue visit(Identifier n, CodeSymbolTable e) {
		LlNamedValue pointer = e.getVariable("%" + n.s + "_addr");
		if (pointer != null) {
			// it is one local variable
			return pointer;
		} else {
			// it is one field
			LlNamedValue _object = e.getVariable("%object" + "_para");
			Structure _class = e.getClass(((LlStructure) ((LlPointer) _object.type).content).name);
			// position is the argument for getelementpointer
			List<LlValue> position = new LinkedList<LlValue>();
			// offset of this field
			int offset = _class.getOffset(n.s);
			position.add(new LlIntegerLiteral(0));
			position.add(new LlIntegerLiteral(offset));
			LlType type = ConvertType.convertType(_class.getFieldType(offset), e);
			if ((type instanceof LlPrimitiveType)) {
				pointer = new LlNamedValue("%tmp" + (tmpNr++), new LlPointer(
							type));
				assembler.add(new LlGetElementPointer(pointer, _object, position));
			} else {
				LlNamedValue tmp;
				pointer = new LlNamedValue("%tmp" + (tmpNr++), new LlPointer(
							new LlPointer(type)));
				tmp = new LlNamedValue("%tmp" + (tmpNr++), new LlPointer(type));
				assembler.add(new LlGetElementPointer(tmp, _object, position));
				assembler.add(new LlBitcast(pointer, tmp, new LlPointer(
								new LlPointer(type))));
			}
			return pointer;
		}
	}
}

class CodeDecElaborator implements Visitor<CodeSymbolTable, CodeSymbolTable> {

	public CodeSymbolTable visit(Program n, CodeSymbolTable symbolTable) {
		for (ClassDecl c : n.cl) {
			symbolTable = c.accept(this, symbolTable);
		}
		// TODO deal with use-before-declaration
		// symbolTable.refill();
		return symbolTable;
	}

	public CodeSymbolTable visit(MainClass n, CodeSymbolTable symbolTable) {
		return null;
	}

	public CodeSymbolTable visit(ClassDeclSimple n, CodeSymbolTable symbolTable) {
		Structure theclass = new Structure(n, symbolTable);
		symbolTable.addClass(n.i.s, theclass);
		return symbolTable;
	}

	public CodeSymbolTable visit(ClassDeclExtends n, CodeSymbolTable symbolTable) {
		// LlStructure theclass = new LlStructure(n);
		// symbolTable.addClass(n.i.s, theclass);
		return symbolTable;
	}

	public CodeSymbolTable visit(VarDecl n, CodeSymbolTable symbolTable) {
		return null;
	}

	public CodeSymbolTable visit(MethodDecl n, CodeSymbolTable symbolTable) {
		return null;
	}

	public CodeSymbolTable visit(Formal n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(IntArrayType n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(BooleanType n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(IntegerType n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(IdentifierType n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(Block n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(If n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(While n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(Print n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(Assign n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(ArrayAssign n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(And n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(LessThan n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(Plus n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(Minus n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(Times n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(ArrayLookup n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(ArrayLength n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(Call n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(IntegerLiteral n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(True n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(False n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(IdentifierExp n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(This n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(NewArray n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(NewObject n, CodeSymbolTable env) {
		return null;
	}

	public CodeSymbolTable visit(Not n, CodeSymbolTable env) {

		return null;
	}

	public CodeSymbolTable visit(Identifier n, CodeSymbolTable env) {
		return null;
	}

}

class CodeSymbolTable {
	private Map<String, Structure> classes = new HashMap<String, Structure>();
	private LinkedList<Map<String, LlNamedValue>> localScopes = new LinkedList<Map<String, LlNamedValue>>();

	/*
	   public void refill() {
	   Iterator<String> iterator = classes.keySet().iterator();
	   int i;
	   List<LlType> fields;
	   while (iterator.hasNext()) {
	   Structure thisClass = classes.get(iterator.next());
	   i = 0;
	   fields = new ArrayList<LlType>();
	   for (VarDecl v : thisClass.vl) {
	   LlType type = ConvertType.convertType(v.t, e);
	   fields.add(type);
	   thisClass.offset.put(v.i.s, new LlIntegerLiteral(i));
	   i++;
	   }
	   thisClass.fields = new LlStructure(fields);
	   }
	   }
	   */
	public Set<String> classSet() {
		return classes.keySet();
	}


	public void addClass(String className, Structure structure) {
		classes.put(className, structure);
	}

	public Structure getClass(String className) {
		return classes.get(className);
	}

	public void addLocalScope() {
		localScopes.add(new HashMap<String, LlNamedValue>());
	}

	public void removeLocalScope() {
		localScopes.removeLast();
	}

	public void addVariable(String varName, LlNamedValue t) {
		Map<String, LlNamedValue> localScope = localScopes.getLast();
		localScope.put(varName, t);
	}

	public LlNamedValue getVariable(String varName) {
		Map<String, LlNamedValue> localScope = localScopes.getLast();
		return localScope.get(varName);
	}

}

/**
 * The data structure for class. It contains the information for fields and method.
 */
class Structure {
	private LlStructure llType;
	private String parent;
	private List<VarDecl> vl;
	private Map<String, Integer> offset;
	private Map<String, LlNamedValue> methods = new HashMap<String, LlNamedValue>();

	public LlIntegerLiteral getObjectSize() {
		return new LlIntegerLiteral(4 * vl.size());
	}

	public int getOffset(String name) {
		return offset.get(name);
	}

	public LlNamedValue getMethod(String methodname) {
		return methods.get(methodname);
	}

	public Type getFieldType(int i) {
		return vl.get(i).t;
	}

	public String getFieldsType(CodeSymbolTable e) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		if (vl.size() != 0) {
			for(VarDecl v: vl) {
				sb.append(ConvertType.convertType(v.t, e));
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("}");
		return sb.toString();
	}

	public LlType toLlType() {
		return llType;
	}

	public Structure(ClassDeclSimple thisClass, CodeSymbolTable e) {
		llType = new LlStructure(thisClass.i.s);
		parent = null;
		offset = new HashMap<String, Integer>();

		// fields
		this.vl = thisClass.vl;
		int i=0;
		for (VarDecl v : thisClass.vl) {
			offset.put(v.i.s, i);
			i++;
		}

		// methods
		LlNamedValue method;
		List<LlType> parameters;
		for (MethodDecl m : thisClass.ml) {
			parameters = new ArrayList<LlType>();
			parameters.add(this.toLlType());
			for (Formal f : m.fl) {
				parameters.add(ConvertType.convertType(f.t, e));
			}
			method = new LlNamedValue("@" + thisClass.i.s + "." + m.i.s,
					new LlFunctionType(ConvertType.convertType(m.t, e), parameters));
			this.methods.put(m.i.s, method);
		}
	}

	public Structure(ClassDeclExtends thisClass) {
		// this.thisClass = thisClass.i.s;
		// this.parent = thisClass.j.s;
		// i = 0;
		// List<LlType> fields = new ArrayList<LlType>();
		// for (VarDecl v : thisClass.vl) {
		// LlType type = ConvertType.convertType(v.t, e);
		// fields.add(type);
		// offset.put(v.i.s, i);
		// i++;
		// }
		// this.fields = new LlStructure(fields);
		// for (MethodDecl m : thisClass.ml) {
		// this.methods.put(m.i.s, "@" + this.thisClass + m.i.s);
		// }
	}

}

/**
 * To convert from minijava type to llvm type
 */
class ConvertType {
	public static LlType convertType(Type t, CodeSymbolTable env) {
		if (t instanceof IntegerType)
			return LlPrimitiveType.I32;
		if (t instanceof BooleanType)
			return LlPrimitiveType.I1;
		if (t instanceof IntArrayType)
			return new LlIntArrayType(0);
		if (t instanceof IdentifierType)
			return new LlStructure(((IdentifierType)t).s);
		return LlPrimitiveType.VOID;
	}
}
