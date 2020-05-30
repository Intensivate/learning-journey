
2) examples that are bare basics, baby steps to get used to logistics (Verilog equivalent)
3) Advanced Chisel stuff (conditionals, types, intialization)
4) then examples that show scala intermixed intimately into chisel  (Scala arrays, functional operators)
5) then examples of specific scala features, like type inference and inheritance, implicit parameters
6) Things from Rocket code -- cake pattern, parameter system, map of RocketChip code 

The big picture, ideal is to have a number of pages, the first gets the bare basics explained, with a hello world that the use writes.  Just the logistics of writing something and running it.  Then next page introduces base level concepts, such as Class, variables, wires, boolean logic.  Hands on exercises for those.  These concepts should be one-to-one with equivalent Verilog concepts, if at all possible.  The page should explain equivalences.


Then the next page is about more complex stuff, like conditional register assignment, complex Chisel code examples (go through existing tutorials and pick stuff out)
 
The next page is about inter-mixing scala with Chisel -- a key idea to illustrate is that wires get assigned to variables and that, in turn, triggers the constructor for what that wire is attached to.  Include examples for doing scala arrays that are filled via functional programming constructs and end up causing a bunch of constructors to fire, and how that translates into a circuit.  

Next page is the complicated scala things needed to understand Rocket code, like implicit parameters, the cake pattern, and similar stuff -- then at the end, a collection of advanced examples that people have asked the Hangout about.
