Now that the basics are covered, we take examples directly from Sodor and Rocket Chip code, and explain them, here.  This is a live wiki, continually expanding.  

## Full Chisel Book
[Chisel book online](https://github.com/schoeberl/chisel-book)

## Quick code testing
[jupyter notebook to quickly test Chisel code snippets](https://mybinder.org/v2/gh/freechipsproject/chisel-bootcamp/master)

## Advanced examples of chisel usage

These examples have been taken from Sodor and Rocket Chip code

Scala things that show up in Rocket Chip, Cake Pattern, and Diplomacy:
* [uses of '=>'](https://github.com/Intensivate/learning-journey/wiki/uses-of-right-arrow)
* [[Trait, Sealed, and Multiple Inheritance]]
* [[trait, with, Mixins]]
* [[case class]]
* [[final]]
* [[Option]]
* [[match and case]]
* [[map function]]
* [implicit parameters](https://docs.scala-lang.org/tour/implicit-parameters.html) (just link to Scala, needs page that explains more hands-on style)
* [uses of _](https://stackoverflow.com/questions/8000903/what-are-all-the-uses-of-an-underscore-in-scala)


Rocket Chip
* [Introduction to how to configure Rocket Chip](https://riscv.org/wp-content/uploads/2015/01/riscv-rocket-chip-tutorial-bootcamp-jan2015.pdf)
* [README of repository has directions to clone, install, build, and run rocket chip](https://github.com/freechipsproject/rocket-chip)
* [[Configuring RocketChip]]
* [[Rocket Chip APIs]]
* [[Introduction to Rocket Chip code style]]
* * [[LazyModule]]
* * [[Cake pattern]]
* * [[Diplomacy]]
* * [[TileLink]]
* * [[Parameterization]] as used in Sodor and Rocket Chip
* * [[Resource]] like `ResourceBinding` `ResourceMap` `ResourceString` aso

Some examples
* [[Tabulate Example]]
* [[Black Box Example]]
* [[Some Rocket Chip examples]]

Other stuff
* [Outside site with possibly helpful info about Rocket code](https://github.com/cnrv/rocket-chip-read)
* [[Chisel Versions]]
* [[Multiple clock domains in Chisel]]
* [[L2 cache project]]

Comments on [[challenges of Object Oriented style]] of writing hardware definitions

## Asking a question

There is a Hangout dedicated to this learning journey.  Each Saturday we make a point of being live, to ask and answer Chisel questions.  https://hangouts.google.com/?action=group&key=qOKQrvMjplnJAC133&pli=1

Alternatively you can join the gitter channel https://gitter.im/chisel-learn/Lobby

If you come across an example that you would like to ask about, do this:

1. Use the web interface to browse the code, to the line that you want to ask about.
2. move the mouse to the left, and click on the line number.  This will make a "..." appear.
3. use the mouse to expand the "..." and choose "copy permalink".
4. Decide whether you want to ask on the Hangout, or Stackoverflow, or just paste to the Suggestions page.
   1. if hangout, go to link above, and ask -- paste the permalink and maybe a snippet of the code if it's simple code
   2. If suggestions page, make a page for yourself (can make it "anonymous X" or use your name)  then paste question to the new page
   3. if stackoverflow, go to https://stackoverflow.com/ and click "ask question" then type title that gives a hint about the code
      1. In the body, say a bit about the context and paste the permalink.
      2. It is very important to add the right tags to the questions.  At the bottom of the quesiton, in the entry box, type "chisel" to add it, and then "riscv" to add that tag too.
      3. someone who monitors those tags will post an answer.  Alert us that the question has been asked, by pasting a link to the question on the [[suggestions for improvement to Learning Journey]] page.  Someone who supports this wiki will turn that answer into a new wiki page.


## Rocket Related Things
[[Glossary of terms used in Rocket code]]

## Chisel documents
As promised, here are those Chisel documents again. They should make more sense now that you've done some hands-on Chisel coding:
1. [Chisel 2012 DAC Paper](https://bytebucket.org/intensivate/developer_resources/wiki/docs/chisel-dac2012.pdf?token=8db2611587b9516a6e6e665b261f07014bcfbb65&rev=3276fb9be43e8fdcf0bba9239ecb7300beacffad) Still just skim this, but now some of the code in it should look familiar. The examples on this page will walk through the coding examples in the paper.

2. [Chisel3 cheatsheet](https://chisel.eecs.berkeley.edu/doc/chisel-cheatsheet3.pdf) This cheatsheet should start being useful now. Many of the constructs may still be too advanced, but by the end of these examples, you should have seen most of what's in the cheat sheet.

-- break down the line of code

Give examples of lines of code that have both chisel and scala constructs, and break the line down, going piece by piece, and say what scala construct that is, and give a link to a scala page that talks about that construct (NOT the scala manual! But one of the helpful tutorial pages that talks about that specific construct and related ones.. like the link I found around tabulate).

Maybe give some more information about how chisel code is almost 95% constructors -- running Chisel program pretty much all it does is go from constructor to constructor. And talk about how Chisel is for quick and dirty -- we won't use it for everything -- in places where we need to optimize the code, we'll use "black box" in Chisel, and write verilog for that critical piece, where we need control.

## More Complex Chisel
We have also provided some examples of more complex circuits `_chisel-tutorial/examples_`. You should take a look at the source and test them out:

`$ cd examples`
`$ make`

To know more about Chisel and get an in-depth analysis, you could quickly go through the [[Chisel Tutorial page]].

## Disorganized Collection of more stuff 
Once you have Scala working in that web environment, then if you are feeling confident, perhaps [[set up the Chisel environment]] (but don't do the examples yet!), and then do some of the scala tutorial examples inside Chisel tutorial environment (For example, just copy Mux2 to a new file, then add the Sala code to the file.

### Chisel Bootcamp

This bootcamp is an alternative that covers multiple aspects: introduction to Scala, basics of Chisel2, generating verilog, making a test harness, and so on: [bootcamp](https://bytebucket.org/intensivate/developer_resources/wiki/docs/bootcamp-20121026.pdf?token=465301eb0a6ac0f168aa5fb88a169c0eacc01318&rev=28c8a1d8563ec7bb5d435ffcc59eb058837b0d28)

--------------------------------------------------------------------------------------------------------------------------
### ToDo:

rearrange the rest of these links and text according to the above structure. Pick out the best links for getting a quick idea of Chisel syntax. Don't want hands on here yet. Just reference material on syntax -- the cheat sheet -- the white paper about Chisel

--------------------------------------------------------------------------------------------------------------------------
### Extra Information
1. There is a lot of content about Chisel available on the internet as well. Most of the information is captured in a [stackoverflow question](https://stackoverflow.com/questions/39265641/is-there-a-consolidated-list-of-documentation-about-chisel), the same is replicated here -

2. A Chisel 2 documentation page, including tutorial, manual, and cheat-sheet: https://chisel.eecs.berkeley.edu/documentation.html

3. Chisel 3 wiki: https://github.com/ucb-bar/chisel3/wiki

4.Style guide, which helps those new, like me, understand nuances and figure out how to do non-obvious things: https://github.com/ccelio/chisel-style-guide

5. This page has a list of repositories, including chisel example code, a skeleton for new projects, tutorials, documentation (see below): https://github.com/ucb-bar
6. The Chisel user group: https://groups.google.com/forum/#!forum/chisel-users
=====================================================

Some items extracted from the above ucb-bar page:

1. A skeleton for "clean" new Chisel projects: https://github.com/ucb-bar/pwm-chisel-example
2. Teaching processors (Simple RISC-V processors, as examples to learn Chisel): https://github.com/ucb-bar/riscv-sodor
3. wiki for teaching processors: https://github.com/ucb-bar/riscv-sodor/wiki
4. Test harnesses for testing Chisel modules -- [edit] warning: early version for Chisel3, which seems limited to non-interactive tests: https://github.com/ucb-bar/chisel-testers
5. Chisel tutorial: https://github.com/ucb-bar/chisel-tutorial
6. Chisel-doc repository, with its own makefile, that builds documentation: https://github.com/ucb-bar/chisel-doc
7. Chisel quick video tutorial https://www.youtube.com/watch?v=pfM1WUWbfBs&t=286s