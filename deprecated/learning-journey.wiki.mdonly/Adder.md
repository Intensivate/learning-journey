### Preparations

Please navigate to `$LJHOME/chisel-tutorial/src/main/scala/problems` and open the `Adder.scala`. This is where the problem is set and where you will implement your solution.

Also, please keep handy the [Chisel Introductory Examples](https://github.com/librecores/riscv-sodor/wiki/Chisel-Introductory-Examples#circuit-implementation) page.

### Understanding what's already there

In this problem you are required to implement an adder in such a way that: _`out` should be the sum of `in0` and `in1`_. So let's do that first and then we will speak about the second line of the problem (parameterization).

Here are the lines provided by the tutorial:

```scala
class Adder(val w: Int) extends Module {
  val io = IO(new Bundle {
    val in0 = Input(UInt(1.W))
    val in1 = Input(UInt(1.W))
    val out = Output(UInt(1.W))
  })

  io.out := 0.U
}
```
Until now, only the line:

```scala
io.out := 0.U
```
was not explained.

The `:=` is equivalent to a wire.  This operator is the main way the circuits get constructed.  To understand the operator `:=`, let's take a look how **nodes** are dealt with in Chisel.

#### Nodes in Chisel - `wire` and `val`

When a `val` is declared in Scala, it creates a **node** that represents the data that it is assigned to. This tells the Chisel compiler to treat the value as wire<sup>[1](#footnote1)</sup>. 

In order to tell the compiler to allocate the value for the first time, i.e. upon the definition, the `=` operator is used. On every subsequent **re-assignment** to the value, we must use the operator `:=`.

Chisel, unlike Verilog, is **compiled sequentially**. Therefore, re-assignment might be necessary in cases when a value or connection is unknown until later part of the Chisel code. The most obvious example of such necessity, is in the construction of the top level I/O for the module shown above; the value of `out` is not immediately known at the time of declaration and, thus, it must be re-assigned later in the code, i.e. in the line:

```scala
io.out := 0.U
```

### Thinking about the implementation

The line re-assigning `io.out`, at the moment, is actually only a placeholder - as the assigned value is always the same and nowhere near the sum of `in0` and `in1`. We need to, somehow, re-assign the value obtained from the _addition_ of these two inputs to `io.out`. After a brief peak into the [Chisel3 Cheat Sheet](https://chisel.eecs.berkeley.edu/doc/chisel-cheatsheet3.pdf), we note that the addition in Chisel is performed as follows:

```scala
in0 + in1
```

However, re-assigning that value to `io.out` would cause an error - because `in0` and `in1` are members of `io` instance of a `Bundle`, just as `out` is. While talking about `out`, notice how it is referenced from outside of `io`. The correct way to access the inputs as analogous: `io.in0` and `io.in1`.

Please take a minute to think and then write down the line that adds these two inputs and re-assigns the value obtained from that addition to the output and then continue to the next subsection.

### Solution

The correct solution to this part of the problem is:

```scala
io.out := io.in0 + io.in1
```

### Testing

In another window, please open `../../AdderTests.scala`. At a glance, everything is familiar, as it was already briefly covered - which is enough for now. Just take a moment to recognize `poke`, `step` and `expect`. Close this file and navigate to `$LJHOME/chisel-tutorial` and run the appropriate test as follows:

```
./run-problem.sh Adder
```
and the standard output should provide:

```
...
================================================================================
Errors: 1: in the following tutorials
Tutorial Adder: exception error: ConcreteUInt(12, 1) bad width 1 needs 4
================================================================================

Exception: sbt.TrapExitSecurityException thrown from the UncaughtExceptionHandler in thread "run-main-0"
java.lang.RuntimeException: Nonzero exit code: 1
	at scala.sys.package$.error(package.scala:27)
[trace] Stack trace suppressed: run last test:runMain for the full output.
[error] (test:runMain) Nonzero exit code: 1
[error] Total time: 9 s, completed Jan 3, 2018 10:48:28 AM
```
However, the error you are seeing is due to the second line of the problem: _Adder width should be parametrized_ and we will take care of that in the [next step of the Learning Journey](Adder,-part-2)

***
<a name="footnote1">1</a>) ...as long as the value is not assigned to be a register type - more details in a later section.