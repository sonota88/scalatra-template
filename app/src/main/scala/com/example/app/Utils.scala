package com.example.app

import scala.io.Source

object Utils {
  
  def readFile(path:String):String = {
    val src = Source.fromFile(path)
    val lines = src.getLines
    var s = ""
    lines.foreach{line => s += line + "\n" }
    s
  }

}
