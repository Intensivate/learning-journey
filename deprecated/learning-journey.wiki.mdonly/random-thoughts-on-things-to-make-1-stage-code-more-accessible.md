In the top level repository function of following directories emulator, project, riscv-fesvr, sbt, src, vsrc..  that can be overwhelming when coming to it fresh, from nothing.

Legacy stuff that's pulled over from Rocket, already in the 1 stage [rv32_1stage/tile](https://github.com/librecores/riscv-sodor/blob/master/src/rv32_1stage/tile.scala) the tile is something that it's natural.. it's an artifact of Rocket that leaked into Sodor..

Coming to "new", it's like "what's this?  why's it here?  What's it's function?"
Learning journey page about how decode is done: [rv32_1stage/cpath](https://github.com/librecores/riscv-sodor/blob/master/src/rv32_1stage/cpath.scala)

The code constructs used make this elegant, but need explanation of how it works [MuxLookup in cpath](https://github.com/librecores/riscv-sodor/blob/f961525baf6b8a70375d4637ec3ce0856fd8ebfb/src/rv32_1stage/cpath.scala#L47) the lookup table

The 1 stage is a good place to build a 2nd tier in the learning journey.  The Journey has hands-on for muxes. Then the 1-stage uses muxes in "real" ways.  For example: [rv32_1stage/cpath.scala#L117](https://github.com/librecores/riscv-sodor/blob/f961525baf6b8a70375d4637ec3ce0856fd8ebfb/src/rv32_1stage/cpath.scala#L117)

That's a more advanced use of muxes -- if we supplied a diagram showing the circuit this turns into, it may be a good learning opportunity.

Also, this line would be good to illustrate a real use of "::" in scala: [rv32_1stage/cpath.scala#L112](https://github.com/librecores/riscv-sodor/blob/f961525baf6b8a70375d4637ec3ce0856fd8ebfb/src/rv32_1stage/cpath.scala#L112)

Last thing is that pictures will help a lot, for newbies -- being able to see what the code turns into.
We could provide a branch that is free from complexities in top.scala and tile.scala, and just has cpath and dpath, plus flat memory..?  For example this code is a bit alien: [rv32_1stage/top.scala#L30](https://github.com/librecores/riscv-sodor/blob/f961525baf6b8a70375d4637ec3ce0856fd8ebfb/src/rv32_1stage/top.scala#L30)

And the implicit configuration is an advanced code feature: [rv32_1stage/top.scala#L25](https://github.com/librecores/riscv-sodor/blob/f961525baf6b8a70375d4637ec3ce0856fd8ebfb/src/rv32_1stage/top.scala#L25)

The imports could use some comments to explain, or point to wiki page: [rv32_1stage/top.scala#L10](https://github.com/librecores/riscv-sodor/blob/f961525baf6b8a70375d4637ec3ce0856fd8ebfb/src/rv32_1stage/top.scala#L10)

The def of "core" could use more explanation, or be simplified: [rv32_1stage/core.scala#L32](https://github.com/librecores/riscv-sodor/blob/f961525baf6b8a70375d4637ec3ce0856fd8ebfb/src/rv32_1stage/core.scala#L32) as it is..  looking first time..  it's like "this doesn't _do_ anything!"

This debug thing maybe could use more explanation: [rv32_1stage/dpath.scala#L90](https://github.com/librecores/riscv-sodor/blob/f961525baf6b8a70375d4637ec3ce0856fd8ebfb/src/rv32_1stage/dpath.scala#L90) and [rv32_1stage/dpath.scala#L194](https://github.com/librecores/riscv-sodor/blob/f961525baf6b8a70375d4637ec3ce0856fd8ebfb/src/rv32_1stage/dpath.scala#L194)