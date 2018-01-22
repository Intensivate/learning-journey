// See LICENSE.txt for license details.
// January 22nd, 2018	- adapting to Learning Journey
package solutions

import Chisel._

// Problem:
//
// Implement a shift register with four 8-bit stages.
// Shift should occur on every clock.
//
class VecShiftRegisterSimple extends Module {
  val io = new Bundle {
    val in  = UInt(INPUT,  8)
    val out = UInt(OUTPUT, 8)
  }
  val delays = Reg(init = Vec(4, UInt(0, width = 8)))
  delays(0) := io.in
  delays(1) := delays(0)
  delays(2) := delays(1)
  delays(3) := delays(2)
  io.out    := delays(3)
}
