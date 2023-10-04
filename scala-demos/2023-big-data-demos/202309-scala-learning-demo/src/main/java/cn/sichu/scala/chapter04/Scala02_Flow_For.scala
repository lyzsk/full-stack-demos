package cn.sichu.scala.chapter04

object Scala02_Flow_For {
  def main(args: Array[String]): Unit = {
    for (i: Int <- 1 to 5) {
      println(i)
    }
    val range = 1.to(5).by(3)
    val range1 = 1 to 5 by 2
    val range3 = Range(1, 5, 2)
    val range4 = 1 until 5 by 2
    for (i: Int <- range) {
      println(i)
    }
    for (i <- range1) {
      println(i)
    }
    for (i <- range3) {
      println(i)
    }
    for (i <- range4) {
      println(i)
    }
    for (i <- Range(5, 0, -1)) {
      println(i)
    }
  }
}
