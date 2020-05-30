**Question**

What is the purpose of the `override` modifier and how to use it?

**Answer**

When member of the child class overrides a concrete member of the parent class, Scala **requires** the `override` modifier to be used. The modifier is **optional** if the member implements an abstract member with the same name. However, it is **forbidden** if the member does not override or implement some other member in the base class.

This rule provides information for the compiler that can help avoid errors and makes code evolution safer. For example, instead of the _accidental override_ (a common manifestation of the problem known as the _fragile base class_), the compiler will give the error:

```scala
error overriding method
```

if the `override` modifier is used correctly.

### References:

Odersky, M: _Programming in Scala_, 3rd, 2016