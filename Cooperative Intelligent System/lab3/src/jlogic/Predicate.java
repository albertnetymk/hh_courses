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
/* Predicate.java,v 1.2 2003/04/16 04:13:43 dfs Exp */

/** Some modifications done by Mikael Boden, 2003
 * - add constructor that takes a list of Terms
 */

import java.util.*;

public class Predicate extends Atom {

  // We cheat and wrap a Function
  private Function function;

  public Predicate(Function f) {
    super(f.getName());
    function = f;
  }

  public Predicate(Function f, Term[] argArray) {
    super(f.getName());
    function = f;
    for (int i=0; i<argArray.length; i++) {
      function.addArgument(argArray[i]);
    }
  }

  public Predicate(String name) {
    super(name);
    function = new Function(name);
  }

  public Predicate(String name, Term[] argArray) {
    super(name);
    function = new Function(name);
    for (int i=0; i<argArray.length; i++) {
      function.addArgument(argArray[i]);
    }
  }

  public void addTerm(Term arg) {
    function.addArgument(arg);
  }

  public Iterator terms() {
    return function.arguments();
  }

  public Iterator elements() throws UnsupportedOperationException {
    return terms();
  }

  public boolean hasElements() { return true; }

  public Atom substitute(Substitution sub) {
    return new Predicate((Function)function.substitute(sub));
  }

  public boolean equals(Object obj) {
    if(!super.equals(obj))
      return false;

    if(obj instanceof Predicate) {
      Predicate p = (Predicate)obj;
      return function.equals(p.function);
    }

    return false;
  }

  public String toString() {
    return function.toString();
  }

}
