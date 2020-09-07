package app

import java.io.BufferedReader
import java.io.InputStream
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

  // TODO
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

    withBufferedReader(
      new BufferedReader(r),
      (br)=>{
        val sb = new java.lang.StringBuilder()

        var done = false
        while (!done) {
          val n = br.read
          if (n == -1) {
            done = true
          } else {
            sb.append(Character.toChars(n))
            if (n == '\n') {
              lines += sb.toString
              sb.setLength(0)
            }
          }
        }
        if (0 < sb.length) {
          lines += sb.toString
        }
      }
    )

    lines.toList
  }

  def readResourceFile(path: String): String = {
    Source.fromResource(path)
      .getLines
      .map({line => line + "\n"})
      .mkString("")
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
