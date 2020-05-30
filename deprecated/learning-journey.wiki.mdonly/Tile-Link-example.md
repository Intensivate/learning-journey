
 I try to do some simple works for learning to learn rocket chip.

I leverage SRAM( in rocket-chip/src/main/scala/tilelink/) and attach it on rocket chip system bus. However, it fails to generate out the emulator.


Below code is my RAM configuration(rocket system is 64 bits): 

trait HasPRAM extends HasSystemBus {
  implicit val p: Parameters

  private val address = 0x2000

  val ram  = LazyModule(new TLRAM(AddressSet(0x2000, 0xff), beatBytes = 8 ))

  ram.node := sbus.toSlave
}

The main error message:

Caused by: java.lang.IllegalArgumentException: requirement failed: MemoryMap region ram only supports TransferSizes[1, 8] Get, but must support TransferSizes[1, 64]

I try to trace out and fix it. And I find the error is related to TLBPageLookup.  However, I still cannot fix with some trying.
Can anyone helps me to point out whats wrong about it?

=======

You can use TLFragmenter which is there in tilelink/Fragmenter.scala

ram.node := TLFragmenter(8, 64) := sbus.toSlave

TLFragmenter will fragment the data transaction size into range that is supported by manager and generate appropriate data request/response

8 since there is no need to fragment accesses with size lesser than beatBytes
64 since that is the maximum possible transfer size as shown by your error
