[<<< Previous Page](Big-Picture) (The Chisel Big Picture)  [Next Page >> ](Chisel-Introductory-Examples) (Chisel Introductory Examples)


**CHISEL2 NOTE**: This and all other parts of the Learning Journey are concerned with Chisel 3 - the current version of the language. However, if you are for some reason **interested in the deprecated Chisel 2 version**, we've set up a page collecting the appropriate documentation [here](Chisel2).
***
The Learning Journey has a curated environment created inside a Virtual Machine, which avoids tool issues:  [[Chisel Environment]].

Go there, perform setup, and then at the end of that page it brings you back here, to continue, with running the script (below), which installs all the tools needed, and clones the tutorial examples into the environment. 

This is the step that encounters the most issues.  Here is a page that lists [[issues seen|Issues with setup of Chisel]] so far and the solutions.

### Tools to use with Chisel
The [[Chisel development tools]] page has tools that increase coding efficiency.  (If learning them is a barrier, it is no problem to wait until later, and make rapid progress now with simple text editor and command line, however for the coding style used in Rocket Chip, these make a dramatic difference).

If you have no prior experience with `git` please take a moment to familiarize yourself with the standard means of version control in the RISCV world. The resources on `git` are omnipresent, but probably the best place to start is: [[https://git-scm.com/doc]]
There are short video tutorials great for beginners, there is also a book and whole bunch of links and fora, but we advise the following approach.
1. watch only the basic videos and then skip everything else;
2. namely, it turns out `git` is sooo vast, so enormous, it's simply impossible to remember all those things... not even the principles, let alone the commands;
3. then use StackOverflow or similar q&a forum where specific cases/issues are explained as they arise - that way you learn by practice, hands-on - which aligns with the Learning Journey philosophy;
4. finally, please bring back to [[User Experiences]] the most interesting and the most useful tricks, for quicker access.

One obvious thing that we would still like to point out, as it is very useful, is the _Search_ feature within the GitHub wiki. Just look at the top of your browser, the first black row - there is a textbox saying: _Search or jump to..._ If you ever find yourself in a situation that you are looking for something inside the Learning Journey, please type the keyword there and press `Enter`. Then, when you get the responses on a new page, select `Wiki` - that will narrow down the search to the Learning Journey, excluding the actual `riscv-sodor` repo. This is a good precaution whenever you feel like another page should be created - please check if somebody already did it. To test this feature, please go ahead and type `precaution` up there and search. Then select Wiki and click on the result returned. That should bring you back here.

Once you are back, please continue on.

### Dependencies and Tutorial Set-up

We provide a script that installs the tools needed for the Journey, setting up everything you need for the rest of the journey. 

The script is written for the [[Chisel Environment]].  However, if you would like to go it on your own with a different environment, it _should_ work on any other Ubuntu-like OS. (It is provided without a warranty with no responsibility by the author nor the provider for any harm caused.  The VM environment provides isolated environment.)

To get the script, please clone the following repository:

```
git clone https://github.com/apaj/learning-journey.git
```

Enter the downloaded directory and run the script as follows:

```shell
cd learning-journey
sudo ./set-learning-journey.sh
```

It will ask at each step before it performs an action.

**Note**: The script is still in development, so please do report back any bugs to the e-mail given in the header of the script.

### Hello World

Now we get to run our first Chisel program :-) 

Go to the directory `learning-journey`:

```scala
$ sbt run
```

This runs the _Chisel_ program, which computes a verilog circuit as its result.  When run, it first downloads the appropriate versions of Chisel3 and Scala and caches them (usually in `~/.ivy2)`.  Then it generates the `(Hello)` Verilog circuit.  Then it runs a test harness that stimulates the circuit, checks the circuit output, and reports whether that output matches expected output.  The hello circuit always outputs the number 42 (its value in hexadecimal being 0x2a). You should see `[success]` on the last line of output (from `sbt`) and `PASSED` on the line before indicating the block passed the testcase, as follows:

```
[info] Running hello.Hello 
[info] [0.011] Elaborating design...
[info] [0.270] Done elaborating.
Total FIRRTL Compile Time: 2238.9 ms
Total FIRRTL Compile Time: 27.6 ms
End of dependency graph
Circuit state created
[info] [0.010] SEED 1514026331022
test Hello Success: 1 tests passed in 6 cycles taking 0.036811 seconds
[info] [0.017] RAN 1 CYCLES PASSED
[success] Total time: 90 s, completed Dec 23, 2017 11:52:15 AM
```
What this did was first run the Chisel program "Hello", which generated a circuit in Chisel's intermediate format, which is called FIRRTL (the Chisel code also printed "Hello" to let us know it ran).  Then this compiled the FIRRTL representation into Verilog format.  Then it ran the Verilator tool to compile the Verilog format into C++ format.  Then it ran the C++ code, which simulated a simple circuit, inside of a test harness, which verified that the output of the circuit was, indeed, 42, then the test harness indicated success.

Congratulations, you've successfully run your first Chisel program, which generated Verilog code and simulated it with Verilator.

[<<< Previous Page](Big-Picture) (The Chisel Big Picture)  [Next Page >> ](Chisel-Introductory-Examples) (Chisel Introductory Examples)

***

It is certainly possible to to follow the Chisel Learning Journey within a different environment.  However, you will be on your own for any issues encountered.  If you would like to try on your own, without support, instructions to install Chisel3 are provided at the the [original repo](https://github.com/freechipsproject/chisel3#installation)<sup>[1](#footnote1)</sup>.

<a name="footnote1">1</a>) Don't get confused because those instructions mention Firrtl - as you will be building from the release branch of Chisel3, the required `jar` is taken care of by `sbt`, so just ignore that part for now.