A `Bundle` class is used to group together named fields of arbitrary type, but of the same context into a whole. A good example is a `Bundle` of input/output interfaces of a `Module`, like so:

```scala
...
val io = IO(new Bundle {
        val invalidExc  = Input(Bool())   // overrides 'infiniteExc' and 'in'
        val infiniteExc = Input(Bool())   // overrides 'in' except for 'in.sign'
        ...
        val exceptionFlags = Output(Bits(5.W))
    })
...
```

However, in more complex modules, there might be to many interfaces not only of different types, but also of different contexts. In such cases, `Bundle` allows the user to group together similar signals, e.g. all input signals might go to a `Bundle` called `input` and all output signals might go to a `Bundle` called `output`.

A more concrete example, where module ports are divided in different `Bundle` classes (based on their context) within a the "top" `Bundle` called `io` is given below:

```scala
...
    val io = IO(new Bundle {
        val input = Decoupled(new DivRecF64_io).flip

        val output = new Bundle {
            val a = Output(Bits(64.W))
            ...
            val detectTininess = Output(UInt(1.W))
        })

        val expected = new Bundle {
            val out = Output(Bits(64.W))
            ...
            val recOut = Output(Bits(65.W))
        }

        val actual = new Bundle {
            val out = Output(Bits(65.W))
            val exceptionFlags = Output(Bits(5.W))
        }

        val check = Output(Bool())
        val pass = Output(Bool())
    }
...
```
This introduction of `Bundle` hierarchy does not introduce additional complexity to the hardware implementation and can be used for improving code readability.

Furthermore, in Chisel *bulk connect* can be used to connect two similar bundles together. This is achieved using the `<>` operator to connect the interfaces of opposite gender between sibling modules or interfaces of the same gender between parent/child modules.

However, take care of the interfaces names and gender, because, if the names do not match or are missing or the interfaces genders are wrong, Chisel does not generate a connection.

Finally, note that, in Chisel3, operator `:=` also bulk connects.

Examples are taken from [hardfloat](https://github.com/ucb-bar/berkeley-hardfloat) repository.