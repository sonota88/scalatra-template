package com.example.app

import org.scalatra._
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.StringReader
import java.io.BufferedReader
import java.io.Writer
import java.nio.charset.StandardCharsets
import java.nio.file.Paths
import java.util.regex.Pattern
import java.util.regex.Matcher

import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

class MyScalatraServlet extends ScalatraServlet with MethodOverride {

  def readStaticFile(path:String):String = {
    val myAppDir = sys.env("MY_APP_DIR")
    Utils.readFile(
      Paths.get(myAppDir, "public", path).toString()
    )
  }

  def parseRequestBody(body: String): scala.collection.mutable.Map[String, String] = {
    val sr = new StringReader(body)
    var lines = Utils.readAllLines(sr)
    sr.close()

    // --------------------------------

    var blocks: List[List[String]] = List()

    val boundary = lines.head.stripLineEnd
    lines = lines.tail

    var buf: List[String] = List()
    for (line <- lines) {
      if (
           line.stripLineEnd.equals(boundary)
        || line.stripLineEnd.equals(boundary + "--")
      ) {
        blocks = buf.reverse :: blocks
        buf = List()
      } else {
        buf = line :: buf
      }
    }
    blocks = blocks.reverse

    // --------------------------------

    val map: scala.collection.mutable.Map[String, String] = scala.collection.mutable.Map()

    for (block <- blocks) {
      val pattern = Pattern.compile("Content-Disposition: form-data; name=\"(.+)\"")
      val m: Matcher = pattern.matcher(block(0))
      m.find()
      val k: String = m.group(1)

      val rest = block.tail.tail
      var v = ""
      for (line <- rest) {
        v = v + line
      }

      map.put(k, v)
    }

    // println(map)
    map
  }

  def withOutputStream(os: OutputStream, fn: Function1[OutputStream, Unit]) = {
    try {
      fn.apply(os)
    } finally {
      os.close
    }
  }

  def appendToFile(path: String, text: String) = {
    withOutputStream(
      new FileOutputStream(new File(path), true),
      (os: OutputStream) => {
        os.write(
          text.getBytes(StandardCharsets.UTF_8)
        )
      }
    )
  }

  get("/api/sample") {
    println(multiParams)
    println(params)
    println(request.contentType)
    println(request.getClass().getName())
    // println(request.body)

    val formParams = parseRequestBody(request.body)
    println("_params (" + formParams("_params") + ")")

    appendToFile("/tmp/sample.txt", "foo\n");

    contentType = "application/json"

    val json =
      ("result" ->
        ("id" -> 123) ~
        ("a" ->
          ("b" -> "foo") ~
          ("c" -> List(1, 2, 3))
        )
      ) ~
      ("errors" ->
        List(
          // Map("trace" -> "{trace}", "msg" -> "{message}")
        )
      )

    println(compact(render(json)))
    compact(render(json))
  }

  post("/api/sample") {
    println(multiParams)
    println(params)
    println(params.get("_params"))
    // println(request.body)

    val formParams = parseRequestBody(request.body)
    println("_params (" + formParams("_params") + ")")

    contentType = "application/json"

    val json =
      ("result" ->
        ("id" -> 456) ~
        ("a" ->
          ("b" -> "foo") ~
          ("c" -> List(1, 2, 3))
        )
      ) ~
      ("errors" ->
        List(
          Map("trace" -> "{trace}", "msg" -> "{message}")
        )
      )

    println(compact(render(json)))
    compact(render(json))
  }

  get("/") {

    val myAppDir = sys.env("MY_APP_DIR")

    contentType = "text/html"
    Utils.readFile(myAppDir + "/views/index.html")
  }

  notFound {
    contentType = null

    if (requestPath.endsWith(".js")) {
      contentType = "text/javascript"
      readStaticFile(requestPath)
    } else {
      "not found"
    }
  }
}
