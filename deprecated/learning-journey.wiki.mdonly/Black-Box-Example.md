# Black Boxes in Chisel
As of April 2018, here's how to place a black box inside a lazy module, such inside an inner twin:
https://github.com/ucb-bar/riscv-boom/pull/74


## Simple Example of Using Black Boxes -- ca 2016

A Black Box is a bridge between Chisel code and Verilog or SystemVerilog.  Black boxing is used when some IP is written in Verilog or SystemVerilog would like to include in the Chisel Design.

There is an [[issue with BlackBox in Chisel 2]].

There is also source of the BlackBox construct is here:
https://github.com/freechipsproject/chisel3/blob/master/chiselFrontend/src/main/scala/chisel3/core/BlackBox.scala#L12

## You will find more details about BlackBox in the next resources:

### ((Chisel 2.00))
1) This [manual](https://chisel.eecs.berkeley.edu/2.0.0/manual.html).

2) My question on chisel google groups: [BlackBox help in Chisel 2.0](https://groups.google.com/forum/#!topic/chisel-users/Mk6AYNu9I0M).

3) Old question on chisel google groups which states the problem of test harness using blackbox in chisel 2.0: [Wrap verilog code in Chisel](https://groups.google.com/forum/#!topic/chisel-users/ZSAhItKXR2Q).

4) The stackoverflow version of : [Wrap verilog code in Chisel](https://stackoverflow.com/questions/24154279/wrap-verilog-code-in-chisel).

### Chisel 3 ((Try to mimic it to Chisel 2.0))
5) Here is my question on Stackoverflow: [How can I make a blackbox in Chiesl 2.0](https://stackoverflow.com/questions/46131235/how-can-i-make-a-blackbox-in-chisel-2-0/46160719#46160719)

6) A working example of [BlackBox in Chisel 3](https://github.com/chick/chisel-experiments/blob/master/src/main/scala/essam/Black.scala)

--------------------------------------------------------------------------------------------------------------------------
The codes that I wrote, but it imports Chisel 3.0 but the difference is when we want to perform test harness. It is different and as Armia said "Scripts involves heavily in this process"

You will find all the details of my code [here](https://github.com/chick/chisel-experiments) where Chick Markley helped me to resolve its errors where: 
The essam package and resource directories have my somewhat refactored versions of my code.
I am able to run the main in src/main/scala/essam/Black.scala
Note: the black box file is in src/main/resources/essam/Black.v
From IntelliJ and also from
sbt with
runMain essam.BlackBox_tbTests

### Debugging to explore how blackbox processed in Chisel 2.0 follow the next instructions:
1) First, know the how to make instance through the syntax from the above resources.
2) Open this repo: "IntensivateRoot/lowrisc-chip"
3) Check this: "lowrisc-chip/rocket/src/main/scala/ipipie.scala", where both the ctm and the stm are Verilog modules.
4) The system Verilog implementationcan be found under "lowrisc-chip/opensocdebug/hardware/modules/" folder.
Note: 
** The scripts are heavily involved in this process.
** The IPIPE instantiates the scala file "lowrisc-chip/opensocdebug/hardware/modules/ctm/riscv/rocket/ctm.scala", which contains the blackbox glue logic to instantiate the Verilog file in "lowrisc-chip/opensocdebug/hardware/modules/ctm/common/osd_ctm.sv"

5) The script is in: "lowrisc-chip/vsim". You may execute "CONFIG=DebugConfig make -C $TOP/vsim sim" to see the compilation process.








