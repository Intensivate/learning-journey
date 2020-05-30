
## hands on example comments

1. Adder
    This was straight question but I was also thinking about the handling "carry" as well, and trying to figure out how to make this work. It turned out we don't have to consider about it for this problem, it might be nice to mention to ignore carry for this problem, and if user wants, providing some more problem with carry component would be helpful.

2. Counter
    This problem was not so much of issue other than syntax, for the first user like me I think.

3. Accumulator
    This is very straightforward

4. Vector Shift Register
    I had struggled with how to represent shift register with Vectors. I was under the impression that there is some way of representing a shift register as a block without instantiating as many as vector element counts; now I understand how to describe them.

5. Dynamic Memory Search
    Other than syntax issues, this is very straightforward once I read thru the test code part as to how it's operated.

6. Vending Machine
    This was the first problem I did not have any issue running thru to pass by myself, however when I checked the solution code, I am shocked how simply the code could be written... so I think for me this is rather a coding style issue and requires more coding experiences, or maybe I should see & understand good number of "elegant" codes so that I can write these good codes myself.

7. Memo
    This is very straightforward, except one thing; It requires memory output initialization... so I guess Chisel does not have logic state "X"...? I remember I either heard or saw something like this. 

8. Mul
    At first I had no idea how to create table; and ArrayBuffer is one dimension list. When I figured out how to put each data, then I had the next problem; how to look up. For this part I had no idea how to do it, so I searched on the net...but didn't find one; so I looked at the solution code, but at first I did not figure out why row/col(whichever the reference is) input has to be shifted. This was because I did not know when data is shifted to left, the data width will be increased to as many as shifted amount. Once I figure out that, I fully understood how it's done. I think this shifting code is not something physical design engineers might not immediately understood... as I thought the data width will not change at first.

## higher level

I think my question would be the following two categories I can think right now:
1) Chisel/scala syntax/coding style questions
2) RISC-V architecture related questions within codes
I suppose I can ask on stackoverflow and hangout for answers right?

The first thing I was trying to figure out is, what codes are located where. rv32_1stage one is rather simple.

I would like to understand code block partitions, and within each block, what it does roughly, along with variable descriptions, then deep dive into the code as an ideal procedure 

## parameter system questions

the very first one, would be: nxprlen, nxpr, neprbits, rvc, vm, usingUser(all in common/configurations.scala) for example; I can 'guess' what they might be but it's far better if there's comments in the code
in csr.scala, there are a lot more; if I don't figure out that easy, I have to trace codes and dig into them one by one to find out a)what they are, b)for what and how they're used, and c)where they're referenced

## parameters take 1

I've been looking into rv32_1stage codes, and the very first question I came up with, is the following:
Is there a "single" table of entire parameters for the chip, with the descriptions, and in which file they're defined?
I mean also val variables (am I using this terminology right...?) defined in the codes. 

If there is some description as to which pipestage these variables are used, it would be very useful, especially for long pipestage cores.

Initial coding examples/problems were rather straightforward as we know what they do does and how to code to match expected behaviors, but when reading thru sodor(, rocket and boom as well) codes, I find them hard to figure out without these object's descriptions...and how/why they interact with other code blocks.

## Further questions
Removed: What is “nxpr = 32” used for? defined in the file but never used in any other files
Removed: What is “nxprbits = log2ceil(nxpr)” used for? only defined in the file but never used in any other files
What is “uingUser = false”? looks like a condition if the operation is under user mode…
>> Yes; X0 register is initialized to be used immediately after reset
Is “X0 = 0.U” just used only for initialization? 
>> Yes; X0 register is initialized to be used immediately after reset
Removed: What is “PERM_BITS = 6” used for? not used any other code…

What function is “override” and what does this do, and how is this used(syntax)? Any diff. btw the followings?
Case1: memory.scala
class Rport(val addrWidth : Int,val dataWidth : Int) extends Bundle{
   val addr = Input(UInt(addrWidth.W))
   val data = Output(UInt(dataWidth.W))
   override def cloneType = { new Rport(addrWidth,dataWidth).asInstanceOf[this.type] }
}

Case2: debug.scala
class DMIReq(addrBits : Int) extends Bundle {
  val op   = Output(UInt(DMConsts.dmiOpSize.W))
  val addr = Output(UInt(addrBits.W))
  val data = Output(UInt(DMConsts.dmiDataSize.W))
  override def cloneType = new DMIReq(addrBits).asInstanceOf[this.type]
}

Is there any differences btw. CSR.X & CSR.N? (the assigned values are the same…)

What does the 2nd term of the following line in csr.scala do…?
val misa = BigInt(0) | isa_string.map(x => 1 << (x - 'A')).reduce(_|_) 
Answer: https://github.com/librecores/riscv-sodor/wiki/map-function

What does .andR do in the following?
io.decode.write_illegal := io.decode.csr(11,10).andR; e.g. x.andR
Answer: Do ANDing for all the inputs x and give a single bit result

 What is the function of  Mux1H()?
io.rw.rdata := Mux1H(for ((k, v) <- read_mapping) yield decoded_addr(k) -> v)
Answer: https://github.com/freechipsproject/chisel3/wiki/Muxes-and-Input-Selection#mux1h 

What does “implicit” mean in below? And what/how does it make ‘val’ any different?
Case 1. class DMIIO(implicit val conf: SodorConfiguration) extends Bundle {...
Case 2. class SimDTM(implicit val conf: SodorConfiguration) extends BlackBox {...

Syntax for Mem definition:
Chisel cheetsheet describes
val my_mem = Mem(n:Int, out:Data)
  out: memory element type
  n: memory depth (elements)
but in Sodor code, it’s defined as the following:
val regfile = Mem(UInt(conf.xprlen.W), 32) [From rv32_3stage:dpath.scala]
and looks like n:Int and out:Data are in reverse...which one is correct?

 io.mem.req.valid variable definition:
class MemPortIo(data_width: Int)(implicit conf: SodorConfiguration) extends Bundle 
{
   val req    = new DecoupledIO(new MemReq(data_width))
   val resp   = Flipped(new ValidIO(new MemResp(data_width)))
  override def cloneType = { new MemPortIo(data_width).asInstanceOf[this.type] }
}
class MemReq(data_width: Int)(implicit conf: SodorConfiguration) extends Bundle
{
   val addr = Output(UInt(conf.xprlen.W))
   val data = Output(UInt(data_width.W))
   val fcn  = Output(UInt(M_X.getWidth.W))  // memory function code
   val typ  = Output(UInt(MT_X.getWidth.W)) // memory type
  override def cloneType = { new MemReq(data_width).asInstanceOf[this.type] }
}
class CpathIo(implicit conf: SodorConfiguration) extends Bundle() 
{
   val dcpath = Flipped(new DebugCPath())  
   val mem  = new MemPortIo(conf.xprlen)
   val dat  = Flipped(new DatToCtlIo())
   val ctl  = new CtlToDatIo()
   override def cloneType = { new CpathIo().asInstanceOf[this.type] }
}
class CtlPath(implicit conf: SodorConfiguration) extends Module
{
   val io = IO(new CpathIo())
…}
So, “io.mem.req.valid” should be defined inside “MemReq” class definition but val valid is not defined in it. Where is it defined? Or is this no longer used? since these definition use ‘implicit’ keyword, is there anything to do with these variables? Same for io.mem.resp.valid signal. if not defined, what would happen to the logics…? tied off, or left dangling(it cannot be)…?
(These variable seems to be used only in rv32_3stage; only used in 3-stage core)

$SODOR/rv32_ucode:dpath.scala
if reg_addr == 0.U, is there any issue? Or architectural requirement the value should not be “0”...(which seems to be the case IMO); the following code for reg. write:
   when (io.ctl.en_reg & io.ctl.reg_wr & reg_addr != 0.U)
   {
      regfile(reg_addr) := bus
   }

$SODOR/rv32_2stage:top.scala
Diff. from rv32_1stage:top.scala is a lib import line and the following object definition below:
$SODOR/rv32_2stage/top.scala: object elaborate {
$SODOR/rv32_1stage/top.scala: object elaborate extends ChiselFlatSpec{
What is the reason for the removal?

 $SODOR/rv32_3stage:alu.scala
it defines “ALU_COPY1”; what is this used?

$SODOR/rv32_3stage:alu.scala
What does “Reverse()” operation do? reverse the entire bit order of the data([31:0] to [0:31])?

$SODOR/rv32_3stage:dpath.scala
what does “next = …” phrase do? Is this same as clock gating…(looks like it)?
csr.io.exception := Reg(next = io.ctl.exception)

Clarification for understanding: csr.scala has a line
val reg_hpmcounter = io.counters.map(c => WideCounter(CSR.hpmWidth, c.inc, reset = false))
and this refers to 
CSRFile.CSRFileIO.counters(Vec(60, new PerfCounterIO)), 
WideCounter(width[Int], PerfCounterIO.inc[Input(UInt(conf.xprlen.W))], reset[bool]) (utils.scala file’s class definition), CSR.hemWidth(=40)
for input arguments of mapping reg_hpmcounter value

Privileged Arch. document v1.10 describe WARL CSRs “will not raise an exception on writes of unsupported values to WARL field.” but what happens if unsupported values are actually written? (not sure if this condition really occur as I do not know all the states/registers yet)
　
In RISC-V Privileged Architecture V1.10
  Section 3.6.1 “Physical Memory Protection CSRs“ describes PMP configuration register first, and PMP address registers are described. However, after thes PMP address register description, then again PMP configuration register descriptions for the details of configuration register’s bit field definition(above Fig.3.27). Probably it’s better moving this part above PMP address register to complete PMP configuration register’s description, then go over PMP address registers.

In RISC-V Privileged Architecture V1.10…ambiguity(?)
  “Locking and Privilege Mode” section, in 3.6.1. “Physical Memory Protection CSRs”, it states ‘When the L bit is set, these permissions are enforced for all privilege modes. When the L bit is clear, any M-mode access matching the PMP entry will succeed; the R/W/X permissions apply only to S and U modes.’ the description seems to be somewhat confusing... It might be more clear by saying, ‘R/W/X permission of M-mode access will be enforced when the L bit is set. Otherwise R/W/X permission will only apply to S-/U- modes, and M-mode have all access permissions.”

In RISC-V External Debug Support Version 0.13
  4.5 describes about Virtual debug register’s ‘prv’ spec, but this seems to be overlapping with ebreakm/h/s/u; What is the differences between them?
