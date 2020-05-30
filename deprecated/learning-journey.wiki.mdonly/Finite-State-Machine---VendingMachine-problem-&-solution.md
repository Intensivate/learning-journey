Solution to the Vending Machine [problem](https://github.com/apaj/learning-journey/blob/master/src/main/scala/problems/VendingMachine.scala) is provided and commented within this page, omitting the overhead (such as `import` statements, etc).

The problem is given as follows:

```
// Problem:
//
// Implement a vending machine using 'when' states.
// 'nickel' is a 5 cent coin
// 'dime'   is 10 cent coin
// 'sOk' is reached when there are coins totalling 20 cents or more in the machine.
// The vending machine should return to the 'sIdle' state from the 'sOk' state.
//
```

We first define the box and the ports, in a standard way:

```scala
class VendingMachine extends Module {
  val io = IO(new Bundle {
    val nickel = Input(Bool())
    val dime   = Input(Bool())
    val valid  = Output(Bool())
  })
```

Next is bookkeeping for the states, explained in more detail in [[Enum]], as follows:

```scala
  val sIdle :: s5 :: s10 :: s15 :: sOk :: Nil = Enum(5)
```

Define the beginning state, i.e. the state in which the FSM goes at reset:

```scala
  val state = RegInit(sIdle)
```

For each of the possible states and conditions, we use `when`, `===` and `:=` to determine where to go next from the current state given the appropriate condition:

```scala
  when (state === sIdle) { // if we are in state sIdle, meaning "no money"
    when (io.nickel) { state := s5 } // and a nickle appears, next state is s5
    when (io.dime)   { state := s10 } // if a dime appears, next state is s10
  }
  when (state === s5) { // if we are in state s5
    when (io.nickel) { state := s10 } // another nickle takes us to s10
    when (io.dime)   { state := s15 } // and a dime takes us to 5+10 => s15
  }
  when (state === s10) { // if the first coin was actually a dime
    when (io.nickel) { state := s15 } // a nickle takes us to 10+5 =>s15
    when (io.dime)   { state := sOk } // another dime take us back to zero 
  }
  when (state === s15) { // if there were a nickle and a dime,
    when (io.nickel) { state := sOk } // in both cases
    when (io.dime)   { state := sOk } // FSM is taken back to zero
  }
  when (state === sOk) {
    state := sIdle // once zero is reached, machine goes back to idle
  }
```

Finally, the output will assert if the state of zero money, i.e. sum of 20 cents is reached:

```scala
  io.valid := (state === sOk)
}
```