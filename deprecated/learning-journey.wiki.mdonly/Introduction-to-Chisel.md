## First Pass at Chisel 

Chisel is a hardware construction language embedded in the high-level programming language Scala. Chisel extends Scala with new syntax, special class definitions, predefined objects, and usage conventions. So, the code we write at Intensivate mixes Scala with Chisel, often intermixing the two within the same line of code.

This page is intended to provide a quick introduction to the details of Chisel.  

For Big Picture and reason why Chisel, see the [Big Picture page](https://github.com/librecores/riscv-sodor/wiki/Big-Picture). 

For hands on examples of writing chisel, generating a simulator to see it run, and so forth, see the [Hands On](https://github.com/librecores/riscv-sodor/wiki/Chisel-hands-on) page.

For advanced examples of individual lines of Chisel taken from Rocket Chip code, see [Examples Page](https://github.com/librecores/riscv-sodor/wiki/Advanced-Examples-of-Using-Chisel).  It is recommended to start with [big picture](https://github.com/librecores/riscv-sodor/wiki/Big-Picture) then come here for details.  

This page covers:

1. Base Scala syntax
1. Base Chisel syntax

As of Aug 2017, we just point to other documents.  

### Please just skim these quickly.  

The real learning will happen in the [Hands On](https://github.com/librecores/riscv-sodor/wiki/Chisel-hands-on) page.  Just take an hour or two to skim these documents, which provide the syntax of Chisel.  Then move quickly to the Hands On, where you will compile and run your first Chisel program, and then work through a number of simple Chisel examples.  Use the documents linked in this page as reference to complete the hands on problems.


## Documents that introduce Scala 

* [Chisel 3 wiki's Scala page](https://github.com/freechipsproject/chisel3/wiki/Scala-Things-You-Should-Know)

* [intro slides](https://www.slideshare.net/al3x/why-scala-presentation?next_slideshow=1) Just flip through these, maybe 5 to 10 minutes -- you'll see terms used when talking about Scala, and you'll see language constructs.  This just exposes you to the words and syntax that the other documents talk about.

* [Scala Hands On](http://www.scala-lang.org/docu/files/ScalaTutorial.pdf) Spend maybe 30min on this.  

* [Use this page](https://www.tutorialspoint.com/scala/index.htm) to enter Scala code and run it.


## Documents that introduce Chisel syntax 

The following two documents would give some introduction about Chisel and it's usage. **Please do not spend much time reading these documents** as these are meant to just introduce you to Chisel and answer some of the early question you might get.  Rather, use these as reference documents when doing the [hands on examples](https://github.com/librecores/riscv-sodor/wiki/Chisel-hands-on)

2. [Chisel 2012 DAC Paper](https://bytebucket.org/intensivate/developer_resources/wiki/docs/chisel-dac2012.pdf?token=8db2611587b9516a6e6e665b261f07014bcfbb65&rev=3276fb9be43e8fdcf0bba9239ecb7300beacffad) Just spend 20 to 30 minutes on this.  Skim it.  This gives you a preview of advanced concepts that Chisel has to offer.  Don't try to understand any of the code.  It is all too advanced for right now.  We bring this paper back in the advanced examples, after you've completed the hands on.

1. [Chisel cheatsheet](https://bytebucket.org/intensivate/developer_resources/wiki/docs/chisel-cheatsheet.pdf?token=2b8bf6e4daeeaf58de92e2eebc3a639f2ea9a863&rev=28c8a1d8563ec7bb5d435ffcc59eb058837b0d28) Just spend 10 to 20 minutes on this, just to see what's there.  You may use this while doing the hands-on problems 

## Introductory Examples of Chisel Code 

Now you are ready for the [next step in the learning Journey](https://github.com/librecores/riscv-sodor/wiki/Chisel-hands-on)