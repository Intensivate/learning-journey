
```
s3   := Mux1H(Reg(next=UIntToOH(Reg(next=s1(log2Up(n) - 1, 0)))), s2)
```

The inner `Reg` selects LSBs of the `s1`. How many bits are selected is determined by `n`. For example, if `n` is 8, then selected LSBs are `(2,0)`, i.e. the three LSBs.

Then, `UIntToOH` logic converts the three selected bits to the one-hot type.

The outer `Reg` stores thus obtained value.

The `Mux1H` syntax is as follows:

```
Mux1H(sel:Bits/Iterable[Bool], in:Iterable[Data]): Data
```

meaning that the value stored in the outer `Reg` represents the select input of the Mux, whereas the `s2` represents the information input.

Finally, based on the value of the select input, the `s3` takes the appropriate value from `s2`.

Further information: https://github.com/freechipsproject/chisel3/wiki/Muxes-and-Input-Selection#mux1h