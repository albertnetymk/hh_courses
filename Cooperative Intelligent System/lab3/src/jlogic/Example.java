package jlogic;

public class Example {
	// ToDo:
	// add assertions re male/female people contained in the family tree
	// rewrite daughter/son clauses to make use of male/female assertions
	// add clauses for mother, father, sister, aunt, grandmother/grandfather
	// add more people to the family tree and check if everything works...

	static TheoremProver engine=new TheoremProver(100);
	static Clause clause;
	static Clause goal;

	public static void printResult(Substitution result) {
		if(result==null)
			System.out.println("False.\n"+goal);
		else {
			clause=goal.substitute(result);
			System.out.println("True.\n"+clause);
			// System.out.println("True.\n"+clause);
		}
	}
	public static void main(String[] args) {
		// female(X) <- daughter(X, Y)
		engine.addClause(new Clause(
					new Predicate("female", new Term[] { new Variable("Child") }),
					new Predicate[] { new Predicate("daughter", 
						new Term[] { new Variable("Child"), new Variable("Parent") } ) }
					)
				);

		// male(X) <- son(X, Y)
		engine.addClause(new Clause(
					new Predicate("male", new Term[] { new Variable("Child") }),
					new Predicate[] { new Predicate("son", 
						new Term[] { new Variable("Child"), new Variable("Parent") } ) }
					)
				);

		// grandfather(X, Y) <- father(X, Z), father(Z, Y)
		engine.addClause(new Clause(
					new Predicate("grandfather", new Term[] { new Variable("X"), new Variable("Y") }),
					new Predicate[] { 
						new Predicate("father", new Term[] { new Variable("X"), new Variable("Z") }),
						new Predicate("father", new Term[] { new Variable("Z"), new Variable("Y") })
					}
				)
			);

		// uncle(Uncle, Nephew) <- brother(Uncle, X),child(Nephew, X).
		engine.addClause(new Clause(
					new Predicate("uncle", new Term[] {new Variable("Uncle"), new Variable("Nephew")}),
					new Predicate[] {
						new Predicate("brother", new Term[] {new Variable("Uncle"), new Variable("X")}),
			new Predicate("child", new Term[] {new Variable("Nephew"), new Variable("X")})}));

		// aunt(X, Y) <- sister(X, Z), child(Y, Z)
		engine.addClause(new Clause(
					new Predicate("aunt", new Term[] {new Variable("X"), new Variable("Y")}),
					new Predicate[] {
						new Predicate("sister", new Term[] {new Variable("X"), new Variable("Z")}),
						new Predicate("child", new Term[] {new Variable("Y"), new Variable("Z")})
					}
				)
			);

		// father(X, Y) <- male(X), child(Y, X)
		engine.addClause(new Clause(
					new Predicate("father", new Term[] { new Variable("X"), new Variable("Y") }),
					new Predicate[] { 
						new Predicate("male", new Term[] { new Variable("X") }),
						new Predicate("child", new Term[] { new Variable("Y"), new Variable("X") })
					}
				)
			);


		// mother(X, Y) <- female(X), child(Y, X)
		engine.addClause(new Clause(
					new Predicate("mother", new Term[] { new Variable("X"), new Variable("Y") }),
					new Predicate[] { 
						new Predicate("female", new Term[] { new Variable("X") }),
						new Predicate("child", new Term[] { new Variable("Y"), new Variable("X") })
					}
				)
			);

		// wife(X, Y) <- mother(X, Z), father(Y, Z)
		engine.addClause(new Clause(
					new Predicate("wife", new Term[] { new Variable("X"), new Variable("Y") } ),
					new Predicate[] {
						new Predicate("mother", new Term[] { new Variable("X"), new Variable("Z") }),
						new Predicate("father", new Term[] { new Variable("Y"), new Variable("Z") })
					}
				)
			);


		// brother(X, Y) <- brother(Y, X).
		engine.addClause(new Clause(
					new Predicate("brother", new Term[] {new Variable("B1"), new Variable("B2")}),
					new Predicate[] {
						new Predicate("brother", new Term[] {new Variable("B2"), new Variable("B1")})}));

		// sister(X, Y) <- sister(Y, X).
		engine.addClause(new Clause(
					new Predicate("sister", new Term[] {new Variable("X"), new Variable("Y")}),
					new Predicate[] {
						new Predicate("sister", new Term[] {new Variable("Y"), new Variable("X")})}));

		// child(X, Y) <- son(X, Y)
		engine.addClause(new Clause(
					new Predicate("child", new Term[] {new Variable("Child"), new Variable("Parent")}),
					new Predicate[] {
						new Predicate("son", new Term[] {new Variable("Child"), new Variable("Parent")})}));

		// child(X, Y) <- daughter(X, Y)
		engine.addClause(new Clause(
					new Predicate("child", new Term[] {new Variable("Child"), new Variable("Parent")}),
					new Predicate[] {
						new Predicate("daughter", new Term[] {new Variable("Child"), new Variable("Parent")})}));

		// son(steven, danny).
		engine.addClause(new Clause(
					new Predicate("son",
						new Term[] {new Constant("steven"), new Constant("danny")})));

		// daughter(kelly, douglas).
		engine.addClause(new Clause(
					new Predicate("daughter",
						new Term[] {new Constant("kelly"), new Constant("douglas")})));

		// brother(douglas, danny).
		engine.addClause(new Clause(
					new Predicate("brother",
						new Term[] {new Constant("douglas"), new Constant("danny")})));

		/*
		// son(douglas, douglasFather)
		engine.addClause(new Clause(
					new Predicate("son",
						new Term[] { new Constant("douglas"), new Constant("douglasFather") }
						)
					)
				);

		// male(douglasFather)
		engine.addClause(new Clause(
					new Predicate("male",
						new Term[] { new Constant("douglasFather") } 
						)
					)
				);
				*/
		// mother(douglasWife, kelly)
		engine.addClause(new Clause(
					new Predicate("mother",
						new Term[] { new Constant("douglasWife"), new Constant("kelly") }
						)
					)
				);

		// male(douglas)
		engine.addClause(new Clause(
					new Predicate("male",
						new Term[] { new Constant("douglas") }
						)
					)
				);

		// father(douglasFather, douglas)
		engine.addClause(new Clause(
					new Predicate("father",
						new Term[] { new Constant("douglasFather"), new Constant("douglas") }
						)
					)
				);


		// <- uncle(X, kelly).
		// who is kelly's uncle?
		goal=new Clause(null,
				new Predicate[] {
					new Predicate("uncle",
						new Term[] {new Variable("X"), new Constant("kelly")}
					)
				}
			);

		printResult(engine.prove(goal));

		// <- female(kelly)
		// the gender of kelly
		goal = new Clause(null, new Predicate[] { 
			new Predicate("female", new Term[] { new Constant("kelly") } )
		});
		printResult(engine.prove(goal));
		
		// <- father(X, kelly)
		goal = new Clause(null, 
				new Predicate[] { 
					new Predicate("father", 
						new Term[] { new Variable("X"), new Constant("kelly") } 
					)
				}
			);
		printResult(engine.prove(goal));

		// <- grandfather(X, kelly)
		goal = new Clause(null, 
				new Predicate[] { 
					new Predicate("grandfather", 
						new Term[] { new Variable("X"), new Constant("kelly") } 
					)
				}
			);
		printResult(engine.prove(goal));
		
		// <- wife(X, douglas)
		goal = new Clause(null, 
				new Predicate[] { 
					new Predicate("wife", 
						new Term[] { new Variable("X"), new Constant("douglas") } 
					)
				}
			);
		printResult(engine.prove(goal));
	}
}
