package myapp

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

  def withReader(r: Reader, fn: Reader => Any) = {
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

  def jsonType(json: String): String = {
    val stripped = json.trim
    if (stripped.startsWith("{")) {
      "object"
    } else if (stripped.startsWith("[")) {
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
  def parseJsonObject(json: String): Map[String, Any] = {
    val data = org.json4s.jackson.JsonMethods.parse(json)
    // println(data)

    data match {
      case jo:JObject => println("JObject")
      case ja:JArray => println("JArray")
      case _ => println("other type")
    }

    data.values.asInstanceOf[Map[String, Any]]
  }

}
