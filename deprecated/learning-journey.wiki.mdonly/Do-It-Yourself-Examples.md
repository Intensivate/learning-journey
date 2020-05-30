Here will be given some examples that are asked of the user to actually implement them, solutions provided at the bottom for all - asking for more users to provide more assignments and offer interesting solutions to them.

# How to add and test your own examples?

The easiest way to add your own example at this point would be to use the existing project infrastructure of the tutorials within Learning Journey.

The first thing to do is to select the package you want to implement your example into. Let's say it's the `examples` package, located in `src/main/scala/examples`, which we will refer to as _package home_ in further text.

You will have to create two files and edit another one. The two files you need to create are:

* example itself - `src/main/scala/examples/example1.scala`
* testbench - `src/test/scala/examples/example1Tests.scala`

while the one you need to edit is `src/test/scala/examples/Launcher.scala`.

The example itself must contain the following header:

```scala 
package examples

import chisel3._
```

and the rest of the file should be organized analogous to the examples/problems/solutions you've seen up to now, importing additional libraries if required.

The testbench must contain the following header:

```scala
package examples

import chisel3.iotesters.{PeekPokeTester, Driver, ChiselFlatSpec}
```

and the rest should analogous to the files of the same purpose you've already seen.

Finally, you need to add the following lines to `Launcher.scala`:

```scala
      "example1" -> { (manager: TesterOptionsManager) =>
        Driver.execute(() => new example1(), manager) {
          (c) => new example1Tests(c)
        }
      },
```

When you open the file, you will see that this is a pattern that repeats for each of the existing circuits/tests existing within package home. So, just copy one of the patterns and change the names where appropriate.

Once your code and tests are written, `Launcher.scala` edited as advised, you can run your example from Learning Journey home as follows:

```scala
$ sbt
> test:run-main examples.Launcher example1
```

which will probably throw an error or two that you need to take care of :)

After the functionality tests come out clean, you can proceed to generate Verilog as follows:

```scala
$ sbt
> test:runMain examples.Launcher exampl1 --backend-name=verilator
```
# One way to monitor runtime execution

...is to use the provided `printf` function in one of the following ways:

## 1. Scala-style: 

There is a custom string interpolator `p`, example usage being:

```scala
val myUInt = 33.U
printf(p"myUInt = $myUInt") // myUInt = 33
```

For now, these formats are available:

```scala
// Hexadecimal
printf(p"myUInt = 0x${Hexadecimal(myUInt)}") // myUInt = 0x21
// Binary
printf(p"myUInt = ${Binary(myUInt)}") // myUInt = 100001
// Character
printf(p"myUInt = ${Character(myUInt)}") // myUInt = !
```

There are more options, but we will provide them in the page [[Advanced-Printing]] (yet to be made).

## 2. C-style:

In this case, Chisel allows the following format specifiers:

|Specifier|Meaning|
|---|---|
|`%d`|decimal number|
|`%x`|hexadecimal number
|`%b`|binary number|
|`%c`|8-bit ASCII character|
|`%%`|literal percent|

whereas the escape characters are given as:

|Character|Meaning|
|---|---|
|`\n`|newline|
|`\t`|tab|
|`\"`|literal double quote|
|`\'`|literal single quote|
|`\\`|literal backslash|

Finally, example usage:

```scala
val myUInt = 32.U
printf("myUInt = %d", myUInt) // myUInt = 32
```