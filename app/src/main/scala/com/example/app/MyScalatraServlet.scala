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

    contentType = "text/html"

    """
<html>
<body>
hello
</body>
</html>
    """
  }

}
