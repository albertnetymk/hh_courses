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
/* Clause.java,v 1.2 2003/04/16 04:13:43 dfs Exp */

/** Some modifications done by Mikael Boden, 2003
 * - add constructor that takes a list of Terms
 */

import java.util.*;

public class Clause {
  private Predicate head;
  private ArrayList body;

  public Clause() {
    setHead(null);
    body = new ArrayList();
  }

  public Clause(Predicate head) {
    setHead(head);
    body = new ArrayList();
  }

  public Clause(Predicate head, Predicate[] bodyPreds) {
    setHead(head);
    body = new ArrayList();
    for (int i=0; i<bodyPreds.length; i++)
      body.add(bodyPreds[i]);
  }

  public void clear() {
    setHead(null);
    body.clear();
  }

  public void setHead(Predicate p) {
    head = p;
  }

  public Predicate getHead() {
    return head;
  }

  public void addPredicate(Predicate p) {
    body.add(p);
  }

  public Iterator body() {
    return body.iterator();
  }

  public boolean isHeadless() {
    return (head == null);
  }

  public boolean isBodyless() {
    return (body.size() == 0);
  }

  public Clause substitute(Substitution sub) {
    Iterator it;
    Clause result = new Clause();

    if(head != null)
      result.setHead((Predicate)head.substitute(sub));

    it = body();
    while(it.hasNext()) {
      Predicate p = (Predicate)it.next();
      result.addPredicate((Predicate)p.substitute(sub));
    }

    return result;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    Iterator it;

    if(!isHeadless())
      buffer.append(head.toString());

    buffer.append(" <-\n");

    if(!isBodyless()) {
      it = body();

      while(it.hasNext()) {
        buffer.append("\t");
        buffer.append(it.next().toString());

        if(it.hasNext())
          buffer.append("\n");
      }
    }

    return buffer.toString();
  }

}

