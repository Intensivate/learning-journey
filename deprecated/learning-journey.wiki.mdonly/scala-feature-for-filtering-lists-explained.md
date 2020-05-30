
scala> val list1 = List(false,true,true,true)
list1: List[Boolean] = List(false, true, true, true)

scala> val list2 = List(1,0,1,1)
list2: List[Int] = List(1, 0, 1, 1)

scala> val addr = (list1 zip list2).filter(_.\_1).map(\_._2)
addr: List[Int] = List(0, 1, 1)
