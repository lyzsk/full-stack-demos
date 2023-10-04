package cn.sichu.scala.chapter02

import java.io.OutputStream
import java.net.Socket

object Scala04_Master {
  def main(args: Array[String]): Unit = {
    // 连接服务器
    val client = new Socket("localhost", 9999)
    val out: OutputStream = client.getOutputStream
    out.write(300)
    out.flush()
    out.close()
    println("client send data to server with data 1")
    client.close()
  }
}
