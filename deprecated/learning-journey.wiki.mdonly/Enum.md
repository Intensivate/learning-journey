`Enum(UInt(), 3)` creates a list of `UInt` literals with values 0, 1, 2.

Its usage includes the contruction of states when the Finite State Machine is constructed, as follows:

```scala
val sNone :: sOne1 :: sTwo1s :: Nil = Enum(UInt(), 3)
```
where the left hand side is a destructuring expression that binds successive elements from a list creating one binding for each and the final Nil matches the `Nil` at the end of the list.

`Nil` is required as the operators ending with colon, i.e. methods with names ending in a colon are executed on the right operand. 

[Example usage in Rocket Chip](https://github.com/freechipsproject/rocket-chip/blob/1f18a37f01f1034b501a7f4c2edaaffb292d7186/src/main/scala/rocket/TLB.scala#L87)

------------
[1] [[https://groups.google.com/forum/#!topic/chisel-users/mDSV1ML-u9c]]
[2] [[https://github.com/freechipsproject/chisel3/blob/70ca35b9d7b3884e5f701d49bc5286f89701fd14/src/test/scala/cookbook/FSM.scala]]