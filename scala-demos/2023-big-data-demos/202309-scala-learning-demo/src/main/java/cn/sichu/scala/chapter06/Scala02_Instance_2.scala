package cn.sichu.scala.chapter06

object Scala02_Instance_2 {
  def main(args: Array[String]): Unit = {
    new User("zhangsan")
  }

  class Person {
    println("aaa")
  }

  class User(var name: String) extends Person {
    println("bbb")
  }
}
