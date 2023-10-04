package cn.sichu.scala.chapter04

import scala.util.control.Breaks
import scala.util.control.Breaks.breakable

object Scala04_Flow_Break {
  def main(args: Array[String]): Unit = {
    for (i <- 1 to 5) {
      if (i != 3) {
        println(i)
      }
    }

    //        Breaks.breakable {
    //          for (i <- 100 to 105) {
    //            if (i == 103) {
    //              Breaks.break()
    //            }
    //            println(i)
    //          }
    //        }
    //        println("other")

    breakable {
      for (i <- 100 to 105) {
        if (i == 103) {
          Breaks.break()
        }
        println(i)
      }
    }
    println("other")
  }
}
