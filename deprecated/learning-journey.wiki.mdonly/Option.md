# Description

A good description of using Option in Chisel is found in the Chisel tutorials [here](https://github.com/ucb-bar/generator-bootcamp/blob/master/3.1_parameters.ipynb).  Just skip down to the section on Option.

In a nutshell, Option is a fancy way of handling errors, especially errors that come up when passing parameters, and when applying a map to a list.

Option is mainly used in conjunction with the `get` method of `Map`. In this case, `get` returns a value of `abstract class Option`, which has two subclasses: `Some` and `None`.  Which means, that in the case that the `map` could not be applied, instead of throwing an error, instead it returns an `Option` of subclass `None`.  Then, the code that receives the result of the `map` can check which subclass, and take action in the case of `None`, without any errors being thrown.


# Examples

from `RocketTiles.scala`, lines: 17-18 & 20

```scala
    icache: Option[ICacheParams] = Some(ICacheParams()),
    dcache: Option[DCacheParams] = Some(DCacheParams()),
    ...
    btb: Option[BTBParams] = Some(BTBParams()),
```

## Options for Parameters with Defaults
When objects or functions have a lot of parameters, it can be tedious and error-prone to fully specify them all the time. In module 1, you were introduced to named arguments and parameter defaults. Sometimes, a parameter doesn't have a good default value. Option can be used with a default value of None in these situations.

**Example: Optional Reset**
The following shows a block that delays its input by one clock cycle. If resetValue = None, which is the default, the register will have no reset value and be initialized to garbage. This avoids the common but ugly case of using values outside the normal range to indicate "none", like using -1 as the reset value to indicate that this register is not reset.

```
class DelayBy1(resetValue: Option[UInt] = None) extends Module {
    val io = IO(new Bundle {
        val in  = Input( UInt(16.W))
        val out = Output(UInt(16.W))
    })
    val reg = if (resetValue.isDefined) { // resetValue = Some(number)
        RegInit(resetValue.get)
    } else { //resetValue = None
        Reg(UInt())
    }
    reg := io.in
    io.out := reg
}

println(getVerilog(new DelayBy1))
println(getVerilog(new DelayBy1(Some(3.U)))) //adds a default for reset value, which is "3"
```

See: https://github.com/ucb-bar/generator-bootcamp/blob/464f6bd7d314a99af3aa74ff5be56da971dafc8b/3.1_parameters.ipynb
