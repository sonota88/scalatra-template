package com.example.app

import org.scalatra._
import java.nio.file.Paths

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

  get("/api/sample") {
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
