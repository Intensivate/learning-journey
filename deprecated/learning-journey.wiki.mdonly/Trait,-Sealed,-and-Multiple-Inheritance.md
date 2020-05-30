# Description

In Scala, multiple inheritance is implemented using `traits`, which are similar to abstract classes, except for:

1. a class can inherit from multiple traits,
1. a trait can not have constructor parameters.

To inherit multiple traits, the following arrangement is used:

```scala
class MyClass extends HasTrait1 with HasTrait2 with ...
```

A `sealed` trait can be extended only in the same file as its declaration.

# Examples

From `RocketTiles.scala` lines 26-28

```scala
class RocketTile(val rocketParams: RocketTileParams, val hartid: Int)(implicit p: Parameters) extends BaseTile(rocketParams)(p)
    with HasLazyRoCC  // implies CanHaveSharedFPU with CanHavePTW with HasHellaCache
    with CanHaveScratchpad { // implies CanHavePTW with HasHellaCache with HasICacheFrontend
```

From `Resources.scala` line 9:

```scala
sealed trait ResourceValue
```

Many more examples are given in [[Mixins]]