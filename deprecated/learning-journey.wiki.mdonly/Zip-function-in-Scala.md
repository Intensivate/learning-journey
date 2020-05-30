### About zip
The scala `zip` method takes another collection as parameter and will merge its elements with the elements of the current collection to create a new collection consisting of pairs or tuple elements from both collections.

Important points about zip method:
* zip() is applicable to both Scala's mutable and immutable collection data structures;
* zip() is applied to two collections;
* zip() returns a new collection of pairs elements from both collections.

As per the Scala documentation, the definition of the zip method is as follows:
```scala
def zip[B](that: GenIterable[B]): Iterable[(A, B)]
```
### Basic example

Let's create a two lists, one list of integers and one of strings:
```scala
val nums = List(1, 2, 3)
val fruit = List("apple", "banana", "orange")
```
Let's call zip method to merge two collections into a single one which will consist of pairs or elements as tuples
```scala
val result : List[İnt, String)] = nums.zip(fruit)
println(s"Resulting list = $result")
```
Output of this is: `Resulting list = List((1, apple), (2, banana), (3, orange))`

We called zip function on list of integers and get new list of pairs (tuples) ((1,apple), (2,banana), (3,orange)). Notice that if we called zip function on string list we would get final list like ((apple,1), (banana,2), (orange,3)).

One more thing that is good to mention, if one collection contains more elements than the other, the items on the end of the longer collection would be dropped.
