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
/* Atom.java,v 1.2 2003/04/16 04:13:43 dfs Exp */

import java.util.*;

public abstract class Atom {

  protected String name;

  public Atom() {
    setName("");
  }

  public Atom(String name) {
    setName(name);
  }

  public boolean isVariable() { return false; }

  public boolean hasElements() { return false; }

  public Iterator elements() throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  public boolean contains(Variable var) {
    if(hasElements()) {
      Iterator it = elements();

      while(it.hasNext()) {
        Atom e = (Atom)it.next();

        if(e.hasElements()) {
          if(e.contains(var))
            return true;
        } else if(var.equals(e))
          return true;
      }
    }

    return false;
  }

  public abstract Atom substitute(Substitution sub);

  public void setName(String name) {
    this.name = name;
  }

  public String getName() { return name; }

  public String toString() {
    return getName();
  }

  public boolean equals(Object obj) {
    if(obj == null)
      return false;

    try {
      return getName().equals(((Atom)obj).getName());
    } catch(ClassCastException cce) {
      return false;
    }
  }

}

