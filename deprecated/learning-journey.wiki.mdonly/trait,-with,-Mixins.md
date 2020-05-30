### Definition

Mixin is a class that contains methods for use by other classes without having to be the parent class of those other classes. Mixins are sometimes described as being "included" rather than "inherited".

Mixins generally use the "trait" keyword and the "with" keyword.  The thing you want included is declared using "trait" keyword, and then included using "with" keyword.. (However, once you have created at trait, you can still use "extends" to inherit that trait as the superclass.) see examples below.

Mixins are a language concept that allows a programmer to inject some code into a class. Mixin programming is a style of software development, in which units of functionality are created in a class and then mixed in with other classes.

A mixin class acts as the parent class, containing the desired functionality. A subclass can then inherit or simply reuse this functionality, but not as a means of specialization. Typically, the mixin will export the desired functionality to a child class, without creating a rigid, single "is a" relationship. Here lies the important difference between the concepts of mixins and inheritance, in that the child class can still inherit all the features of the parent class, but, the semantics about the child "being a kind of" the parent need not be necessarily applied.

### Mixins In Rocket
Rocket code uses mixins extensively, and those modifying the code must be careful because a mixin represents part of the interface to a module that uses them.  The issue is that when editing the upstream code that is being mixed-in, it's hard to track all the places that mix the code in, and there can be unintended effects on the down stream modules.


### Advantages
1. It provides a mechanism for multiple inheritance by allowing multiple classes to use the common functionality, but without the complex semantics of multiple inheritance.
1. Code reusability: Mixins are useful when a programmer wants to share functionality between different classes. Instead of repeating the same code over and over again, the common functionality can simply be grouped into a mixin and then included into each class that requires it.
1. Mixins allow inheritance and use of only the desired features from the parent class, not necessarily all of the features from the parent class.

### Examples

Mixins are traits which are used to compose a class.

```scala
abstract class A {
  val message: String
}
class B extends A {
  val message = "I'm an instance of class B"
}
trait C extends A {
  def loudMessage = message.toUpperCase()
}
class D extends B with C

val d = new D
println(d.message)  // I'm an instance of class B
println(d.loudMessage)  // I'M AN INSTANCE OF CLASS B
```

Class `D` has a superclass `B` and a mixin `C`. Classes can only have one superclass but many mixins (using the keywords extends and with respectively). The mixins and the superclass may have the same supertype.

Now let’s look at a more interesting example starting with an abstract class:

```scala
abstract class AbsIterator {
  type T
  def hasNext: Boolean
  def next(): T
}
```

The class has an abstract type `T` and the standard iterator methods.

Next, we’ll implement a concrete class (all abstract members `T`, `hasNext`, and `next` have implementations):

```scala
class StringIterator(s: String) extends AbsIterator {
  type T = Char
  private var i = 0
  def hasNext = i < s.length
  def next() = {
    val ch = s charAt i
    i += 1
    ch
  }
}
```

`StringIterator` takes a `String` and can be used to iterate over the String (e.g. to see if a String contains a certain character).

Now let’s create a trait which also extends `AbsIterator`:

```scala
trait RichIterator extends AbsIterator {
  def foreach(f: T => Unit): Unit = while (hasNext) f(next())
}
```

This trait implements `foreach` by continually calling the provided function `f: T => Unit` on the next element (`next()`) as long as there are further elements (`while (hasNext)`). Because `RichIterator` is a trait, it doesn’t need to implement the abstract members of AbsIterator.

We would like to combine the functionality of `StringIterator` and `RichIterator` into a single class.

```scala
object StringIteratorTest extends App {
  class RichStringIter extends StringIterator("Scala") with RichIterator
  val richStringIter = new RichStringIter
  richStringIter foreach println
}
```

The new class `RichStringIter` has `StringIterator` as a superclass and `RichIterator` as a mixin.

With single inheritance we would not be able to achieve this level of flexibility.

more on Mixins, traits, with, and extends: https://www.artima.com/pins1ed/traits.html

### Examples in Rocket code

Complete collection of all mixins within the `rocket` package is found within [this file](https://docs.google.com/document/d/1uitsrMXCNGNCOWTkbR36DLpZAZm2y-Ss7Fu1w3Y7wPA/edit?usp=sharing)

These examples have been taken from RocketTile.scala found in (this)[will add link later] commit (July 2017) of RocketChip:

```scala
//@L26 of RocketTile.scala
class RocketTile(val rocketParams: RocketTileParams, val hartid: Int)(implicit p: Parameters) extends BaseTile(rocketParams)(p)
    with HasLazyRoCC  // @L80 of tile/LazyRoCC.scala // implies CanHaveSharedFPU with CanHavePTW with HasHellaCache
    with CanHaveScratchpad { // @L101 in rocket/ScratchpadSlavePort.scala // implies CanHavePTW with HasHellaCache with HasICacheFrontend
```
```scala
//@119 of RocketTile.scala
class RocketTileBundle(outer: RocketTile) extends BaseTileBundle(outer) // @L86 in tile/BaseTile.scala
    with CanHaveScratchpadBundle // @L128 rocket/ScrachpadSlavePort.scala
```
```scala
@L122 of RocketTile.scala
class RocketTileModule(outer: RocketTile) extends BaseTileModule(outer, () => new RocketTileBundle(outer)) // @L92 in tile/BaseTile.scala
    with HasLazyRoCCModule // @L96 tile/LazyRocc.scala
    with CanHaveScratchpadModule { // @L133 in rocket/ScarchpadSlavePort.scala
```
```scala
// @L183 of RocketTile.scala
  lazy val module = new LazyModuleImp(this) { // @L159 in diplomacy/LazyModule.scala
    val io = new CoreBundle with HasExternallyDrivenTileConstants { // @L75 in tile/Core.scala & @L73 in tile/BaseTile.scala
```
```scala
// @L230 of RocketTile.scala
  lazy val module = new LazyModuleImp(this) {
    val io = new CoreBundle with HasExternallyDrivenTileConstants {
```
```scala
// @L278 of RocketTile.scala
  lazy val module = new LazyModuleImp(this) {
    val io = new CoreBundle with HasExternallyDrivenTileConstants {
```

### References:

1. https://en.wikipedia.org/wiki/Mixin
1. https://docs.scala-lang.org/tour/mixin-class-composition.html
1. Rocket Code - will add direct link to July2017 commit -> RocketTile.scala