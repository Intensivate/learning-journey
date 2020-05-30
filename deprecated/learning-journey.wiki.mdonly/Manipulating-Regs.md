At the moment, the examples from `rocket-chip` code are in Chisel 2, since that's what was found. As we go, we will add examples we will provide Chisel 3, as well.

# Example 1

A register is defined as follows:

```scala
val A        = Reg(init=UInt(0, ADDR_LEN - log2Up(CACHE_LINE_LEN/8)))
```

and later on, in the code it is used again:

```scala
A     := B(ADDR_LEN-1, log2Up(CACHE_LINE_LEN/8))
```

To find out what happens in here, let's first translate the constants:

```scala
ADDR_LEN = 40
CACHE_LINE_LEN = 32*8
```

Since `32*8/8 = 32` and `log2(32) = 5`, we can translate these two code lines as:

```scala
val A        = Reg(init=UInt(0, 40 - 5))
```

i.e.

```scala
val A        = Reg(init=UInt(0, 35))
```
This means that Reg `A` was created as a 35-bit wide register with the initial value of `0`.

The use case line translates into:

```scala
A     := B(39, 5)
```

where `B(39,5)` is a way to extract bits. Therefore, this means that a 35-bit wide `A` is now assigned bits 39 through 5 of the `B` register at every new clock cycle.