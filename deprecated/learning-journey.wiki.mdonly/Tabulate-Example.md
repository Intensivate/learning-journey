```val data = Vec.tabulate(4)(i => io.req.bits.wdata(8*(i+1)-1, 8*i))```

the thing that’s interesting is the ```tabulate```, which is a scala construct

Another use of tabulate:

  ```def fill[T <: Data](n: Int)(gen: => T): Vec[T] = tabulate(n){ i => gen }```
in https://github.com/ucb-bar/chisel/blob/master/src/main/scala/Vec.scala line 86

A long list of Scala ```List``` class examples, including creating lists, iterating over lists with ```for``` and ```foreach```, filtering lists, appending to lists, and more.

https://alvinalexander.com/scala/scala-list-class-examples

Here are a few examples of how to use the Scala tabulate method, which can be used to create and populate a List:

https://www.alvinalexander.com/source-code/scala/scala-tabulate-method-use-list-array-vector-seq-and-more

Some more uses of tabulate, that aren’t clear to me what’s going on: ```def tabulate[T <: Data](n: Int) (gen: (Int) => T): Vec[T]``` and ```def tabulate[T <: Data](n1: Int, n2: Int) (gen: (Int, Int) => T): Vec[Vec[T]]```

From the chisel manual, page 4: https://chisel.eecs.berkeley.edu/2.2.0/chisel-manual.pdf

The confusing part is the way that def is being used.. is it redefining “tabulate” for the Vec class? 
But I’m not seeing what it is defined to, exactly.

The answers in this stackoverflow shed a bit of light on def: https://stackoverflow.com/questions/5009411/two-ways-of-defining-functions-in-scala-what-is-the-difference

But there is still more mystery, of exactly what’s going on.. 

Two ways of defining functions in Scala. Here is a little Scala session that defines and tries out some functions: 
```
scala> def test1(str: String) = str + str; 
       test1: (str: String)java.lang.String 
scala> test1("ab") 
res0: java.l...
```

You may find definitions for tabulate in: https://github.com/ucb-bar/chisel2/src/main/scala/Vec.scala

There are two prototypes for tabulate: it can be called using either 
```tabulate(n: Int)``` or ```tabulate(n1: Int, n2: Int)```, so there are two different definitions
