Speaking for my self so these thoughts may not apply for everyone else. 

whenever I am learning new language, I like to practice as much as possible, and by practice, I mean, create my own code files from scratch and start with small coding examples. However, in learning journey, when I tried to create my own file and write some practice code, I had no idea how to compile that code (the new file). took me two days (and help from apaj) to figure out that I also have to write the test harness and add some description to "Launcher.scala" in test/scala sub folders. And when I created my own file, I just wanted to compile for syntax errors. 

---

`apaj` on 9/5/2018: _This is a good point, I will embed it in the Learning Journey. On the other side, please don't spend two days on anything - post questions after a few trials and making sure no documentation on the topic exists._

---

Coding requires practice. First I want to become comfortable with the language syntax. This part is where I have a compiler in my head and code on piece of paper (no computer / IDEs involved). For this, learning journey is a good start, however,  to become comfortable with syntax, a working IDE is really helpful, so I would stress more on Intelij / Eclipse.

---

`apaj` on 9/5/2018: _We tried this back in the time. It simply wouldn't work - those IDEs were far to slow and to buggy to make it work. Please read the [[Chisel development tools]] page._

---

There are some miscellaneous questions:

1. Is there some starting point of code in chisel? like main in C++ / top module in verilog. 

---

`apaj` on 9/5/2018: _That depends on the project structure, I guess - but it comes down to the module that instantiates all the others, I guess again._

---

2. The package is the name of the folder where all your scala files reside?

---

`apaj` on 9/5/2018: _Not strictly speaking, but it is a good practice advised by Chisel creators to name your package in accordance with the name of the directory you are in_

---

3. All your code files must reside in src/main/scala?

---

`apaj` on 9/5/2018: _Again, not sure if they *must*, but it is something _strongly_ advised, as Chisel is, after all, a Scala library._

---

4. what is the meaning of test:runMain. Starting from an empty directory, how should I just write a simple OR gate in 
   chisel and compile it?

---

`apaj` on 9/5/2018: _It's not that simple - you need the project infrastructure. Scala, again, sorry. The simplest way would be to do it within the tutorials (like you did using `Launcher.scala`). However, if you really want to start from scratch, please use the [chisel template](https://github.com/freechipsproject/chisel-template) that the Chisel guys put together._

---

5. What is build.sbt file. Do you have to manually create it or is it automatically created for you? 

---

`apaj` on 9/5/2018: _It probably does get created if you use IntelliJ or some other IDE. However, the plain approach we are using won't do it. The template mentioned above should take care of this, also._

---