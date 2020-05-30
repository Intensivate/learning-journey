Just passed through the Chisel Learning Journey. My fresh impressions: Chisel seems to be a powerful tool and quite a new approach in hardware description domain. For those who are used to Verilog/VHDL, it should take some time to adopt all those Object-oriented features of Chisel and Scala. 

Learning Journey that is behind me is written in a "read and don't think" way. It is like a teaser for us Verilog/VHDL users to motivate us continue exploring Chisel world. At the end of the Journey the reader can't say he/she is a Chisel expert. It's just a first date with the Chisel tool that throws the light on it. Diving deeper is undoubtedly necessary. In this sense, the links at the end of Journey are well placed. 

It seems that a Learning Journey material is refined after the feedback from many previous readers. However, here are a few points that I would like to rise:

* It is quite easy to follow the Setup instructions and install VM with Chisel working environment. However, when explaining how to install and set up the VirtualBox VM with Linux Mint, I would add a part that explains with more details how to share the files between host Operting System and VirtualBox VM. You have explained what is needed on VM side. You should just add what needs be done on the host side as well (e.g. [here](https://www.youtube.com/watch?v=XjbxFRUoPDQ)).

*  What seems confusing and non-consistent to me is the declaration of IO bundle:
 ```scala
 val io = IO(new Bundle {
 ...
 })
 ```
 Sometimes you have `IO(...)` keyword with the `(` brackets like above, sometimes you are omitting `IO(...)` keyword and `(` brackets like below:
 ```scala
 class BasicALU extends Module { 
   val io = new Bundle { 
     val a      = Input(UInt(4.W)) 
     val b      = Input(UInt(4.W)) 
     val opcode = Input(UInt(4.W)) 
     val output = Output(UInt(4.W)) 
   }
 ```
 Am I missing something about IO bundle or this is just an inconsistency? 

* At the bottom of each tutorial page, you have a pointer to the next page. I have a feeling a pointer to the previous page should be added as well. This will make the movement through the Journey easier. 

* Finally, I think that the solution of the last Chisel assignment (`~/learning-journey/src/main/scala/solutions/Adder.scala`) contains some errors and it is impossible to compile. 