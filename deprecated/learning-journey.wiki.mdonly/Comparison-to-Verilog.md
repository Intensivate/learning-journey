
A few quick examples of Verilog code, and then the equivalent in Chisel. Please do not spend too much time here, especially not in the first pass, rather use this page as a reference as you go along the Learning Journey.

### Basic differences between Verilog and Chisel
In Verilog and VHDL, you have signals and variables.  With a variable, you can change the value of a variable several times in the same process.  A process is code that happens in between two clock edges.  Within the code of a process, you can write to a variable in one line, then a line later in the process that reads the variable sees that changed value.  In contrast, if code inside a process writes to a signal, and another line of code in the same process reads that signal, it will see to old value, not the new updated value.

These concepts don't exist in Chisel.  Instead, Chisel declares the functionality of a circuit.  It does not have fine level control over time related things.  In Chisel, there is the concept of variables, which are Scala programming language concepts.. and the concept of wires, which are circuit concepts, and there is the concept of circuit elements, some of which imply register behavior.

In Chisel, if you want control over assignment, such that some kinds of assignment directly put a signal onto a wire, where other kinds of assignment go through a register and are only seen elsewhere after a clock edge, then you would use "Reg" construct in Chisel.  So, you know that when you assign to a wire that in turn is the input to a Reg, that nothing on the other side of the Reg sees this assignment until after the clock edge.  But if you assign to a wire and that wire goes to other combinational logic, then that other logic receives the assignment right away, it doesn't wait for a clock edge.

These differences will be covered by the end of the following examples.  

#### Create a module

In Verilog, a module is created as follows:

```verilog
module adder(a, b, sum, cout);
...
endmodule
```
whereas, to achieve the same purpose, in Chisel we define a Scala class:
```scala
class Adder(val w: Int) extends Module {
...
}
```
***
#### Ports or interfaces

Arguments passed to the module created in Verilog represent the module ports. It only remains to provide them direction:
```verilog
module adder(a, b, sum, cout);
input a, b;
output sum, cout;
...
endmodule
```
However, the parameters passed to the Scala class are not the interfaces, they are utilized for parameterization - a later topic, so those will be omitted here. Interfaces are specified within an instance of a `Bundle`, as follows:
```scala
class Adder(...) extends Module {
val io = IO(new Bundle {
  val a = Input(UInt(1.W))
  val b = Input(UInt(1.W))
  val sum = Output(UInt(1.W))
  val cout = Output(UInt(1.W))
})
...
}
```
***
#### Combinational Logic

A complete implementation of a one-bit adder represents an example of boolean logic, with named wires feeding values in and a named wire as output, given first in Verilog:
```verilog
module adder(a, b, sum, cout);
input a, b;
output sum, cout;
assign sum = a ^ b; // xor
assign cout = a & b; // and
endmodule
```
and then also in Chisel:
```scala
class Adder(...) extends Module {
val io = IO(new Bundle {
  val a = Input(UInt(1.W))
  val b = Input(UInt(1.W))
  val sum = Output(UInt(1.W))
  val cout = Output(UInt(1.W))
})
io.sum := io.a ^ io.b // xor
io.cout := io.a & io.b // and
}
```
However, parameterization brings advantage to Chisel, which is demonstrated by the [[Adder]] tutorial, where the operator `:=` for re-assignment is also explained.
***
#### Conditional Updates

To specify conditional behavior in Verilog we use the `always` statement with the list of sensitivity:
```verilog
...
always @ (posedge clk, a, b);
...
```
In Chisel, a `when` block is executed if its condition evaluates to true:
```scala
when (io.a | io.b) {
  ...
}
```
***
#### Synchronous Logic

As an example, Verilog implementation of a shift register is provided:
```verilog
module ShiftRegister(input clk, input reset,
    input  io_in,
    output io_out);

  reg[0:0] r3;
  reg[0:0] r2;
  reg[0:0] r1;
  reg[0:0] r0;

  assign io_out = r3;
  always @(posedge clk) begin
    r3 <= r2;
    r2 <= r1;
    r1 <= r0;
    r0 <= io_in;
  end
endmodule
```
Each of the internal one-bit registers takes the value of the preceding register at the positive edge of `clk`, and this is specified by the `always` statement. In Chisel, however, both clock and reset are implicitly created by the following implementation of the shift register:
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
As a matter of fact, the Verilog code of the shift register has been generated from this Chisel representation - meaning that this is an example a wire that feeds a value into a register, where register is triggered at every rising clock edge.
***
#### Multiplexer

Verilog code of a multiplexer with named wires feeding in and a named wire accepting output:
```verilog
module mux2(in0, in1, sel, out);
input in0, in1, sel;
output out;
assign out = in0 & ~sel | in1 & sel;
endmodule
```
Chisel equivalent:
```scala
class Mux2 extends Module {
  val io = IO(new Bundle {
    val sel = Input(UInt(1.W))
    val in0 = Input(UInt(1.W))
    val in1 = Input(UInt(1.W))
    val out = Output(UInt(1.W))
  })
  io.out := (io.sel & io.in1) | (~io.sel & io.in0)
}
```

***

#### Module Instantiation

A 4-to-1 multiplexer can be created using three 2-to-1 multiplexers. In Verilog, modules are instantiated in the following way:
```verilog
Mux2 m0 (
    .io_sel(m0_io_sel),
    .io_in0(m0_io_in0),
    .io_in1(m0_io_in1),
    .io_out(m0_io_out)
  );
```
Chisel equivalent:
```scala
  val m0 = Module(new Mux2()) // the first instance of the 2-to-1 multiplexer
  m0.io.sel := io.sel(0)
  m0.io.in0 := io.in0
  m0.io.in1 := io.in1
```
***

### Next Steps

Once you've scrolled through these, not more than a couple of minutes, please proceed to the [next step](Examples-line-by-line) of the Learning Journey.