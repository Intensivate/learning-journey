
Learner: "I wrote my own code of FullAdder in ''problems'' package by creating a new file."

Here is what you need to do to add the new file/circuit to the existing package. Say that you want to add it to the package `Problems`, than go to `/src/test/scala/problems` and open the file `Launcher.scala` for editing. Once there, you will note the pattern, but for completeness, what you need to do is add the following piece of code:

    "NewCircuitName" -> { (manager: TesterOptionsManager) =>
      Driver.execute(() => new NewCircuitName (), manager) {
        (c) => new NewCircuitNameTests(c)
      }
    },

where `NewCircuitName` is the name of your circuit, `FullAdder` in your case, and `NewCircuitNameTests` is the name of the testbench you wrote for that new circuit.
