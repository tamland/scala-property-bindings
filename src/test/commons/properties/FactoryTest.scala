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

object FactoryTest extends WordSpec with ShouldMatchers {
  
  "Factory method bind" should {
    "produce property with same value" in {
      val a = new Property(1)
      val b = Property.bind(a)
      b.value should be(1)
    }
    "produce property that is bound bidirectional" in {
      val a = new Property(1)
      val b = Property.bind(a)
      b.value should be(1)
      
      a() = 2
      a.value should be(2)
      b.value should be(2)
      
      b() = 3
      a.value should be(3)
      b.value should be(3)
    }
  }
  
  "Factory method observes(Observable)" should {
    "produce property with same value" in {
      val a = new Property(1)
      val b = Property.bind(a)
      b.value should be(1)
    }
    "produce property that is bound unidirectional" in {
      val a = new Property(1)
      val b = Property.observes(a)
      b.value should be(1)
      
      a() = 2
      a.value should be(2)
      b.value should be(2)
      
      b() = 3
      a.value should be(2)
      b.value should be(3)
    }
  }
  
  "Factory method observes(Observable, Function)" should {
    "produce property with same value" in {
      val a = new Property(1)
      val b = Property.bind(a)
      b.value should be(1)
    }
    "produce property that is bound unidirectional with function" in {
      var called = false;
      var arg = -1
      val slot = (x: Int) => { called = true; arg = x }
      
      val a = new Property(1)
      val b: Int = Property.observes(a, slot)
      
      a() = 2
      called should be (true)
      arg should be (2)
    }
  }
  
  def main(args: Array[String]) {
    FactoryTest.execute()
  }
}