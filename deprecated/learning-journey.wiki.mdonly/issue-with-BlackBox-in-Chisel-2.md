
The issue with the Blackboxing in Chisel2 is simulation with the C backend.

In Chisel3 we can point Chisel to the Verilog source and it has Verilator generate the C++ code and integrates it automatically. In Chisel2, it doesn't use Verilator. Instead, Chisel 2 backend generates C++ directly from the Chisel code. So Chisel 2 doesn't generate C++ behavior for the Verilog that's inside the BlackBox.

What we tried is to use Verilator by hand to generate C++ from the Verilog code. But we haven't figured out how to integrate that C++ with the C++ that Chisel 2 generates. The issue is that every time we invoke sbt it regenerates the c++ code so all manual modifications get lost.

We also looked into porting c++ code in to Scala but it looks like we would have to port the c++ model of our verilog RTL through JNI (Java Native Interface) in order to run the simulation.

Grey boxing is not an option -- it doesn't actually test the integration or the Verilog internals.

One suggestion: Put the verilog code inside the blackbox.  Chisel2 allows that but chisel3 doesn't.  In other words, inside the Chisel2 file, add Verilog code.  So the Verilog appears in the middle of the Chisel code.

Here is link, in the Chisel 3 documentation, that talks about in-line Verilog (but it is really about in-line in Chisel 2): https://github.com/freechipsproject/chisel3/wiki/BlackBoxes#blackboxes-with-in-line-verilog

To make this work, you need to import black box modules.

in lowrisc code, there are 2 blackbox modules.
1. osd_ctm - instantiated inside RocketCoreTracer
2. osd_stm - instantiated inside RocketSoftwareTracer
Both these RocketCoreTracer and RocketSoftwareTracer, inside an inherited file. One thing to note is that both these modules (RocketCoreTracer and RocketSoftwareTracer) don't have any outputs, they only have inputs. Having a module which has only inputs doesn't make sense in the simulation.

Scala/Chisel is a complex language and it may contain a lot of stuff that is hidden. We are not in favor of this type of prgramming, but this is how it is implemented.

"RocketCoreTracer" extends "DebugModuleModule" implementation, you may find its implementation in "debug_module.scala".

With this extention, the "RocketCoreTracer" inherits output ports from "DebugModuleModule". Note the line "io.net <> bbox_port.io.chisel" line in RocketCoreTracer.

Although Rocket and LowRISC use Chisel 2.0, LowRISC uses Verilator, which will convert the verilog files listed in this Makefile into C++, and integrate it to replace the blackbox.