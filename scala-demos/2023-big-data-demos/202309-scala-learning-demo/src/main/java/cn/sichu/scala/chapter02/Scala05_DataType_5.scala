package cn.sichu.scala.chapter02

object Scala05_DataType_5 {
  def main(args: Array[String]): Unit = {
    // scala编译器将类型进行了隐式转换, 所以可以编译通过
    val b: Byte = 20
    val i: Int = b
    println(i)

    // idea 会报错, 但是能够通过编译
    val ch: Char = 'A' + 1
    println(ch)
  }

}
