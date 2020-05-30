
Because Chisel is based on a general purpose programming language (Scala), many code bases use Object Oriented development style. For example, Rocket Code relies on very deep inheritance hierarchies.  As such, this kind of code style can be cumbersome to navigate using simple tools like `vi` and `grep`. Even though directions on using both [IntelliJ](https://github.com/librecores/riscv-sodor/wiki/Chisel-development-tools/#intellij) and [Netbeans](https://github.com/librecores/riscv-sodor/wiki/Chisel-development-tools/#netbeans) are given below, our current weapon of choice is [Sublime](https://github.com/librecores/riscv-sodor/wiki/Chisel-development-tools/#sublime), so please take a moment to learn about it so you can use it in future projects.

### Sublime

[Sublime](https://www.sublimetext.com/) is a text editor, not an IDE - so it does not allow direct control of compilation, simulation, running the tests; however, it provides a great tracking feature useful for large codebase projects, such as RocketChip, thus enabling easier understanding and changing of the code. It is far more lightweight than either IntelliJ or Netbeans and it does not require any kind of plugins, i.e. it works off-the-shelf. In order to have a Chisel project up and running in Sublime, just take care to use the following option `File -> Open Folder` leading it to the folder you consider root for your project. That does not have to be the actual root directory of the SBT project, it can be only a subfolder.

For example, if you are making changes exclusively on the `rocket` part of the `rocket-chip` repo, just `Open Folder` and lead it to the `rocket` directory. It will list all the files found in `rocket` and its subfolders on the left pane and it will easily show all interconnections between the item definitions and their usage, as long as those are within the `rocket`.

Recommendation: for faster jumping to code definitions, it is recommended to set up CTRL + left_click for this. The description how to configure Sublime is here: https://stackoverflow.com/questions/16235706/sublime-3-set-key-map-for-function-goto-definition

#### Example Usage

To demonstrate how lightweight and easy to use Sublime is, please open the diagram representing the [Cake Pattern in Rocket Chip](https://github.com/apaj/learning-journey/raw/master/doc/diagrams/Cake%20Pattern%20in%20Rocket.pdf).

The diagram contains a brief introduction to the concept of the [[Cake Pattern]], but please do not spend much time on the text at this very moment - rather, focus on the map of a part of Rocket Chip represented by the block schematic. To start navigating through the code following the map, open Sublime, clik `File`, `Open Folder` and lead it to the `rocket` folder.

Now, double click on either of the files within the `rocket` folder opens it up. The top entity in this case is found in `RocketTile.scala`, so please double click that file. That also represents the starting point on our map.

Use the mouse wheel to navigate the file. Next, use `Ctrl+F` to find the class called `RocketTile`, which represents the outer twin (more on that in [[Cake Pattern]]). Leaving the inner/outer theory aside for now, let's focus on the line found:

```
class RocketTile(val rocketParams: RocketTileParams, val hartid: Int)(implicit p: Parameters) extends BaseTile(rocketParams)(p)
    with HasLazyRoCC
    with CanHaveScratchpad {
```

In order to see what does it actually mean that `RocketTile` extends `BaseTile`, go ahead and hover the mouse pointer over `BaseTile`. Information on the line and file where `BaseTile` is defined will show up in the form of a link - click the link. Sublime will open up that file in a new tab. The same effect may be achieved if the shortcut key is set as described above and then you click the `BaseTile` while holding `Ctrl`. 

Go ahead and test this feature by checking where exactly do `HasLazyRoCC` and `CanHaveScratchpad` come from. Please do follow our [map](https://github.com/apaj/learning-journey/raw/master/doc/diagrams/Cake%20Pattern%20in%20Rocket.pdf) all the way around and let us know if you find any mistakes.

### Netbeans
This tool is not recommended because of its slow speed when used for bigger projects.
Recommended tool: [Sublime](https://github.com/librecores/riscv-sodor/wiki/Chisel-development-tools/#sublime).

One free tool that many have used successfully is [[Netbeans|https://netbeans.org/downloads/]].  For Chisel, you only need the "Java SE" version.  Then add the Scala Plugin: [[Scala plugin blog|https://blogs.oracle.com/geertjan/10-steps-to-happiness-with-scala-and-netbeans-ide]] and the [[Scala plugin wiki|http://wiki.netbeans.org/Scala]] 

Download Netbeans version 8.1 (8.2 is not supported by the Scala Plugin at the moment!).
Once you install the Netbeans, you can download the plugin from this page: http://plugins.netbeans.org/plugin/67889/nbscala-1-8-1-2

Just click on the Download button and you will download the plugin in zip file. Unzip it and open Netlist. Click Tools -> Plugins -> Downloaded -> select all .nbm files that were stored in the downloaded zip file.

### IntelliJ
Perhaps the most loved and arguably most productive tool out there is IntelliJ from JetBrains.  [[IntelliJ wiki|https://en.wikipedia.org/wiki/IntelliJ_IDEA]] and the [[IntelliJ download page|https://www.jetbrains.com/idea/]]
