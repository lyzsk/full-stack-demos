package cn.sichu.scala.chapter06

object Scala02_Instance_1 {
  def main(args: Array[String]): Unit = {
    // 再构造参数前使用var/val
    val user = new User("zhangsan")
    user.name = "lisi"
    println(user.username)
    println(user.name)
  }

  class User(var name: String) {
    var username: String = name
    println(username)
  }
}
