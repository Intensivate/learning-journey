# Description

Final on a function prevents it from being overridden.
Final on a class prevents it from being subclassed.

# Examples

From `Resources.scala` lines 10-15 -- these cannot be subclasses:

```scala
final case class ResourceAddress(address: Seq[AddressSet], r: Boolean, w: Boolean, x: Boolean, c: Boolean) extends ResourceValue
final case class ResourceMapping(address: Seq[AddressSet], offset: BigInt) extends ResourceValue
final case class ResourceInt(value: BigInt) extends ResourceValue
final case class ResourceString(value: String) extends ResourceValue
final case class ResourceReference(value: String) extends ResourceValue
final case class ResourceMap(value: Map[String, Seq[ResourceValue]], labels: Seq[String] = Nil) extends ResourceValue
```