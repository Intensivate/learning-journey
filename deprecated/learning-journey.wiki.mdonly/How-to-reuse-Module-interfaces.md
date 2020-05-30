**Question**

Rocket Chip can be synthesized so that it contains a floating-point unit, which is actually contained in the HardFloat module.

In [this](https://github.com/ucb-bar/berkeley-hardfloat/blob/master/src/main/scala/ValExec_DivSqrtRecFN_small.scala) source file of the HardFloat, there are classes such as:

```scala
class DivRecFN_io(expWidth: Int, sigWidth: Int) extends Bundle {
    val a = ...
    val b = ...
    val ...
    ...
}
```

that are later on used as follows:

```scala
class ValExec_DivSqrtRecFN_small_div(expWidth: Int, sigWidth: Int) extends Module
{
    val io = IO(new Bundle {
        val input = Decoupled(new DivRecFN_io(expWidth, sigWidth)).flip
        ...
```

i.e. the input/output interfaces of `ValExec_DivSqrtRecFN_small_div` is created using `DivRecFN_io`. 

What are the implications of such practice in hardware and why isn't a just plain-old `Bundle` used directly?

**Answer**

Current opinion is that this is a way encapsulating a set of inputs/outputs that can be reused later to instantiate interfaces of other modules. Furthermore, according to the translation of an article found [here](https://translate.google.com/translate?sl=ja&tl=en&js=y&prev=_t&hl=en&ie=UTF-8&u=http%3A%2F%2Fmsyksphinz.hatenablog.com%2Fentry%2F2017%2F11%2F28%2F020000&edit-text=&act=url) it seems that:

> In practice, each design may have different way of coding to define/divide architecture of the floating point units. In such cases, only interface block's definition can be re-used.

Finally, as the class in question extends a Bundle, it is a Bundle itself, also. Therefore, no further implications to the synthesis are noticed.