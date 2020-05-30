
This is based on the [Main Diplomacy Page](http://www.lowrisc.org/docs/diplomacy/)

There is a Carrv paper that adds more high level philosophy and internal design (https://carrv.github.io/2017/papers/cook-diplomacy-carrv2017.pdf)

### Intro

Diplomacy is for top level interconnect of the major pieces of a chip, usually with TileLink or AXI buses.

In Diplomacy, the main thing you see as a user is the [[Cake pattern]], with outer twin, inner twin, and bundle -- the part of Diplomacy that calculates parameter values is hidden.  When modifying the circuits in Rocket Chip, the main thing to be careful about is to trace out the "cake" graph of [mixin](https://github.com/librecores/riscv-sodor/wiki/trait%2C-with%2C-Mixins)s.  As a modifier of the circuit, you'll most likely want to maintain the current interfaces to TileLink2 and AXI, which is the point at which Diplomacy comes into play.  You can keep an eye out for Diplomacy specific naming conventions and patterns, to find those within the cake iheritance graph.

### Background

Diplomacy is a parameter negotiation framework for on-chip interconnect.  Given a description of sets of inter-connected master and slave devices, and a bus protocol template, Diplomacy cross-checks the requirements of all connected devices, negotiates free parameters, and supplies final parameter bindings to adapters and endpoints.  Beyond confirming the mutual compatibility of the system endpoints, Diplomacy enables them to specialize themselves based on knowledge of the other endpoints included in a particular system.

TileLink is a highly-parameterized chip-scale shared-memory interconnect protocol.  The protocol is hierarchically composable and guaranteed to be deadlock-free at the transaction level. In Rocket Chip, the implementation of TileLink exploits Diplomacy to allowing mixing of protocols via a single pattern, while also allowing specializing to the capabilities of the devices being connected.

### Diplomacy

Diplomacy extends Chisel to provide a parameter negotiation framework for generating an interconnection network. The SoC interconnect is expressed as a directed graph of nodes.  Diplomacy checks requirements for each of the communication protocols.  It also allows negotiation of free parameters, such as particular data or control wire widths.

After parameter negotiation, Diplomacy supplies concrete parameter values to individual adapters and endpoints.  The end points then generate their final concrete circuit based on those parameter values.  Diplomacy thus performs two-phase hardware elaboration. The first phase is parameter negotiation, wherein the topology of the graph is discovered and the nodes negotiate the values of the parameters. The second phase is concrete module generation, in which the Chisel compiler is invoked on the module hierarchy. As each Chisel module is elaborated, it can make use of the diplomacy provided parameters.

#### A Diplomacy graph consists of:
1. **nodes** - points in the design where diplomatic parameters are used to generate circuits. A node may have multiple edges and so one node may connect to multiple other nodes.  A node may have a single type of interface (an *endpoint* node), such as TileLink2, or it may forward from one type to the other (an *adapter* node), such as forwarding between TileLink2 and AXI;
1. **edges** - an edge has a direction and connects two nodes.  The node that sends signals is the master, while the node that receives is the slave. Other terms are *source* for the master interface and *sink* for the slave interface. During elaboration, an edge communicates a protocol-determined set of parameters between master and slave. The parameters can flow *outward* (from master to slave), or *inward* (from slave to master).  An edge elaborates the wires of the physical connection in the generated circuit, in the form of Chisel wires or module IOs. A module may contain multiple nodes and a node may have multiple edges.

During parameter exchange, each end point node learns about the other nodes it is connected to, and can specialize itself based on knowledge of the other endpoints.  The circuit generator associated with a given node receives a set of parameter values, which it then uses during generation of the circuit.  The circuit can specialize in ways such as matching the number of internal elements to the number of wires coming in, or choose one particular option from among a variety available, and so on.

For an example Rocket Chip design's diplomatic graph and further explanations, see the slides given in [1].

Although it should not be needed, those curious can see the implementation the Diplomacy package within `rocket-chip` [here](https://github.com/freechipsproject/rocket-chip/blob/master/src/main/scala/diplomacy).

Further details in the context of LowRISC may be found in [3].

### TileLink

TileLink is a chip-scale interconnect protocol standard for providing multiple processing elements with coherent access to shared memory and other memory-mapped devices. Specifically, TileLink is designed to be deployed in a System-on-Chip (SoC) to connect general-purpose multiprocessors, co-processors, accelerators, caches, DMA engines, memory controller, and simple or complex peripheral devices. The protocol is optimized to be efficient when deployed within tightly-coupled, low-latency SoC buses. It can also be implemented over hierarchically-composable, point-to-point networks, and can scale down to interface with low-throughput slave devices or scale up to provide high-throughput interconnects. TileLink provides coherent access for an arbitrary mix of caching or non-caching masters to a physically-addressed, shared-memory
system. Cache coherence is maintained by a customizable, MOESI-equivalent protocol based on hierarchical composition. The protocol supports out-of-order transaction completion to improve throughput for concurrent operations. Protocol can be implemented as either update or invalidation based. Rocket-Chip currently uses an invalidation based coherence protocol.

The TileLink specification maps closely onto the abstractions used by Diplomacy. TileLink **agents** are semantically equivalent to diplomatic graph **nodes**. TileLink **links** are semantically equivalent to diplomatic graph **edges**.

Within a link, TileLink contains five logical channels, which correspond to the priorities of the messages that they carry. Each channel consists of control and data signals that are transmitted using a decoupled, ready-valid based handshaking protocol. TileLink memory operations comprise a series of messages sent over channels, obeying certain transaction rules (e.g. all requests have responses). To avoid deadlock, TileLink specifies a priority amongst the channels’ messages that must be strictly enforced. The prioritization of messages across channels is A « B « C « D « E, in order of increasing priority. Channels are directional, in that each passes messages either from master to slave interface or from slave to master interface.

TileLink provides formally-verifiable deadlock freedom for any SoC consisting of compliant network and endpoint implementations.

## Diplomacy in RocketTile

Rocket tile interface nodes: [tile/RocketTile.scala:43](https://github.com/freechipsproject/rocket-chip/blob/20a88768560ca810c0dbe79f1a33604268d921c7/src/main/scala/tile/RocketTile.scala#L43)

RocketTile is a LazyModule, the actual implementation is instantiated [tile/RocketTile.scala:95](https://github.com/freechipsproject/rocket-chip/blob/20a88768560ca810c0dbe79f1a33604268d921c7/src/main/scala/tile/RocketTile.scala#L95)

Core instantiation: [tile/RocketTile.scala:103](https://github.com/freechipsproject/rocket-chip/blob/20a88768560ca810c0dbe79f1a33604268d921c7/src/main/scala/tile/RocketTile.scala#L103)

In Diplomacy language, the RocketTile is a node.  In TileLink language RocketTile is also an agent (a TileLink agent is semantically equivalent to a Diplomacy graph node).  A Diplomacy node may contain many different directed links to other nodes.  In Diplomacy, those links are called edges, in TileLink, they're called links. 

Inside the definition of the RocketTile class, you can see that several Node val are created inside the class:
https://github.com/freechipsproject/rocket-chip/blob/20a88768560ca810c0dbe79f1a33604268d921c7/src/main/scala/tile/RocketTile.scala#L43

Additional simple example may be found in [3].

### References

1. Henry Cook, Wesley Terpstra, and Yunsup Lee. 2017. *[Diplomatic Design Patterns: A TileLink Case Study](https://carrv.github.io/2017/slides/cook-diplomacy-carrv2017-slides.pdf)*. In Proceedings of First Workshop on Computer Architecture Research with RISC-V, Boston, MA USA, October 2017 (CARRV’17), 7 pages
2. Wei Song, *[Diplomacy and TileLink from the Rocket Chip](http://www.lowrisc.org/docs/diplomacy/)*, lowRISC website, 2018
3. Edmond Cote, *[Parameters and lazy modules](http://blog.edmondcote.com/2017/04/parameters-and-lazy-modules.html)*, Edmond Cote's Blog, 2017
