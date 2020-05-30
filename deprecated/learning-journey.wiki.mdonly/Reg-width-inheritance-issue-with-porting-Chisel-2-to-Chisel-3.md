
Chisel 2 is able to calculate the width of a Reg through inheritance.  But this does seem to work when the code is ported to Chisel 3.

For example, in Chisel 2, "a_reg = b+c"

a_reg width shall equal the width of "b" or "c". Chisel 2 was able to understand that, and we do not need to set the width of a_reg explicitly.

So the following register was defined like:
val prodP1_reg    = Reg(UInt(0))

it is initialized to zero, but no width is given.

Then used in this fashion:
```scala
prodP1_reg   := Cat(Fill(1, UInt(0)), op1(part1Size-1, 0)) + Cat(Fill(1, UInt(0)), accum(part1Size-1, 0))
```
here prodP1_reg   shall equal to the size of Cat(Fill(1, UInt(0)), op1(part1Size-1, 0))                   OR      Cat(Fill(1, UInt(0)), accum(part1Size-1, 0))


both has the same size. And equal    Fill(1, UInt(0)) size          +          op1(part1Size-1, 0) size.

Fill(1, UInt(0))'s size is 1.

op1(part1Size-1, 0)'s size is part1Size

That works fine with Chisel 2,  However, with the new environment, the width of prodP1_reg is only one bit, which cause functionality and compiling problems.  I had to set the width explicitly for those registers.

```scala
val prodP1_reg    = Reg(UInt(0, part1Size+1))
```

I do not know if this issue is the normal behavior for Chisel 3, or this just due to the complexity of the code

Note that, 2017 version contains the same code. The only difference is the equivalent of prodP1_reg is a wire (not a register.)