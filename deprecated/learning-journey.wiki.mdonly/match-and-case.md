
A good explanation of `match` operator is in the [Chisel tutorial](https://github.com/ucb-bar/generator-bootcamp/blob/master/3.1_parameters.ipynb)

Here, we give some more advanced examples and walk through the details of what's going on.
```scala
val root = res match { //res is passed in, then we compare "case" values to it, and result of match is returned
      case ResourceMap(value, _) => value.toList match { //ResourceMap defined in diplomacy/Resources.scala
        case Seq(("/", Seq(subtree))) => subtree //"Seq" creates a list, to which the case checks for a match
        case _ => res
      }
      case _ => res
    }
```
The tricky parts are the use of ResourceMap and the embedded second level of `match`.  

The ResourceMap is defined in diplomacy/Resources.scala as:
```scala
final case class ResourceMap(value: Map[String, Seq[ResourceValue]], labels: Seq[String] = Nil) extends ResourceValue
```
It's use results in 
 embedded `match` takes the result of the ResourceMap as an input.. and the result of the second match is actually the valu

## pattern matching with case
[pattern matching page](https://docs.scala-lang.org/tour/pattern-matching.html)
