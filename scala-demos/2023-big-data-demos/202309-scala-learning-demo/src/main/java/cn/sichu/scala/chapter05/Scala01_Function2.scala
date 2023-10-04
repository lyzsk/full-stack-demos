package cn.sichu.scala.chapter05

object Scala01_Function2 {
  def main(args: Array[String]): Unit = {
    // 可变参数
    def func1(names: String*): Unit = {
      println(names)
    }

    func1()
    func1("zhangsan")
    func1("zhangsan", "lisi", "wangwu", "zhaoliu")

    // 参数默认值
    def func2(name: String, password: String = "123456"): Unit = {
      println(name + ", " + password)
    }

    func2("zhangsan", "6543321")
    func2("zhangsan")

    // 带名参数
    def func3(password: String = "000000", name: String): Unit = {
      println(name + ", " + password)
    }

    func3("123123", "zhangsan")
    func3(name = "zhangsan")


    // 省略return
    def func4(): String = {
      //      return "zhangsan"
      "zhangsan"
    }

    println(func4())

    // 省略花括号
    def func5(): String = "zhangsan"

    println(func5())

    // 省略返回值类型
    def func6() = "zhangsan"

    println(func6())

    // 省略参数
    def func7 = "func7"

    println(func7)

    /**
     * 省略等号
     * 如果函数体有明确的返回语句, 那么返回值类型不能省略
     * 如果函数体返回值类型明确为Unit, 那么函数体中即使有return关键词也不起作用
     * 如果函数体返回值类型声明为Unit, 此时必须连通等号一起省略
     */
    def func8(): String = {
      "zhangsan"
    }

    println(func8())

    def func9(): Unit = {
      "func9"
    }

    println(func9())

    def func10() {
      "func10"
    }

    println(func10())

    def func11(): String = {
      "func11"
    }

    println(func11())
  }
}
