[<<< Previous Page](Tutorial-page-1) (Tutorial page 1)  [Next Page >> ](Tutorial-page-3) (Tutorial page 3)

## 2.2 Combinational Logic

### Difference between Chisel and Verilog
Verilog and Chisel have some fundamental differences in how to think about time while writing code.  See [[Comparison to Verilog]] for more about this. 

### Generating Verilog and waveforms

In general, Verilog is generated from Chisel code within the Learning Journey as follows:

a) from `sbt`:
```scala
test:runMain examples.Launcher GCD --backend-name=verilator
```

or b) from `bash`:

```scala
./run-examples.sh GCD --backend-name=verilator
```

Either way, a number of files is generated in the `test_run_dir/examples/GCD/` directory. Among them the most interesting for us at this point are:

  * GCD.v - Verilog code generated, and
  * GCD.vcd - vcd waveform dump of the signals during the simulation.

More information on the Verilog code is given in an example presented below. The generated vcd dump can be viewed using [GTKWave](http://gtkwave.sourceforge.net/), for which a basic tutorial is given [here](http://billauer.co.il/blog/2017/08/linux-vcd-waveform-viewer/).

### 2.2.1 The Scala Node: Declaring Wires

Constructing combinational logic blocks in Chisel is often done using a combination of Scala `val` constructs and Chisel `:=` constructs. 

Suppose we want to construct a single full adder. A full adder takes two inputs a and b, and a carry in cin and produces a sum and carry out cout. The Chisel source code for our full adder will look something like:

```scala
class FullAdder extends Module {
  val io = IO(new Bundle {
    val a    = Input(UInt(1.W))
    val b    = Input(UInt(1.W))
    val cin  = Input(UInt(1.W))
    val sum  = Output(UInt(1.W))
    val cout = Output(UInt(1.W))
  })

  // Generate the sum
  val a_xor_b = io.a ^ io.b
  io.sum := a_xor_b ^ io.cin
  // Generate the carry
  val a_and_b = io.a & io.b
  val b_and_cin = io.b & io.cin
  val a_and_cin = io.a & io.cin
  io.cout := a_and_b | b_and_cin | a_and_cin
}
```
   
where cout is defined as a combinational function of inputs a, b, and cin.

You will notice that in order to access the input values from the io bundle, you need to first reference io since the input and output values belong to the io bundle. The |, &, and ˆ  operators correspond to bitwise OR, AND, and XOR operations respectively.


### 2.2.2 Bit Width Inference

If you don’t explicitly specify the width of a value in Chisel, the Chisel compiler will infer the bit width for you based on the inputs that define the value. Notice in the FullAdder definition, the widths for a_xor_b, a_and_b, b_and_cin, and a_and_cin are never specified anywhere. However, based on how the input is computed, Chisel will correctly infer each of these values are one bit wide since each of their inputs are the results of bitwise operations applied to one bit operands.

To generate Verilog code, move to the Learning Journey home directory and run:

```scala
test:runMain examples.Launcher FullAdder --backend-name=verilator
```

The Verilog is generated in `FullAdder.v` found in `./test_run_dir/FullAdder`.

A quick inspection of the generated Verilog shows these values are indeed one bit wide:

```verilog
module FullAdder(
  input   io_a,
  input   io_b,
  input   io_cin,
  output  io_sum,
  output  io_cout
);
  wire  a_xor_b; 
  wire  _T_7; 
  wire  a_and_b; 
  wire  b_and_cin; 
  wire  a_and_cin; 
  wire  _T_8; 
  wire  _T_9; 
  assign a_xor_b = io_a ^ io_b; 
  assign _T_7 = a_xor_b ^ io_cin; 
  assign a_and_b = io_a & io_b;
  assign b_and_cin = io_b & io_cin;
  assign a_and_cin = io_a & io_cin;
  assign _T_8 = a_and_b | b_and_cin;
  assign _T_9 = _T_8 | a_and_cin;
  assign io_sum = _T_7;
  assign io_cout = _T_9;
endmodule
```
 
Suppose we change the widths of the FullAdder to be 2 bits wide each instead such that the Chisel source now looks like:

 
```scala
class FullAdder extends Module {
  val io = IO(new Bundle {
    val a    = Input(UInt(2.W))
    val b    = Input(UInt(2.W))
    val cin  = Input(UInt(2.W))
    val sum  = Output(UInt(2.W))
    val cout = Output(UInt(2.W))
  })

  // Generate the sum
  val a_xor_b = io.a ^ io.b
  io.sum := a_xor_b ^ io.cin
  // Generate the carry
  val a_and_b = io.a & io.b
  val b_and_cin = io.b & io.cin
  val a_and_cin = io.a & io.cin
  io.cout := a_and_b | b_and_cin | a_and_cin
}
```
 
As a result, the Chisel compiler should infer each of the intermediate values a_xor_b, a_and_b, b_and_cin, and a_and_cin are two bits wide. An inspection of the Verilog code correctly shows that Chisel inferred each of the intermediate wires in the calculation to be 2 bits wide.

```verilog
module FullAdder(
  input  [1:0] io_a,
  input  [1:0] io_b,
  input  [1:0] io_cin,
  output [1:0] io_sum,
  output [1:0] io_cout
);
  wire [1:0] a_xor_b;
  wire [1:0] _T_7;
  wire [1:0] a_and_b;
  wire [1:0] b_and_cin;
  wire [1:0] a_and_cin;
  wire [1:0] _T_8;
  wire [1:0] _T_9;
  assign a_xor_b = io_a ^ io_b;
  assign _T_7 = a_xor_b ^ io_cin;
  assign a_and_b = io_a & io_b;
  assign b_and_cin = io_b & io_cin;
  assign a_and_cin = io_a & io_cin;
  assign _T_8 = a_and_b | b_and_cin;
  assign _T_9 = _T_8 | a_and_cin;
  assign io_sum = _T_7;
  assign io_cout = _T_9;
endmodule
```

### 2.3 Using Registers 

Unlike Verilog, specifying a register in Chisel tells the compiler to actually generate a positive edge triggered register. In this section we explore how to instantiate registers in Chisel by constructing a shift register.

In Chisel, when you instantiate a register there are several ways to specify the connection of the input to a register. As shown in the GCD example, you can "declare" the register and assign what it’s input is connected to in a when... block or you can simply specify the input of the register within declaration of the register.

If you choose to specify the input to the register within declaration, use RegNext instead of Reg as a type of val. It will clock the new value every cycle unconditionally. Example:

```scala
// io.x is input of register z that is clocked on positive edge of the clock
val z = RegNext(io.x)
```   

The example above has the same meaning as if we did it the way shown in Tutorial page 1:

```scala
// register z that is clocked on positive edge of the clock
val z = Reg(UInt())
// input of register z is io.x
z := io.x
```   

If we only want to update if certain conditions are met we use a when block to indicate that the registers are only updated when the condition is satisfied:

```scala
// Clock the new register value when the condition a > b 
val x = Reg(UInt()) 
when (a > b) { x := y } 
.elsewhen ( b > a) {x := z} 
.otherwise { x := w}
```

It is important to note that when using the conditional method, the values getting assigned to the input of the register match the type and bitwidth of the register you declared. In the unconditional register assignment, you do not need to do this as Chisel will infer the type and width from the type and width of the input value.

The following sections show how these can be used to construct a shift register.

### 2.3.1 Unconditional Register Update

Suppose we want to construct a basic 4 bit shift register that takes a serial input in and generates a serial output out. For this first example we won’t worry about a parallel load signal and will assume the shift register is always enabled. We also will forget about the register reset signal.

If we instantiate and connect each of these 4 registers explicitly, our Chisel code will look something like:

```scala
class ShiftRegister extends Module {
  val io = IO(new Bundle {
    val in  = Input(UInt(1.W))
    val out = Output(UInt(1.W))
  })
  val r0 = RegNext(io.in)
  val r1 = RegNext(r0)
  val r2 = RegNext(r1)
  val r3 = RegNext(r2)
  io.out := r3
}
``` 

If we take a look at the generated Verilog, we will see that Chisel did indeed map our design to a shift register. One thing to notice is that the clock signal and reset signals are implicitly attached to our design.

```scala
module ShiftRegister(
  input   clock,
  input   reset,
  input   io_in,
  output  io_out
);
  reg  r0;
  reg  r1;
  reg  r2;
  reg  r3;
  assign io_out = r3;

  always @(posedge clock) begin
    r0 <= io_in;
    r1 <= r0;
    r2 <= r1;
    r3 <= r2;
  end
endmodule
```
 
### 2.3.2 Conditional Register Update

As mentioned earlier, Chisel allows you to conditionally update a register (use an enable signal) using the when, .elsewhen, .otherwise block. Suppose we add an enable signal to our shift register, that allows us to control whether data is shift in and out on a given cycle depending on an enable input signal. The new shift register now looks like:

```scala
class ShiftRegister extends Module { 
  val io = IO(new Bundle { 
    val in     = Input(UInt(1.W))
    val enable = Input(Bool()) 
    val out    = Output(UInt(1.W))
  } 
 
  val r0 = Reg(UInt()) 
  val r1 = Reg(UInt()) 
  val r2 = Reg(UInt()) 
  val r3 = Reg(UInt()) 
 
  when (io.enable) { 
    r0 := io.in 
    r1 := r0 
    r2 := r1 
    r3 := r2 
  } 
  io.out := r3 
}
```
 
Notice that it is not necessary to specify an .otherwise condition as Chisel will correctly infer that the old register value should be preserved otherwise.

### 2.3.3 Register Reset

Chisel allows you to specify a synchronous reset to a certain value by using RegInit and specifying the initial (reset) value as a parameter of the RegInit. In our shift register, let’s add a reset capability that resets all the register values to zero synchronously. To do this we need to provide our register declarations a little more information using the init parameter with what value we want on a synchronous reset:

```scala
class ResetShiftRegister extends Module {
  val io = IO(new Bundle {
    val in    = Input(UInt(4.W))
    val shift = Input(Bool())
    val out   = Output(UInt(4.W))
  })
  // Register reset to zero
  val r0 = RegInit(0.U(4.W))
  val r1 = RegInit(0.U(4.W))
  val r2 = RegInit(0.U(4.W))
  val r3 = RegInit(0.U(4.W))
  when (io.shift) {
    r0 := io.in
    r1 := r0
    r2 := r1
    r3 := r2
  }
  io.out := r3
}
```
 
Notice that reset value can actually be any value, simply replace the init value (0.U) and width (4.W) to appropriate values.

Chisel also has an implicit global reset signal that you can use in a when block. The reset signal is conveniently called reset and does not have to be declared. The shift register using this implict global reset now looks like:

```scala
class ShiftRegister extends Module { 
  val io = IO(new Bundle { 
    val in     = Input(UInt(1.W))
    val enable = Input(Bool()) 
    val out    = Output(UInt(1.W))
  } 
 
  val r0 = Reg(UInt()) 
  val r1 = Reg(UInt()) 
  val r2 = Reg(UInt()) 
  val r3 = Reg(UInt()) 
 
  when(reset) { 
    r0 := UInt(0) 
    r1 := UInt(0) 
    r2 := UInt(0) 
    r3 := UInt(0) 
  } .elsewhen(io.enable) { 
    r0 := io.in 
    r1 := r0 
    r2 := r1 
    r3 := r2 
  } 
  io.out := r3 
}
```
 
### 2.3.4 Sequential Circuit Problem

The following exercises can be found in your $TUT_DIR/problems/ folder. You will find that some parts of the tutorial files have been completed for you and the section that you need to will need to complete is indicated in the file. The solutions to each of these exercises can be found in the $TUT_DIR/solutions/ folder.

The first tutorial problem is to write write a sequential circuit that sums in values. You can find the template in `$LJHOME/src/main/scala/problems/Accumulator.scala` including a stubbed out version of the circuit:

```scala
// Problem:
//
// Count incoming trues
// (increase counter every clock if 'in' is asserted)
//
class Accumulator extends Module {
  val io = IO(new Bundle {
    val in  = Input(UInt(1.W))
    val out = Output(UInt(8.W))
  })

  // Implement below ----------

  io.out := 0.U

  // Implement above ----------
}
```
   
and a complete tester that confirms that you have successfully designed the circuit. Run:
 
```scala
test:run-main problems.Launcher Accumulator
```
 
until your circuit passes the tests.

[<<< Previous Page](Tutorial-page-1) (Tutorial page 1)  [Next Page >> ](Tutorial-page-3) (Tutorial page 3)

***
[[Tutorial Home]]