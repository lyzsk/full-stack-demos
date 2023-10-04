package cn.sichu.scala.chapter04

object Scala03_Flow_While {
  def main(args: Array[String]): Unit = {
    var age = 0
    while (age < 5) {
      println("******" + age)
      age += 1
    }

    var age1 = 0
    do {
      println("######" + age1)
      age1 += 1
    } while (age < -1)
  }
}
