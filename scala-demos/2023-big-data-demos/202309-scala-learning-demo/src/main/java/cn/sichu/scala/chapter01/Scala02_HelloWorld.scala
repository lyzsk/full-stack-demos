package cn.sichu.scala.chapter01

/**
 * scala 基于 java 开发的
 *
 * package: java中的包
 * object: 声明一个对象, 在编译时, 会编译为两个class文件
 * 声明的对象的类型为: 当前类型+$
 *
 * def: 声明方法的关键词
 * main: scala程序的入口方法名
 * (): 方法参数列表
 *
 * java => String[] args
 * scala => args: Array[String]
 *
 * args: 参数名
 * Array[String]: 参数类型
 * : => 分隔符
 * Array[String]: Array是一个数组类型, 数组也是对象, 有自己的类型, [] 表示泛型
 *
 * Unit => "名称 : 类型", "参数名 : 参数类型", "变量名 : 变量类型", "方法名 : 方法的返回类型"
 * Unit 表示返回值类型, Unit类型是Scala中的新类型, 为了代替void
 *
 * "=": 赋值
 */
object Scala02_HelloWorld {
  def main(args: Array[String]): Unit = {
    // scala 基于 java 开发, 说明大部分java代码可以直接在scala方法体中使用, 且没有分号, 但可以用分号划分一行内多个逻辑
    System.out.println("Hello Scala World");
    System.out.println("Hello Scala World")
    println("Hello Scala")
  }
}
