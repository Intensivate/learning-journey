## Rocket Chip Code Techniques

Rocket Chip code employs some advanced programming techniques.  The goal of the techniques is to implement a powerful parameterization system, that can easily change sizes of buses, change number of connections going to or from circuit elements, and so forth.

The team behind Rocket Chip chose to do this with four inter-related code patterns:
* [Mixins](https://github.com/librecores/riscv-sodor/wiki/trait%2C-with%2C-Mixins)
* [[LazyModule]]
* [[Cake pattern]]
* [[Diplomacy]] 

So, [Mixins](https://github.com/librecores/riscv-sodor/wiki/trait%2C-with%2C-Mixins) and [[LazyModule]] are base mechanics.  Then Cake Pattern is a layer above those two.  The Cake Pattern is how code gets connected together -- it is like plumbing in the code space.  The way that a circuit ends up wired to another circuit, involves using the Cake pattern.  It can be used on its own, but is required (I believe) for Diplomacy.
Lastly, Diplomacy is a massive package of infrastructure, which is all about TileLink -- it’s the way that buses get interconnected in the on-chip network -- so connecting core to memory controller, and so on.

I'll give a brief explanation of each of these, then show how they interrelate.  The individual pages are available for reference and more detail.

### Mixins and Lazy Module

A “mixin” is a form of inheritance -- it’s how you get multiple inheritance in Scala.  To create a mixin, use the "extends" keyword for the first thing to inherit, and then use “with” keyword to inherit more things.  However, there's a special form of class, called a "trait", whose constructor takes no parameters -- "with" can only be used to inherit "trait"s.

Next, LazyModule is a pattern that is tightly coupled to the Cake pattern.  The basic idea of LazyModule is that you don’t actually create the instance until the instance is _used_.  So, that gives you time to _calculate_ the parameters to pass.  In other words, inside a class’s constructor, call it Class A, you can say: ```val foo = LazyModule( some_params )``` and this will _only_ create a place holder.  Later, when you try to read the value of  foo or wire foo to something else, that is when the constructor is called that actually creates foo.
Like this:
```
class A( paramsA ) 
{  ...
   Lazy val foo = new Circuit( paramsA )
   ...
}
```
Then somewhere else: ```val bar = new A( someParams )```  And then, later, do ```bar.foo := ```  to wire up the foo.  That foo was lazy..  So, it is only at the point that wiring happens that the constructor for foo is called.  So, it is only at the point of wiring it up that the parameters to foo are accessed..  Which means you have all the time between constructing bar and accessing foo to calculate the actual parameters that you want to pass in to foo!   The construction and wiring can be in widely separated places, with a lot of stuff happening between.

### [[Cake pattern]]
So, now, that is the basis underneath [[Cake pattern]].  What Cake does is make “twins” -- an outer twin and an inner twin..  In this (June 2017) version there is also a “bundle”

The twins are [[LazyModule]]s that are used in inheritance graph.  The inheritance graph is the mixin mechanism.

### Wiring circuits together within Cake pattern
When wiring things up, wiring is done as part of the inner twin.  But the interface is part of the outer twin.  So inside the inner twin you'll see things like `outer.foobar`. The “outer” is a standard part of the pattern, and is always passed in to the inner twin.  So every inner twin has an `outer` val that references the outer twin and give access to all the values an interface of the outer twin.  The details about bus widths, and so forth can be calculated (by [[Diplomacy]]) before performing the actual wiring operation and generating the nodes in the FIRRTL graph.

There will be things referenced in the inner twin that are not values present in the inner or outer twin.  For example, `frontend` is referenced in the RocketTile inner twin.  But it doesn’t appear in RocketTile class, which is the outer twin.  That means it is mixed in, through cake pattern.  The way to find it quickly is to glance at the Cake Mixin Map.  Somewhere is a single-walled rounded corner box titled `frontend` which indicates the file where “val fronted = “ is present.
