package cn.sichu.scala.chapter07

import scala.collection.mutable.ListBuffer

object Scala01_Collection_Seq_1 {
  def main(args: Array[String]): Unit = {
    // seq 数据有序, 可以放重复数据, 相当于Java List
    val list = List(1, 4, 3, 2, 1)
    val listBuffer = ListBuffer(1, 4, 3, 2, 1)

    // 改变自身
    listBuffer.update(0, 5)
    println(listBuffer)
    // 创建新的
    val newList = listBuffer.updated(0, 6)
    println(listBuffer)
    println(newList)

    listBuffer.remove(1, 2)
    println(listBuffer)
  }
}
