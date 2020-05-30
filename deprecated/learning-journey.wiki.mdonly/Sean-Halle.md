
## Branches for various levels of complexity

We were talking about also having branches that add more advanced features.  Actually simplify the main branch, strip things out.  Then add other branches, the ratchet up the complexity.

For example, add caches in a different branch, and add multi-core in a different branch.
Right..  we actually discussed a system of branches.
What do you think about possibly choosing names for the branches that are descriptive of the goal of the branch?

## Vision of "commented" branch in Sodor repository

Kritik created a separate branch, that add verbose comments to the code

The important thing will be making this obvious and advertising its existence to everyone, so that beginners don't struggle, and then later discover a commented version exists.

Also, it will be important to make it quick and easy to switch between the two.  And it will be important to make it easy to keep the two in sync.   

I'm thinking that in practice, people will jump back and forth often..  at first most time in commented version..  then gradually spend more time in non-commmented version..

As spend more time in non-commented, they will start jumping -- hit a place where they don't understand, jump over to commented version, then jump back.

### From Slack discussions:
Those fresh off journey, thrown into project involving Rocket Chip
R: "Initially I've been trying to understand how everything is put together, for instance how classes and traits from other packages are being called in the file, cache configuration etc. Then I treied to understand the L2 cache code. Still working on it."

K: "Right now, I am trying to understand the overall architecture, figuring out what's been placed in rocket chip. The scala codes placed inside src are quite daunting and require much focus to get better understanding for me."

A: "yes, the whole thing is very complex and the programming paradigm, the code practice is terribly abstract
Sean and myself came to a conclusion that the key lays within the lazy modules and their instantiations - have you had the chance to learn about `lazy`?"

### another
most of the developers are aware of the object oriented programming concepts. I would like add to what @Kartik proposed, we should also try to bridge the gap between a complex chisel code and the hardware. For instance, what specific feature of the hardware the code is trying to mimic. We are trying to reverse engineer the Rocket-chip etc chisel codes which has not been properly commented, and it is difficult to get a sense what the flow is, and how things actually relate to the hardware. For this I think its a good idea that people work together on the same piece of code and discuss about it at specific allocated times or within a time frame, if doubts arise put them up on the #chisel channel for further advice from @apaj and @Sean, and reach to conclusion, comment the piece of code properly and move forward with the next code. The code with proper explanation can be placed in the experiences page. To execute this I would like to team up with @Kartik. I'll discuss with @Kartik about how best we can approach this, and inform you.

## another 2
We could take one complex code which encompasses many good chisel concepts and explain them part by part

## another 3
write test bench
* coming up -> https://github.com/ucb-bar/generator-bootcamp/issues/32
* https://github.com/librecores/riscv-sodor/tree/master/src/test Memory related logic should make sense to a newbie even if that person has not gone through the rest of the project 

## config in FireSim
class FireSimRocketChipConfig extends Config(
  new WithBootROM ++
  new WithPeripheryBusFrequency(BigInt(3200000000L)) ++
  new WithExtMemSize(0x400000000L) ++ // 16GB
  new WithoutTLMonitors ++
  new WithUARTKey ++
  new WithNICKey ++
  new WithBlockDevice ++
  new WithRocketL2TLBs(1024) ++
  new WithPerfCounters ++
  new freechips.rocketchip.system.DefaultConfig)

class WithNDuplicatedRocketCores(n: Int) extends Config((site, here, up) => {
  case RocketTilesKey => List.tabulate(n)(i => up(RocketTilesKey).head.copy(hartId = i))
})

class FireSimRocketChipTracedConfig extends Config(
  new WithTraceRocket ++ new FireSimRocketChipConfig)

see https://github.com/firesim/firesim/blob/844ff9f761c187ebaf04699675bcb129ef2d51ee/sim/src/main/scala/firesim/TargetConfigs.scala#L81

## map example
https://github.com/firesim/firesim/blob/844ff9f761c187ebaf04699675bcb129ef2d51ee/sim/src/main/scala/firesim/TargetConfigs.scala#L56
   case RocketTilesKey => up(RocketTilesKey, site) map { r => r.copy(trace = true) }
