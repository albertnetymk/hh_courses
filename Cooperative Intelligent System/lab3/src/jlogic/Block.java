package jlogic;

public class Block {
	static TheoremProver engine=new TheoremProver(100);
	static Clause clause;
	static Clause goal;

	// ToDo:
	// add assertions re male/female people contained in the family tree
	// rewrite daughter/son clauses to make use of male/female assertions
	// add clauses for mother, father, sister, aunt, grandmother/grandfather
	// add more people to the family tree and check if everything works...

	public static void printResult(Substitution result) {
		if(result==null)
			System.out.println("False.\n"+goal);
		else {
			clause=goal.substitute(result);
			System.out.println("True.\n"+goal);
			// System.out.println("True.\n"+clause);
		}
	}

	public static void move(Constant o, Constant src, Constant des) {
		// ontop(o)
		Clause tmp1 = new Clause(null,
				new Predicate[] { new Predicate("ontop",
					new Term[] { o } ) }
					);
		// ontop(des)
		Clause tmp2 = new Clause(null,
				new Predicate[] { new Predicate("ontop",
					new Term[] { des } ) }
					);
		if(null != engine.prove(tmp1) && null != engine.prove(tmp2)) {
			System.out.println("moved");
			engine.removeClause(new Clause(
					new Predicate("below", 
						new Term[] { src, new Constant("Nothing") } )));
			engine.removeClause(new Clause(
					new Predicate("below", 
						new Term[] { des, new Constant("Nothing") } )));
			engine.addClause(new Clause( new Predicate("below", new Term[] { des, o } )));
			engine.addClause(new Clause( new Predicate("below", new Term[] { src, new Constant("Nothing") })));
		}
	}

	public static void main(String[] args) {
		// ontop(X) <- above(Nothing, X)
		engine.addClause(new Clause(
					new Predicate("ontop", new Term[] { new Variable("X") }),
					new Predicate[] { new Predicate("above",
						new Term[] { new Constant("Nothing"), new Variable("X") } ) }
					)
				);
		// TODO Is it OK to introduce such auxilary function?
		// above(X, Y) <- below(Y, X)
		engine.addClause(new Clause(
					new Predicate("above", new Term[] { new Variable("X"), new Variable("Y") }),
					new Predicate[] { new Predicate("below",
						new Term[] { new Variable("Y"), new Variable("X") } ) }
					)
				);
		// under(X) <- above(Y, X)
		engine.addClause(new Clause(
					new Predicate("under", new Term[] { new Variable("X") } ),
					new Predicate[] { 
						new Predicate("above", new Term[] { new Variable("Y"), new Variable("X") } ),
						new Predicate("box", new Term[] { new Variable("Y") } )
				   	}
				)
			);

		// box(A), box(B), box(C), box(D), box(E)
		engine.addClause(new Clause(
					new Predicate("box", new Term[] { new Constant("A") } )
					)
				);
		engine.addClause(new Clause(
					new Predicate("box", new Term[] { new Constant("B") } )
					)
				);
		engine.addClause(new Clause(
					new Predicate("box", new Term[] { new Constant("C") } )
					)
				);

		engine.addClause(new Clause(
					new Predicate("box", new Term[] { new Constant("D") } )
					)
				);
		engine.addClause(new Clause(
					new Predicate("box", new Term[] { new Constant("E") } )
					)
				);

		// below(C, Nothing)
		engine.addClause(new Clause(
					new Predicate("below", 
						new Term[] { new Constant("C"), new Constant("Nothing") } )));
		// below(B, C)
		engine.addClause(new Clause(
					new Predicate("below", 
						new Term[] { new Constant("B"), new Constant("C") } )));
		// below(A, B)
		engine.addClause(new Clause(
					new Predicate("below", 
						new Term[] { new Constant("A"), new Constant("B") } )));
		// below(E, Nothing)
		engine.addClause(new Clause(
					new Predicate("below", 
						new Term[] { new Constant("E"), new Constant("Nothing") } )));
		// below(D, E)
		engine.addClause(new Clause(
					new Predicate("below", 
						new Term[] { new Constant("D"), new Constant("E") } )));

		/*
		goal = new Clause(null,
				new Predicate[] { new Predicate("ontop",
					new Term[] { new Constant("A") } ) }
					);
		printResult(engine.prove(goal));
		goal = new Clause(null,
				new Predicate[] { new Predicate("ontop",
					new Term[] { new Constant("B") } ) }
					);
		printResult(engine.prove(goal));
		goal = new Clause(null,
				new Predicate[] { new Predicate("ontop",
					new Term[] { new Constant("C") } ) }
					);
		printResult(engine.prove(goal));
		goal = new Clause(null,
				new Predicate[] { new Predicate("under",
					new Term[] { new Constant("A") } ) }
					);
		printResult(engine.prove(goal));
		goal = new Clause(null,
				new Predicate[] { new Predicate("under",
					new Term[] { new Constant("B") } ) }
					);
		printResult(engine.prove(goal));
		goal = new Clause(null,
				new Predicate[] { new Predicate("under",
					new Term[] { new Constant("C") } ) }
					);
		printResult(engine.prove(goal));
		*/

		goal = new Clause(null,
				new Predicate[] { new Predicate("ontop",
					new Term[] { new Constant("C") } ) }
					);
		printResult(engine.prove(goal));
		goal = new Clause(null,
				new Predicate[] { new Predicate("ontop",
					new Term[] { new Constant("E") } ) }
					);
		printResult(engine.prove(goal));
		goal = new Clause(null,
				new Predicate[] { new Predicate("ontop",
					new Term[] { new Constant("B") } ) }
					);
		printResult(engine.prove(goal));
		move(new Constant("C"), new Constant("B"), new Constant("E"));
		goal = new Clause(null,
				new Predicate[] { new Predicate("ontop",
					new Term[] { new Constant("C") } ) }
					);
		printResult(engine.prove(goal));
		goal = new Clause(null,
				new Predicate[] { new Predicate("ontop",
					new Term[] { new Constant("E") } ) }
					);
		printResult(engine.prove(goal));
		goal = new Clause(null,
				new Predicate[] { new Predicate("ontop",
					new Term[] { new Constant("B") } ) }
					);
		printResult(engine.prove(goal));

		// engine.printAxioms();

	}
}
