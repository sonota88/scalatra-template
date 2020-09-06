package app

import java.io.InputStream
import java.io.OutputStream
import java.io.Reader
import java.io.BufferedReader

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

  def readFileBinary(path:String): String = {
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

  def readResourceFile(path: String): String = {
    Source.fromResource("dir.js")
      .getLines
      .map({line => line + "\n"})
      .mkString("")
  }

  def intListToString(ns: List[Integer]): String = {
    String.valueOf(ns.map{ _.toChar }.toArray)
  }

  def withReader(r: Reader, fn: Reader => Any) = {
    try {
      fn(r)
    } finally {
      r.close
    }
  }

  def withInputStream(is: InputStream, fn: InputStream => Unit) = {
    try {
      fn(is)
    } finally {
      is.close
    }
  }

  def withOutputStream(os: OutputStream, fn: OutputStream => Unit) = {
    try {
      fn(os)
    } finally {
      os.close
    }
  }

  def withBufferedReader(br: BufferedReader, fn: BufferedReader => Unit) = {
    try {
      fn(br)
    } finally {
      br.close
    }
  }

  def mkdirs(path: String): Unit = {
    val file = new java.io.File(path)
    file.mkdirs
  }

  def dirExists(path: String): Boolean = {
    val file = new java.io.File(path)
    file.exists
  }

  def toCanonicalPath(path: String): String = {
    val file = new java.io.File(path)
    file.getCanonicalPath
  }

  def getExt(path: String): String = {
    val basename = path.split("/").last
    val basenameParts = basename.split("\\.")
    if (2 <= basenameParts.length) {
      "." + basenameParts.last
    } else {
      ""
    }
  }

}
