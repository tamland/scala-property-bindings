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

object ObservableTest extends WordSpec with ShouldMatchers {
  
  "An Observable" when {
    "applied" should {
      "return it's value" in {
        val a = new Observable(1)
        a() should be(1)
      }
    }
    
    "bound to a Property" should {
      "propagate its value when updated" in {
        val a = new Observable(1)
        val b = new Property(2)
        b observes a
        a() = 3
        a.value should be(3)
        b.value should be(3)
      }
      "know if others are bound to it" in {
        val a = new Property(1)
        val b = new Observable(1)
        a observes b
        (b isObservedBy a) should be (true)
      }
      "allow unbinding" in {
        val a = new Property(1)
        val b = new Observable(1)
        a observes b
        Observable.unbind(a, b) should be (true)
        (b isObservedBy a) should be (false)
      }
    }
    
    "bound to a function" should {
      "invoke it when updated" in {
        val a = new Observable(1)
        var called = false
        var arg = -1
        
        Observable.observe(a) { x =>
          called = true
          arg = x
        }
        
        a() = 2
        called should be (true)
        arg should be (2)
      }
    }
  }
  
 def main(args: Array[String]) {
    ObservableTest.execute()
  }
}
