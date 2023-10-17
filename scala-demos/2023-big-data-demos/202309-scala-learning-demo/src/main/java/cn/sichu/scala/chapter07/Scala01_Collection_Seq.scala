package cn.sichu.scala.chapter07

object Scala01_Collection_Seq {
  def main(args: Array[String]): Unit = {
    val seq = Seq(1, 2, 3, 4)
    val list = List(1, 2, 3, 4)
    val ints: List[Int] = list :+ 5
    val ints1: List[Int] = 5 +: list

    println(list eq ints)
    println(seq)
    println(list)
    println(ints)
    println(ints1)
    println(Nil)
  }
}
