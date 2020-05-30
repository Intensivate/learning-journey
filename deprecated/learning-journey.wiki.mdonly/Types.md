# Module 3.6: Generators: Types

Prev: [[Object Oriented Programming]]

## Motivation

Scala is a strongly-typed programming language. This is a two-edged sword; on one hand, many programs that would compile and execute in Python (a dynamically-typed language) would fail at compile time in Scala. On the other hand, programs that compile in Scala will contain many fewer runtime errors than a similar Python program.

In this section, our goal is to familiarize you with types as a first class citizen in Scala. While initially you may feel you have limited productivity, you will soon learn to understand compile-time error messages and how to architect your programs with the type system in mind to catch more errors for you.

## Setup

```scala
val path = System.getProperty("user.dir") + "/source/load-ivy.sc"
interp.load.module(ammonite.ops.Path(java.nio.file.FileSystems.getDefault().getPath(path)))
import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
```

## Static Types

### Types in Scala

All objects in Scala have a type, which is usually the object's class. Let's see some:

```scala
println(10.getClass)
println(10.0.getClass)
println("ten".getClass)
```

When you declare your own class, it has an associated type.

class MyClass {

    def myMethod = ???

}

println(new MyClass().getClass)

While not required, it is HIGHLY recommended that you define input and output types for all function declarations. This will let the Scala compiler catch improper use of a function.

def double(s: String): String = s + s

// Uncomment the code below to test it

// double("hi")      // Proper use of double

// double(10)        // Bad input argument!

// double("hi") / 10 // Inproper use of double's output!

Functions that don't return anything return type Unit.

var counter = 0

def increment(): Unit = {

    counter += 1

}

increment()

Scala vs. Chisel Types
TODO: ISN'T THIS IN 2.2?

Module 2.2 discussed the difference between Chisel types and Scala types, for example the fact that

val a = Wire(UInt(4.W))
a := 0.U

is legal because 0.U is of type UInt (a Chisel type), whereas

val a = Wire(UInt(4.W))
a := 0

is illegal because 0 is type Int (a Scala type).

This is also true of Bool, a Chisel type which is distinct from Boolean.

val bool = Wire(Bool())
val boolean: Boolean = false
// legal
when (bool) { ... }
if (boolean) { ... }
// illegal
if (bool) { ... }
when (boolean) { ... }

If you make a mistake and mix up UInt and Int or Bool and Boolean, the Scala compiler will generally catch it for you. This is because of Scala's static typing. At compile time, the compiler is able to distinguish between Chisel and Scala types and understand that if () expects a Boolean and when () expects a Bool.
Type Coercion
TODO: SHOULD WE EVEN HAVE THESE TWO?
getTypeOf?
asInstanceOf

​

Type Casting in Chisel

The code below will give an error if you try to run it without removing the comment. What's the problem? It is trying to assign a UInt to an SInt, which is illegal.

Chisel has a set of type casting functions. The most general is asTypeOf(), which is shown below. Some chisel objects also define asUInt() and asSInt() as well as some others.

If you remove the // from the code block below, the example should work for you.

class TypeConvertDemo extends Module {

    val io = IO(new Bundle {

        val in  = Input(UInt(4.W))

        val out = Output(SInt(4.W))

    })

    io.out := io.in//.asTypeOf(io.out)

}

​

Driver(() => new TypeConvertDemo) { c =>

  new PeekPokeTester(c) {

      poke(c.io.in, 3)

      expect(c.io.out, 3)

      poke(c.io.in, 15)

      expect(c.io.out, -1)

  }}

Type Matching
Match Operator

Recall that in 3.1 the match operator was introduced. Type matching is especially useful when trying to write type-generic generators. The following example shows an example of a "generator" that can add two literals of type UInt or SInt. Later sections will talk more about writing type-generic generators.

Note: there are much better and safer ways to write type-generic generators in Scala

class ConstantSum(in1: Data, in2: Data) extends Module {

    val io = IO(new Bundle {

        val out = Output(in1.cloneType)

    })

    (in1, in2) match {

        case (x: UInt, y: UInt) => io.out := x + y

        case (x: SInt, y: SInt) => io.out := x + y

        case _ => throw new Exception("I give up!")

    }

}

println(getVerilog(dut = () => new ConstantSum(3.U, 4.U)))

println(getVerilog(dut = () => new ConstantSum(-3.S, 4.S)))

println(getVerilog(dut = () => new ConstantSum(3.U, 4.S)))

​

It is good to remember that Chisel types generally should not be value matched. Scala's match executes during circuit elaboration, but what you probably want is an post-elaboration comparison. The following gives a syntax error:

class InputIsZero extends Module {

    val io = IO(new Bundle {

        val in  = Input(UInt(16.W))

        val out = Output(Bool())

    })

    io.out := (io.in match {

        // note that case 0.U is an error

        case (0.U) => true.B

        case _   => false.B

    })

}

println(getVerilog(dut = () => new InputIsZero))

Unapply

What's actually going on when you do a match? Why can you do fancy value matching with case classes like this:

case class Something(a: String, b: Int)
val a = Something("A", 3)
a match {
    case Something("A", value) => value
    case Something(str, 3)     => 0
}

As it turns out, the companion object that is created for every case class also contains an unapply method, in addition to an apply method. What is an unapply method?

Scala unapply methods are another form of syntactic sugar that give match statements the ability to both match on types and extract values from those types during the matching.

Let's look at the following example. For some reason, let's say that if the generator is being pipelined, the delay is 3*totalWidth, otherwise the delay is 2*someOtherWidth. Because case classes have unapply defined, we can match values inside the case class, like so:

def delay(p: SomeGeneratorParameters): Int = p match {

    case sg @ SomeGeneratorParameters(_, _, true) => sg.totalWidth * 3

    case SomeGeneratorParameters(_, sw, false) => sw * 2

}

​

println(delay(SomeGeneratorParameters(10, 10)))

println(delay(SomeGeneratorParameters(10, 10, true)))

If you look at the delay function, you should note that addition to matching on the type of each character, we are also:

    Directly reference internal values of the parameters
    Sometimes, are matching directly on the internal values of the parameters

These are possible due to the compiler implementing an unapply method. Note that unapplying the case is just syntactic sugar; e.g. the following two cases examples are equivalent:

case p: SomeGeneratorParameters => p.sw * 2
case SomeGeneratorParameters(_, sw, _) => sw * 2

In addition, there are more syntaxes and styles of matching. The following two cases are also equivalent, but the second allows you to match on internal values while still reference the parent value:

case SomeGeneratorParameters(_, sw, true) => sw
case sg@SomeGeneratorParameters(_, sw, true) => sw

Finally, you can directly embed condition checking into match statements, as demonstrated by the third of these equivalent examples:

case SomeGeneratorParameters(_, sw, false) => sw * 2
case s@SomeGeneratorParameters(_, sw, false) => s.sw * 2
case s: SomeGeneratorParameters if s.pipelineMe => s.sw * 2

All these syntaxes are enabled by a Scala unapply method contained in a class's companion object. If you want to unapply a class but do not want to make it a case class, you can manually implement the unapply method. The following example demonstrates how one can manually implement a class's apply and unapply methods:

class Boat(val name: String, val length: Int)

object Boat {

    def unapply(b: Boat): Option[(String, Int)] = Some((b.name, b.length))

    def apply(name: String, length: Int): Boat = new Boat(name, length)

}

​

def getSmallBoats(seq: Seq[Boat]): Seq[Boat] = seq.filter { b =>

    b match {

        case Boat(_, length) if length < 60 => true

        case Boat(_, _) => false

    }

}

​

val boats = Seq(Boat("Santa Maria", 62), Boat("Pinta", 56), Boat("Nina", 50))

println(getSmallBoats(boats).map(_.name).mkString(" and ") + " are small boats!")

Partial Functions

This is a brief overview, this guide has a more detailed overview.

Partial functions are functions that are only defined on a subset of their inputs. Like an option, a partial function may not have a value for a particular input. This can be tested with isDefinedAt(...).

Partial functions can be chained together with orElse.

val partialFunc1: PartialFunction[Int, String] = {

  case i if (i + 1)%3 == 0 => "Something"

}

partialFunc1.isDefinedAt(2) // should be true

partialFunc1.isDefinedAt(1) // should be false

​

val partialFunc2: PartialFunction[Int, String] = {

  case i if (i + 2)%3 == 0 => "Something else"

}

partialFunc2.isDefinedAt(1) // should be true

partialFunc2.isDefinedAt(0) // should be false

​

val partialFunc3 = partialFunc1 orElse partialFunc2

partialFunc3.isDefinedAt(2) // should be true

partialFunc3.isDefinedAt(1) // should be false

​

partialFunc3(2)

partialFunc3(1)

​

Type Safe Connections
TODO: What to say here?
Type Generics

Scala's generic types (also known as polymorphism) is very complicated, especially when coupling it with inheritance.

This section will just get your toes wet; to understand more, check out this tutorial.

Classes can be polymorphic in their types. One good example are sequences, which require knowing what the type of the elements it contains.

val seq1 = Seq("1", "2", "3") // Type is Seq[String]

val seq2 = Seq(1, 2, 3)       // Type is Seq[Int]

val seq3 = Seq(1, "2", true)  // Type is Seq[Any]

Sometimes, the Scala compiler needs help determining a polymorphic type, which requires the user to explicitly put the type:

//val default = Seq() // Error!

val default = Seq[String]() // User must tell compiler that default is of type Seq[String]

Seq(1, "2", true).foldLeft(default){ (strings, next) =>

    next match {

        case s: String => strings ++ Seq(s)

        case _ => strings

    }

}

Functions can also be polymorphic in their input or output types. The following example defines a function that times how long it takes to run a block of code. It is parameterized based on the return type of the block of code. Note that the => T syntax encodes an anonymous function that does not have an argument list, e.g. { ... } versus { x => ... }.

def time[T](block: => T): T = {

    val t0 = System.nanoTime()

    val result = block

    val t1 = System.nanoTime()

    val timeMillis = (t1 - t0) / 1000000.0

    println(s"Block took $timeMillis milliseconds!")

    result

}

​

// Adds 1 through a million

val int = time { (1 to 1000000).reduce(_ + _) }

println(s"Add 1 through a million is $int")

​

// Finds the largest number under a million that, in hex, contains "beef"

val string = time {

    (1 to 1000000).map(_.toHexString).filter(_.contains("beef")).last

}

println(s"The largest number under a million that has beef: $string")

Chisel Type Hierarchy

To write type generic code with Chisel, it is helpful to know a bit about the type hierarchy of Chisel.

chisel3.Data is the base class for Chisel hardware types. UInt, SInt, Vec, Bundle, etc. are all instances of Data. Data can be used in IOs and support :=, wires, regs, etc.

Registers are a good example of polymorphic code in Chisel. Look at the implementation of RegEnable (a register with a Bool enable signal) here. The apply function is templated for [T <: Data], which means RegEnable will work for all Chisel hardware types.

Some operations are only defined on subtypes of Bits, for example +. This is why you can add UInts or SInts but not Bundles or Vecs.

Example: Type Generic ShiftRegister
In Scala, objects and functions aren't the only things we can treat as parameters. We can also treat types as parameters.

We usually need to provide a type constraint. In this case, we want to be able to put objects in a bundle, connect (:=) them, and create registers with them (RegNext). These operations cannot be done on arbitrary objects; for example wire := 3 is illegal because 3 is a Scala Int, not a Chisel UInt. If we use a type constraint to say that type T is a subclass of Data, then we can use := on any objects of type T because := is defined for all Data.

Here is an implementations of a shift register that take types as a parameter. gen is an argument of type T that tells what width to use, for example new ShiftRegister(UInt(4.W)) is a shift register for 4-bit UInts. gen also allows the Scala compiler to infer the type T- you can write new ShiftRegisterUInt) if you want to to be more specific, but the Scala compiler is smart enough to figure it out if you leave out the [UInt].

class ShiftRegisterIO[T <: Data](gen: T, n: Int) extends Bundle {

    require (n >= 0, "Shift register must have non-negative shift")

    

    val in = Input(gen.cloneType)

    val out = Output(Vec(n + 1, gen.cloneType)) // + 1 because in is included in out

}

​

class ShiftRegister[T <: Data](gen: T, n: Int) extends Module {

    val io = IO(new ShiftRegisterIO(gen, n))

    

    io.out.foldLeft(io.in) { case (in, out) =>

        out := in

        RegNext(in)

    }

}

​

class ShiftRegisterTester[T <: Bits](c: ShiftRegister[T]) extends PeekPokeTester(c) {

    println(s"Testing ShiftRegister of type ${c.io.in} and depth ${c.io.out.length}")

    for (i <- 0 until 10) {

        poke(c.io.in, i)

        println(s"$i: ${peek(c.io.out)}")

        step(1)

    }

}

​

Driver(() => new ShiftRegister(UInt(4.W), 5)) { c => new ShiftRegisterTester(c) }

Driver(() => new ShiftRegister(SInt(6.W), 3)) { c => new ShiftRegisterTester(c) }

We generally recommend avoiding to use inheritance with type generics. It can be very tricky to do properly and can get frustrating quickly.
Type Generics with Typeclasses

The example above was limited to simple operations that could be performed on any instance of Data such as := or RegNext(). When generating DSP circuits, we would like to do mathematical operations like addition and multiplication. The dsptools library provides tools for writing type parameterized DSP generators.

Here is an example of writing a multiply-accumulate module. It can be used to generate a multiply-accumulate (MAC) for FixedPoint, SInt, or even DspComplex[T] (the complex number type provided by dsptools). The syntax of the type bound is a little different because dsptools uses typeclasses. They are beyond the scope of this notebook. Read the dsptools readme and documentation for more information on using typeclasses.

T <: Data : Ring means that T is a subtype of Data and is also a Ring . Ring is defined in dsptools as a number with + and * (among other operations).

An alternative to Ring would be Real, but that would not allow us to make a MAC for DspComplex() because complex numbers are not Real.

import chisel3.experimental._

import dsptools.numbers._

​

class Mac[T <: Data : Ring](genIn : T, genOut: T) extends Module {

    val io = IO(new Bundle {

        val a = Input(genIn.cloneType)

        val b = Input(genIn.cloneType)

        val c = Input(genIn.cloneType)

        val out = Output(genOut.cloneType)

    })

    io.out := io.a * io.b + io.c

}

​

println(getVerilog(dut= () => new Mac(UInt(4.W), UInt(6.W)) ))

println(getVerilog(dut= () => new Mac(SInt(4.W), SInt(6.W)) ))

println(getVerilog(dut= () => new Mac(FixedPoint(4.W, 3.BP), FixedPoint(6.W, 4.BP))))

​

Exercise: Mac as Object

The Mac Module has a small number of inputs and just one output. It might be convenient for other Chisel generators to write code like

val out = Mac(a, b, c)

Implement an apply method in the Mac companion object below that implements the Mac functionality.

object Mac {

    def apply[T <: Data : Ring](a: T, b: T, c: T): T = {

    }

}

​

class MacTestModule extends Module {

    val io = IO(new Bundle {

        val uin = Input(UInt(4.W))

        val uout = Output(UInt())

        val sin = Input(SInt(4.W))

        val sout = Output(SInt())

        //val fin = Input(FixedPoint(16.W, 12.BP))

        //val fout = Output(FixedPoint())

    })

    // for each IO pair, do out = in * in + in

    io.uout := Mac(io.uin, io.uin, io.uin)

    io.sout := Mac(io.sin, io.sin, io.sin)

    //io.fout := Mac(io.fin, io.fin, io.fin)

}

println(getVerilog(dut = () => new MacTestModule))

Solution (click to toggle displaying)

object Mac {
    def apply[T <: Data : Ring](a: T, b: T, c: T): T = {
        // you can also instantiate the Mac from above and connect the IOs to arguments
        a * b + c
    }
}

Exercise: Integrator
Implement an integrator as pictured below. n1 is the width of genReg and n2 is the width of genIn.

Don't forget that Reg, RegInit, RegNext, RegEnable, etc. are templated for types T <: Data.

Integrator

class Integrator[T <: Data : Ring](genIn: T, genReg: T) extends Module {

    val io = IO(new Bundle {

        val in  = Input(genIn.cloneType)

        val out = Output(genReg.cloneType)

    })

    

    val reg = RegInit(genReg, Ring[T].zero) // init to zero

    reg := reg + io.in

    io.out := reg

}

​

class IntegratorSIntTester(c: Integrator[SInt]) extends PeekPokeTester(c) {

    poke(c.io.in, 3)

    expect(c.io.out, 0)

    step(1)

    poke(c.io.in, -4)

    expect(c.io.out, 3)

    step(1)

    poke(c.io.in, 6)

    expect(c.io.out, -1)

    step(1)

    expect(c.io.out, 5)

}

​

chisel3.iotesters.Driver(() => new Integrator(SInt(4.W), SInt(8.W))) { c => new IntegratorSIntTester(c) }

Solution (click to toggle displaying)


class Integrator[T <: Data : Ring](genIn: T, genReg: T) extends Module {
    val io = IO(new Bundle {
        val in  = Input(genIn.cloneType)
        val out = Output(genReg.cloneType)
    })

    val reg = RegInit(genReg, Ring[T].zero) // init to zero
    reg := reg + io.in
    io.out := reg
}

Creating a Custom Type

One of the things that makes Chisel powerful is its extensibility. You can add your own types that have their own operations and representations that are tailored to your application. This section will introduce ways to make custom types.

Example: DspComplex
DspComplex is a custom data type defined in dsptools here. The key line to understand is this:

class DspComplex[T <: Data:Ring](val real: T, val imag: T) extends Bundle { ... }

DspComplex is a type-generic container. That means the real and imaginary parts of a complex number can be any type as long as they satisfy the type constraints, given by T <: Data : Ring.

T <: Data means T is a subtype of chisel3.Data, the base type for Chisel objects. This means that DspComplex only works for objects that are Chisel types and not arbitrary Scala types.

T : Ring means that a Ring typeclass implementation for T exists. Ring typeclasses define + and * operators as well as additive and multiplicative identities (see this Wikipedia article) for details about rings). dsptools defines typeclasses for commonly used Chisel types here.

dsptools also defines a Ring typeclass for DspComplex, so we can reuse our MAC generator with complex numbers:

println(getVerilog(dut= () => new Mac(DspComplex(SInt(4.W), SInt(4.W)), DspComplex(SInt(6.W), SInt(6.W))) ))

Exercise: Sign-magnitude Numbers
Suppose you wanted to use a sign-magnitude representation and want to reuse all of your DSP generators. Typeclasses enable this kind of ad-hoc polymorphism. The following example gives the beggining of an implementation of a SignMagnitude type as well as an implementation of a Ring typeclass that will allow the type to be used with the Mac generator.

Fill in implementations for + and *. You should pattern them after the implementation for unary_-(). The next block contains a test that checks the correctness of a Mac that uses SignMagnitude.

class SignMagnitude(val magnitudeWidth: Option[Int] = None) extends Bundle {

    val sign = Bool()

    val magnitude = magnitudeWidth match {

        case Some(w) => UInt(w.W)

        case None    => UInt()

    }

    def +(that: SignMagnitude): SignMagnitude = {

        // Implement this!

    }

    def -(that: SignMagnitude): SignMagnitude = {

        this.+(-that)

    }

    def unary_-(): SignMagnitude = {

        val result = Wire(new SignMagnitude())

        result.sign := !this.sign

        result.magnitude := this.magnitude

        result

    }

    def *(that: SignMagnitude): SignMagnitude = {

        // Implement this!

    }

    override def cloneType: this.type = new SignMagnitude(magnitudeWidth).asInstanceOf[this.type]

}

trait SignMagnitudeRing extends Ring[SignMagnitude] {

    def plus(f: SignMagnitude, g: SignMagnitude): SignMagnitude = {

        f + g

    }

    def times(f: SignMagnitude, g: SignMagnitude): SignMagnitude = {

        f * g

    }

    def one: SignMagnitude = {

        val one = Wire(new SignMagnitude(Some(1)))

        one.sign := false.B

        one.magnitude := 1.U

        one

    }

    def zero: SignMagnitude = {

        val zero = Wire(new SignMagnitude(Some(0)))

        zero.sign := false.B

        zero.magnitude := 0.U

        zero

    }

    def negate(f: SignMagnitude): SignMagnitude = {

        -f

    }

    

    // Leave unimplemented for this example

    def minusContext(f: SignMagnitude, g: SignMagnitude): SignMagnitude = ???

    def negateContext(f: SignMagnitude): SignMagnitude = ???

    def plusContext(f: SignMagnitude,g: SignMagnitude): SignMagnitude = ???

    def timesContext(f: SignMagnitude,g: SignMagnitude): SignMagnitude = ???

}

implicit object SignMagnitudeRingImpl extends SignMagnitudeRing

class SignMagnitudeMACTester(c: Mac[SignMagnitude]) extends PeekPokeTester(c) {

    // 3 * 3 + 2 = 11

    poke(c.io.a.sign, 0)

    poke(c.io.a.magnitude, 3)

    poke(c.io.b.sign, 0)

    poke(c.io.b.magnitude, 3)

    poke(c.io.c.sign, 0)

    poke(c.io.c.magnitude, 2)

    expect(c.io.out.sign, 0)

    expect(c.io.out.magnitude, 11)

    // 3 * 3 - 2 = 7

    poke(c.io.c.sign, 1)

    expect(c.io.out.sign, 0)

    expect(c.io.out.magnitude, 7)

    // 3 * (-3) - 2 = -11

    poke(c.io.b.sign, 1)

    expect(c.io.out.sign, 1)

    expect(c.io.out.magnitude, 11)

}

val works = iotesters.Driver(() => new Mac(new SignMagnitude(Some(4)), new SignMagnitude(Some(5)))) {

  c => new SignMagnitudeMACTester(c)

}

assert(works) // Scala Code: if works == false, will throw an error

println("SUCCESS!!") // Scala Code: if we get here, our tests passed!

Look at the verilog to see if the output looks reasonable:

println(getVerilog(dut= () => new Mac(new SignMagnitude(Some(4)), new SignMagnitude(Some(5)))))

SignMagnitude even works with DspComplex!

println(getVerilog(dut= () => new Mac(DspComplex(new SignMagnitude(Some(4)), new SignMagnitude(Some(4))), DspComplex(new SignMagnitude(Some(5)), new SignMagnitude(Some(5))))))

Solution (click to toggle displaying)

    // implementations for class SignMagnitude

    def +(that: SignMagnitude): SignMagnitude = {
        // TODO hide me
      val result = Wire(new SignMagnitude())
      val signsTheSame = this.sign === that.sign
      when (signsTheSame) {
        result.sign      := this.sign
        result.magnitude := this.magnitude + that.magnitude
      } .otherwise {
        when (this.magnitude > that.magnitude) {
          result.sign      := this.sign
          result.magnitude := this.magnitude - that.magnitude
        } .otherwise {
          result.sign      := that.sign
          result.magnitude := that.magnitude - this.magnitude
        }
      }
      result
    }
    def (that: SignMagnitude): SignMagnitude = {
        // TODO hide me
        val result = Wire(new SignMagnitude())
        result.sign := this.sign ^ that.sign
        result.magnitude := this.magnitude  that.magnitude
        result
    }


