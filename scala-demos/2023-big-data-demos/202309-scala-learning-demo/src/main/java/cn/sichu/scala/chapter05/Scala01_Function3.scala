package cn.sichu.scala.chapter05

object Scala01_Function3 {
  def main(args: Array[String]): Unit = {
    // 函数作为值
    def func1(): String = {
      "zhangsan"
    }

    val a = func1
    val b = func1 _
    val c: () => Unit = func1
    println(a)
    println(b)
    println(c)

    // 函数作为参数
    def func2(i: Int): Int = {
      i * 2
    }

    def func22(f: Int => Int): Int = {
      f(10)
    }

    println(func22(func2))

    // 函数作为返回值
    def func3(i: Int): Int = {
      i * 2
    }

    def func33() = {
      func3 _
    }

    println(func33() {
      10
    })

    // 匿名函数
    def func4(f: Int => Int): Int = {
      f(10)
    }

    println(func4((x: Int) => {
      x * 20
    }))
    println(func4(x => x * 20))
    println(func4(_ * 20))

    // 控制抽象
    def func5(op: => Unit) = {
      op
    }

    func5(println("xx"))

    // 闭包
    def func6() = {
      val i = 20

      def func66() = {
        i * 2
      }

      func66 _
    }

    println(func6()())

    // 惰性函数
    // 当函数返回值被声明为lazy时，函数的执行将被推迟，直到我们首次对此取值，该函数才会执行
    def func7(): String = {
      println("func7...")
      "zhangsan"
    }

    lazy val aa = func7()
    println("------")
    println(aa)
  }
}
