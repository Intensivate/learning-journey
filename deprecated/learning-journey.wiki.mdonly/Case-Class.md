# Description

A `case class` represents a special type of class in Scala that has the features:
1. external access to the class parameters,
1. eliminates the need to use `new` when instantiating the class
1. automatically creates an `unapply` method that supplies access to all of the class Parameters,
1. cannot be subclassed from.

It's often used to essentially create a data structure.

# Examples

From `RocketTiles.scala` line 15:

```scala
case class RocketTileParams(
    core: RocketCoreParams = RocketCoreParams(),
    icache: Option[ICacheParams] = Some(ICacheParams()),
    dcache: Option[DCacheParams] = Some(DCacheParams()),
    rocc: Seq[RoCCParams] = Nil,
    btb: Option[BTBParams] = Some(BTBParams()),
    dataScratchpadBytes: Int = 0) extends TileParams {
    ...
```
Which is used this way:
```scala
class RocketTile(val rocketParams: RocketTileParams, val hartid: Int)(implicit p: Parameters) extends BaseTile(rocketParams)(p)
    ...
      val m = if (rocketParams.core.mulDiv.nonEmpty) "m" else ""
    ...
```
