This is where we provide a step-by-step instruction how to use `BlackBox`, which is a mechanism to include Verilog code in a Chisel project.

The main resource is a working `BlackBox` example provided in the `TryBlackBox` repo, so please go ahead and clone it as follows:

```
git clone https://github.com/apaj/TryBlackBox
```

given that you already have standard Learning Journey environment and toolbox all set up nicely.

In this page we will analyse the working example and another page is coming up that will explain how to make one.

## 1. How it works? Basic Principles

`BlackBox` is created to be a Chisel tool that enables the Verilog part of code to be included in a Chisel project. It can be, therefore, considered as an interface between Chisel and Verilog, but an **interface in the context of code inclusion**, but also an **interface in the context of hardware communication** between Chisel hardware and Verilog hardware. This latter context means that there exist some hardware pins on the Chisel side that we directly connect to the corresponding pins on the Verilog side.

The MWE includes four things:
1. Hardware described in Verilog code,
2. Chisel wrapper code that will be connecting to the hardware in Verilog,
3. Chisel top module that will be instantiated in test harnesses, and 
4. Chisel test harness.

## 2. Where to put Verilog code?

Looking at the repo you just cloned we see the following directory structure:

```
d- src 
d-- main
d--- resources ##################### <<--- this is the root for setResource("/tryblackbox/TryBlackBox.v")
d---- tryblackbox
f----- TryBlackBox.v ############### <<--- this is the Verilog code
d---- scala 
d----- tryblackbox
f------ TryBlackBox.scala ########## <<--- this is the Chisel wrapper code
f------ TryBlackBoxTop.scala ####### <<--- this is the Chisel top module code
d-- test
d--- scala
d---- tryblackbox
f----- TryBlackBoxMain.scala ####### <<--- this is where the top module is instantiated and tested
f----- TryBlackBoxUnitTest.scala ### <<--- this is where the unit tests for the harness are designed
```

To put it clearly: Verilog code is introduced to the Scala file which holds the Chisel wrapper code as follows:

```scala
setResource("/tryblackbox/TryBlackBox.v")
```
whereas the root of `setResource` is in `/src/main/resources/`. Therefore, files holding the Verilog code should be in the location provided by:

```
<project-home>/src/main/resources/tryblackbox/
```

just as `TryBlackBox.v` is in this particular example.

## 3. How to make it work?

First things first, so in any project that needs to be running a `BlackBox` interface, we need appropriate headers. These are found in the wrapper code, i.e. `TryBlackBox.scala`:

```scala
package tryblackbox

import chisel3._
import chisel3.util._
import chisel3.experimental._
import chisel3.util.HasBlackBoxResource
```

## 4. Where is what?

The wrapper then continues to provide input/output interfaces and to include the required Verilog:

```scala
class TryBlackBox extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle() {
    val in1 = Input(UInt(64.W))
    val in2 = Input(UInt(64.W))
    val out = Output(UInt(64.W))
  })
  setResource("/tryblackbox/TryBlackBox.v")
}
```

The two inputs and one output are just 64-bit wide unsigned integers and that is enough, as the Verilog code is just a simple adder implementation as provided in `TryBlackBox.v`:

```verilog
module TryBlackBox(
    input  [63:0] in1,
    input  [63:0] in2,
    output reg [63:0] out
);
  always @* begin
  out <= $realtobits($bitstoreal(in1) + $bitstoreal(in2));
  end
endmodule
```

As `TryBlackBox` is actually a **class inheriting from `BlackBox`**, it can **not** serve as a **top module**, since the test harnesses **expect to instantiate a `Module` type**. Therefore, a **top module** is created in `TryBlackBoxTop.scala` as follows:

```scala
class TryBlackBoxTop extends Module {
	val io = IO(new Bundle {
		val in1top = Input(UInt(64.W))
		val in2top = Input(UInt(64.W))
		val outtop = Output(UInt(64.W))
	})

	val tryBlackBoxTopInst = Module(new TryBlackBox()) ### <<--- this is where Verilog is instantiated
	tryBlackBoxTopInst.io.in1 := io.in1top   \                    through a Chisel wrapper
	tryBlackBoxTopInst.io.in2 := io.in2top    |- ######### <<--- these three lines represent hardware interface
	io.outtop := tryBlackBoxTopInst.io.out   /                   between Chisel project and Verilog code
}
```

## 5. Where to provide tests?

On the side of test harness, there is no need for any particular header, only the standard `iotesters`. The place where to write the tests is in the `TryBlackBoxUnitTest.scala` and in the case of our adder it looks like this:

```scala
class TryBlackBoxUnitTester(c: TryBlackBoxTop) extends PeekPokeTester(c) {
  
  def sumBlackBox(a: Int, b: Int): (Int) = { ##### <<--- function that calculates the correct result
    var x = a
    var y = b
    x+y
  }

  private val tryblackbox = c ##################### <<--- instantiate the top module

  for(i <- 1 to 40 by 3) {
    for (j <- 1 to 40 by 7) {
      poke(tryblackbox.io.in1top, i) ############## <<--- generate values for inputs
      poke(tryblackbox.io.in2top, j)
      step(1) ##################################### <<--- advance a cycle

      val expected_sum = sumBlackBox(i, j) ######## <<--- calculate correct result by software

      expect(tryblackbox.io.outtop, expected_sum) # <<--- compare the result obtained in hardware with
                                                          the one calculated by software (sumBlackBox function)
    }
  }
}
```

## 5. How to run it?

Just like any other Chisel project - in the project root, do from terminal:

```
sbt 'test:runMain tryblackbox.TryBlackBoxMain --backend-name=verilator'
```

or from `sbt` as follows:

```
sbt
...
sbt:TryBlackBox> test:runMain tryblackbox.TryBlackBoxMain --backend-name=verilator
```

In either case, the output should look something like this:

```
STARTING test_run_dir/tryblackbox.TryBlackBoxMain439741000/VTryBlackBoxTop
[info] [0.038] SEED 1541511470084
Enabling waves..
Exit Code: 0
[info] [6.768] RAN 84 CYCLES PASSED
[success] Total time: 47 s, completed Nov 6, 2018 2:38:16 PM
```

## 6. What do we get?

Looking at the `test_run_dir` directory and its subdir with a name that starts with: `tryblackbox.TryBlackBoxMain......` we find the file we are looking for: `TryBlackBoxTop.v` - that's where the output Verilog file is found - **including both the Chisel and the Verilog** codes we had in our project:

```verilog
module TryBlackBoxTop(
  input         clock,
  input         reset,
  input  [63:0] io_in1top,
  input  [63:0] io_in2top,
  output [63:0] io_outtop
);
  wire [63:0] tryBlackBoxTopInst_out; // @[TryBlackBoxTop.scala 12:40]
  wire [63:0] tryBlackBoxTopInst_in2; // @[TryBlackBoxTop.scala 12:40]
  wire [63:0] tryBlackBoxTopInst_in1; // @[TryBlackBoxTop.scala 12:40]
  TryBlackBox tryBlackBoxTopInst ( // @[TryBlackBoxTop.scala 12:40]
    .out(tryBlackBoxTopInst_out),
    .in2(tryBlackBoxTopInst_in2),
    .in1(tryBlackBoxTopInst_in1)
  );
  assign io_outtop = tryBlackBoxTopInst_out; // @[TryBlackBoxTop.scala 15:19]
  assign tryBlackBoxTopInst_in2 = io_in2top; // @[TryBlackBoxTop.scala 14:35]
  assign tryBlackBoxTopInst_in1 = io_in1top; // @[TryBlackBoxTop.scala 13:35]
endmodule
```

## References

This page was developed based on the discussion in [this](https://stackoverflow.com/questions/52880180/the-type-mismatch-error-while-using-chisel3-blackbox) StackOverflow thread. Please feel free to post any comments/updates there, but also kindly consider supporting the Learning Journey by contributing to the [[User Experiences]] page.