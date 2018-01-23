// See LICENSE.txt for license details.
// January 22nd, 2018	- adapting to Learnin Journey
package problems

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
  val delays = Vec.fill(4){ Reg(UInt(width = 8)) } 
//  ... 
  io.out := UInt(0) 
}
