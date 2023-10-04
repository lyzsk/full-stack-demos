package cn.sichu.scala.chapter02

import cn.sichu.scala.chapter01.User

object Scala05_DataType_1 {
  def main(args: Array[String]): Unit = {
    val list: AnyRef = List(1, 2, 3, 4)
    val obj: AnyRef = new User()
    val obj1: AnyRef = Scala05_DataType_1

    println(list)
    println(obj)
    println(obj1)
  }
}
