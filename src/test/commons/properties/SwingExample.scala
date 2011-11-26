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

private class Model {
  val text = new Observable("count: 0")
  
  private var count = 0
  def increment() {
    count += 1
    text() = "count: " + count
  }
}

import scala.swing._
import scala.swing.event._

import Observable._
import Property._

object SwingExample extends SimpleSwingApplication {
  private val model = new Model
  
  def top = new MainFrame {
    title = "Example"
    contents = new GridPanel(2, 1) {
      val button = new Button("Press Me!")
      contents += button
      
      val label = new Label {
        text = observes(model.text, text_=)
      }
      contents += label
      
      
      listenTo(button)
      reactions += {
        case ButtonClicked(_) => model.increment()
      }
    }
  }
}