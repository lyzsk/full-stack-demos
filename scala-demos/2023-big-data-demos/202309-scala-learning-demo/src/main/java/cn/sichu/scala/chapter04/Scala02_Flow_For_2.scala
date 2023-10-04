package cn.sichu.scala.chapter04

object Scala02_Flow_For_2 {
  def main(args: Array[String]): Unit = {
    val result1 = for (i <- 1 to 5) {
      i
    }
    val result2 = for (i <- 1 to 5) yield {
      i * 2
    }
    println(result1)
    println(result2)

    Thread.`yield`()
  }
}
