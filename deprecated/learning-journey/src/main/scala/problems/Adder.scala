// See LICENSE.txt for license details.
// January 22nd, 2018	- Adapted to Learning Journey
package problems

import Chisel._

// Problem:
//
// 'out' should be the sum of 'in0' and 'in1'
// Adder width should be parametrized
//
// Implement below ----------
class Adder(val w: Int) extends Module { 
  val io = new Bundle { 
    val in0 = UInt(INPUT,  1) 
    val in1 = UInt(INPUT,  1) 
    val out = UInt(OUTPUT, 1) 
  } 
//  ... 
  io.out := UInt(0) 
}
