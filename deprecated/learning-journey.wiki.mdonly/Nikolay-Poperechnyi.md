First of all, I have to note that I am new to hardware design but not new to Scala. Here is my experience after passing Learning Journey and reading Chisel tutorial: The journey was easy to read and follow. It greatly fulfills its goal — to be a quick introduction to the language. A few important things are missing and the advanced material is a little disorganised. I have found the following issues:

* [[Examples-line-by-line]] is only accessible from [[Comparison-to-Verilog]]
* [RealGCD](https://github.com/Intensivate/learning-journey/blob/master/src/main/scala/problems/RealGCD.scala) problem is not covered in the journey. I think it should be there with the description of `DeqIO` and `DecoupledIO`.
* [Memo](https://github.com/Intensivate/learning-journey/blob/master/src/main/scala/problems/Memo.scala) — same for `Mem`.
* `<>` operator is not introduced but used intensively. I found [this](https://github.com/freechipsproject/chisel3/wiki/Interfaces-Bulk-Connections#bulk-connections) as a good explanation.
