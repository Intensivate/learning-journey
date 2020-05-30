
The big picture, ideal is to have a number of pages 

1) , the first page gets the bare basics explained, with a hello world that the use writes.  Just the logistics of writing something and running it.  

For this, copy the [Tutorial page](https://github.com/librecores/riscv-sodor/wiki/Chisel-Tutorial-page)  cut the copy down to just the first part of chapter 2.  Up to until 2.2.  Add Hello World example (should be already out there, or just copy existing tutorial and modify, just have the user insert their name into the code, compile it, and run it.  Time is most important here.)

2) Then next page introduces base level concepts, such as Class, variables, wires, boolean logic.  Hands on exercises for those.  

Use the rest of Chapter 2 for this page -- for each example inside the copied chapter 2 text, there are a number of examples like "2.2.1 The Scala Node: Declaring Wires" add to this the path to a file that contains the code being discussed.  Also, provide a hands-on problem that exercises the understanding of that example.  Find the closes existing problem in one of the existing bootcamps or sets of problems (don't try to roll your own hands-on example!)

3)  Then the next page is about more complex stuff 

Start with extracting from the hands-on tutorial examples page that we already have, just fill it in by copy-pasting examples from other places -- minimum time spent on this.  Take from Chapter 4, take from other hands-on tutorials, and so on.  Done quickly.  No new pages created.  Just take existing stuff, copy-paste.  Get first pass done in 30 to 40min.  

4) The next page is about inter-mixing scala with Chisel -- the idea that wires get assigned to variables and that, in turn, triggers constructor for what that wire is attached to -- doing scala arrays that are filled via functional programming constructs and end up causing a bunch of constructors to fire, and how that translates into a circuit.  

This one is where we provide value-add.  We will need to put in some time, collaboratively, on this one.

5) Then a page on more advanced Chisel stuff -- like operators on lists -- things that come up in Rocket and Sodor code.  

This one we will need community help on.  Get skeletons of the preceeding pages up, then make this page.  Then put out a call on the Hangout for people to suggest things to go on this page.

6) then page on to the complicated scala things needed to understand Rocket code, like implicit parameters, the cake pattern, and similar stuff.

Same as previous page -- need community involvement.  Get the pages up, then ask for content for this page.

7) at the end, a collection of advanced examples that people have asked the Hangout about (we have this page already, just clean up the extra stuff at the end -- move it to the reference page already created)

This is catchall -- examples that aren't specifically supporting the previous two pages go here.