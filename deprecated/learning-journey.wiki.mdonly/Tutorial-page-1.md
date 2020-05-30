[<<< Previous Page](Chisel-Introductory-Examples) (Chisel Introductory Examples)  [Next Page >> ](Tutorial-page-2) (Tutorial page 2)

### 2.1. The Basics 

### 2.1.1 The Chisel Directory Structure

Once you have acquired the tutorial files you should see the following Chisel tutorial directory structure under $LJHOME:

``` 
learning-journey/ 
  build.sbt # project description
  doc # documentation
  set-learning-journey.sh # script to set up environment
  run-examples.sh # script to run all example codes
  run-problem.sh # script to run all problem codes
  run-solution.sh # script to run all solutions to the problems (solutions are provided)
  src/ # source code
    main/scala/ # Chisel sources constructing hardware
      hello/ # the simplest example, HelloWorld, including its test harness
      examples/ # Chisel code examples
      problems/ # Chisel problems
      solutions/ # Solutions to problems
    test/scala/ # Scala sources containing test harnesses
      examples/ # Scala test harnesses for Chisel examples
      problems/ # Scala test harnesses for Chisel problems
      solutions/ # Scala test harnesses for Chisel solutions
```    
 
Chisel source files are distributed between examples, problems, and solutions directories. The tutorial contains the files that you will be modifying under problems/ while the solutions/ folder contains the reference implementations for each of the problems. Finally, examples/ contains source to the complete examples given in this tutorial.

Finally, the build.sbt files contain the build configuration information used to specify what version of Chisel to make your project with.

### 2.1.2 The Chisel Source Code

Now that you are more familiar with what your Chisel directory structure contains, let’s start by exploring one of the Chisel files. Change directory into the examples/ directory and open up the GCD.scala file with your favorite text editor.

You will notice that file is already filled out for you to perform the well known GCD algorithm and should look like:

```scala
// See LICENSE.txt for license details.
package examples

import chisel3._

/**
 * Compute the GCD of 'a' and 'b' using Euclid's algorithm.
 * To start a computation, load the values into 'a' and 'b' and toggle 'load'
 * high.
 * The GCD will be returned in 'out' when 'valid' is high.
 */
class GCD extends Module {
  val io = IO(new Bundle {
    val a     = Input(UInt(16.W))
    val b     = Input(UInt(16.W))
    val load  = Input(Bool())
    val out   = Output(UInt(16.W))
    val valid = Output(Bool())
  })
  val x = Reg(UInt())
  val y = Reg(UInt())

  when (io.load) {
    x := io.a; y := io.b
  } .otherwise {
    when (x > y) {
      x := x - y
    } .elsewhen (x <= y) {
      y := y - x
    }
  }

  io.out := x
  io.valid := y === 0.U
}
```

The first thing you will notice is the import chisel3._ declaration; this imports the chisel3 library files that allow us to leverage Scala as a hardware construction language. After the import declarations you will see the Scala class definition for the Chisel component you are implementing. You can think of this as almost the same thing as a module declaration in Verilog.

Next we see the I/O specification for this component in the val io = IO(new Bundle{...}) definition. You will notice that the bundle takes several arguments as part of its construction, each with a specified direction (either Input or Output), type (UInt, Bool, etc.), and a bit width. If a bit width is not specified, Chisel will infer the appropriate bit width for you (in this case default to 1). The 16.W represents a width of 16 bits. The io Bundle is essentially a constructor for the component that we are constructing.

The next section of code performs the actual GCD computation for the module. The register declarations for x and y tell Chisel to treat x and y as a register of type UInt().

```scala
val x = Reg(UInt()) // declares x as UInt register 
val y = Reg(UInt()) // declares y as UInt register
```
   
The `when` statement is just like if statements in other languages. Depending on whether io.load is true or false, the appropriate block of code is used. If io.load is true, then x := io.a; y := io.b is applied. Otherwise, if io.load is false, then the code followed by .otherwise statement is applied.

Note that there is no clock mentioned in this code. This is because the Chisel language automatically handles this for you. The difference between combinational/asynchronous logic assignment for wires and sequential/synchronous logic assignment for registers is specified by declaring the type of value (val keyword). Values that are declared as Reg (i.e. the x and y values in this example) are updated on positive edge of an implicit clock.

Finally we see the output assignments for the computation for io.out and io.valid. One particular thing to notice is that, we do not have to specify the width of x and y in this example. This is because Chisel does the bit width inference for you and sets these values to their appropriate widths based on the computation they are storing.

### 2.1.3 Running the Chisel Simulation

Now that we are familiar with the Chisel code for the GCD.scala file, let’s try to simulate it by generating the C++ models. Change directory into the Learning Journey home directory. To speed things up, we will keep sbt running. To get started:

```scala
    $ sbt
```

And then, run the test as follows:

```scala
> test:run-main examples.Launcher GCD
```
 
This will fire off the Chisel emulator that will run the simulation for the component defined in GCD.scala. If the simulation succeeds, you should see some debug output followed by:

```scala
test GCD Success: 3 tests passed in 29 cycles taking 0.151776 seconds
[info] [0.123] RAN 24 CYCLES PASSED
Tutorials passing: 1
[success] Total time: 6 s, completed Jan 20, 2018 2:15:14 PM
```
 
The debug output is generated by the test harness which is found in `src/test/scala/examples/GCDTests.scala` file. We will talk about this more later. In addition to the debug output, the build also creates C++ models which can be used to simulate and debug more complicated designs.

[<<< Previous Page](Chisel-Introductory-Examples) (Chisel Introductory Examples)  [Next Page >> ](Tutorial-page-2) (Tutorial page 2)
***
[[Tutorial Home]]