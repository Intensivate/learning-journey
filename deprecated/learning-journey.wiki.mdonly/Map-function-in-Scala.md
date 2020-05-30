### About map 
The scala `map` function converts one collection A to another B by applying a function to every element in A .
As per the Scala documentation, the definition of the map method is as follows:
```scala
def map[B](f: (A) ⇒ B): Traversable[B]
```
Important points about map method:
* every collection object has the map() method;
* map() takes some function as a parameter;
* map() applies the function to every element of the source collection;
* map() returns a new collection of the same type as the source collection, each element of which is the result of applying the input function to the corresponding element of the input array.

Simply put, you can call the map function on your collection, pass it a function, or an anonymous function, and transform each element of the collection.

### Basic examples
Let's create a two lists, one list of integers and one of strings:
```scala
val nums = List(1, 2, 3)
```
If we call map function like this:
```scala
val result = nums.map((i: Int) => i * 2)
println(s"Resulting list of numbers = $result")
```
Output would be: `Resulting list of numbers = List(2, 4, 6)`

We see that this operation returns another list with the doubled elements.

Let's create another list, this time list of strings
```scala
val str = List("one", "two", "three")
```
We can call scala built in functions, something like this:
```scala
println(s"Original list: $str")
val caps = str.map(_.capitalize)
println(s"Capital first letter: $caps")
val lengths = str.map(_.length)
println(s"Lengths of strings: $lengths")
```
Output of this code is: 
```
Original list: List(one, two, three)
Capital first letter: List(One, Two, Three)
Lengths of strings: List(3, 3, 5)
```
### P-rocket-chip examples

Lets look some examples of map method used in P-rocket-chip:
* in the [first](https://bitbucket.org/intensivate/p-rocket-chip/src/8f2b8c9f7f6eebb4833ac8ddcc39f3c7a2e67f88/src/main/scala/rocket/RocketCore.scala#lines-213) example map function was called on indexed sequence `id_raddr`.This map method apply function `rf_read` on every element of that indexed sequence(reading from every address from sequence) and we get variable `id_rs` consisting of elements which are second value from read tuples.

* in the [second](https://bitbucket.org/intensivate/p-rocket-chip/src/8f2b8c9f7f6eebb4833ac8ddcc39f3c7a2e67f88/src/main/scala/rocket/RocketCore.scala#lines-196) example map function was used to iterate through all elements of vector `inst`, which is IBuf port, and get every parameter-`inst` of class Instruction through decoupled interface(`_.bits.inst`), thus generating vector of instuctions `id_expanded_inst`.

* in the [third](https://bitbucket.org/intensivate/p-rocket-chip/src/8f2b8c9f7f6eebb4833ac8ddcc39f3c7a2e67f88/src/main/scala/rocket/RocketCore.scala#lines-290) example map function was called on indexed sequence-`bypass_sources` of tuples with three elements. On this way we would get indexed sequence `bypass_mux` consisting of third elements of tuples.

### Additional things

Notice that `.capitalize` (returns a new string with the first letter only converted to upper case) and `.length` (which gives the length of the string in characters), is scala string applicable methods.
Also good to mention is that `_` is used as placeholder syntax which means apply method on every element of list.

We need to distinguish between map() and Map(which is a collection of key/value pairs).
