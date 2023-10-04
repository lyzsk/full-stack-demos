package cn.sichu.scala.chapter04

object Scala02_Flow_For_1 {
  def main(args: Array[String]): Unit = {
    for (i <- Range(1, 5) if i != 3) {
      println(i)
    }
    for (i <- Range(1, 5)) {
      if (i != 3) {
        println(i)
      }
    }
  }
}
