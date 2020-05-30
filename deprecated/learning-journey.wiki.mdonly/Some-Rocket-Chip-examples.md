
[[Resource String in RocketTile]] -- inside RocketTile, there is this strange "cpuDevice = new Device" stuff.. what it does is create a resources string that is essentially the same as a Device Tree, and is used to generate the machine configuration string, which is used by Linux.

  override lazy val module = new RocketTileModule(this)

[[Reg width inheritance issue with porting Chisel 2 to Chisel 3]]
