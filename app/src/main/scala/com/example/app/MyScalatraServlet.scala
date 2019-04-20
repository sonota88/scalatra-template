package com.example.app

import org.scalatra._

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
    Utils.readFile(myAppDir + "/views/index.html")
  }

}
