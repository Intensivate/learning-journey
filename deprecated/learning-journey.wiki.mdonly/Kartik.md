SBT should be able to connect when you run it - can you confirm that you have a successfully running internet connection from your virtual machine?  

In case that is confirmed, can you, please:
send me the output of "ls -la" command you run in "learning-journey" directory. Even better, actually, just repeat the question and post both the question and command output it to the Hangouts
make sure that you've set the VM as instructed here. Of course, if you meet any issues there, please do contact me. 
Also, I've updated the Wiki page to be more precise - it says now "learning-journey directory", instead of "chisel-tutorial directory".

Looking forward to hearing from you,
Aleksandar

On 5/15/2018 3:26 AM, Srinivas Kartik Angadi wrote:
Hello Aleks,

I am having trouble in installation process. According to the manual I need to run the following command:

$ sbt run

in chisel-directory: which is the same as learning-journey directory I cloned from git ?

ERROR MESSAGE:-------------------------------------------------------------------

Getting org.scala-sbt sbt 0.13.16  (this may take some time)...

:: problems summary ::
:::: WARNINGS
        module not found: org.scala-sbt#sbt;0.13.16

    ==== local: tried

      /home/srinivas/.ivy2/local/org.scala-sbt/sbt/0.13.16/ivys/ivy.xml

      -- artifact org.scala-sbt#sbt;0.13.16!sbt.jar:

      /home/srinivas/.ivy2/local/org.scala-sbt/sbt/0.13.16/jars/sbt.jar

    ==== local-preloaded-ivy: tried

      file:////home/srinivas/.sbt/preloaded/org.scala-sbt/sbt/0.13.16/ivys/ivy.xml

    ==== local-preloaded: tried

      file:////home/srinivas/.sbt/preloaded/org/scala-sbt/sbt/0.13.16/sbt-0.13.16.pom

      -- artifact org.scala-sbt#sbt;0.13.16!sbt.jar:

      file:////home/srinivas/.sbt/preloaded/org/scala-sbt/sbt/0.13.16/sbt-0.13.16.jar

    ==== Maven Central: tried

      https://repo1.maven.org/maven2/org/scala-sbt/sbt/0.13.16/sbt-0.13.16.pom

      -- artifact org.scala-sbt#sbt;0.13.16!sbt.jar:

      https://repo1.maven.org/maven2/org/scala-sbt/sbt/0.13.16/sbt-0.13.16.jar

    ==== sbt-maven-releases: tried

      https://repo.scala-sbt.org/scalasbt/maven-releases/org/scala-sbt/sbt/0.13.16/sbt-0.13.16.pom

      -- artifact org.scala-sbt#sbt;0.13.16!sbt.jar:

      https://repo.scala-sbt.org/scalasbt/maven-releases/org/scala-sbt/sbt/0.13.16/sbt-0.13.16.jar

    ==== sbt-maven-snapshots: tried

      https://repo.scala-sbt.org/scalasbt/maven-snapshots/org/scala-sbt/sbt/0.13.16/sbt-0.13.16.pom

      -- artifact org.scala-sbt#sbt;0.13.16!sbt.jar:

      https://repo.scala-sbt.org/scalasbt/maven-snapshots/org/scala-sbt/sbt/0.13.16/sbt-0.13.16.jar

    ==== typesafe-ivy-releases: tried

      https://repo.typesafe.com/typesafe/ivy-releases/org.scala-sbt/sbt/0.13.16/ivys/ivy.xml

    ==== sbt-ivy-snapshots: tried

      https://repo.scala-sbt.org/scalasbt/ivy-snapshots/org.scala-sbt/sbt/0.13.16/ivys/ivy.xml

        ::::::::::::::::::::::::::::::::::::::::::::::

        ::          UNRESOLVED DEPENDENCIES         ::

        ::::::::::::::::::::::::::::::::::::::::::::::

        :: org.scala-sbt#sbt;0.13.16: not found

        ::::::::::::::::::::::::::::::::::::::::::::::


:::: ERRORS
    Server access Error: java.lang.RuntimeException: Unexpected error: java.security.InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty url=https://repo1.maven.org/maven2/org/scala-sbt/sbt/0.13.16/sbt-0.13.16.pom

    Server access Error: java.lang.RuntimeException: Unexpected error: java.security.InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty url=https://repo1.maven.org/maven2/org/scala-sbt/sbt/0.13.16/sbt-0.13.16.jar

    Server access Error: java.lang.RuntimeException: Unexpected error: java.security.InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty url=https://repo.scala-sbt.org/scalasbt/maven-releases/org/scala-sbt/sbt/0.13.16/sbt-0.13.16.pom

    Server access Error: java.lang.RuntimeException: Unexpected error: java.security.InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty url=https://repo.scala-sbt.org/scalasbt/maven-releases/org/scala-sbt/sbt/0.13.16/sbt-0.13.16.jar

    Server access Error: java.lang.RuntimeException: Unexpected error: java.security.InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty url=https://repo.scala-sbt.org/scalasbt/maven-snapshots/org/scala-sbt/sbt/0.13.16/sbt-0.13.16.pom

    Server access Error: java.lang.RuntimeException: Unexpected error: java.security.InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty url=https://repo.scala-sbt.org/scalasbt/maven-snapshots/org/scala-sbt/sbt/0.13.16/sbt-0.13.16.jar

    Server access Error: java.lang.RuntimeException: Unexpected error: java.security.InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty url=https://repo.typesafe.com/typesafe/ivy-releases/org.scala-sbt/sbt/0.13.16/ivys/ivy.xml

    Server access Error: java.lang.RuntimeException: Unexpected error: java.security.InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty url=https://repo.scala-sbt.org/scalasbt/ivy-snapshots/org.scala-sbt/sbt/0.13.16/ivys/ivy.xml`

---------------------------------------
On 05/16/2018/ Srinivas Kartik Angadi wrote:
Hello,

Output of command: `ls -la`

--------------------------------------------------------
```
`srinivas@srinivas:~/learning-journey$ ls -la
total 104
drwxr-xr-x  9 srinivas srinivas  4096 May 15 15:38 .
drwxr-xr-x 21 srinivas srinivas  4096 May 15 15:10 ..
-rw-r--r--  1 srinivas srinivas  2044 May 14 00:17 build.sbt
drwxr-xr-x  4 srinivas srinivas  4096 May 14 00:17 doc
drwxr-xr-x  4 srinivas srinivas  4096 May 14 00:17 generator-bootcamp
drwxr-xr-x  8 srinivas srinivas  4096 May 15 15:39 .git
-rw-r--r--  1 srinivas srinivas    49 May 14 00:17 .gitignore
drwxr-xr-x 12 srinivas srinivas  4096 May 14 00:22 jupyter-scala
-rw-r--r--  1 srinivas srinivas 35147 May 14 00:17 LICENSE
drwxr-xr-x  2 srinivas srinivas  4096 May 14 00:17 project
-rw-r--r--  1 srinivas srinivas   795 May 14 00:17 README.md
-rwxr-xr-x  1 srinivas srinivas    75 May 14 00:17 run-examples.sh
-rwxr-xr-x  1 srinivas srinivas    72 May 14 00:17 run-problem.sh
-rwxr-xr-x  1 srinivas srinivas    73 May 14 00:17 run-solution.sh
-rwxr-xr-x  1 srinivas srinivas  4590 May 14 00:17 set-learning-journey.sh
drwxr-xr-x  4 srinivas srinivas  4096 May 14 00:17 src
drwxr-xr-x 13 srinivas srinivas  4096 May 15 16:47 verilator
srinivas@srinivas:~/learning-journey$ 
```

---------------------------------------------------------------------------
On 05/16/2018/ Aleksandar wrote:
Hello, 

thanks for reporting back.

Unfortunately, all I can say is that (since the script worked and you have all the pieces in the correct place) this issue has to do with something else. This is the reason why we decided to create a uniform environment for everyone. Therefore, I can only advise you to create a virtual machine (or, if you prefer to make a dual-boot, that's also fine) as advised here: [[https://github.com/librecores/riscv-sodor/wiki/Chisel-Environment]]

In this way we will be able to track down the problem.

Best regards,
  Aleksandar

--------------------------------------------------------------------------------

05/23/2018 08:45 AM

After struggling quiet a few times, I concluded  that the installation script doesn't work properly on Ubuntu 18. The script specifically checks for the software packages such as make, bison, pip3 etc. and installs them if necessary, which did not execute properly on Ubuntu machine. I tried re-installing them separately before running the actual script only to encounter more errors related to SBT and JAVA. 

MINT 18 all the way!!!!

In Mint, during installation you might see a small error:

 ./set-learning-journey.sh: line 155: pip3: command not found

 ./set-learning-journey.sh: line 156: pip3: command not found

but you can ignore it or reinstall pip3 and try again. Command to install pip3:

`sudo apt-get install python3-pip`

-----------------------------------------------------------------------------------------------------------
05/25/2018 03:40 PM

**Compatibility Issues:**
Error Message:

sudo ./run-problem.sh Adder

[info] Loading project definition from /home/kartik/Desktop/learning-journey/project

[info] Set current project to chisel-tutorial (in build file:/home/kartik/Desktop/learning-journey/)

[info] Compiling 1 Scala source to /home/kartik/Desktop/learning-journey/target/scala-2.11/classes...

[error] /home/kartik/Desktop/learning-journey/src/main/scala/problems/Adder.scala:15: not found: value Input

[error] val in0 = Input(UInt(w.W))

[error]           ^

[error] /home/kartik/Desktop/learning-journey/src/main/scala/problems/Adder.scala:16: not found: value Input

[error] val in1 = Input(UInt(w.W))

[error]           ^

[error] /home/kartik/Desktop/learning-journey/src/main/scala/problems/Adder.scala:17: not found: value Output

[error] val out = Output(UInt(w.W))

[error]           ^

[error] three errors found

[error] (compile:compileIncremental) Compilation failed

[error] Total time: 4 s, completed May 22, 2018 4:04:11 PM

code snippet causing error:

class Adder(val w: Int) extends Module {
  val io = IO(new Bundle {
  val in0 = Input(UInt(w.W))
  val in1 = Input(UInt(w.W))
  val out = Output(UInt(w.W))
  })

**Solution:** Update the "learning-journey" repo but typing following command on terminal inside this directory

`git pull`