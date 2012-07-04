import lab3.TypeChecker;
import lab4.CodeGenerator;

	public class Main{
		public static void main(String [] argv) 
			throws java.io.FileNotFoundException, java.io.IOException{

			minijavaLexer lexer = new minijavaLexer( new java.io.FileReader(argv[0]) );
			lexer.yylex();

			minijavaParser parser = new minijavaParser(lexer);
			parser.parse();

			if (parser.ast!=null){
				// parser.ast.accept(new visitor.PrettyPrintVisitor(),null);
				TypeChecker.typeCheck(parser.ast);
				CodeGenerator.translate(parser.ast);
			}
		}
	}
