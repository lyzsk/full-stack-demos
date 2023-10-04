package cn.sichu.scala.chapter06

object Scala01_Object {
  def main(args: Array[String]): Unit = {
    val test = new Test()
    test.test()
    // 1. new object
    // 2. relection
    // 3. deserialization
    // 4. clone
    //    val user = new User("zhangsan")
    new User()
  }

  class Test {
    def test(): Unit = {

    }
  }

  class User(name: String) {

    def xxx(): Unit = {

    }

    println("user..." + name)

    // 辅助构造方法
    def this() {
      this("aaa")
      println("bbb")
    }
  }
}
