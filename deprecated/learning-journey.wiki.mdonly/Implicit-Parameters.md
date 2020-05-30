# Page Purpose

There are many mentions of `implicit` parameters across the Learning Journey, but no center point, these are nowhere explained in detail, nor the examples are collected. This page is created to act as a gathering point where all the materials using `implicit` will point to.

# Implicits Basics in Scala

## Introduction

The fundamental difference between the code a user writes and the libraries other people wrote is that one can change or extend own code, whereas someone else's libraries usually have to be taken as they are. Scala's constructs created with the purpose of alleviating this problem are **implicit conversions and parameters**. These make existing libraries much more pleasant to deal with by letting us leave out tedious, obvious details that obscure the interesting parts of our code. Therefore, implicits are a powerful Scala feature, enabling us to condense code.

## Implicit conversions

These are used when we need to work with two bodies of software that were developed independently, one not being aware of the other. Every library has its own way to code an idea that essentially represents the same thing. Purpose of implicit conversions is to reduce the number of required explicit conversions from one type to another. Implicit **definitions** are definitions the compiler is allowed to introduce into a program with the purpose of fixing any of its type errors. 

There exists a specific set of **rules for implicits** that govern the implicit conversions. Further details on these rules, but also on _implicit conversions to an expected type_, _converting the receiver_, _implicit classes_, etc. are available [here](https://www.artima.com/pins1ed/implicit-conversions-and-parameters.html#21.2)

## Implicit parameters

Another place where the compiler inserts implicits is within the argument lists. It will sometimes replace a call with a another call containing different argument list in order to add a missing parameter list to complete a function call. It supplies the whole last curried parameter list, not only the last parameter. So, if the missing last parameter list of some call, say `aFunctionCall(a)`, is missing three parameters, it will be replaced by a call supplying all the three missing parameters as follows:

```scala
aFunctionCall(a) -> aFunctionCall(a)(b,c,d)
```

For this to work, we must:

* mark `implicit` inserted identifiers `b,c,d` where defined; and
* mark `implicit` the last parameter list in `aFunctionCall`'s definition.

Keyword `implicit` applies to an entire parameter list, not the parameters individually. When `implicit` is used on a parameter, the compiler will both:

* try to supply that parameter with an implicit value; and
* use that parameter as an available implicit in the method body.

# Implicits in Chisel



# References

1. M. Odresky, [Programming in Scala](https://www.artima.com/pins1ed/index.html), 2010, 1st
2. A. Izraelevitz, [Advanced Parameterization Manual](https://chisel.eecs.berkeley.edu/2.2.0/parameters.html), 2015