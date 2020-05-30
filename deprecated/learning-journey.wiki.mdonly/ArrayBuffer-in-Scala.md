### About ArrayBuffer
An `ArrayBuffer` is like an `Array*`, except that you can additionally add and remove elements from the beginning and end of the sequence. 

To use an ArrayBuffer, you must first import it from the mutable collections package:
```scala
import scala.collection.mutable.ArrayBuffer
```
### Basic example
When you create an ArrayBuffer, you must specify a type parameter, but you don’t need to specify a length. The ArrayBuffer will adjust the allocated space automatically as needed:

```scala
val buf = new ArrayBuffer[Int]() //empty buffer is created
```

You can append to an ArrayBuffer using the += method:
```scala
buf += 12
buf += 14
buf += 15 //adding three elements in buffer
pritnln(s"Elements of buf = $buf)
```
Output of this is: `Elements of buf = ArrayBuffer(12, 14, 15)`

All the normal Array methods are available. For example, you can ask an ArrayBuffer its length or you can retrieve an element by its index:
```scala
println(s"Length of buffer is ${buf.length} and element at index 0 = ${buf(0)}") 
``` 
The following output looks like: `Length of buffer is 3 and element at index 0 = 12`

Just as we can add elements, we also can delete elements from ArrayBuffer using -= method:

```scala
buf -= 14 //deleting element from buffer
```
After calling this line
```scala
println(s"After deleting buf = $buf)
```
we have: `After deleting buf = ArrayBuffer(12, 15)`

### P-rocket-chip example

Let's look on some example of ArrayBuffer from [p-rocket-chip](https://bitbucket.org/intensivate/p-rocket-chip/src/8f2b8c9f7f6eebb4833ac8ddcc39f3c7a2e67f88/src/main/scala/rocket/RocketCore.scala#lines-839)

```scala
private val reads = ArrayBuffer[(UInt,UInt)]() 
```
On this way we instantiate buffer which contains tuples (pairs) that holding two elements of chisel type UInt (unsigned integer). [This line](https://bitbucket.org/intensivate/p-rocket-chip/src/8f2b8c9f7f6eebb4833ac8ddcc39f3c7a2e67f88/src/main/scala/rocket/RocketCore.scala#lines-843) is filling up buffer with tuples created by using `-> operator` (key -> value, creates a tuple (key, value)).

 If we want last element of buffer just type: `reads.last`, like in [this case](https://bitbucket.org/intensivate/p-rocket-chip/src/8f2b8c9f7f6eebb4833ac8ddcc39f3c7a2e67f88/src/main/scala/rocket/RocketCore.scala#lines-845)

### Additional things
All Array operations are available, though they are a little slower due to a layer of wrapping in the implementation. The new addition and removal operations are constant time on average, but occasionally require linear time due to the implementation needing to allocate a new array to hold the buffer’s contents.

`Array* is a data structure which stores a fixed-size sequential collection of elements of the same type. It is used to store a collection of data, but it is often more useful to think of an array as a collection of variables of the same type.`