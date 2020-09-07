package myapp

import java.io.BufferedReader
import java.io.InputStream
import java.io.OutputStream
import java.io.Reader

import scala.collection.mutable.ListBuffer
import scala.io.Source

import org.json4s._
import org.json4s.jackson.JsonMethods._

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

  def jsonType(json: String): String = {
    val trimmed = json.trim
    if (trimmed.startsWith("{")) {
      "object"
    } else if (trimmed.startsWith("[")) {
      "array"
    } else {
      throw new RuntimeException("not supported")
    }
  }

  /*
{ "fooBar": 123, "b": {"c":456}, "d": "fdsa", "e": [1,"a"], "f": null
  , "g": [true,false], "h": 123.456, "i": new Date() }
=> JObject(
     List(
       (fooBar,JInt(123))
     , (b,JObject(List((c,JInt(456)))))
     , (d,JString(fdsa))
     , (e,JArray(List(JInt(1), JString(a))))
     , (f,JNull)
     , (g,JArray(List(JBool(true), JBool(false))))
     , (h,JDouble(123.456))
     , (i,JString(2019-11-28T20:33:39.461Z))))
   */
  def parseJsonAsObject(json: String): Map[String, Any] = {
    val data = org.json4s.jackson.JsonMethods.parse(json)
    // println(data)

    data match {
      case jo:JObject => println("JObject")
      case ja:JArray => println("JArray")
      case _ => println("other type")
    }

    data.values.asInstanceOf[Map[String, Any]]
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

}
