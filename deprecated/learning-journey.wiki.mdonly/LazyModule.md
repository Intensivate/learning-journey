## Intro
The LazyModule construct was created as a way to delay calling the constructor until after there's been a chance to calculate the parameters to give it.  LazyModule decouples the point in the code where a circuit element is instantiated, from the point in the code where the constructor is called and that instance is actually built.  In Rocket, one main driver of this lazy approach is [[Diplomacy]]), which creates the TileLink2 and AXI connections on the chip.  Diplomacy, in turn, is built on top of the [[Cake pattern]]

LazyModule is built on top of Scala's Lazy val mechanism:

### Lazy `val` in Scala

If you put `lazy` in front of a `val` definition, the initializing expression on the right-hand side will **only be evaluated at the time** the `val` **is used** (which can be long after the system was initialized and after other computation has completed, such as computation that figured out how you want to initialize the value).

To show the difference, start with example of normal initialization:

```scala
object Demo {
  val x = { println("initializing x"); "done" }
}
```
Now, first refer to `Demo`, then to `Demo.x`:

```scala
>Demo
initializing x

res3: Demo.type = Demo$@2129a843
Chapter 20 · Abstract Members
>Demo.x
res4: String = done
```
The "initializing x" shows that as soon as you create `Demo`, all it's internal `val`s are also created.  The `x` field was initialized at the same point as Demo was initialized. 

Now, see how it's different if `x` is lazy:
```scala
object Demo {
    lazy val x = { println("initializing x"); "done" }
}
defined object Demo
> Demo
res5: Demo.type = Demo$@5b1769c
> Demo.x
initializing x
res6: String = done
```

Aha, when Demo is initialized, nothing happens with `x`! Only when you try to get the value of `x`, does `x` actually get created. Note that a `lazy val` is **never evaluated more than once**. After the first evaluation of a `lazy val` the result of the evaluation is stored, to be reused when the same `val` is used subsequently.

More detailed explanations of `lazy val` usage in Scala, and examples, may be found [here](https://www.scala-exercises.org/scala_tutorial/lazy_evaluation).

### LazyModule
LazyModule leverages the Lazy val concept, extending it to Chisel Modules.  The reason is that the parameterization system needs to do computation before it figures out the interface to a particular module.  For example the interface to RocketTile changes dramatically based on what parameter choices are made, and those parameters are not fully known at the point a RockeTile instance is created. 

However, `LazyModule` is normally not used in isolation.  Rather, it's most often used as the base for the [`cake pattern`](Cake pattern), which is the main mechanism for connecting circuits together in Rocket Chip.  The Cake pattern is also the base for [`diplomacy`](Diplomacy) [3], which is how chip level elements are wired together, such as through TileLink, AXI4, and other and on-chip networks.

As anticipated, instances of classes that inherit from `LazyModule` have their constructor delayed until the instance is used in some way.

Due to inheritance, a very large number of classes defined in Rocket end up being LazyModules.

Here are examples found in `RocketTile.scala`:

```scala
(from June 2017 version of RocketTiles.scale): class RocketTile(val rocketParams: RocketTileParams, val hartid: Int)(implicit p: Parameters) extends BaseTile(rocketParams)(p)  where BaseTile extends LazyModule
~
val xing = LazyModule(new IntXing(3)) // @L173
~
val intXbar = LazyModule(new IntXbar) // @L176
```

## Point of Lazy Modules in Rocket Chip

The point of making modules "lazy" (read: "not instantiated until accessed for the first time") is to allow the Diplomacy and TileLink to negotiate during the elaboration phase about creating interfaces between those modules.  

RocketTile is a LazyModule, the actual implementation is instantiated [here](https://github.com/freechipsproject/rocket-chip/blob/20a88768560ca810c0dbe79f1a33604268d921c7/src/main/scala/tile/RocketTile.scala#L95)

RocketTile uses [Diplomacy](https://github.com/librecores/riscv-sodor/wiki/Diplomacy)

`LazyModule` is an abstract class of `rocket-chip` whose complete code is contained in `LazyModule.scala` found [here](https://github.com/freechipsproject/rocket-chip/blob/master/src/main/scala/diplomacy/LazyModule.scala)

### References

1. M. Odersky, *Programming in Scala*
1. K. Bhimani, *[Updates to Sodor](https://codelec.github.io/gsoc/gsoc3/)*
3. W. Song, *[Notes for Rocket-Chip](https://github.com/cnrv/rocket-chip-read/)*