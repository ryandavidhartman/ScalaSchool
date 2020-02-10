
val myConcat: ((String, String) => String) = (s1: String, s2: String ) => {
  s1 + s2
}

val bob = "Bob"
val sally = "Sally"
val bobSally = myConcat(bob, sally)


val funny: (Int) => ((Int) => Int) = { i =>
  val add: (Int) => Int = { j =>
    i + j
  }
  add
}

val funny1 = funny(1)
val test1 = funny1(2)

val funnier = { i:Int =>  { j:Int => i + j }}

val funny2 = funnier(1)
val test2 = funny2(2)


