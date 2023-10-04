package cn.sichu.scala.chapter02

import java.io.ObjectInputStream
import java.net.ServerSocket

object Scala04_Slaver_Object {
  def main(args: Array[String]): Unit = {
    // 启动服务器
    val server = new ServerSocket(9999)
    println("server started...wating data from client...")
    val clinet = server.accept()
    val objIn = new ObjectInputStream(clinet.getInputStream)
    val user = objIn.readObject()
    println("recieved data from client: " + user)
    objIn.close()
    clinet.close()
    server.close()
  }
}
