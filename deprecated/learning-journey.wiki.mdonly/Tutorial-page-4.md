Here we revisit some constructs that you've already been using, and introduce some new ones.  The idea is to call out, explicitly, what is Scala syntax, vs what is Chisel syntax, and explain the mechanics behind the Scala parts.

So, this page collects a number of common Scala syntax examples, with explanations.

### Example 1 "map"

io.out := (taps zip io.consts).map { case (a, b) => a * b }.reduce(_ + _)

Let's break it down:

(taps zip io.consts) takes two lists, taps and io.consts, and combines them into one list where each element is a tuple of the elements at the inputs at the corresponding position. Concretely, its value would be [(taps(0), io.consts(0)), (taps(1), io.consts(1)), ..., (taps(n), io.consts(n))]. Remember that periods are optional, so this is equivalent to (taps.zip(io.consts)).
.map { case (a, b) => a * b } applies the anonymous function (takes a tuple of two elements returns their product) to the elements of the list, and returns the result. In this case, the result is equivalent to muls in the verbose example, and has the value [taps(0) * io.consts(0), taps(1) * io.consts(1), ..., taps(n) * io.consts(n)]. You'll revisit anonymous functions in the next module. For now, just learn this syntax.
Finally, .reduce(_ + _) also applies the function (addition of elements) to elements o

------------------

4. The next page is about inter-mixing scala with Chisel -- the idea that wires get assigned to variables and that, in turn, triggers constructor for what that wire is attached to -- doing scala arrays that are filled via functional programming constructs and end up causing a bunch of constructors to fire, and how that translates into a circuit.

This one is where we provide value-add. We will need to put in some time, collaboratively, on this one.

Examples: use .map on scala lists, using inheritance

[[temp page to gather syntax examples]]

[[temp page 2 to gather syntax examples]]

### Next step

[[Tutorial page 5]]
***
[[Tutorial Home]]