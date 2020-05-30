Assumptions and conventions
---------------------------
* Data here reflects rocket chip as of [5d7a0e](https://github.com/freechipsproject/rocket-chip/tree/5d7a0e7c42278e523d68f980bf6d1c9674f6bdab) (dated 2018-04-23)
* All Scala sources are referenced relatively to `src/main/scala`
* Useful tools for reverse-engineering configuration process, within a custom config:
  * `println` statements
  * `new Exception("comment").printStackTrace()`
  * Overriding `toString` in existing classes
  * `???` statement to cause execution to fail

Information
-----------
* Default configs are located in `system/Configs.scala` (package `freechips.rocketchip.system`)
* Configs in `system/Configs.scala` are defined as **chained config** - concatenation of partial configs which are defined in `subsystem/Configs.scala`
* Usual approach is to define a partial config(s) and chain them together like this:
```scala
class MyCustomPartialConfig extends Config((site, here, up) => {
  case SomePredefinedConfigKey => ???
}

class MyCustomConfig extends Config(new MyCustomPartialConfig ++ new BaseConfig)
```
* These partial configs are defined as `(View, View, View) => PartialFunction[Any, Any]`
  * Left-hand side arguments are named as `site, here, up` and their meaning are as follows:

|Default name|Description|Runtime and usage details|
|-|-|-|
|site|entire config chain being used|For the example above that would be `MyCustomConfig` wrapped within `Config(ChainParameters(MyCustomConfig, EmptyParameters))`. It's usually used to fetch values of other keys.|
|here|current partial config within configuration chain|For the example above, the partial function from `MyCustomPartialConfig` will be taken and wrapped within `PartialParameters`. Note that `MyCustomPartialConfig` instance itself **will not be reachable**, only its partial function is being passed here|
|up|partial configs following current (and thus having lower priority) in configuration chain|Nested `ChainView`s of partial configs terminated with `TerminalView`. For the example above: `ChainView(BaseConfig, ChainView(EmptyParameters, TerminalView))`. It is usually used as `up(param, site)`, i.e. `site` is being passed explicitly|

* 
  * Right-hand side is a partial function from one of the pre-defined keys (which extends `Field[T]`) to a result of type `T`. Value is then retrieved from config via `params(Key)` (where `params` is a configuration instance)
* `make CONFIG=MyCustomConfig` within emulator folder will do a lookup for the `freechips.rocketchip.system.MyCustomConfig` (lookup is done by SBT). That config is NOT required to reside specifically in `system/Configs.scala` file.
* Rocket tiles are configured via `RocketTilesKey` and `RocketCrossingKey`. They both extends `Field[Seq[...]]` because each tile has its own config.
  * Actual layout of rocket tiles is done in `subsystem/RocketSubsystem.scala`, see `trait HasRocketTiles`.
  * Each of these config params has nested structure, so tweaking some embedded knob in partial config is done like this:
```scala
case RocketTilesKey => up(RocketTilesKey, site) map { r =>
  r.copy(dcache = r.dcache.map(_.copy(nSets = 100500)))
}
```