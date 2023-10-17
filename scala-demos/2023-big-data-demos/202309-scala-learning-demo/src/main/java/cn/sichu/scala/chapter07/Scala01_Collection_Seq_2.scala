package cn.sichu.scala.chapter07

import scala.collection.mutable.ListBuffer

object Scala01_Collection_Seq_2 {
  def main(args: Array[String]): Unit = {
    val list = ListBuffer(1, 3, 4, 2, 1)
    println(list)
    val list1 = list.toList
    println(list1)
    val buffer = list1.toBuffer
    println(buffer)
  }
}
