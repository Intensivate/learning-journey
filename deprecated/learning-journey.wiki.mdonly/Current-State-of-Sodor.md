Sodor has two main branches, that are kept in sync:  
*  `master` branch contains the simplest code
*  `comments-dev` branch mirrors master, and adds verbose comments to the code.

Sodor also has advanced branches:
* `tilelink2_fpga` compatible with the latest tilelink api from freechipsproject/rocket-chip and runs on both PYNQ-Z1 and Arty

Branches with `-dev` appended to them are in development and may not have its flagship feature working

Note: On switching from one branch to another please use `git submodule update --recursive`

### Code
Apart from the above main repository changes have also been to made to the following forked repositories
to get support for not supported features:
- [riscv-fesvr](https://github.com/codelec/riscv-fesvr)

### Current Feature Set
- priv1.10
- chisel3
- debug spec v0.13
- Tilelink integration (code reuse from rocket)
- Port to FPGA
- Microarchitecture diagrams
- Communicate between sodor on [pynq-z1](https://reference.digilentinc.com/reference/programmable-logic/pynq-z1/reference-manual) and fesvr(x86) using xsdb 
- Pass isa and bmarks on both pynq-z1 and arty(with uncertainty in pass rates only in arty)

### Future Work / To-Do
[Feature Pipeline](feature_pipeline)

[[random thoughts on things to make 1-stage code more accessible]] 

[[brainstorming on progression in difficulty of sodor code]]