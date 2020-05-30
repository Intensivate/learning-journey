# Module 3.4: Functional Programming

Prev: [Higher-Order Functions](temp-page-to-gather-syntax-examples)

Next: [[Object Oriented Programming]]

## Motivation

You saw functions in many previous modules, but now it's time to make our own and use them effectively.

## Setup

```scala
val path = System.getProperty("user.dir") + "/source/load-ivy.sc"
interp.load.module(ammonite.ops.Path(java.nio.file.FileSystems.getDefault().getPath(path)))
```

This module uses the Chisel FixedPoint type, which currently resides in the experimental package.

```scala
import chisel3._
import chisel3.util._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import chisel3.experimental._
import chisel3.internal.firrtl.KnownBinaryPoint
```

## Functional Programming in Scala

Scala functions were introduced in Module 1, and you saw then used a lot in the previous module. Here's a refresher on functions. Functions take any number of inputs and produce one output. Inputs are often called arguments to a function. To produce no output, return the Unit type.

Example: Custom Functions
Below are some examples of functions in Scala.

// no inputs or outputs (two versions)

def hello1(): Unit = print("Hello!")

def hello2 = print("Hello again!")

​

// math operation, one input and one output

def times2(x: Int): Int = 2 * x

​

// inputs can have default values, and the return type is optional

def timesN(x: Int, n: Int = 2) = n * x

​

// call the functions listed above

hello1()

hello2

times2(4)

timesN(4)         // no need to specify n to use the default value

timesN(4, 3)      // argument order is the same as the order where the function was defined

timesN(n=7, x=2)  // arguments may be reordered and assigned to explicitly

Functions as Objects

Functions in Scala are first-class objects. That means we can assign a function to a val and pass it to classes, objects, or other functions as an argument.

Example: Function Objects
Below are the same functions implemented as functions and as objects.

// these are normal functions

def plus1funct(x: Int): Int = x + 1

def times2funct(x: Int): Int = x * 2

​

// these are functions as vals

// the first one explicitly specifies the return type

val plus1val: Int => Int = x => x + 1

val times2val = (x: Int) => x * 2

​

// calling both looks the same

plus1funct(4)

plus1val(4)

plus1funct(x=4)

//plus1val(x=4) // this doesn't work

Why would you want to create a val instead of a def? With a val, you can now pass the function around to other functions, as shown below. You can even create your own functions that accept other functions as arguments. Formally, functions that take or produce functions are called higher-order functions. You saw them used in the last module, but now you'll make your own!

Example: Higher-Order Functions
Here we show map again, and we also create a new function, opN, that accepts a function, op, as an argument.

// create our function

val plus1 = (x: Int) => x + 1

val times2 = (x: Int) => x * 2

​

// pass it to map, a list function

val myList = List(1, 2, 5, 9)

val myListPlus = myList.map(plus1)

val myListTimes = myList.map(times2)

​

// create a custom function, which performs an operation on X N times using recursion

def opN(x: Int, n: Int, op: Int => Int): Int = {

  if (n <= 0) { x }

  else { opN(op(x), n-1, op) }

}

​

opN(7, 3, plus1)

opN(7, 3, times2)

Example: Functions vs. Objects
A possibly confusing situation arises when using functions without arguments. Functions are evaluated every time they are called, while vals are evaluated at instantiation.

import scala.util.Random

​

// both x and y call the nextInt function, but x is evaluated immediately and y is a function

val x = Random.nextInt

def y = Random.nextInt

​

// x was previously evaluated, so it is a constant

println(s"x = $x")

println(s"x = $x")

​

// y is a function and gets reevaluated at each call, thus these produce different results

println(s"y = $y")

println(s"y = $y")

Anonymous Functions

As the name implies, anonymous functions are nameless. There's no need to create a val for a function if we'll only use it once.

Example: Anonymous Functions
The following example demonstrates this. They are often scoped (put in curly braces instead of parentheses).

val myList = List(5, 6, 7, 8)

​

// add one to every item in the list using an anonymous function

// arguments get passed to the underscore variable

// these all do the same thing

myList.map( (x:Int) => x + 1 )

myList.map(_ + 1)

​

// a common situation is to use case statements within an anonymous function

val myAnyList = List(1, 2, "3", 4L, myList)

myAnyList.map {

  case (_:Int|_:Long) => "Number"

  case _:String => "String"

  case _ => "error"

}

Exercise: Sequence Manipulation
A common set of higher-order functions you'll use are scanLeft/scanRight, reduceLeft/reduceRight, and foldLeft/foldRight. It's important to understand how each one works and when to use them. The default directions for scan, reduce, and fold are left, though this is not guaranteed for all cases.

val exList = List(1, 5, 7, 100)

​

// write a custom function to add two numbers, then use reduce to find the sum of all values in exList

def add(a: Int, b: Int): Int = ???

val sum = ???

​

// find the sum of exList using an anonymous function (hint: you've seen this before!)

val anon_sum = ???

​

// find the moving average of exList from right to left using scan; make the result (ma2) a list of doubles

def avg(a: Int, b: Double): Double = ???

val ma2 = ???

Solution

def add(a: Int, b: Int): Int = a+b
val sum = exList.reduce(add)

val anon_sum = exList.reduce(_+_)

def avg(a: Int, b: Double): Double = (a+b)/2.0
val ma2 = exList.scanRight(0.0)(avg)

Functional Programming in Chisel

Let's look at some examples of how to use functional programming when creating hardware generators in Chisel.

Example: FIR Filter
First, we'll revisit the FIR filter from previous examples. Instead of passing in the coefficients as parameters to the class or making them programmable, we'll pass a function to the FIR that defines how the window coefficients are calculated. This function will take the window length and bitwidth to produce a scaled list of coefficients. Here are two example windows. To avoid fractions, we'll scale the coefficients to be between the max and min integer values. For more on these windows, check out the this Wikipedia page.

// get some math functions

import scala.math.{abs, round, cos, Pi, pow}

​

// simple triangular window

val TriangularWindow: (Int, Int) => Seq[Int] = (length, bitwidth) => {

  val raw_coeffs = (0 until length).map( (x:Int) => 1-abs((x.toDouble-(length-1)/2.0)/((length-1)/2.0)) )

  val scaled_coeffs = raw_coeffs.map( (x: Double) => round(x * pow(2, bitwidth)).toInt)

  scaled_coeffs

}

​

// Hamming window

val HammingWindow: (Int, Int) => Seq[Int] = (length, bitwidth) => {

  val raw_coeffs = (0 until length).map( (x: Int) => 0.54 - 0.46*cos(2*Pi*x/(length-1)))

  val scaled_coeffs = raw_coeffs.map( (x: Double) => round(x * pow(2, bitwidth)).toInt)

  scaled_coeffs

}

​

// check it out! first argument is the window length, and second argument is the bitwidth

TriangularWindow(10, 16)

HammingWindow(10, 16)

Now we'll create a FIR filter that accepts a window function as the argument. This allows us to define new windows later on and retain the same FIR generator. It also allows us to independently size the FIR, knowing the window will be recalculated for different lengths or bitwidths. Since we are choosing the window at compile time, these coefficients are fixed.

// our FIR has parameterized window length, IO bitwidth, and windowing function

class MyFir(length: Int, bitwidth: Int, window: (Int, Int) => Seq[Int]) extends Module {

  val io = IO(new Bundle {

    val in = Input(UInt(bitwidth.W))

    val out = Output(UInt((bitwidth*2+length-1).W)) // expect bit growth, conservative but lazy

  })

​

  // calculate the coefficients using the provided window function, mapping to UInts

  val coeffs = window(length, bitwidth).map(_.U)

  

  // create an array holding the output of the delays

  // note: we avoid using a Vec here since we don't need dynamic indexing

  val delays = Seq.fill(length)(Wire(UInt(bitwidth.W))).scan(io.in)( (prev: UInt, next: UInt) => {

    next := RegNext(prev)

    next

  })

  

  // multiply, putting result in "mults"

  val mults = delays.zip(coeffs).map{ case(delay: UInt, coeff: UInt) => delay * coeff }

  

  // add up multiplier outputs with bit growth

  val result = mults.reduce(_+&_)

​

  // connect output

  io.out := result

}

Those last three lines could be easily combined into one. Also notice how we've handled bitwidth growth conservatively to avoid loss.

Example: FIR Filter Tester
Let's test our FIR! Previously, we provided a custom golden model. This time we'll use Breeze, a Scala library of useful linear algebra and signal processing functions, as a golden model for our FIR filter. The code below compares the Chisel output with the golden model output, and any errors cause the tester to fail.

Try uncommenting the print statment at the end just after the expect call. Also try changing the window from triangular to Hamming.

// math imports

import scala.math.{pow, sin, Pi}

import breeze.signal.{filter, OptOverhang}

import breeze.signal.support.{CanFilter, FIRKernel1D}

import breeze.linalg.DenseVector

​

// test parameters

val length = 7

val bitwidth = 12 // must be less than 15, otherwise Int can't represent the data, need BigInt

val window = TriangularWindow

​

// test our FIR

Driver(() => new MyFir(length, bitwidth, window)) {

  c => new PeekPokeTester(c) {

    

    // test data

    val n = 100 // input length

    val sine_freq = 10

    val samp_freq = 100

    

    // sample data, scale to between 0 and 2^bitwidth

    val max_value = pow(2, bitwidth)-1

    val sine = (0 until n).map(i => (max_value/2 + max_value/2*sin(2*Pi*sine_freq/samp_freq*i)).toInt)

    //println(s"input = ${sine.toArray.deep.mkString(", ")}")

    

    // coefficients

    val coeffs = window(length, bitwidth)

    //println(s"coeffs = ${coeffs.toArray.deep.mkString(", ")}")

​

    // use breeze filter as golden model; need to reverse coefficients

    val expected = filter(DenseVector(sine.toArray), 

                          FIRKernel1D(DenseVector(coeffs.reverse.toArray), 1.0, ""), 

                          OptOverhang.None)

    //println(s"exp_out = ${expected.toArray.deep.mkString(", ")}")

​

    // push data through our FIR and check the result

    reset(5)

    for (i <- 0 until n) {

      poke(c.io.in, sine(i))

      if (i >= length-1) { // wait for all registers to be initialized since we didn't zero-pad the data

        expect(c.io.out, expected(i-length+1))

        //println(s"cycle $i, got ${peek(c.io.out)}, expect ${expected(i-length+1)}")

      }

      step(1)

    }

  }

}

Chisel Exercises

Complete the following exercises to practice writing functions, using them as arguments to hardware generators, and avoiding mutable data.

Exercise: Neural Network Neuron
Our first example will have you build a neuron, the building block of fully-connected layers in artificial neural networks. Neurons take inputs and a set of weights, one per input, and produce one output. The weights and inputs are multiplied and added, and the result is fed through an activation function. In this exercise, you will implement different activation functions and pass them as an argument to your neuron generator.

Neuron

First, complete the following code to create a neuron generator. We'll make the inputs and outputs 16-bit fixed point values with 8 fractional bits.

class Neuron(inputs: Int, act: FixedPoint => FixedPoint) extends Module {

  val io = IO(new Bundle {

    val in      = Input(Vec(inputs, FixedPoint(16.W, 8.BP)))

    val weights = Input(Vec(inputs, FixedPoint(16.W, 8.BP)))

    val out     = Output(FixedPoint(16.W, 8.BP))

  })

  

  ???

}

Solution

  val mac = io.in.zip(io.weights).map{ case(a:FixedPoint, b:FixedPoint) => a*b}.reduce(+)
  io.out := act(mac)

Now let's create some activation functions! Typical activation functions are the sigmoid function and the rectified linear unit (ReLU).

The sigmoid we'll use is called the logistic function, given by

logistic(x)=11+e−βx

where β is the slope factor. However, computing the exponential function in hardware is quite challenging and expensive. We'll approximate this as the step function.

step(x)={0if x≤01if x>0

The second function, the ReLU, is given by a similar formula.

relu(x)={0if x≤0xif x>0

Implement these two functions below. You can specify a fixed-point literal like -3.14.F(8.BP).

val Step: FixedPoint => FixedPoint = ???

val ReLU: FixedPoint => FixedPoint = ???

Solution

val Step: FixedPoint => FixedPoint = x => Mux(x <= 0.5.F(8.BP), 0.F(8.BP), 1.F(8.BP))
val ReLU: FixedPoint => FixedPoint = x => Mux(x <= 0.5.F(8.BP), 0.F(8.BP), x)

Finally, let's create a tester that checks the correctness of our Neuron. With the step activation function, neurons may be used as logic gate approximators. Proper selection of weights can perform AND, OR, NOT, and other binary functions. Since NOT requires a bias (which we did not implement) and XOR requires chaining multiple neurons, we'll test our neuron just with AND and OR logic. Complete the following tester to check our neuron with the step function.

Note that since the circuit is purely combinational, the reset(5) and step(1) calls are not necessary.

// test our Neuron 

Driver(() => new Neuron(2, Step)) {

  c => new PeekPokeTester(c) {

    

    val inputs = Seq(Seq(0, 0), Seq(0, 1), Seq(1, 0), Seq(1, 1))

    

    // make these a sequence of two values

    val and_weights = ???

    val or_weights  = ???

​

    // push data through our Neuron and check the result (AND gate)

    reset(5)

    for (i <- inputs) {

      pokeFixedPoint(c.io.in(0), i(0))

      pokeFixedPoint(c.io.in(1), i(1))

      pokeFixedPoint(c.io.weights(0), and_weights(0))

      pokeFixedPoint(c.io.weights(1), and_weights(1))

      expectFixedPoint(c.io.out, i(0) & i(1), "ERROR")

      step(1)

    }

    

    // push data through our Neuron and check the result (OR gate)

    reset(5)

    for (i <- inputs) {

      pokeFixedPoint(c.io.in(0), i(0))

      pokeFixedPoint(c.io.in(1), i(1))

      pokeFixedPoint(c.io.weights(0), or_weights(0))

      pokeFixedPoint(c.io.weights(1), or_weights(1))

      expectFixedPoint(c.io.out, i(0) | i(1), "ERROR")

      step(1)

    }

  }

}

Solution

val and_weights = Seq(0.5, 0.5)
val or_weights  = Seq(1.0, 1.0)

You're done!

Return to the top.
