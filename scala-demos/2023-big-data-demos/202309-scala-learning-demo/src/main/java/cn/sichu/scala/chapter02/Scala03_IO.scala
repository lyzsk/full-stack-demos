package cn.sichu.scala.chapter02

import scala.io.Source

object Scala03_IO {
  def main(args: Array[String]): Unit = {
    // read line
    //    val line = StdIn.readLine()
    //    println(line)

    // 从文件输入
    val source = Source.fromFile("202309-scala-learning-demo/data/word.txt")
    val strs = source.getLines()
    while (strs.hasNext) {
      println(strs.next())
    }
    source.close()
  }
}
