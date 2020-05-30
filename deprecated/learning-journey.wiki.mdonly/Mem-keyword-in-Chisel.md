Chisel contains a primitive for memories called `Mem`. Using it, it is possible to construct multiported memory of either synchronous or asynchronous type. However, in general - it is not used to instantiate read-only memories - it is way simpler to just use a vector of constant literals.
`Mem` general structure is given as:

```scala
val aMemory = Mem(size, type)
```

where size represents the memory size and type represents the data type of which the memory is composed of.

Initial value of memory contents can not be specified, therefore please *never* assume anything about initial contents of the instantiated `Mem`.

Asynchronous read 128 entry memory of 32 bit `UInt` types is obtained as follows:

```scala
val asyncMemory = Mem(128, UInt(32.W))
```

In the same way, synchronous memory of the same features is instantiated as follows:

```scala
val syncMemory = SyncReadMem(128, UInt(32.W))
```

## Adding Write Ports

Write ports are added to the Mem using the when block. This allows Chisel to infer a write port. Within the conditional block we specify the location and data for the write operation. General definition is as follows:

```scala
when (writeCondition) { 
  memoryName(writeAddress) := writeData
}
```

Please note the use of the reassignment operator, the `:=`.

Assume the following scenario: There is a 128 entry memory of 32 bit `UInt` types and we want to write a 32 bit value `dataIn` to the memory at location `writeAddr`, if the write enable signal `wen` is `HIGH`.
Chisel response would be the following:

```scala
... 
val aMemory = Mem(128, UInt(32.W)) 
val wen = io.writeEnable
val writeAddr = io.waddr
val dataIn = io.wdata
when (wen) { 
  myMem(writeAddr) := dataIn 
} 
...

If there were multiple write ports their priority would be based on their position within the when block.

## Adding Asynchronous Read Ports

In this case, an assignment is placed inside a when block, where we take care of the triggering condition. Inferring multiple read ports is done by simply adding more assignments in the when block definition. Coming back to the 128 entry memory of 32 bit `UInt` example, let’s add two asynchronous read ports: `raddr1` and `raddr2`, along with the read enable `re`. Here is the when block for this particular case:
```scala
... 
val aMemory = Mem(128, UInt(32.W)) 
val raddr1 = io.raddr
val raddr2 = io.raddr + 4.U
val re = io.readEnable
val read_port1 = UInt(32.W)
val read_port2 = UInt(32.W)
when (re) {
  read_port1 := aMemory(raddr1)
  read_port2 := aMemory(raddr2)
}
...
```

## Adding Synchronous Read Ports

In this case, Chisel requires that the output from the memory is assigned to a `Reg` type. Analogous to the asynchronous read port, assignments must occur in a when block, as well. Adding a single read port to the same example memory now looks like:

```scala
... 
val aMemory = SyncReadMem(128, UInt(32.W)) 
val raddr = io.raddr
val read_port = Reg(UInt(32.W)) ``when (re) { 
  read_port := aMemory(raddr) 
} 
...
```

## Mem Full Example

An example of `Mem` in action is provided within the Learning Journey, here.

It is an implementation of a simple stack which works as follows:
1. takes two signals `push` and `pop`
1. `push` tells the stack to push an input `dataIn` to the top of the stack
1. `pop` tells the stack to pop off the top value from the stack
1. enable signal en disables pushing or popping if not asserted
1. stack should always output the top value of the stack

Looking at the example code provided, please focus on the following line:

```scala
val dataOut = RegInit(0.U(32.W)) 
```

and conclude that this is synchronous read port that we are talking about. Other than that, everything else should be familiar. So, let’s investigate how this code translates to hardware.

## `Mem` in Hardware

Verilog can be emitted easily by running:
```
./run-examples.sh Stack --backend-name=verilator
```

and seen at locaiton:

```
less test_run_dir/examples/Stack/Stack.v
```

The line `20` which instantiates a `Mem` object:

```scala
val stack_mem = Mem(depth, UInt(32.W))
```

is directly translated as:

```
reg [31:0] stack_mem [0:7];
```

which makes sense, given that the test harness provides 8 as the depth argument.

Additional Verilog lines have to do with this construct, as well:

```
  wire [31:0] stack_mem__T_35_data; 
  wire [2:0] stack_mem__T_35_addr; 
  wire [31:0] stack_mem__T_17_data;
  wire [2:0] stack_mem__T_17_addr;
  wire  stack_mem__T_17_mask;
  wire  stack_mem__T_17_en;
```

the first four lines representing a read/write port (two for each) and the last line being the `en` signal. However, it is vague what does the penultimate wire stands for (`..._mask`-one) - any contribution on this matter would be highly appreciated. These are all later used within the `always` block, thus defining the actual behavior of the `Stack`.

Given that we are talking about synchronous circuit, the most interesting part of the Verilog code is given within the always block:

```
  always @(posedge clock) begin
    if(stack_mem__T_17_en & stack_mem__T_17_mask) begin
      stack_mem[stack_mem__T_17_addr] <= stack_mem__T_17_data; // @[Stack.scala 20:22]
    end
    if (reset) begin
      sp <= 4'h0;
    end else begin
      if (io_en) begin
        if (_T_15) begin
          sp <= _T_20;
        end else begin
          if (_T_23) begin
            sp <= _T_27;
          end
        end
      end
    end
    if (reset) begin
      out <= 32'h0;
    end else begin
      if (io_en) begin
        if (_T_22) begin
          out <= stack_mem__T_35_data;
        end
      end
    end
  end
```

Within this block there are two parts - one is concerned with the first `when` block, while the other is concerned with the second `when` block used in the [https://github.com/Intensivate/learning-journey/blob/master/src/main/scala/examples/Stack.scala|Stack] example. To be more precise, the first `if(reset)` block:

```
  if (reset) begin
      sp <= 4'h0;
    end else begin
      if (io_en) begin
        if (_T_15) begin
          sp <= _T_20;
        end else begin
          if (_T_23) begin
            sp <= _T_27;
          end
        end
      end
    end
```

corresponds to the Chisel code:

```scala
// Push condition - make sure stack isn't full
  when(io.en && io.push && (sp != (size-1).U)) {
    stack_mem(sp + 1.U) := io.dataIn
    sp := sp + 1.U
  }
    // Pop condition - make sure the stack isn't empty
    .elsewhen(io.en && io.pop && (sp > 0.U)) {
    sp := sp - 1.U
  }
```

Except for the line:

```scala
stack_mem(sp + 1.U) := io.dataIn
```

which is described in Verilog by:

```
  if(stack_mem__T_17_en & stack_mem__T_17_mask) begin
      stack_mem[stack_mem__T_17_addr] <= stack_mem__T_17_data;
    end
```

At this point there is some vagueness as to why this particular line has been separated, however, this is where actual execution happens - this is where a particular data is written to a particular address within the 32x8 `reg` generated by Chisel. The remaining parts of the Verilog code are concerned with updates to the variables such as `sp` and `out`.
This is, of course, possible because the complex boolean expressions within the Chisel conditional constructs have been replaced by assign commands in Verilog output file out of the always block. Thus variables such as `T_##` have been obtained.
The second `if(reset)` block:

```
  if (reset) begin
      out <= 32'h0;
    end else begin
      if (io_en) begin
        if (_T_22) begin
          out <= stack_mem__T_35_data;
        end
      end
    end
```

corresponds to further Chisel code:

```scala
  when(io.en) {
    dataOut := stack_mem(sp)
  }
```
