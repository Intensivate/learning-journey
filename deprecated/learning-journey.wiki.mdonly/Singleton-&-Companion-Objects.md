# Singleton Objects
A singleton object is a class with exactly one instantiated lazy object. Syntax:
```scala
object objectName{
  // Internal object definitions.
}
```
An object can be imported into a class using the keyword `import` as follows:
```scala
import objectName._
```
# Companion Objects
In a Scala source file, if a singleton object is defined with the same name as a class, then that object is said to be a _**companion**_ to that class. If an object and a class are companions, then either of them can access the other's private members such as variables, objects and methods.

**For more information about singleton and companion objects, please visit [Scala's Documentation](https://docs.scala-lang.org/tour/singleton-objects.html).**