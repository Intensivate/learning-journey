# Introduction

The Cake Pattern was chosen as the main method for constructing circuits in Rocket Chip.  The prime example is RocketTile.  Here, the various pieces, such as integer pipeline, icache, dcache, and so forth, are "mixed in" according to the Cake Pattern.

The [[LazyModule]] and [Mixins](https://github.com/librecores/riscv-sodor/wiki/trait%2C-with%2C-Mixins) are the base underneath Cake Pattern.  What Cake does is make “twins” -- an outer twin and an inner twin..  There is also a “bundle” in the June 2017 version we discuss here.

The term `twin` is actually non-standard, we're using it because we need an easy name, when talking about these things.  And they always have to be in pairs.. so “twin” is a natural word to use.. 

The twins are connected together in an inheritance graph.  In the case of RocketTile, the way that circuit elements get pulled into the tile is by inheriting the circuit code. (this method of using inheritance is the essence of the Cake Pattern).  It ends up being pretty straight forward once you see the elements of the pattern.

### Code Illustrating Cake Pattern
Here’s some code:
```scala
class RocketTile(val rocketParams: RocketTileParams, val hartid: Int)(implicit p: Parameters) extends BaseTile(rocketParams)(p)
    with HasLazyRoCC  // implies CanHaveSharedFPU with CanHavePTW with HasHellaCache
    with CanHaveScratchpad { // implies CanHavePTW with HasHellaCache with HasICacheFrontend
```
So, `HasLazyRoCC` is a cake outer twin, and `CanHaveScratchpad` is also an outer twin.
`RocketTile` itself, is also an outer twin!

Mixing these twins in is how circuit code gets placed inside a RocketTile. (By "mixing them in" we mean inheriting them via `with` and `extends`)

Here’s one path through the code:
```scala
trait HasLazyRoCC extends CanHaveSharedFPU with CanHavePTW with HasTileLinkMasterPort {
```
This mixes in three more outer twins.  One of them is CanHavePTW:
```scala
trait CanHavePTW extends HasHellaCache {
```

Which mixes in yet another outer twin: `HasHellaCache`:
```scala
trait HasHellaCache extends HasTileLinkMasterPort with HasTileParameters {
```
This time, the mixed-in twins are just Rocket plumbing -- they’re not circuit related..
BUT, inside `HasHellaCache` is this:

```scala
 val dcache = HellaCache(hartid, tileParams.dcache.get.nMSHRs == 0, findScratchpadFromICache _)
```

This is where an actual circuit is created!

So, that is how a cache gets included inside RocketTile.

### Wiring Circuit Elements
Separately, that cache has to be wired up..

This wiring happens back inside RocketTile:
```scala
  dcachePorts += core.io.dmem
```
And that’s how the cake pattern works..

Note that the twins are connected in this way:
The outer twin always includes an instance of the inner twin, and passes the constructor a reference to the outer twin.  For RocketTile:
```scala
class RocketTile ...
...
  lazy val module = new RocketTileModule(this)
```

The inner twin always receives a reference to the outer twin, and it is always named `outer`
```scala
class RocketTileModule(outer: RocketTile) ...
```
### Diagram of Cake Inheritance Graph in RocketTile
We’ve created a diagram of the code:
http://dev1.intensivate.com:8080/intensivate/?https=0#G1w5LD3RJFXGNl2pRhaX_F-cp3cP_5GdMW

This shows the cake pattern linkages:
* the double lined boxes with sharp edges are outer twins
* the doubled lined boxes with rounded corners are inner twins
* the single lined boxes with rounded corners are circuit instantiations.

Each arrow corresponds one-to-one with “extends” or “with” keyword
* The red arrows are for outer twins inheritance
* The thin black arrows are for inner twin inheritance.

This diagram provides quick navigation of the code, to find the spot that you need to modify -- so if you’re in RocketTile and see “dcache.something” then you can look in the diagram, and find “val dcache = “ and then you know where the code for dcache is.

And if you’re thinking about making a change, you can look at the diagram and see what will have to be changed, and then go straight to those spots.

# Scala

A very basic, step-by-step, introduction (with plenty of examples) to the Scala Cake Pattern is given [here](http://www.warski.org/blog/2014/02/using-scala-traits-as-modules-or-the-thin-cake-pattern/).

# Chisel

A brief explanation of a design that uses Cake Pattern and an example in Chisel are given [here](https://github.com/cnrv/rocket-chip-read/blob/master/other/cake_pattern.md).

# References

[1] Adam Warski, *[Using Scala traits as modules](http://www.warski.org/blog/2014/02/using-scala-traits-as-modules-or-the-thin-cake-pattern/)*, 2014
[2] Wei Song, *[Cake pattern](https://github.com/cnrv/rocket-chip-read/blob/master/other/cake_pattern.md)*, 2017