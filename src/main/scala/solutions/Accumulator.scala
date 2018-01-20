// See LICENSE.txt for license details.
// January 20th, 2018	- adapted for Learning Journey
package solutions

import Chisel._

// Problem:
//
// Count incoming trues
// (increase counter every clock if 'in' is asserted)
class Accumulator extends Module {
  val io = new Bundle {
    val in  = UInt(width = 1, dir = INPUT)
    val out = UInt(width = 8, dir = OUTPUT)
  }
  val accumulator = Reg(init=UInt(0, 8))
  accumulator := accumulator + io.in
  io.out := accumulator
}
