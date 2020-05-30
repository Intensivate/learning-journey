Scala Boolean may be converted to Chisel Bool as follows:

```scala
val thisIsScala : Boolean
val thisIsChisel = Bool(thisIsScala)
```

[Example usage in Rocket Chip](https://github.com/freechipsproject/rocket-chip/blob/1f18a37f01f1034b501a7f4c2edaaffb292d7186/src/main/scala/rocket/TLB.scala#L96)