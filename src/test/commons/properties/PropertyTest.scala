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

import org.scalatest.matchers.ShouldMatchers
import org.scalatest._

object PropertyTest extends WordSpec with ShouldMatchers {
  import Property._
  
  "A Property" should {
    "not allow binding with itself" in {
      val a = new Property(1)
      (a observes a) should be (false)
      (a isObservedBy a) should be (false)
      
      bind(a, a) should be (false)
      isBound(a, a) should be (false)
    }
    "know if others are bound to it" in {
      val a = new Property(1)
      val b = new Property(1)
      bind(a, b)
      isBound(a, b) should be (true)
    }
    "not allow updating from outside owner class" is (pending)
  }
  
  "A Property" when {
    "bound to a property with different value" should {
      "update itself to this value" in {
        val a = new Property(1)
        val b = new Property(2)
        bind(a, b)
        a.value should be (2)
        b.value should be (2)
      }
    }
    "bound unidirectional" should {
      "complain when trying to update it's value" in {
        val a = new Property(1)
        val b = new Observable(1)
        a observes b
        b observedBy a
        evaluating { a() = 2 } should produce [UnsupportedOperationException]
        a() should be (1)
        b() should be (1)
      }
      "convert it to bidirectional when trying to bind" is (pending)
    }
    "bound" should {
      "deny duplicate bindings" in {
        val a = new Property(1)
        val b = new Property(1)
        bind(a, b) should be (true)
        bind(b, a) should be (false)
        a() = 2
        a() should be (2)
        b() should be (2)
      }
      "allow unbinding" in {
        val a = new Property(1)
        val b = new Property(1)
        bind(a, b)
        a() = 2
        a() should be (2)
        b() should be (2)
        unbind(a, b) should be (true)
        a() = 3
        b() = 4
        a() should be (3)
        b() should be (4)
      }
    }
    "cyclical bound" should {
      "act normally" in {
        val a = new Property(1)
        val b = new Property(1)
        val c = new Property(1)
        b observes a
        c observes b
        a observes c
        
        try {
          a() = 2
          a() should be (2)
          b() should be (2)
          c() should be (2)
        
          b() = 3
          a() should be (3)
          b() should be (3)
          c() should be (3)
        
          c() = 4
          a() should be (4)
          b() should be (4)
          c() should be (4)
        } catch {
          case e: StackOverflowError => fail()
        }
      }
    }
  }
  
  def main(args: Array[String]) {
    PropertyTest.execute()
  }
}