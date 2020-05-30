
Shows parameterization system used in Sodor:

Sodor uses a global SodorConfiguration class for all cores not using the tilelink uncore to pass parameters around implicitly.

For example:
[top.scala](https://github.com/librecores/riscv-sodor/blob/83b21d49f7f6e147f951cf0cdcbb0b102b586b05/src/fpgatop/top.scala#L17) creates a new "case class" called MasterConfig, and then adds cases to it.  One case is ExtMem.  The ExtMem object is extracted from the MasterConfig case class by the syntax: MasterConfig.ExtMem

MasterConfig is sent implicitly:
[memtotl.scala](https://github.com/librecores/riscv-sodor/blob/83b21d49f7f6e147f951cf0cdcbb0b102b586b05/src/fpgatop/memtotl.scala#L26)
shows an implicit "p" of type Parameters: "(implicit p: Parameters)"

The "implicit" means you don't have to pass that parameter, instead the compiler searches the code, and assigns a value to the parameter that way.  It searches to find the first definition of an object with the same type as the parameter, and that's the one bound to the parameter.  "Implicit" means you pass the object just by defining the object someplace else in the code.

On line 28: 
class WithZynqAdapter extends Config((site, here, up) => {

and line 32:
  case ExtMem => MasterConfig(base= 0x10000000L, size= 0x10000000L, beatBytes= 4, idBits= 4)

An object of type Config is defined, so the implicit can now find this WithZynqAdapter object and match to it.  Inside this object, the case ExtMem is also defined.  It's given concrete values.  Now, when a class is declared whose constructor takes an implicit parameter, and that parameter's type matches to "Config" and that class declaration has "WithZynqAdapter" in its scope, then any objects created from that class will receive a WithZynqAdapter object, and can use "ExtMem" to extract "MasterConfig(base= 0x10000000L, size= 0x10000000L, beatBytes= 4, idBits= 4)" from that parameter.

### Example
As an example, consider the mem to tile link file:
[memtotl.scala](https://github.com/librecores/riscv-sodor/blob/83b21d49f7f6e147f951cf0cdcbb0b102b586b05/src/fpgatop/memtotl.scala#L26)

it defines a class whose objects take implicit parameters: "(implicit p: Parameters)"

[memtotl.scala](https://github.com/librecores/riscv-sodor/blob/83b21d49f7f6e147f951cf0cdcbb0b102b586b05/src/fpgatop/memtotl.scala#L37)

it extracts values via: "p(ExtMem).base.U"

where p picked up the object definition:
  "case ExtMem => MasterConfig(base= 0x10000000L, size= 0x10000000L, beatBytes= 4, idBits= 4)"

