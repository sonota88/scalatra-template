package app

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

import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable.ListBuffer

import org.scalatra.util.MimeTypes


class MainServlet extends ScalatraServlet {

  val logger = LoggerFactory.getLogger(getClass)

  def puts(msgs: String*): Unit = {
    msgs.foreach({ msg =>
      logger.debug(msg)
    })
  }

  def toContentType(path: String): String = {
    val lower = path.toLowerCase
    if (lower.endsWith(".txt")) {
      "text/plain"
    } else if (lower.endsWith(".html")) {
      "text/html"
    } else if (lower.endsWith(".js")) {
      "application/javascript"
    } else if (lower.endsWith(".css")) {
      "text/css"
    } else if (lower.endsWith(".json")) {
      "application/json"
    } else if (lower.endsWith(".xml")) {
      "application/xml"

    } else if (lower.endsWith(".gif")) {
      "image/gif"
    } else if (lower.endsWith(".jpg")) {
      "image/jpeg"
    } else if (lower.endsWith(".jpeg")) {
      "image/jpeg"
    } else if (lower.endsWith(".png")) {
      "image/png"
    } else if (lower.endsWith(".svg")) {
      "image/svg+xml"
    } else if (lower.endsWith(".ico")) {
      "image/vnd.microsoft.icon"

    } else if (lower.endsWith(".woff")) {
      "font/woff"
    } else if (lower.endsWith(".woff2")) {
      "font/woff2"

    } else {
      // "application/octet-stream"
      MimeTypes.mimeType(path)
    }
  }

  def escapeHtml(str: String) = {
    str
      .replace("&", "&amp;")
      .replace("<", "&lt;")
      .replace(">", "&gt;")
      .replace("\"", "&quot;")
      .replace("'", "&#039;")
  }

  def readDir(requestPath: String, file: java.io.File) = {
    val realPath = sys.env("PUBLIC_DIR") + "" + requestPath

    val pathHead =
      if (requestPath.endsWith("/")) {
        requestPath
      } else {
        requestPath + "/"
      }

    val js = Utils.readResourceFile("dir.js")

    var html = ""
    html += s"""
<html>
<body>
<head>
  <meta charset="utf-8" />
  <title>${realPath} | static-server-scalatra</title>

  <link rel="shortcut icon" href="/internal_favicon.ico" type="image/x-icon" />

<style>
* {
  font-family: monospace;
}

body {
  padding: 0.2rem 3%;
}

a {
  text-decoration: none;
}

h1 {
  font-size: 120%;
}

hr {
  border: solid #ccc;
  border-width: 0.1rem;
}
</style>

<script>
// console.log(123);
${js}
</script>

</head>
<h1>${realPath}</h1>
<hr />

<pre style="
    line-height: 150%;
  ">
- <a href="../">../</a>

"""

    val LF = "\n"

    file.listFiles
      .filter({ item => item.isDirectory })
      .sortBy({ item => item.getName })
      .foreach({ item =>
        val href = escapeHtml(pathHead + item.getName)
        val content = escapeHtml(item.getName)
        html += s"""- <a href="${href}">${content}/</a>"""
        html += LF
      })

    html += LF

    file.listFiles
      .filterNot({ item => item.isDirectory })
      .sortBy({ item => item.getName })
      .foreach({item =>
        val href = escapeHtml(pathHead + item.getName)
        val content = escapeHtml(item.getName)
        html += s"""- <a href="${href}">${content}</a>"""
        html += LF
      })

    html += """
</pre>
</body>
</html>
    """

    html
  }

  def copyStream(is: java.io.InputStream, os: java.io.OutputStream): Unit = {
    Utils.withInputStream(
      is,
      (is)=>{
        val bufSize = 4096
        val buf = new Array[Byte](bufSize)
        var numRead = 0
        var eof = false

        while (! eof) {
          numRead = is.read(buf, 0, bufSize)
          // puts("read: " + numRead)
          if (numRead == -1) {
            eof = true
          } else {
            os.write(buf, 0, numRead)
          }
        }
      }
    )
  }

  def readContent(file: java.io.File): Unit = {
    copyStream(
      new java.io.BufferedInputStream(
        new java.io.FileInputStream(file)
      ),
      response.getOutputStream
    )

    Unit
  }

  get("/*") {
    // puts("-->> /*")
    val realPath = sys.env("PUBLIC_DIR") + "" + requestPath
    puts(realPath)

    val file = new File(realPath)

    if (file.exists) {
      if (file.isDirectory) {
        contentType = "text/html"
        readDir(requestPath, file)
      } else {
        contentType = toContentType(realPath)
        readContent(file)
      }
    } else {
      NotFound { "Not Found" }
    }
  }

  get("/internal_favicon.ico") {
    val is = getClass
	      .getClassLoader
          .getResourceAsStream("favicon.ico")

    contentType = "image/x-icon"

    copyStream(
      is,
      response.getOutputStream
    )

    Unit
  }

  get("/shutdown") {
    puts("bye");
    System.exit(0);

    Unit
  }

}
