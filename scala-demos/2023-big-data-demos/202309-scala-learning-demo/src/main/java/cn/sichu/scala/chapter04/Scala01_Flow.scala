package cn.sichu.scala.chapter04

object Scala01_Flow {
  def main(args: Array[String]): Unit = {
    val age = 30
    if (age == 30) {
      println("age = 30")
    }
    println("else")

    val result = if (age == 30) {
      "zhangsan"
    } else {
      null
    }
    println(result)
  }
}
