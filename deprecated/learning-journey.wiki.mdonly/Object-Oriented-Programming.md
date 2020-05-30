# Module 3.5: Object Oriented Programming

Prev: [[Functional Programming]]

Next: [[Types]]

## Motivation

Scala and Chisel are object-oriented programming languages, meaning code may be compartmentalized into objects. Scala, which is built on Java, inherits many of Java's object-oriented features. However, as we'll see below, there are some differences. Chisel's hardware modules are similar to Verilog's modules, in that they can be instantiated and wired up as single or multiple instances.

## Setup

```scala
val path = System.getProperty("user.dir") + "/source/load-ivy.sc"
interp.load.module(ammonite.ops.Path(java.nio.file.FileSystems.getDefault().getPath(path)))
import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import chisel3.experimental._
```

## Object Oriented Programming

This section outlines how Scala implements the object-oriented programming paradigm. So far you have already seen classes, but Scala also has the following features:

    * Abstract classes
    * Traits
    * Objects
    * Companion Objects
    * Case Classes

### Abstract Classes

Abstract classes are just like other programming language implementations. They can define many unimplemented values that subclasses must implement. Any object can only directly inherit from one parent abstract class.

Example: Abstract Class

abstract class MyAbstractClass {

  def myFunction(i: Int): Int

  val myValue: String

}

class ConcreteClass extends MyAbstractClass {

  def myFunction(i: Int): Int = i + 1

  val myValue = "Hello World!"

}

// Uncomment below to test!

// val abstractClass = new MyAbstractClass() // Illegal! Cannot instantiate an abstract class

val concreteClass = new ConcreteClass()      // Legal!

​

Traits

Traits are very similar to abstract classes in that they can define unimplemented values. However, they differ in two ways:

    a class can inherit from multiple traits
    a trait cannot have constructor parameters

Example: Traits and Multiple Inheritance
Traits are how Scala implements multiple inheritance, as shown in the example below. MyClass extends from both traits HasFunction and HasValue:

trait HasFunction {

  def myFunction(i: Int): Int

}

trait HasValue {

  val myValue: String

  val myOtherValue = 100

}

class MyClass extends HasFunction with HasValue {

  override def myFunction(i: Int): Int = i + 1

  val myValue = "Hello World!"

}

// Uncomment below to test!

// val myTraitFunction = new HasFunction() // Illegal! Cannot instantiate a trait

// val myTraitValue = new HasValue()       // Illegal! Cannot instantiate a trait

val myClass = new MyClass()                // Legal!

To inherit multiple traits, chain them like

class MyClass extends HasTrait1 with HasTrait2 with HasTrait3 ...

In general, always use traits over abstract classes, unless you are certain you want to enforce the single-inheritance restriction of abstract classes.
Objects

Scala has a language feature for these singleton classes, called objects. You cannot instantiate an object (no need to call new); you can simply directly reference it. That makes them similar to Java static classes.

Example: Objects

object MyObject {

  def hi: String = "Hello World!"

  def apply(msg: String) = msg

}

println(MyObject.hi)

println(MyObject("This message is important!")) // equivalent to MyObject.apply(msg)

Companion Objects

When a class and an object share the same name and defined in the same file, the object is called a companion object. When you use new before the class/object name, it will instantiate the class. If you don't use new, it will reference the object:

Example: Companion Object

object Lion {

    def roar(): Unit = println("I'M AN OBJECT!")

}

class Lion {

    def roar(): Unit = println("I'M A CLASS!")

}

new Lion().roar()

Lion.roar()

Companion objects are usually used for the following reasons:

    to contain constants related to the class
    to execute code before/after the class constructor
    to create multiple constructors for a class

In the example below, we will instantiate a number of instances of Animal. We want each animal to have a name, and to know its order within all instantiations. Finally, if no name is given, it should get a default name.

object Animal {

    val defaultName = "Bigfoot"

    private var numberOfAnimals = 0

    def apply(name: String): Animal = {

        numberOfAnimals += 1

        new Animal(name, numberOfAnimals)

    }

    def apply(): Animal = apply(defaultName)

}

class Animal(name: String, order: Int) {

  def info: String = s"Hi my name is $name, and I'm $order in line!"

}

​

val bunny = Animal.apply("Hopper") // Calls the Animal factory method

println(bunny.info)

val cat = Animal("Whiskers")       // Calls the Animal factory method

println(cat.info)

val yeti = Animal()                // Calls the Animal factory method

println(yeti.info)

​

What's happening here?

    Our Animal companion object defines a constant relevant to class Animal:

    val defaultName = "Bigfoot"

    It also defines a private mutable integer to keep track of the order of Animal instances:

    private var numberOfAnimals = 0

    It defines two apply methods, which are known as factory methods in that they return instances of the class Animal.
        The first creates an instance of Animal using only one argument, name, and uses numberOfAnimals as well to call the Animal class constructor.

        def apply(name: String): Animal = {
             numberOfAnimals += 1
             new Animal(name, numberOfAnimals)
        }

        The second factory method requires no argument, and instead uses the default name to call the other apply method.

        def apply(): Animal = apply(defaultName)

    These factory methods can be called naively like this

    val bunny = Animal.apply("Hopper")

    which eliminates the need to use the new keyword, but the real magic is that the compiler assumes the apply method any time it sees parentheses applied to an instance or object:

    val cat = Animal("Whiskers")

    Factory methods, usually provided via companion objects, allow alternative ways to express instance creations, provide additional tests for constructor parameters, conversions, and eliminate the need to use the keyword new. Note that you must call the companion object's apply method for numberOfAnimals to be incremented.

Chisel uses many companion objects, like Module. When you write the following:

val myModule = Module(new MyModule)

you are calling the Module companion object, so Chisel can run background code before and after instantiating MyModule.
Case Classes

Case classes are a special type of Scala class that provides some cool additional features. They are very common in Scala programming, so this section outlines some of their useful features:

    Allows external access to the class parameters
    Eliminates the need to use new when instantiating the class
    Automatically creates an unapply method that supplies access to all of the class Parameters.
    Cannot be subclassed from

In the following example, we declare three different classes, Nail, Screw, and Staple.

class Nail(length: Int) // Regular class

val nail = new Nail(10) // Requires the `new` keyword

// println(nail.length) // Illegal! Class constructor parameters are not by default externally visible

​

class Screw(val threadSpace: Int) // By using the `val` keyword, threadSpace is now externally visible

val screw = new Screw(2)          // Requires the `new` keyword

println(screw.threadSpace)

​

case class Staple(isClosed: Boolean) // Case class constructor parameters are, by default, externally visible

val staple = Staple(false)           // No `new` keyword required

println(staple.isClosed)

Nail is a regular class, and its parameters are not externally visible because we did not use the val keyword in the argument list. It also requires the new keyword when declaring an instance of Nail.

Screw is declared similarly to Nail, but includes val in the argument list. This allows its parameter, threadSpace, to be visible externally.

By using a case class, Staple gets the benefit of all its parameters being externally visible (without needing the val keyword).

In addition, Staple does not require using new when declaring a case class. This is because the Scala compiler automatically creates a companion object for every case class in your code, which contains an apply method for the case class.

Case classes are nice containers for generators with lots of parameters. The constructor gives you a good place to define derived parameters and validate input.

case class SomeGeneratorParameters(

    someWidth: Int,

    someOtherWidth: Int = 10,

    pipelineMe: Boolean = false

) {

    require(someWidth >= 0)

    require(someOtherWidth >= 0)

    val totalWidth = someWidth + someOtherWidth

}

Inheritance with Chisel

You've seen Modules and Bundles before, but it's important to realize what's really going on. Every Chisel module you make is a class extending the base type Module. Every Chisel IO you make is a class extending the base type Bundle (or, in some special cases, Bundle's supertype Record). Chisel hardware types like UInt or Bundle all have Data as a supertype. We'll explore using object oriented programming to create hierarchical hardware blocks and explore object reuse. You'll learn more about types and Data in the next Module on type generic generators.
Module

Whenever you want to create a hardware object in Chisel, it needs to have Module as a superclass. Inheritance might not always be the right tool for reuse (composition over inheritance is a common principle), but inheritance is still a powerful tool. Below is an example of creating a Module and connecting multiple instantiations of them together hierarchically.

Example: Gray Encoder and Decoder
We'll create a hardware Gray encoder/decoder. The encode or decode operation choice is hardware programmable.

class NoGlitchCounterIO(bitwidth: Int) extends Bundle {

    val en  = Input(Bool())

    val out = Output(UInt(bitwidth.W))

}

​

abstract class NoGlitchCounter(val maxCount: Int) extends Module {

    val bitwidth: Int

    val io = IO(new NoGlitchCounterIO(bitwidth))

}

​

abstract class AsyncFIFO(depth: Int) extends Module {

    val io = IO(new Bundle{

        // write inputs

        val write_clock = Input(Clock())

        val write_enable = Input(Bool())

        val write_data = Input(UInt(32.W))

​

        // read inputs/outputs

        val read_clock = Input(Clock())

        val read_enable = Input(Bool())

        val read_data = Output(UInt(32.W))

​

        // FIFO status

        val full = Output(Bool())

        val empty = Output(Bool())

    })

    

    def makeCounter(maxCount: Int): NoGlitchCounter

​

    // add extra bit to counter to check for fully/empty status

    assert(isPow2(depth), "AsyncFIFO needs a power-of-two depth!")

    val write_counter = withClock(io.write_clock) {

        val count = makeCounter(depth * 2)

        count.io.en := io.write_enable && !io.full

        count.io.out

    }

    val read_counter = withClock(io.read_clock) {

        val count = makeCounter(depth * 2)

        count.io.en := io.read_enable && !io.empty

        count.io.out

    }

​

    // synchronize

    val sync = withClock(io.read_clock) { ShiftRegister(write_counter, 2) }

​

    // status logic goes here

}

class GrayCounter(val bitwidth: Int) extends NoGlitchCounter(bitwidth) {

    // todo

}

​

class RingCounter(maxCount: Int) extends NoGlitchCounter(maxCount) {

    // todo

}

import scala.math.pow

​

// create a module

class GrayCoder(bitwidth: Int) extends Module {

  val io = IO(new Bundle{

    val in = Input(UInt(bitwidth.W))

    val out = Output(UInt(bitwidth.W))

    val encode = Input(Bool()) // decode on false

  })

  

  when (io.encode) { //encode

    io.out := io.in ^ (io.in >> 1.U)

  } .otherwise { // decode, much more complicated

    io.out := Seq.fill(log2Ceil(bitwidth))(Wire(UInt(bitwidth.W))).zipWithIndex.fold((io.in, 0)){

      case ((w1: UInt, i1: Int), (w2: UInt, i2: Int)) => {

        w2 := w1 ^ (w1 >> pow(2, log2Ceil(bitwidth)-i2-1).toInt.U)

        (w2, i1)

      }

    }._1

  }

}

Give it a whirl!

// test our gray coder

val bitwidth = 4

Driver(() => new GrayCoder(bitwidth)) {

  c => new PeekPokeTester(c) {

  

    def toBinary(i: Int, digits: Int = 8) =

      String.format("%" + digits + "s", i.toBinaryString).replace(' ', '0')

​

    println("Encoding:")

    for (i <- 0 until pow(2, bitwidth).toInt) {

      poke(c.io.in, i)

      poke(c.io.encode, true)

      step(1)

      println(s"In = ${toBinary(i, bitwidth)}, Out = ${toBinary(peek(c.io.out).toInt, bitwidth)}")

    }

    

    println("Decoding:")

    for (i <- 0 until pow(2, bitwidth).toInt) {

      poke(c.io.in, i)

      poke(c.io.encode, false)

      step(1)

      println(s"In = ${toBinary(i, bitwidth)}, Out = ${toBinary(peek(c.io.out).toInt, bitwidth)}")

    }

  }

}

Gray codes are often used in asynchronous interfaces. Usually Gray counters are used rather than fully-featured encoders/decoders, but we'll the above module to simplify things. Below is an example AsyncFIFO, built using the above Gray coder. The control logic and tester is left as an exercise for later on. For now, look at how the Gray coder is instantiated multiple times and connected.

class AsyncFIFO(depth: Int = 16) extends Module {

  val io = IO(new Bundle{

    // write inputs

    val write_clock = Input(Clock())

    val write_enable = Input(Bool())

    val write_data = Input(UInt(32.W))

    

    // read inputs/outputs

    val read_clock = Input(Clock())

    val read_enable = Input(Bool())

    val read_data = Output(UInt(32.W))

    

    // FIFO status

    val full = Output(Bool())

    val empty = Output(Bool())

  })

  

  // add extra bit to counter to check for fully/empty status

  assert(isPow2(depth), "AsyncFIFO needs a power-of-two depth!")

  val write_counter = withClock(io.write_clock) { Counter(io.write_enable && !io.full, depth*2)._1 }

  val read_counter = withClock(io.read_clock) { Counter(io.read_enable && !io.empty, depth*2)._1 }

  

  // encode

  val encoder = new GrayCoder(write_counter.getWidth)

  encoder.io.in := write_counter

  encoder.io.encode := true.B

  

  // synchronize

  val sync = withClock(io.read_clock) { ShiftRegister(encoder.io.out, 2) }

  

  // decode

  val decoder = new GrayCoder(read_counter.getWidth)

  decoder.io.in := sync

  decoder.io.encode := false.B

  

  // status logic goes here

  

}

You're done!

Return to the top.
