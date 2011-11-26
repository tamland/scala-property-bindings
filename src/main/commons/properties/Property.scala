/*  
 * Copyright (C) 2011 Thomas Amland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package commons.properties

import scala.collection.mutable.HashSet

/** Represents a mutable property that can be observed and updated by other
  * properties. */
class Property[A](v: A) extends Observable[A](v) {
  
  /** Creates a unidirectional binding other to this. Other updates this.
    * Returns true if binding was not yet present, false otherwise. */
  def observes(other: Observable[A]): Boolean = {
    if (other eq this) return false
    other observedBy this
  }
  
  /** Same as Property.bind(this. other) */
  def bind(other: Property[A]) = Property.bind(this, other)
 
}

object Property extends Observable {
  
  /** Creates a Property that is bidirectional bound with p. */
  def bind[A](p: Property[A]): Property[A] = {
    val ret = new Property(p.value)
    Property.bind(p, ret)
    ret
  }
  
  /** Creates a Property that observes obs. */
  def observes[A](obs: Observable[A]) = {
    val ret = new Property(obs.value)
    ret observes obs
    ret
  }
  
  /** Connect obs to f. Returns current value of obs. */
  def observes[A](obs: Observable[A], f: A => Any): A = {
    Observable.observe(obs)(f)
    obs()
  }
  
  
  /** Creates a bidirectional binding between a and b. If a and b have
    * different value, a is updated.
    * Returns true if the binding was not yet present, false otherwise. */
  def bind[A](a: Property[A], b: Property[A]): Boolean = {
    if (a == b) return false
    if (a.observers.contains(b) && b.observers.contains(a))
      return false
    
    if (a.value != b.value)
      a() = b.value
    a observes b
    b observes a
    true
  }
  
  def unbind[A](a: Property[A], b: Property[A]) = {
    val r1 = a.observers.remove(b)
    val r2 = b.observers.remove(a)
    r1 || r2
  }
  
  def isBound[A](a: Property[A], b: Property[A]) = {
    a.isObservedBy(b) || b.isObservedBy(a)
  }
  
}
