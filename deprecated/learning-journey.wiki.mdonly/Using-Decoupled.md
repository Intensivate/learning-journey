Aleksandar Pajkanovic (11:32 AM):
yes, that's fine
what I didn't get is about input/output
you said we don't need to assign
what do you mean by that?
does this mean that every member of input is now expanded
?
for example, we had a
now we have a.ready, a.valid and a.bits?
Raj (11:34 AM):
yes, since it's an interface it already has these input ouputs
Aleksandar Pajkanovic (11:35 AM):
No,
I was wrong
that was a wrong assumption
look at line 92
it accesses roundingMode of input
by doing: input.bits.roundingMode
so there are the following now:
input.ready
input.valid

and then there are:

input.bits.a
input.bits.b
input.bits.roundingMode
input.bits. all the rest...