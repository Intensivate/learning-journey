# Module 3.3: Higher-Order Functions

Next: [[Functional Programming]]

## Motivation

Those pesky for loops in the previous module are verbose and defeat the purpose of functional programming! In this module, your generators will get funct-ky.

## Setup

```scala
val path = System.getProperty("user.dir") + "/source/load-ivy.sc"
interp.load.module(ammonite.ops.Path(java.nio.file.FileSystems.getDefault().getPath(path)))

import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
```

## A Tale of Two FIRs

From the last module, we had the convolution part of the FIR filter written like this:

```scala
val muls = Wire(Vec(length, UInt(8.W)))
for(i <- 0 until length) {
  if(i == 0) muls(i) := io.in * io.consts(i)
  else       muls(i) := regs(i - 1) * io.consts(i)
}

val scan = Wire(Vec(length, UInt(8.W)))
for(i <- 0 until length) {
  if(i == 0) scan(i) := muls(i)
  else scan(i) := muls(i) + scan(i - 1)
}

io.out := scan(length - 1)
```

As a recap, the idea is to multiply each element of io.in with the corresponding element of io.consts, and store it in muls. Then, the elements in muls are accumulated into scan, with scan(0) = muls(0), scan(1) = scan(0) + muls(1) = muls(0) + muls(1), and in general scan(n) = scan(n-1) + muls(n) = muls(0) + ... + muls(n-1) + muls(n). The last element in scan (equal to the sum of all muls) is assigned to io.out.

However, it's very verbose for what might be considered quite a simple operation. In fact, all that could be written in one line:

io.out := (taps zip io.consts).map { case (a, b) => a * b }.reduce(_ + _)

What's it doing?! Let's break it down:

    (taps zip io.consts) takes two lists, taps and io.consts, and combines them into one list where each element is a tuple of the elements at the inputs at the corresponding position. Concretely, its value would be [(taps(0), io.consts(0)), (taps(1), io.consts(1)), ..., (taps(n), io.consts(n))]. Remember that periods are optional, so this is equivalent to (taps.zip(io.consts)).
    .map { case (a, b) => a * b } applies the anonymous function (takes a tuple of two elements returns their product) to the elements of the list, and returns the result. In this case, the result is equivalent to muls in the verbose example, and has the value [taps(0) * io.consts(0), taps(1) * io.consts(1), ..., taps(n) * io.consts(n)]. You'll revisit anonymous functions in the next module. For now, just learn this syntax.
    Finally, .reduce(_ + _) also applies the function (addition of elements) to elements of the list. However, it takes two arguments: the first is the current accumulation, and the second is the list element (in the first iteration, it just adds the first two elements). These are given by the two underscores in the parentheses. The result would then be, assuming left-to-right traversal, (((muls(0) + muls(1)) + muls(2)) + ...) + muls(n), with the result of deeper-nested parentheses evaluated first. This is the output of the convolution.

Functions as Arguments

Formally, functions like map and reduce are called higher-order functions: they are functions that take functions as arguments. As it turns out (and hopefully, as you can see from the above example), these are very powerful constructs that encapsulate a general computational pattern, allowing you to concentrate on the application logic instead of flow control, and resulting in very concise code.
Different ways of specifying functions

You may have noticed that there were two ways of specifying functions in the examples above:

    For functions where each argument is referred to exactly once, you may be able to use an underscore (_) to refer to each argument. In the example above, the reduce argument function took two arguments and could be specified as _ + _. While convenient, this is subject to an additional set of arcane rules, so if it does't work, try:
    Specifying the inputs argument list explicitly. The reduce could have been explicitly written as (a, b) => a + b, with the general form of putting the argument list in parentheses, followed by =>, followed by the function body referring to those arguments.
    When tuple unpacking is needed, using a case statement, as in case (a, b) => a * b. That takes a single argument, a tuple of two elements, and unpacks it into variables a and b, which can then be used in the function body.

Practice in Scala

In the last module, we've seen major classes in the Scala Collections API, like Lists. These higher-order functions are part of these APIs - and in fact, the above example uses the map and reduce API on Lists. In this section, we'll familiarize ourselves with these methods through examples and exercises. In these examples, we'll operate on Scala numbers (Ints) for the sake of simpliciy and clarify, but because Chisel operators behave similarly, the concepts should generalize.

Example: Map
List[A].map has type signature map[B](f: (A) ⇒ B): List[B]. You'll learn more about types in a later module. For now, think of types A and B as Ints or UInts, meaning they could be software or hardware types.

In plain English, it takes an argument of type (f: (A) ⇒ B), or a function that takes one argument of type A (the same type as the element of the input List) and returns a value of type B (which can be anything). map then returns a new list of type B (the return type of the argument function).

As we've already explained the behavior of List in the FIR explanation, let's get straight into the examples and exercises:

println(List(1, 2, 3, 4).map(x => x + 1))  // explicit argument list in function

println(List(1, 2, 3, 4).map(_ + 1))  // equivalent to the above, but implicit arguments

println(List(1, 2, 3, 4).map(_.toString + "a"))  // the output element type can be different from the input element type

​

println(List((1, 5), (2, 6), (3, 7), (4, 8)).map { case (x, y) => x*y })  // this unpacks a tuple, note use of curly braces

​

// Related: Scala has a syntax for constructing lists of sequential numbers

println(0 to 10)  // to is inclusive , the end point is part of the result

println(0 until 10)  // until is exclusive at the end, the end point is not part of the result

​

// Those largely behave like lists, and can be useful for generating indices:

val myList = List("a", "b", "c", "d")

println((0 until 4).map(myList(_)))

// Now you try: 

// Fill in the blanks (the ???) such that this doubles the elements of the input list.

// This should return: List(2, 4, 6, 8)

println(List(1, 2, 3, 4).map(???))

Example: zipWithIndex
List.zipWithIndex has type signature zipWithIndex: List[(A, Int)].

It takes no arguments, but returns a list where each element is a tuple of the original elements, and the index (with the first one being zero). So List("a", "b", "c", "d").zipWithIndex would return List(("a", 0), ("b", 1), ("c", 2), ("d", 3))

This is useful when then element index is needed in some operation.

Since this is a pretty straightforward, we'll just have some examples:

println(List(1, 2, 3, 4).zipWithIndex)  // note indices start at zero

println(List("a", "b", "c", "d").zipWithIndex)

println(List(("a", "b"), ("c", "d"), ("e", "f"), ("g", "h")).zipWithIndex)  // tuples nest

Example: Reduce
List[A].map has type signature similar to reduce(op: (A, A) ⇒ A): A. (it's actually more lenient, A only has to be a supertype of the List type, but we're not going to deal with that syntax here)

As it's also been explained above, here are some examples:

println(List(1, 2, 3, 4).reduce((a, b) => a + b))  // returns the sum of all the elements

println(List(1, 2, 3, 4).reduce(_ * _))  // returns the product of all the elements

println(List(1, 2, 3, 4).map(_ + 1).reduce(_ + _))  // you can chain reduce onto the result of a map

// Important note: reduce will fail with an empty list

println(List[Int]().reduce(_ * _))

// Now you try: 

// Fill in the blanks (the ???) such that this returns the product of the double of the elements of the input list.

// This should return: (1*2)*(2*2)*(3*2)*(4*2) = 384

println(List(1, 2, 3, 4).map(???).reduce(???))

Example: Fold
List[A].fold is very similar to reduce, except that you can specify the initial accumulation value. It has type signature similar to fold(z: A)(op: (A, A) ⇒ A): A. (like reduce, the type of A is also more lenient)

Notably, it takes two argument lists, the first (z) is the initial value, and the second is the accumulation function. Unlike reduce, it will not fail with an empty list, instead returning the initial value directly.

Here's some examples:

println(List(1, 2, 3, 4).fold(0)(_ + _))  // equivalent to the sum using reduce

println(List(1, 2, 3, 4).fold(1)(_ + _))  // like above, but accumulation starts at 1

println(List().fold(1)(_ + _))  // unlike reduce, does not fail on an empty input

// Now you try: 

// Fill in the blanks (the ???) such that this returns the double the product of the elements of the input list.

// This should return: 2*(1*2*3*4) = 48

// Note: unless empty list tolerance is needed, reduce is a much better fit here.

println(List(1, 2, 3, 4).fold(???)(???))

Exercise: Decoupled Arbiter
Let's put everything together now into an exercise.

For this example, we're going to build a Decoupled arbiter: a module that has n Decoupled inputs and one Decoupled output. The arbiter selects the lowest channel that is valid and forwards it to the output.

Some hints:

    Architecturally:
        io.out.valid is true if any of the inputs are valid
        Consider having an internal wire of the selected channel
        Each input's ready is true if the output is ready, AND that channel is selected (this does combinationally couple ready and valid, but we'll ignore it for now...)
    These constructs may help:
        map, especially for returning a Vec of sub-elements, for example io.in.map(_.valid) returns a list of valid signals of the input Bundles
        PriorityMux(List[Bits, Bool]), which takes in a list of bits and valid signals, returning the first element that is valid
        Dynamic index on a Vec, by indexing with a UInt, for example io.in(0.U)

class MyRoutingArbiter(numChannels: Int) extends Module {

  val io = IO(new Bundle {

    val in = Vec(Flipped(Decoupled(UInt(8.W))), numChannels)

    val out = Decoupled(UInt(8.W))

  } )

​

  // Your code here

  ???

}

​

// verify that the computation is correct

class MyRoutingArbiterTester(c: MyRoutingArbiter) extends PeekPokeTester(c) {

  // Set input defaults

  for(i <- 0 until 4) {

    poke(c.io.in(i).valid, 0)

    poke(c.io.in(i).bits, i)

    poke(c.io.out.ready, 1)

  }

    

  expect(c.io.out.valid, 0)

    

  // Check single input valid behavior with backpressure

  for (i <- 0 until 4) {

    poke(c.io.in(i).valid, 1)

    expect(c.io.out.valid, 1)

    expect(c.io.out.bits, i)

      

    poke(c.io.out.ready, 0)

    expect(c.io.in(i).ready, 0)

      

    poke(c.io.out.ready, 1)

    poke(c.io.in(i).valid, 0)

  }

    

  // Basic check of multiple input ready behavior with backpressure

  poke(c.io.in(1).valid, 1)

  poke(c.io.in(2).valid, 1)

  expect(c.io.out.bits, 1)

  expect(c.io.in(1).ready, 1)

  expect(c.io.in(0).ready, 0)

    

  poke(c.io.out.ready, 0)

  expect(c.io.in(1).ready, 0)

}

​

val works = Driver(() => new MyRoutingArbiter(4)) {

  c => new MyRoutingArbiterTester(c)

}

assert(works)        // Scala Code: if works == false, will throw an error

println("SUCCESS!!") // Scala Code: if we get here, our tests passed!

Solution

class MyRoutingArbiter(numChannels: Int) extends Module {
  val io = IO(new Bundle {
    val in = Vec(Flipped(Decoupled(UInt(8.W))), numChannels)
    val out = Decoupled(UInt(8.W))
  } )

  // YOUR CODE BELOW
  io.out.valid := io.in.map(_.valid).reduce(_ || _)
  val channel = PriorityMux(
    io.in.map(_.valid).zipWithIndex.map { case (valid, index) => (valid, index.U) }
  )
  io.out.bits := io.in(channel).bits
  for ((ready, index) <- io.in.map(_.ready).zipWithIndex) {
    ready := io.out.ready && channel === index.U
  }
}

You're done!

Return to the top.
