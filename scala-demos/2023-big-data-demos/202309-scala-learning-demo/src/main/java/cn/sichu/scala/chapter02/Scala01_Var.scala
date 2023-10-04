package cn.sichu.scala.chapter02

object Scala01_Var {
  def main(args: Array[String]): Unit = {
    var name1: String = "zhangsan"
    name1 = "lisi"
    val name2: String = "lisi"
    //    name2 = "wangwu"
    println(name1)
    println(name2)
  }
}
