# Scala property bindings
This library provides property bindings similar to Qt signal slot system, JavaFx's properties and other.


# Classes
#### Observable
An Observable represents a property that can only be observed i.e. no other properties can change it. (Not related to java's observable)

#### Property
The Proptery class extends Observable, and differ in that it's value can be updated by any other Observable and Propterty. This allows you to make a bidirectional (two-way) connection between two Property instances.


# Usage
### Creating properties
```scala
val a = new Observable(1)
val b = new Property(2.0)
```

### Update property and update all observing or bound properties
```scala
a() = 2
```

### Binding properties unidirectional (one-way)
```scala
b observes a
```

### Bidirectional
```scala
a bind b
```

### Functions
Any Observable and Property can be bound to functions. This means that whenever the property changes the function is called with the new value as argument. This is similar to Qt's signal/slot system.
```scala
observe(a) { x =>
  println(x)
}
a() = 3
```
Prints "3"


### Factory methods
Using the factory method `bind` and `observes` is generally preferred because this guaranties it initially holds the correct value.
This method creates a new property binds in bidirectional with given Property.
```scala
val a = Property(1)
val b = bind(a)
val c = observes(b)
```

### Swing integration
#### Binding to swing properties
When dealing with language level properties, factory method `observes` can be used. It takes an Observable and a function, bind them and returns the Observable's current value. Example:
```scala
val label = new Label {
  text = observes(model.text, text_=)
}```
By passing the setter of property `text`, it will be unidirectionally bound to `model.text` and act as a normal `Property`.


#### Example Application
```scala
private class Model {
  val text = new Observable("count: 0")
  private var count = 0
  def increment() {
    count += 1
    text() = "count: " + count
  }
}

object Example extends SimpleSwingApplication {
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
```





