// See LICENSE.txt for license details.
// January 20th, 2018	-adapted for Learning Journey
package problems

import Chisel._

// Problem:
//
// Count incoming trues
// (increase counter every clock if 'in' is asserted)
//
class Accumulator extends Module { 
  val io = new Bundle { 
    val in  = UInt(INPUT, 1) 
    val out = UInt(OUTPUT, 8) 
  } 
 
  // flush this out ... 
 
  io.out := UInt(0) 
}
