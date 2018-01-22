// See LICENSE.txt for license details.
// January 21st, 2018	- adapting for Learning Journey
package solutions

import Chisel._

// Problem:
//
// Implement a 16-bit Fibonacci Linear-feedback shift register
// with polynomial x^16 + x^14 + x^13 + x^11 + 1
// State change is allowed only when 'inc' is asserted
//
class LFSR16 extends Module {
  val io = new Bundle {
    val inc = Bool(INPUT)
    val out = UInt(OUTPUT, 16)
  }
  val res = Reg(init = UInt(1, 16))
  when (io.inc) { 
    val nxt_res = Cat(res(0)^res(2)^res(3)^res(5), res(15,1)) 
    res := nxt_res
  }
  io.out := res
}
