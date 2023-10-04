package cn.sichu.scala.chapter02

object Scala02_String {
  def main(args: Array[String]): Unit = {
    val name = "zhangsan"
    val password = "123456"
    println("Hello " + name)
    val json = "{\"username\": \"" + name + "\", \"password\":\"" + password + "\"}"
    println(json)

    // 传值字符串 (太麻烦, 不推荐)
    printf("username: %s", name + "\t")
    printf("username: %s", password + "\n")

    // 插值字符串
    println(s"username: $name")
    // scala 官方无法使用插值字符串封装JSON
    //    val json1 = s"{\"username\":\"${name}\", \"password\":\"${password}\"}"
    //    println(json1)

    // 多行字符串
    // 竖线表示顶格符
    val s =
    s"""
       |Hello
       | Scala
      WTF1
       |test
      WTF2
       |""".stripMargin
    println(s)

    val json1 =
      """
        |{"username": "${name}", "password":"${password}"}
        |""".stripMargin
    println(json1)

    val sql =
      """
        |select id
        |from
        |(
        |   select * from t_user
        |   where id = 1
        |   order by id desc
        |) a
        |group by id
        |""".stripMargin
    println(sql)
  }
}
