// See LICENSE.txt for license details.
// January 22nd, 2018	- Adapted to Learning Joruney
package solutions

import Chisel._

// Problem:
//
// 'out' should be the sum of 'in0' and 'in1'
// Adder width should be parametrized
//
class Adder(val w: Int) extends Module {
  val io = new Bundle {
    val in0 = UInt(INPUT,  w)
    val in1 = UInt(INPUT,  w)
    val out = UInt(OUTPUT, w)
  }
  io.out := io.in0 + io.in1
}
