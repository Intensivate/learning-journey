In class `MulAddRecFNToRaw_preMul` first part of FPU multiplication and addition is performed.

In the case of multiplication we want to add the exponents, so raw format operad A exponent is added to raw format B exponent:

```scala
val sExpAlignedProd =
        rawA.sExp +& rawB.sExp + SInt(-(BigInt(1)<<expWidth) + sigWidth + 3)
```

Here `+&` means _add and include carry_, however, we are *not sure what does the last member do*: `SInt(-(BigInt(1)<<expWidth) + sigWidth + 3)`

`BigInt` is an arbitrary precision representation of integers. What is its bit width in Chisel/Scala? Is it maybe 128 (since Long is 64)?

Taking the piece of code out of the context of FPU, here is what happens - an integer variable equal to 1 and bit width of BigInt is:

1. negated
1. shifted to the left by the width of the exponent
1. casted into a signed integer
1. added the bit width of the significand and added 3
1. added to the sum of the exponents of rawA and rawB

It is unclear what purpose is achieved here.

---
In `ValExec_MulAddRecFN.scala` there are three instantiations of `MulAddRecFN`: 

1. multiplication
1. addition
1. fused multply-add

in all of them `io.op` is assigned value `UInt(0)`, even though from the context of the `MulAddRecFNToRaw_preMul` function it seems that the puropose of `io.op` is actually to select the operation:

```scala
val signProd = rawA.sign ^ rawB.sign ^ io.op(1)
```

and then:

```scala
val doSubMags = signProd ^ rawC.sign ^ io.op(0)
```

Given that fused multiply-add is performed as: `A * B + C`.

---

