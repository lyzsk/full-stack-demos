package cn.sichu.scala.chapter03

object Scala01_Operator {
  def main(args: Array[String]): Unit = {
    // scala 中双等号相当于非空的equals, eq为比较内存地址
    val s1 = new String("abc")
    val s2 = new String("abc")
    println(s1 == s2)
    println(s1.equals(s2))
    println(s1.eq(s2))

    //    val s3: String = null
    //    val s4 = new String("abc")
    //    println(s3 == s4)
    //    println(s3.equals(s4))

    // scala 中运算符是可以自己修改的
    val s = "abc" * 2
    val ss = "abc" + 1
    println(s)
    println(ss)
  }
}
