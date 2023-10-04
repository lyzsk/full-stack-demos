package cn.sichu.scala.chapter05

object Scala01_Function1 {
  def main(args: Array[String]): Unit = {
    // 无参, 无返回值
    def func1(): Unit = {
      println("function1")
    }

    func1()

    // 无参, 有返回值
    def func2(): String = {
      "zhangsan"
    }

    println(func2())

    // 有参, 无返回值
    def func3(name: String): Unit = {
      println(name)
    }

    func3("zhangsan")

    // 有参, 有返回值
    def func4(name: String): String = {
      "hello " + name
    }

    println(func4("zhangsan"))

    // 多参, 无返回值
    def func5(hello: String, name: String): Unit = {
      println(hello + " - " + name)
    }

    func5("hello", "zhangsan")

    // 多参, 有返回值
    def func6(hello: String, name: String): String = {
      hello + " = " + name
    }

    println(func6("hello", "zhangsan"))
  }
}
