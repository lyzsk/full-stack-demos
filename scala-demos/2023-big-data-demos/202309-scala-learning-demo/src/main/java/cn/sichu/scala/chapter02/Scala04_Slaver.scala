package cn.sichu.scala.chapter02

import java.net.{ServerSocket, Socket}

object Scala04_Slaver {
  def main(args: Array[String]): Unit = {
    // 启动服务器
    val server = new ServerSocket(9999)
    println("server start...wating for client coonection...")
    val client: Socket = server.accept()
    val input = client.getInputStream
    val i = input.read()
    // 网络传输传的是ASCII字节码, 会自动截断
    println("data recieved from client:" + i)
    input.close()
    client.close()
    server.close()
  }
}
