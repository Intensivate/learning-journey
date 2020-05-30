Assuming you are already familiar with the basics of Objected Oriented Programming in Scala, you may get across the following syntax and wonder about its meaning:

`class Class1[+C <: Class2]{...}`

To illustrate this syntax, we need to introduce two main concepts: **subtyping** & **covariance**. We also introduce closely related concepts, such as **supertyping**, **contravariance** and **invariance**.

# Upper & Lower Bounds

## Subtyping
If we have two classes A and B, such that `A <: B`, then A is said to be a _**subtype**_ of B. It is also conventional to say that B is an _**upper bound**_ to A.

### Example

To illustrate what that means and how to use it, let's consider the following example:

```scala
abstract class Human{
  def name: String
}

abstract class Male extends Human
abstract class Female extends Human

class Man(str: String) extends Male{
  name = str
}

class Boy(str: String) extends Male{
  name = str
}

class Woman(str: String) extends Female{
  name = str
}

class Girl(str: String) extends Female{
  name = str
}

// Consider a certain family:
class FamilyMaleMember[HumanClass <: Male](member: HumanClass){
  def familyMember: HumanClass = member
}

class FamilyFemaleMember[HumanClass <: Female](member: HumanClass){
  def familyMember: HumanClass = member
}
```
In the previous example, let's consider class `FamilyMaleMember`. We see that a class type argument `HumanClass` has been passed to it to define the class of the `familyMember` object. The syntax `[HumanClass <: Male]` means that `HumanClass` has to be a `Male`, but not restricted to only class `Male` itself; since it could be any class that inherits the `Male` class, such as classes `Man` and `Boy`. The same applies for class `FamilyFemaleMember`. Therefore, we could do the following:

```scala
val Christina = new FamilyFemaleMember[Girl](new Girl("Christina"))
val Sarah = new FamilyFemaleMember[Woman](new Woman("Sarah"))
val John = new FamilyMaleMember[Boy](new Boy("John"))
val Jack = new FamilyMaleMember[Man](new Man("Jack"))
```

Here, we created four family members, where we could instantiate `FamilyFemaleMember` objects of type `Girl` or `Woman` and `FamilyMaleMember` objects of type `Boy` or `Man`. Meanwhile, we cannot create a `Male` `FamilyFemaleMember` object or a `Female` `FamilyMaleMember` object. Therefore, the following will not compile:

```scala
val Eren = new FamilyFemaleMember[Boy](new Boy("Eren")) // Error: object Eren has to be a Female.
val Mikasa = new FamilyMaleMember[Woman](new Woman("Mikasa")) // Error: object Mikasa has to be a Male.
```

## Supertyping

Likewise, if we have two classes A and B, such that `A >: B`, then A is said to be a _**supertype**_ of B. It is also conventional to say that B is a _**lower bound**_ to A.

### Example

In the previous example of family members, if the family member classes were instead defined as follows:

```scala
class FamilyMaleMember[HumanClass >: Male](member: HumanClass){
  def familyMember: HumanClass = member
}

class FamilyFemaleMember[HumanClass >: Female](member: HumanClass){
  def familyMember: HumanClass = member
}
```

Then, we can no longer instantiate objects of `FamilyMaleMember` as `Boy` or as `Man`; since the definition restricts `HumanClass` to be directly a `Male` or a `Human` object, which in turn cannot be instantiated here; since `Male` and `Human` are abstract classes. The same goes for the `FamilyFemaleMember` class.

# Variance

Variance in Scala takes three different forms:

```scala
class Class1[+ClassCovariant]
class Class2[-ClassContravariant]
class Class3[ClassInvariant]
```

### Covariance
Assume that you have some class `D` that takes some class `C` as a type argument in the form `class D[+C]`. In this case, class `C` is said to be _**covariant**_, meaning that if you two classes `A` and `B` such that `A` inherits `B`, then `D[A]` also inherits `D[B]`.

### Contravariance
Instead, if class D was defined in the form `class D[-C]`, then class `C` is called to be _**contravariant**_, meaning that if `A` inherits `B`, then `D[B]` inherits `D[A]`.

### Invariance
If, on the other hand, class D was defined in the form `class D[C]`, then class `C` is _**invariant**_, meaning that if `A` inherits `B`, then there are no inheritance relations between `D[A]` and `D[B]`.

# Conclusion

As for the syntax: 

```scala
class Class1[+C <: Class2]{...}
```

It defines some class `Class1` which takes a covariant class type argument `C` that is a subtype of some class `Class2`. In other words, `C` has to be `Class2` or some class that inherits `Class2`, and if we have some class `Class3` that inherits some class `Class4`, then `Class1[Class3]` also inherits `Class1[Class4]`.

# References
[1] "Upper Type Bounds", Scala Documentation. [Online]. Available: https://docs.scala-lang.org/tour/upper-type-bounds.html.

[2] "Variances", Scala Documentation. [Online]. Available: https://docs.scala-lang.org/tour/variances.html.