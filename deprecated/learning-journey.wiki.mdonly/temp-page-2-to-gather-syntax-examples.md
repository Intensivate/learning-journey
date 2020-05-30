
io.out := (taps zip io.consts).map { case (a, b) => a * b }.reduce(_ + _)
