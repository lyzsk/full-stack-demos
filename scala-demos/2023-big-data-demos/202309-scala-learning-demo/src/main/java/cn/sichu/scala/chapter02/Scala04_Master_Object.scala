package cn.sichu.scala.chapter02

import cn.sichu.scala.chapter01.User

import java.io.ObjectOutputStream
import java.net.Socket

object Scala04_Master_Object {
  def main(args: Array[String]): Unit = {
    // 连接服务器
    val client = new Socket("localhost", 9999)
    val objOut = new ObjectOutputStream(client.getOutputStream)
    val user = new User()
    objOut.writeObject(user)
    objOut.flush()
    objOut.close()
    client.close()
  }
}
