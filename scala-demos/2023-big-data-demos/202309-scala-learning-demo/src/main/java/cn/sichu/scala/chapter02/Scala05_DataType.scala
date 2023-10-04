package cn.sichu.scala.chapter02

object Scala05_DataType {
  def main(args: Array[String]): Unit = {
    val b: Byte = 20
    val s: Short = 20
    val c: Char = 'a'
    val i: Int = 20
    val long: Long = 20
    val f: Float = 20.0F
    val d: Double = 20.0
    val flag: Boolean = true
    val u: Unit = test()
    println(u)
  }

  def test(): Unit = {
    println("unit test")
  }
}
