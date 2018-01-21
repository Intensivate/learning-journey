// See LICENSE.txt for license details.
// January 21st, 2018	- adapting BasicALU to Learning Journey
package examples

import Chisel._

class BasicALU extends Module { 
  val io = new Bundle { 
    val a      = UInt(INPUT, 4) 
    val b      = UInt(INPUT, 4) 
    val opcode = UInt(INPUT, 4) 
    val output = UInt(OUTPUT, 4) 
  } 
  io.output := UInt(0) 
  when (io.opcode === UInt(0)) { 
    io.output := io.a                   // pass A 
  } .elsewhen (io.opcode === UInt(1)) { 
    io.output := io.b                   // pass B 
  } .elsewhen (io.opcode === UInt(2)) { 
    io.output := io.a + UInt(1)         // inc A by 1 
  } .elsewhen (io.opcode === UInt(3)) { 
    io.output := io.a - UInt(1)         // inc B by 1 
  } .elsewhen (io.opcode === UInt(4)) { 
    io.output := io.a + UInt(4)         // inc A by 4 
  } .elsewhen (io.opcode === UInt(5)) { 
    io.output := io.a - UInt(4)         // dec A by 4 
  } .elsewhen (io.opcode === UInt(6)) { 
    io.output := io.a + io.b            // add A and B 
  } .elsewhen (io.opcode === UInt(7)) { 
    io.output := io.a - io.b            // sub B from A 
  } .elsewhen (io.opcode === UInt(8)) { 
    io.output := (io.a < io.b)          // set on A < B 
  } .otherwise { 
    io.output := (io.a === io.b)        // set on A == B 
  } 
}

class SimpleALU extends Module {
  val io = IO(new Bundle {
    val a      = Input(UInt(4.W))
    val b      = Input(UInt(4.W))
    val opcode = Input(UInt(2.W))
    val out = Output(UInt(4.W))
  })
  io.out := 0.U
  when (io.opcode === 0.U) {
    io.out := io.a + io.b //ADD
  } .elsewhen (io.opcode === 1.U) {
    io.out := io.a - io.b //SUB
  } .elsewhen (io.opcode === 2.U) {
    io.out := io.a  	     //PASS A
  } .otherwise {
    io.out := io.b        //PASS B
  }
}
