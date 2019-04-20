package com.example.app

import org.scalatra._
import scala.io.Source

class MyScalatraServlet extends ScalatraServlet {

  get("/") {

    contentType = "application/json"

    // views.html.hello()
    """
{
  "a": 123456789
}
    """
  }

  get("/html") {

    val myAppDir = sys.env("MY_APP_DIR")

    contentType = "text/html"

    val src = Source.fromFile(myAppDir + "/views/index.html")
    val lines = src.getLines
    var s = ""
    lines.foreach{line => s += line + "\n" }
    s
  }

}
