// See LICENSE.txt for license details.
// January 21st, 2018	- adapting to Learning Journey

package examples

import Chisel._

class ByteSelector extends Module { 
  val io = new Bundle { 
    val in     = UInt(INPUT, 32) 
    val offset = UInt(INPUT, 2) 
    val out    = UInt(OUTPUT, 8) 
  } 
  io.out := UInt(0, width = 8) 
  when (io.offset === UInt(0)) { 
    io.out := io.in(7,0) // pull out lowest byte 
  } .elsewhen (io.offset === UInt(1)) { 
    io.out := io.in(15,8) // pull out second byte 
  } .elsewhen (io.offset === UInt(2)) { 
    io.out := io.in(23,16) // pull out third byte 
  } .otherwise { 
    io.out := io.in(31,24) // pull out highest byte 
  } 
}
