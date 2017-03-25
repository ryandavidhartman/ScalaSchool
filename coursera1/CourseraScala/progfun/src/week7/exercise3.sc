object exercise3 {
  def expr = {
    val x = {
      println("x"); 1
    }
    lazy val y = {
      println("y"); 2
    }
    def z = {
      println("z"); 3
    }

    z + y + x + z + y + x
  }

  expr

}