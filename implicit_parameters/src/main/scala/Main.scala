import chipsalliance.rocketchip.config.{Config, Field, Parameters}

case object Parameters1Key extends Field[Parameters1]
case object Parameters2Key extends Field[Parameters2]

trait Parameters1 {
  val p1 : Int 
  val p2 : Int 
}

trait Parameters2 {
  val p1 : Int 
  val p3 : Int 
}

case class Parameters1Use(  
  p1 : Int = 100, 
  p2 : Int = 1000 ) extends Parameters1{
}

case class Parameters2Use(  
  p1 : Int = 300, 
  p3 : Int = 3000 ) extends Parameters2{
}

class Parameters1KeyDef extends Config((site, here, up) => {
    case Parameters1Key => Parameters1Use(p1=200)
})

class Parameters2KeyDef extends Config((site, here, up) => {
    case Parameters2Key => Parameters2Use()
})

class TestModule(implicit p: Parameters){
    val x: Int = p(Parameters1Key).p1
}

// implicit val p1 = new Parameters1Use()
object Main extends App{
  
  implicit val p = new Config(new Parameters1KeyDef ++ new Parameters2KeyDef)

  val test = new TestModule
  println( "The Parameter1 key contains", p(Parameters1Key).p1, p(Parameters1Key).p2 )
  println( "The Parameter2 key contains", p(Parameters2Key).p1, p(Parameters2Key).p3 )
  println("Value of x in TestModule is ", test.x)
}