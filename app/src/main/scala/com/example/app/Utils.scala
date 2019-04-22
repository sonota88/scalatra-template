package com.example.app

import java.io.OutputStream
import java.io.Reader

import scala.collection.mutable.ListBuffer
import scala.io.Source

object Utils {
  
  def readFile(path:String):String = {
    val src = Source.fromFile(path)
    var content = ""

    try {
      val lines = src.getLines
      lines.foreach{line => content += line + "\n" }
    } finally {
      src.close()
    }

    content
  }

  def readAllLines(r: Reader): List[String] = {
    val lines: ListBuffer[String] = ListBuffer()

    // val r: java.io.Reader = new java.io.InputStreamReader(System.in, "UTF-8")
    var n: Int = 0
    val buf: ListBuffer[Integer] = ListBuffer()

    while (n >= 0) {
      n = r.read()
      if (n < 0) {
        // break
      } else {
        buf += n
        if (n == '\n') {
          lines += intListToString(buf.toList)
          buf.clear
        }
      }
    }
    lines += intListToString(buf.toList)
    buf.clear

    lines.toList
  }

  def intListToString(ns: List[Integer]): String = {
    String.valueOf(ns.map{ _.toChar }.toArray)
  }

  def withReader(r: Reader, fn: Reader => Unit) = {
    try {
      fn(r)
    } finally {
      r.close
    }
  }

  def withOutputStream(os: OutputStream, fn: OutputStream => Unit) = {
    try {
      fn(os)
    } finally {
      os.close
    }
  }

}
