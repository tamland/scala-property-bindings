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
import scala.actors.Actor
import scala.actors.Actor._

/** Represents a mutable property that can only be observed by other Observable
  * or Property. I.e. you cannot bind bidirectional or have an Observable
  * observe others. */
class Observable[A](private var _value: A) {
  protected val slots = new HashSet[A => Any]
  protected val observers = new HashSet[Property[A]]
  
  def apply() = _value
  def value = _value
  private def value_=(newVal: A) { _value = newVal }
  
  /** Update this and all observing propertie's value, and invoke all connected
    * slots. */
  def update(value: A) {
    this.value = value
    observers.foreach { other =>
      if (other.value != this.value)
        other.update(value)
    }
    slots.foreach { f =>
      f(value)
      //TODO: split slots into sync/async
      //dispatcher ! (() => f(value))
    }
  }
  
  /** Creates a unidirectional binding from this to other. This updates other.
    * Returns true if binding was not yet present, false otherwise. */
  def observedBy(other: Property[A]): Boolean = {
    if (other eq this) return false
    observers.add(other)
  }
  
  def isObservedBy(other: Property[A]) = observers.contains(other)
 
  private val dispatcher: Actor = actor {
    loop {
      react {
        case f: (() => Any) => f()
      }
    }
  }
}

object Observable {
  
  /** Creates unidirectional binding from this to f. When this changes, f is
    * called with the new value as argument. */
  def observe[A](obs: Observable[A])(slot: A => Any) {
    obs.slots.add(slot)
  }
  
  /* Removes all bindings between this and other.
    * Returns true if such binding existed, false otherwise. */
  def unbind[A](a: Observable[A], f: A => Any) =  a.slots.remove(f)
  def unbind[A](a: Observable[A], b: Property[A]) =  a.observers.remove(b)
  def unbind[A](a: Property[A], b: Observable[A]) =  b.observers.remove(a)
  
  
  
}

