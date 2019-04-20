package com.example.app

import org.scalatra._
import java.nio.file.Paths

class MyScalatraServlet extends ScalatraServlet {

  def readStaticFile(path:String):String = {
    val myAppDir = sys.env("MY_APP_DIR")
    Utils.readFile(
      Paths.get(myAppDir, "public", path).toString()
    )
  }

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
