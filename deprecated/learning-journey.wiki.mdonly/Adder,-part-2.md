At this point, in order to provide a complete solution to the Adder problem, we will exercise very basic, but practical cases of Chisel features advertised in the [[Big Picture]]: bit width inference and parameterization.

#### Bit Width Specification

Reminding ourselves of the value definitions:
```scala
val in0 = Input(UInt(1.W))
val in1 = Input(UInt(1.W))
val out = Output(UInt(1.W))

```
we note the part not mentioned during previous explanations: `UInt(1.W)` - this is where the value **type** (`UInt`) and **bit width** (`1.W`) are determined. 

If the bit width of a value wasn't explicitly specified, the Chisel compiler would have inferred the bit width based on the inputs that define the value.

#### Basic Parameterization

Taking another look at the module definition, i.e. Scala class definition in Chisel:
```scala
class Adder(val w: Int) extends Module...
```
note `val w: Int` - a parameter passed to the class constructor. This means that, for every instance of the class `Adder`, this parameter may be passed as a user-defined value.

### Thinking about the solution

Let's use this possibility to create an agile class `Adder` that can be called upon to construct an adder circuit of arbitrary bit width - please take a moment to think of what should be modified in the three lines defining values within the `io` bundle.

### Solution

The correct way to modify value definitions is:

```scala
val in0 = Input(UInt(w.W))
val in1 = Input(UInt(w.W))
val out = Output(UInt(w.W))
```
In this way, every time an `Adder` instance is constructed, user can, without changing the Chisel source code, choose the adder circuit bit width.

### Testing

Please edit and save `Adder.scala` and navigate back to `$LJHOME/chisel-tutorial` and run again:

```
./run-problem.sh Adder
```
The output should provide:
```
Circuit state created
[info] [0.001] SEED 1514975326903
test Adder Success: 10 tests passed in 15 cycles taking 0.128609 seconds
[info] [0.075] RAN 10 CYCLES PASSED
Tutorials passing: 1
[success] Total time: 17 s, completed Jan 3, 2018 11:28:51 AM
```

If there are any issues reported instead, please provide feedback using either the [[User Experiences]] page or the [Hangout meeting](https://hangouts.google.com/?action=group&key=qOKQrvMjplnJAC133&pli=1).

### Next Steps

Please, continue on to the next step of the Learning Journey, design of a [counter](Counter).