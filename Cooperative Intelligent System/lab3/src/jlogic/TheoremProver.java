package jlogic;

/* ====================================================================
 * Copyright (c) 2003 Daniel F. Savarese.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by
 *        Daniel F. Savarese (http://www.savarese.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Daniel F. Savarese" and "Daniel Savarese" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission.  For written
 *    permission, please contact licensing at savarese.org.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL DANIEL F. SAVARESE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ====================================================================
 */
/* TheoremProver.java,v 1.4 2003/04/18 08:22:12 dfs Exp */

import java.util.*;

public class TheoremProver {
  private static final Substitution EMPTY_SUBSTITUTION = new Substitution();

  private ArrayList axioms;
  private HashMap variables;
  private int maxRecursionDepth, depth;

  public TheoremProver(int maxDepth) {
    axioms    = new ArrayList();
    variables = new HashMap();
    maxRecursionDepth = maxDepth;
    depth = 0;
  }

  public TheoremProver() {
    this(Integer.MAX_VALUE);
  }

  public static Substitution unify(Atom expr1, Atom expr2)
    throws ClassCastException
  {
    Substitution sub = null, lastsub = null;
    Atom head1, head2;
    Iterator it1, it2;

    if(expr1.hasElements() && !expr2.hasElements()) {
      Atom e = expr1;
      expr1 = expr2;
      expr2 = e;
    }

    if(!expr1.hasElements()) {
      Term term;
      Variable var;
      Substitution result;

      if(expr1.equals(expr2))
        return EMPTY_SUBSTITUTION;

      if(expr1.isVariable()) {
        var  = (Variable)expr1;

        if(expr2.contains(var))
          return null;

        term = (Term)expr2;
      } else if(expr2.isVariable()) {
        var  = (Variable)expr2;
        term = (Term)expr1;
      } else
        return null;

      result = new Substitution();
      result.add(new Substitution.Element(term, var));

      return result;
    }

    it1 = expr1.elements();
    it2 = expr2.elements();

    if(!expr1.equals(expr2))
      return null;

    while(it1.hasNext()) {
      if(!it2.hasNext())
        return null;

      head1 = (Atom)it1.next();
      head2 = (Atom)it2.next();

      if(sub != null) {
        head1 = head1.substitute(sub);
        head2 = head2.substitute(sub);
      }

      sub = unify(head1, head2);

      if(sub == null)
        return null;

      if(lastsub != null)
        sub = lastsub.compose(sub);
      lastsub = sub;
    }

    return sub;
  }

  void normalize(Atom p, Substitution sub) {
    Iterator it;

    it = p.elements();

    while(it.hasNext()) {
      Atom a = (Atom)it.next();

      if(a.hasElements()) {
        normalize(a, sub);
	  } else if(a.isVariable() && !sub.contains((Variable)a)) {
        int[] i = (int[])variables.get(a.getName());
        String newName;

        if(i == null) {
          i = new int[1];
          i[0] = 0;
          variables.put(a.getName(), i);
        }

        newName = a.getName() + i[0];
        ++i[0];

        sub.add(new Substitution.Element(
                new Variable(newName), (Variable)a));
      }
    }
  }

  Substitution normalize(Clause clause) {
    Iterator it;
    Predicate p = clause.getHead();
    Substitution sub = new Substitution();

    if(p != null)
      normalize(p, sub);

    it = clause.body();

    while(it.hasNext()) {
      p = (Predicate)it.next();
      normalize(p, sub);
    }

    return sub;
  }

  public void addClause(Clause clause) {
    Substitution sub = normalize(clause);
	Clause tmp =clause.substitute(sub);
    axioms.add(tmp);
  }

  public void removeClause(Clause clause) {
	  for(Object o : axioms) {
		  if(o.toString().equals(clause.toString())) {
			  axioms.remove(o);
		  }
	  }
  }

  Substitution chain(Clause goal, Clause clause, Clause resolvent) {
    Predicate head;
    Iterator it;

    if(clause.isHeadless())
      return null;

    head = clause.getHead();

    it = goal.body();

    while(it.hasNext()) {
      Predicate p = (Predicate)it.next();
      Substitution mgu;

      mgu = unify(head, p);

      if(mgu != null) {
        while(it.hasNext())
          resolvent.addPredicate((Predicate)it.next());

        it = clause.body();

        while(it.hasNext())
          resolvent.addPredicate((Predicate)it.next());

        return mgu;
      }

      resolvent.addPredicate(p);
    }

    return null;
  }

  Substitution _prove(Clause goal) {
    Iterator it;
    Clause resolvent;

    if(++depth > maxRecursionDepth)
      return null;

    it = axioms.iterator();
    resolvent = new Clause();

    while(it.hasNext()) {
      Clause clause = (Clause)it.next();
      Substitution s;

      resolvent.clear();
      s = chain(goal, clause, resolvent);

      if(resolvent.isBodyless())
        return s;

      if(s != null) {
        Substitution s2;

        s2 = _prove(resolvent.substitute(s));
        --depth;

        if(s2 != null)
          return s.compose(s2);
      }
    }

    return null;
  }

  public Substitution prove(Clause goal) {
	  /*
	  Iterator iterator = axioms.iterator();
		while(iterator.hasNext()) {
	  System.out.println(iterator.next());
		}
		*/
    depth = 0;
    return _prove(goal);
  }

  public void printAxioms() {
	  for(Object o : axioms) {
		  System.out.println(o);
	  }
  }
}
