  [<<< Previous Page](Learning-journey) (Start Learning Journey)    [Next page >>>](Setting-up-Chisel) (Setting up Chisel)

### Why Chisel?

Chisel was created by UC Berkeley as the language for their Rocket Chip project.  This project provides a complete chip, including processor core, coherent caches, and peripherals, for free.  It is free to make a commercial product with it and keep the changes proprietary.  Rocket Chip is the only sophisticated, well supported example of such a complete chip that is free.  Hence, to get Rocket Chip we have to use Chisel.

The promise of Chisel is that it is quick and easy to make changes.  It is object oriented, so it eliminates low level "plumbing".  For example, in verilog, when a bus is changed, then you have to go through and change the interfaces of all the elements that are connected to the bus.  Much less so in Chisel. 

### Clues about Chisel
One thing to be aware of early on is that Chisel is embedded into [Scala](https://www.scala-lang.org/), which is an object oriented programming language, and also a functional programming language.  As such, Scala has things like [inheritance](http://docs.scala-lang.org/tour/mixin-class-composition.html) and [type inference](http://docs.scala-lang.org/tour/local-type-inference.html), so you can write a library element, like a multiplier, and not even define many details, like the width or type of bus -- Scala will figure that out by the context in which the multiplier is used!

As such, Chisel often embeds scala code into the middle of circuit constructor code.  The scala part handles things like type and size of bus, bit widths, number of repetitions and so on.  In more advanced code, the scala part often intimately intertwines circuit keywords within the syntax of functional programming, which ends up making Chisel very productive once you master it.  The learning journey starts with very simple constructs and works its way up, gently, getting you ready for the [Advanced examples](https://github.com/librecores/riscv-sodor/wiki/Advanced-Examples-of-Using-Chisel) (where individual examples are pulled from Sodor code and Rocket code, and analyzed).  You'll see how the scala code fills in values into the circuit-related chisel constructs.

Chisel code is compiled and run the way an object oriented program is, except that most of the execution is spent constructing objects!  The scala code fills in values and does bookkeeping for the Chisel parts.  The Chisel parts are object constructors, and when they run, they generate objects that represent circuit elements.  At the end, when the Chisel code has all completed, there is a big collection of objects that represent the circuit.  At that point, an internal post-processing step takes place.  The Chisel language has post-processors.  They all take the objects that represent the circuit and generate something from them.  One post-processor generates a Verilog that is tuned for FPGA execution.  A second generates Verilog that is tuned for ASIC.  And, for the older Chisel 2, there is also a generator that makes C++ that simulates the logic (for Chisel 3, this has been replaced by a tool that generates C++ from the Verilog code, called Verilator).

Chisel is used to create individual blocks of a chip.  Normally Chisel code only covers a single clock domain.  If there are multiple clock domains, then they are glued together in the EDA tools (Synopsys, Cadence, etc).  Also, Chisel has an escape mechanism, called a Black Box, to handle circuits that don't fit Chisel well, or to integrate third party IP.

### Issues with Chisel
Several issues exist with Chisel.  The first is that it takes time to learn.  It is a different approach to creating circuits.  The Chisel code often just describes the function, with no hint as to what the circuit will look like.  Hence, it feels uncomfortable at first to circuit people who are used to Verilog.  We want control!  

That is the second issue, is that feeling of not having control over the circuit that is generated.  In many places, Chisel does a poor job of translating the function into an efficient circuit.  For example, Chisel does not have tri-state values, so there are no busses in Chisel!  We have to use an escape mechanism, called a "black box" to handle this.  The Chisel literally has a blackbox construct ([[Black Box Example]]), which connects the Chisel code to Verilog code.  So any place that we want precise control over the circuit, we carve that functionality out of Chisel and insert a black box, then write that piece in Verilog.

### Good things about Chisel
On the positive side, after learning about Scala and learning how it interacts with Chisel, then Chisel has proven to be highly efficient to use.  Changes can be made very quickly.  That has high value later in the design process, in two places.  First, we don't need to write a separate performance simulator.  Instead, it is nearly as quick to modify the Chisel as it is to modify a C++ architectural simulator.  Then, we simply generate an FPGA version of the chisel code, and use that to perform our performance measurements regarding architectural parameters. 

Second, once we start using the EDA tools to generate physical layout we discover what parts of the circuit  are too slow, and places where we need to move functionality from one pipe stage to a different one.  Chisel makes it faster to make these changes.

As far as how efficient Chisel is at generating quality circuits -- the jury is still out.  Some evidence suggests that final layout from Chisel generated Verilog may be inferior. But the Black Box allows inserting hand optimized Verilog code where needed, and head to head examples of Chisel based Rocket chips versus ARM chips are promising.  It is not clear how much of the advantage is due to superior RISC-V ISA, how much due to superior implementation, and how much is due to Chisel itself, but the results are clear that Chisel is able to generate competitive final chips.

More depth on [[efficient circuits generated from Chisel]]

### Preview of the Journey
At first, it won't be clear from the examples how Chisel is any different from Verilog.  That is on purpose, keeping things simple.  It will be once you get to the advanced examples that the true difference becomes clearer.

For example, in Diplomacy, which is used in Rocket Chip, it uses a split scheme, where modules are declared, but not connected to each other.  Instead, calculations are done, which negotiate things like bus widths, optional features, number of connections, and so forth.  All of the declared modules agree on these, and then in a second phase the actual circuits are instantiated, based on the negotiated parameter values.  This is done by using Lazy Module feature, Mixins (a form of multiple inheritance) and so forth.  These are all covered in the advanced examples, after the basic Chisel learning is complete.   

  [<<< Previous Page](Learning-journey) (Start Learning Journey)    [Next page >>>](Setting-up-Chisel) (Setting up Chisel)