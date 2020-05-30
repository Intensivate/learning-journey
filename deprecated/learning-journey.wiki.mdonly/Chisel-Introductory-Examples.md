[<<< Previous Page](Setting-up-Chisel) (Setting up Chisel)  [Next Page >> ](Tutorial-Page-1) (Tutorial page 1)

In this section first an important practice of maintaining the directory structure is described and, then, the introductory examples of digital circuits implementation using Chisel are presented.

### Directory Structure

The learning journey now moves to hands on problems, which are in the repository that you cloned  (FYI, many are copied from Berkeley supplied tutorials).  We start with ultra simple and advance through the levels. This step of the journey is about learning the logistics of writing your own Chisel code and running it, then comparing your solution to that provided by the exercises' authors. (we consider your input very valuable, please provide your thoughts on how to improve this Learning Journey using the [[User Experiences]] section).

The circuit problems are found in: `learning-journey/src/main/scala/problems` and corresponding automated tests (such an automated test framework is called a _harness_) in: `learning-journey/src/test/scala/problems`. Each of the exercises contains clearly marked places where its functionality (in case of circuits) and test cases (in case of harnesses) should be completed. Successful execution of the harness means a successful solution of the problem. The authors' solutions to the circuit problems are found in: `learning-journey/src/main/scala/solutions` and corresponding harnesses are found in: `learning-journey/src/test/scala/solutions`. The solutions can be helpful if you find yourself taking too much time for a certain problem.  They also provide a learning opportunity to see the coding style of the tutorial authors, especially for more advanced problems.  The elegance of Chisel code will surely surprise you!

The separation of circuits and tests makes a good directory structure to keep within future projects.

### First Real Chisel Circuit

The first step of learning the logistics of the hands on problems is to take a look at the Mux2 problem, a multiplexer with two data inputs and one control input. It is already implemented, so let's try running it. First, start `sbt` and keep it running (this will speed up the following executions).  ([sbt](https://stackoverflow.com/questions/24586605/what-is-scala-build-tool-sbt-and-why-it-is-used) is "Scala Built Tool", used to build and run scala programs):

```
$ sbt
```

Then, enter the following line in `sbt` console to run tests on circuit `Mux2`:

```
> test:run-main problems.Launcher Mux2
```

After initial information on what is being run and that the circuit state has been created, this run should result in successful passing of all tests, as follows:

```scala
...
test Mux2 Success: 8 tests passed in 13 cycles taking 0.082884 seconds
[info] [0.050] RAN 8 CYCLES PASSED
Tutorials passing: 1
[success] Total time: 40 s, completed Dec 23, 2017 12:59:27 PM
```

If you faced any other resolution of the test run, please let us know using either [[User Experiences]] section, or as a question at the [Hangout group chat](https://hangouts.google.com/?action=group&key=qOKQrvMjplnJAC133&pli=1). Of course, any other feedback is also valuable.

### Circuit Implementation

Chisel implementation of `Mux2` is found in `src/main/scala/problems/Mux4.scala`, so please go ahead, open up that file and focus on the first circuit (the second being `Mux4` that will be discussed in the next section).

The first line:
```scala
class Mux2 extends Module {
...
}
```
Is a little bit different than you may have been expecting.  It's an object oriented class!  But it's not normal. 
 A class normally has variables and functions.  This one doesn't have any function.  But it does have a constructor.  Every class has a constructor.  The class defines the shape of an object, then you invoke the constructor for the class, and that builds an object.  A class is abstract, while an object is a concrete physical thing (which has the shape defined by the class).  In Chisel, a circuit element is an object.  So, constructing a circuit comes down to invoking constructors.  Hence, most classes in Chisel code don't have any functions, they only have constructors.  So, constructors end up calling other constructors, in a web that generates the whole circuit.  This is how the circuit gets generated.

That's the fundamental mindset to have while writing and reading Chisel code, is that all the circuit behavior exists inside constructors.  It is then the pattern of invoking constructors that builds the circuit.  With other approaches, such as Verilog, the Verilog code explicitly wires up the circuit.  There are commands that say "connect this circuit element to that circuit element".  But in Chisel, this wiring is often implied.

The module class is the basic unit of circuit element, and has inputs to the module and outputs from it.  It's equivalent to a `module` in Verilog.

The next line:
```scala
val io = IO(new Bundle {
...
})
```
Shows how inputs and outputs are defined for a Module.  Every module is required to have an IO bundle, which follows this pattern.  It isn't shown explicitly, but this line is part of the constructor of the Mux2 class.  When an object of the Mux2 class is created, then these `val`s are generated and the right hand side often explicitly creates object, or causes objects to be implicitly constructed.  Here, `new Bundle` invokes the constructor of the `Bundle` class, thereby creating a Bundle object.  A bundle is ultimately a collection of wires, but Chisel and Scala add power to this by allowing you to defer details util later.  You can say the shape of the bundle, and then use a bundle to connect a module, and then during the construction, Scala actually maps an abstract value in the bundle into exact wires.  Here, we're keeping things simple and defining explicit wires:

```scala
val sel = Input(UInt(1.W)) # control input that selects which of the data inputs is wired to the output
val in0 = Input(UInt(1.W)) # first data input
val in1 = Input(UInt(1.W)) # second data input
val out = Output(UInt(1.W) # output
```

where `val` is a keyword of Scala, used to name values that are immutable, so the value assigned to the name **cannot be changed**. The `Input` or `Output` designation is used to select the direction of the port and `UInt(1.W)` is the way to say a single wire. (`1.W` says width of 1 bit, `UInt` says unsigned integer). More on types and widths later on, this is just a heads up for now.

Finally, after defining the input/output interface in the form of a `Bundle`, the internals of the multiplexer circuit are given as:

```scala
io.out := (io.sel & io.in1) | (~io.sel & io.in0)
```

The `.` notation is used to select wires out of the Bundle.  So "io.sel" pulls the "sel" wire out of the "io" bundle.  More importantly `:=` is an important Chisel feature.  It represents wiring things together.  Any place you see `:=` you know that circuit elements on the left hand side have been wired to circuit elements on the right hand side.  In this case, `:=` connects the output of the logic to the output wire leaving the module. (Also, the `:=` is the main way that constructors get implicitly invoked.)

### Test Harness

Corresponding test harness is found in `src/test/scala/problems/Mux2Tests.scala`, so please open this file now.

The first line:

```scala
class Mux2Tests(c: Mux2) extends PeekPokeTester(c) {
...
}
```
extends the base class used for creating tests of Chisel circuits. Then, wrapped in three `for` loops (omitted from explanations here), three functions are used to test the behavior of DUT:

```scala
poke(...) // bring 0 or 1 to each of the circuit inputs: sel, in0 and in1
step(...) // advance one tick of the main clock
expect(...) // read the circuit output and compare to the correct result
```
Please note how the ports of the multiplexer are referenced from within the harness.

[<<< Previous Page](Setting-up-Chisel) (Setting up Chisel)  [Next Page >> ](Tutorial-Page-1) (Tutorial page 1)

If you come with a strong Verilog background, check out the [[Comparison to Verilog]] table.