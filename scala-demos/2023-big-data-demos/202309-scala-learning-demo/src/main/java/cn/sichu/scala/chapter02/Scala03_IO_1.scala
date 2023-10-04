package cn.sichu.scala.chapter02

import java.io.{File, PrintWriter}

object Scala03_IO_1 {
  def main(args: Array[String]): Unit = {
    val writer = new PrintWriter(new File("202309-scala-learning-demo/data/output.txt"))
    writer.write("hello world")
    writer.close()
  }
}
