
In this use, "=>" is just syntax saying particular pattern on one side, and what the case statement returns on the other side:
* ["=>" in pattern matching -- case](https://docs.scala-lang.org/tour/pattern-matching.html)

In this use, "=>" has two different meanings. In one, it declares type, in the other it creates an instance of a function, while implicitly declares the type.
* ["=>" in declaring a type of a function](https://stackoverflow.com/questions/6951895/what-does-and-mean-in-scala)

In this use, "=>" implicitly defines a function that is applied to every element of a list (or collection -- IE thing map is invoked on inherits Iterator).  The "=>" has on its left side a variable that represents each element of the list.  On its right side, is an operation performed on that element of the list.  The result is a new list with the same number of elements, where each is the result of the operation on the right side of "=>".
* ["=>" in map](http://www.brunton-spall.co.uk/post/2011/12/02/map-map-and-flatmap-in-scala/)