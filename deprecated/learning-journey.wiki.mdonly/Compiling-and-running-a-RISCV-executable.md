This page is about compiling custom C programs and using Sodor to run them.
We recommend that this be done in a VM or container, because the tools for Sodor may collide with the tools for rocket-chip
 
The steps to successfully install `riscv-sodor` are given [here](https://github.com/librecores/riscv-sodor).
`make run-emulator` will fail if you have cloned `librecores` since this repo doesn't have precompiled RISCV tests. If you want, you can pull the tests from `ucb-bar`.

You also need to install a RV32-compatible compiler. Any of these will work: [sifive](https://github.com/librecores/riscv-sodor#alternative), [eclipse](https://gnu-mcu-eclipse.github.io/toolchain/riscv/) or [gnu](https://github.com/cliffordwolf/picorv32#building-a-pure-rv32i-toolchain). A quote about compiler naming from [gnu-mcu-eclipse](https://gnu-mcu-eclipse.github.io/toolchain/riscv/): 
>don’t be confused by this unfortunate names. The 64 or 32 prefix attached to the architecture does not mean that the toolchain runs on 64-bit or 32-bit platforms only. It does not mean either that the compiler produces 64-bit or 32-bit RISC-V binaries.

After you have installed Sodor (including **all** [these](https://github.com/librecores/riscv-sodor#building-the-processor-emulators) steps) and RV32-compatible compiler, follow steps below to have Sodor simulator run a RISCV binary.


Assuming you have [env](https://github.com/riscv/riscv-test-env/tree/master), [common](https://github.com/ucb-bar/riscv-benchmarks/tree/master/common) and path to riscv64-unknown-elf-gcc in your PATH, this minimal (i.e. each parameter is required) command will compile a RISCV executable for Sodor:

    $ riscv64-unknown-elf-gcc -I/_path/to_/env -static -std=gnu99 -O2 -mabi=ilp32 -march=rv32i -o /_path/to_/helloworld.riscv _path/to_/helloworld.c _path/to_/common/syscalls.c _path/to_/common/crt.S -static -nostdlib -nostartfiles -lm -lgcc -T _path/to_/common/test.ld

Note that `env` and `common` can be found in `riscv-tests` and `riscv-tests/benchmarks` respectively (these are submodules of `riscv-sodor`).

After you have sucessfully built `riscv-sodor`, this will make Sodor simulator execute `helloworld.riscv` binary:

    $ path/to/riscv-sodor/emulator/rv32_5stage/emulator +loadmem=/path/to/helloworld.riscv
    Instantiated DTM.
    Cyc=          0 (0x80000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000) WB[   00: 0x00000000]       ExeInst: DASM(00004033)
    Cyc=          1 (0x80000004, 0x80000000, 0x00000000, 0x00000000, 0x00000000) WB[   00: 0x00000000]       ExeInst: DASM(00004033)
    Cyc=          2 (0x80000008, 0x80000004, 0x80000000, 0x00000000, 0x00000000) WB[ Z 00: 0x00000000]       ExeInst: DASM(00000093)
    Cyc=          3 (0x8000000c, 0x80000008, 0x80000004, 0x80000000, 0x00000000) WB[MZ 00: 0x00000000]       ExeInst: DASM(00000113)
    Cyc=          4 (0x80000010, 0x8000000c, 0x80000008, 0x80000004, 0x80000000) WB[MZ 01: 0x00000000]       ExeInst: DASM(00000193)
    Cyc=          5 (0x80000014, 0x80000010, 0x8000000c, 0x80000008, 0x80000004) WB[MZ 02: 0x00000000]       ExeInst: DASM(00000213)
    Cyc=          6 (0x80000018, 0x80000014, 0x80000010, 0x8000000c, 0x80000008) WB[MZ 03: 0x00000000]       ExeInst: DASM(00000293)
    Cyc=          7 (0x8000001c, 0x80000018, 0x80000014, 0x80000010, 0x8000000c) WB[MZ 04: 0x00000000]       ExeInst: DASM(00000313)
    Cyc=          8 (0x80000020, 0x8000001c, 0x80000018, 0x80000014, 0x80000010) WB[MZ 05: 0x00000000]       ExeInst: DASM(00000393)
    Cyc=          9 (0x80000024, 0x80000020, 0x8000001c, 0x80000018, 0x80000014) WB[MZ 06: 0x00000000]       ExeInst: DASM(00000413)
    Cyc=         10 (0x80000028, 0x80000024, 0x80000020, 0x8000001c, 0x80000018) WB[MZ 07: 0x00000000]       ExeInst: DASM(00000493)
    Cyc=         11 (0x8000002c, 0x80000028, 0x80000024, 0x80000020, 0x8000001c) WB[MZ 08: 0x00000000]       ExeInst: DASM(00000513)
    Cyc=         12 (0x80000030, 0x8000002c, 0x80000028, 0x80000024, 0x80000020) WB[MZ 09: 0x00000000]       ExeInst: DASM(00000593)
    Cyc=         13 (0x80000034, 0x80000030, 0x8000002c, 0x80000028, 0x80000024) WB[MZ 0a: 0x00000000]       ExeInst: DASM(00000613)
    Cyc=         14 (0x80000038, 0x80000034, 0x80000030, 0x8000002c, 0x80000028) WB[MZ 0b: 0x00000000]       ExeInst: DASM(00000693)
    Cyc=         15 (0x8000003c, 0x80000038, 0x80000034, 0x80000030, 0x8000002c) WB[MZ 0c: 0x00000000]       ExeInst: DASM(00000713)

**N.B. path to +loadmem must be absolute. Simulator fails to find the executable otherwise.**

Five values in brackets stand for `PC` adresses at different stages of `riscv-sodor` pipeline (for 5-stage core): `Fetch`, `Decode`, `Execute`, `Memory`, `Writeback`. M and Z are printed if write to regfile is enabled at wb/mem stage respectively. `ExeInst` is the instruction executed at current exe cycle. If the processor consequently executes only 0x4033 command (NOP) or you don't see any output, then something went wrong.